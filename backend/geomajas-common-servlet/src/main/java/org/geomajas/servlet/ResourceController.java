/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

/**
 * Resource controller for efficiently resolving and rendering static resources from within a JAR file. The cache
 * header is set to a date in the far future to improve caching. For development, you can specify a "files-location"
 * servlet init parameter. Any resources are searched at that location first. When found, the cache header is not set.
 * This assures the jar does not need building for testing.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
@Controller
public class ResourceController implements LastModified, ServletContextAware {

	private static final String RESOURCE_PREFIX = "/resource";
	private static final String HTTP_CONTENT_LENGTH_HEADER = "Content-Length";
	private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";
	private static final String HTTP_EXPIRES_HEADER = "Expires";
	private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";
	private static final String INIT_PARAM_LOCATION = "files-location";

	private ServletContext servletContext;

	private final Logger log = LoggerFactory.getLogger(ResourceController.class);
	private final String protectedPath = "/?WEB-INF/.*";
	private final String[] allowedResourcePaths = new String[]{
			"/**/*.css", "/**/*.gif", "/**/*.ico", "/**/*.jpeg",
			"/**/*.jpg", "/**/*.js", "/**/*.html", "/**/*.png",
			"META-INF/**/*.css", "META-INF/**/*.gif", "META-INF/**/*.ico", "META-INF/**/*.jpeg",
			"META-INF/**/*.jpg", "META-INF/**/*.js", "META-INF/**/*.html", "META-INF/**/*.png",
	};

	private File fileLocation;

	private Map<String, String> defaultMimeTypes = new HashMap<String, String>();

	{
		defaultMimeTypes.put(".css", "text/css");
		defaultMimeTypes.put(".gif", "image/gif");
		defaultMimeTypes.put(".ico", "image/vnd.microsoft.icon");
		defaultMimeTypes.put(".jpeg", "image/jpeg");
		defaultMimeTypes.put(".jpg", "image/jpeg");
		defaultMimeTypes.put(".js", "text/javascript");
		defaultMimeTypes.put(".png", "image/png");
	}

	private Set<String> compressedMimeTypes = new HashSet<String>();

	{
		compressedMimeTypes.add("text/css");
		compressedMimeTypes.add("text/javascript");
		compressedMimeTypes.add("application/javascript");
		compressedMimeTypes.add("application/x-javascript");
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		String fileLocationString = servletContext.getInitParameter(INIT_PARAM_LOCATION);
		if (null != fileLocationString) {
			File location = new File(fileLocationString);
			if (location.exists() && location.isDirectory()) {
				fileLocation = location;
			}
		}
	}

	private String getRawResourcePath(HttpServletRequest request) {
		String rawResourcePath = request.getPathInfo();
		if (rawResourcePath.startsWith(RESOURCE_PREFIX)) {
			rawResourcePath = rawResourcePath.substring(RESOURCE_PREFIX.length());
		}
		return rawResourcePath;
	}
	
	@RequestMapping(value = "/resource/**/*", method = RequestMethod.GET)
	public void getResource(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String rawResourcePath = getRawResourcePath(request);
		log.debug("Attempting to GET resource: {}", rawResourcePath);

		URL[] resources = getRequestResourceUrls(rawResourcePath, request);

		if (resources == null || resources.length == 0) {
			log.debug("Resource not found: {}", rawResourcePath);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		prepareResponse(response, resources, rawResourcePath);

		OutputStream out = selectOutputStream(request, response);

		try {
			for (URL resource : resources) {
				URLConnection resourceConn = resource.openConnection();
				InputStream in = resourceConn.getInputStream();
				try {
					byte[] buffer = new byte[1024];
					while (in.available() > 0) {
						int len = in.read(buffer);
						out.write(buffer, 0, len);
					}
				} finally {
					in.close();
					try {
						resourceConn.getOutputStream().close();
					} catch (Throwable t) {
						/*ignore, just trying to free resources*/
					}
				}
			}
		} finally {
			out.close();
		}
	}

	private OutputStream selectOutputStream(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String acceptEncoding = request.getHeader("Accept-Encoding");
		String mimeType = response.getContentType();

		if (StringUtils.hasText(acceptEncoding) && acceptEncoding.contains("gzip")
				&& compressedMimeTypes.contains(mimeType)) {
			log.debug("Enabling GZIP compression for the current response.");
			return new GzipResponseStream(response);
		} else {
			log.debug("No compression for the current response.");

			log.debug(StringUtils.hasText(acceptEncoding) + "&&" + acceptEncoding.contains("gzip") + "&&" + mimeType);

			return response.getOutputStream();
		}
	}

	private void prepareResponse(HttpServletResponse response, URL[] resources, String rawResourcePath)
			throws IOException {
		long lastModified = -1;
		int contentLength = 0;
		boolean isFile = false;
		String mimeType = null;
		for (URL resource : resources) {
			if ("file".equals(resource.getProtocol())) {
				isFile = true;
			}
			URLConnection resourceConn = resource.openConnection();
			if (resourceConn.getLastModified() > lastModified) {
				lastModified = resourceConn.getLastModified();
			}

			String currentMimeType = servletContext.getMimeType(resource.getPath());
			if (currentMimeType == null) {
				String extension = resource.getPath().substring(resource.getPath().lastIndexOf('.'));
				currentMimeType = defaultMimeTypes.get(extension);
			}
			if (mimeType == null) {
				mimeType = currentMimeType;
			} else if (!mimeType.equals(currentMimeType)) {
				throw new MalformedURLException("Combined resource path: " + rawResourcePath
						+ " is invalid. All resources in a combined resource path must be of the same mime type.");
			}
			contentLength += resourceConn.getContentLength();
			try {
				resourceConn.getInputStream().close();
			} catch (Throwable t) {
				/*ignore, just trying to free resources*/
			}
			try {
				resourceConn.getOutputStream().close();
			} catch (Throwable t) {
				/*ignore, just trying to free resources*/
			}
		}

		response.setContentType(mimeType);
		response.setHeader(HTTP_CONTENT_LENGTH_HEADER, Long.toString(contentLength));
		response.setDateHeader(HTTP_LAST_MODIFIED_HEADER, lastModified);
		configureCaching(response, isFile ? 0 : 31556926);
	}

	public long getLastModified(HttpServletRequest request) {
		log.debug("Checking last modified of resource: {}", request.getPathInfo());
		URL[] resources;
		try {
			resources = getRequestResourceUrls(getRawResourcePath(request), request);
		} catch (MalformedURLException e) {
			return -1;
		}

		if (resources == null || resources.length == 0) {
			return -1;
		}

		long lastModified = -1;

		for (URL resource : resources) {
			URLConnection resourceConn;
			try {
				resourceConn = resource.openConnection();
			} catch (IOException e) {
				return -1;
			}
			if (resourceConn.getLastModified() > lastModified) {
				lastModified = resourceConn.getLastModified();
			}
			try {
				resourceConn.getInputStream().close();
			} catch (Throwable t) {
				/*ignore, just trying to free resources*/
			}
			try {
				resourceConn.getOutputStream().close();
			} catch (Throwable t) {
				/*ignore, just trying to free resources*/
			}
		}
		return lastModified;
	}

	private URL[] getRequestResourceUrls(String rawResourcePath, HttpServletRequest request)
			throws MalformedURLException {
		String appendedPaths = request.getParameter("appended");
		if (StringUtils.hasText(appendedPaths)) {
			rawResourcePath = rawResourcePath + "," + appendedPaths;
		}
		String[] localResourcePaths = StringUtils.delimitedListToStringArray(rawResourcePath, ",");
		URL[] resources = new URL[localResourcePaths.length];
		for (int i = 0; i < localResourcePaths.length; i++) {
			String localResourcePath = localResourcePaths[i];
			if (!isAllowed(localResourcePath)) {
				if (log.isWarnEnabled()) {
					log.warn("An attempt to access a protected resource at " + localResourcePath + " was disallowed.");
				}
				return null;
			}

			URL resource = null;

			// try direct file access first (development mode)
			if (null != fileLocation) {
				File file = new File(fileLocation, localResourcePath);
				log.debug("trying to find {} ({})", file.getAbsolutePath(), file.exists());
				if (file.exists()) {
					log.debug("found {} ({})", file.getAbsolutePath(), file.exists());
					resource = file.toURI().toURL();
				}
			}

			if (resource == null) {
				resource = servletContext.getResource(localResourcePath);
			}
			if (resource == null) {
				if (!isAllowed(localResourcePath)) {
					if (log.isWarnEnabled()) {
						log.warn("An attempt to access a protected resource at " + localResourcePath
								+ " was disallowed.");
					}
					return null;
				}
				log.debug("Searching classpath for resource: {}", localResourcePath);
				resource = ClassUtils.getDefaultClassLoader().getResource(localResourcePath);
				if (resource == null) {
					log.debug("Searching classpath for resource: {}", localResourcePath.substring(1));
					resource = ClassUtils.getDefaultClassLoader().getResource(localResourcePath.substring(1));
				}
			}
			if (resource == null) {
				if (resources.length > 1) {
					log.debug("Combined resource not found: {}", localResourcePath);
				}
				return null;
			} else {
				log.debug(resource.toExternalForm());
				resources[i] = resource;
			}
		}
		return resources;
	}

	private boolean isAllowed(String resourcePath) {
		if (resourcePath.matches(protectedPath)) {
			return false;
		}
		PathMatcher pathMatcher = new AntPathMatcher();
		for (String pattern : allowedResourcePaths) {
			if (pathMatcher.match(pattern, resourcePath)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Set HTTP headers to allow caching for the given number of seconds.
	 *
	 * @param response where to set the caching settings
	 * @param seconds number of seconds into the future that the response should be cacheable for
	 */
	private void configureCaching(HttpServletResponse response, int seconds) {
		// HTTP 1.0 header
		response.setDateHeader(HTTP_EXPIRES_HEADER, System.currentTimeMillis() + seconds * 1000L);
		if (seconds > 0) {
			// HTTP 1.1 header
			response.setHeader(HTTP_CACHE_CONTROL_HEADER, "max-age=" + seconds);
		} else {
			// HTTP 1.1 header
			response.setHeader(HTTP_CACHE_CONTROL_HEADER, "no-cache");

		}
	}

	/** Assure resources are gzip compressed. */
	private class GzipResponseStream extends ServletOutputStream {

		private ByteArrayOutputStream byteStream;

		private GZIPOutputStream gzipStream;

		private boolean closed;

		private HttpServletResponse response;

		private ServletOutputStream servletStream;

		public GzipResponseStream(HttpServletResponse response) throws IOException {
			super();
			closed = false;
			this.response = response;
			this.servletStream = response.getOutputStream();
			byteStream = new ByteArrayOutputStream();
			gzipStream = new GZIPOutputStream(byteStream);
		}

		public void close() throws IOException {
			if (closed) {
				throw new IOException("This output stream has already been closed");
			}
			gzipStream.finish();

			byte[] bytes = byteStream.toByteArray();

			response.setContentLength(bytes.length);
			response.addHeader("Content-Encoding", "gzip");
			servletStream.write(bytes);
			servletStream.flush();
			servletStream.close();
			closed = true;
		}

		public void flush() throws IOException {
			if (closed) {
				throw new IOException("Cannot flush a closed output stream");
			}
			gzipStream.flush();
		}

		public void write(int b) throws IOException {
			if (closed) {
				throw new IOException("Cannot write to a closed output stream");
			}
			gzipStream.write((byte) b);
		}

		public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			if (closed) {
				throw new IOException("Cannot write to a closed output stream");
			}
			gzipStream.write(b, off, len);
		}
	}
}