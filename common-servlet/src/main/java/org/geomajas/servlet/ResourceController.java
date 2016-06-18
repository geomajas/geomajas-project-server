/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.servlet;

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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * Resource controller for efficiently resolving and rendering static resources from within a JAR file. The cache
 * header is set to a date in the far future to improve caching. For development, you can specify a "files-location"
 * servlet init parameter. Any resources are searched at that location first. When found, the cache header is not set.
 * This assures the jar does not need building for testing.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
@Controller("/resource/**")
public class ResourceController implements LastModified, ServletContextAware {

	private static final String RESOURCE_PREFIX = "/resource";
	private static final String HTTP_CONTENT_LENGTH_HEADER = "Content-Length";
	private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";
	private static final String HTTP_EXPIRES_HEADER = "Expires";
	private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";
	private static final String INIT_PARAM_LOCATION = "files-location";

	private ServletContext servletContext;
	
	private boolean compressionAllowed = true;

	private boolean includeServletPath;

	private final Logger log = LoggerFactory.getLogger(ResourceController.class);
	private static final String PROTECTED_PATH = "/?WEB-INF/.*";
	private static final String[] ALLOWED_RESOURCE_PATHS = new String[]{
			"/**/*.css", "/**/*.gif", "/**/*.ico", "/**/*.jpeg",
			"/**/*.jpg", "/**/*.js", "/**/*.html", "/**/*.png",
			"/**/*.otf", "/**/*.eot", "/**/*.ttf", "/**/*.woff",
			"/**/*.woff2", "/**/*.svg",
			"META-INF/**/*.css", "META-INF/**/*.gif", "META-INF/**/*.ico", "META-INF/**/*.jpeg",
			"META-INF/**/*.jpg", "META-INF/**/*.js", "META-INF/**/*.html", "META-INF/**/*.png",
			"META-INF/**/*.otf", "META-INF/**/*.eot", "META-INF/**/*.ttf", "META-INF/**/*.woff",
			"META-INF/**/*.woff2", "META-INF/**/*.svg",
	};
	
	private String[] allowedResourcePaths;

	private File fileLocation;

	private static final Map<String, String> DEFAULT_MIME_TYPES = new HashMap<String, String>();
	private static final Set<String> COMPRESSED_MIME_TYPES = new HashSet<String>();

	static
	{
		DEFAULT_MIME_TYPES.put(".css", "text/css");
		DEFAULT_MIME_TYPES.put(".gif", "image/gif");
		DEFAULT_MIME_TYPES.put(".ico", "image/vnd.microsoft.icon");
		DEFAULT_MIME_TYPES.put(".jpeg", "image/jpeg");
		DEFAULT_MIME_TYPES.put(".jpg", "image/jpeg");
		DEFAULT_MIME_TYPES.put(".js", "text/javascript");
		DEFAULT_MIME_TYPES.put(".png", "image/png");

		COMPRESSED_MIME_TYPES.add("text/css");
		COMPRESSED_MIME_TYPES.add("text/javascript");
		COMPRESSED_MIME_TYPES.add("application/javascript");
		COMPRESSED_MIME_TYPES.add("application/x-javascript");
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
	
	public String[] getAllowedResourcePaths() {
		return allowedResourcePaths;
	}
	
	public void setAllowedResourcePaths(String[] allowedResourcePaths) {
		this.allowedResourcePaths = allowedResourcePaths;
	}

	/**
	 * Returns whether the controller should allow compression of resources. Compression will only be applied if accept
	 * headers are present, however.
	 * 
	 * @return true if compression allowed
	 */
	public boolean isCompressionAllowed() {
		return compressionAllowed;
	}
	
	/**
	 * Determines whether the controller should allow compression to resources.
	 * @param compressionAllowed true if allowed
	 */
	public void setCompressionAllowed(boolean compressionAllowed) {
		this.compressionAllowed = compressionAllowed;
	}
	
	/**
	 * Returns whether the resource path should equal the full servlet path (servlet path + pathinfo). If set to true,
	 * the resource path will include the url mapping part of the servlet. If false, only the pathinfo will be taken.
	 * 
	 * @return true if servlet path should be taken into account
	 */
	public boolean isIncludeServletPath() {
		return includeServletPath;
	}

	/**
	 * Sets whether the resource path should equal the full servlet path (servlet path + pathinfo). If set to true,
	 * the resource path will include the url mapping part of the servlet. If false, only the pathinfo will be taken.
	 * 
	 * @param includeServletPath true if included
	 */
	public void setIncludeServletPath(boolean includeServletPath) {
		this.includeServletPath = includeServletPath;
	}

	protected String getRawResourcePath(HttpServletRequest request) {
		String rawResourcePath = null;
		if (isIncludeServletPath()) {
			rawResourcePath = request.getServletPath() + request.getPathInfo();
		} else {
			rawResourcePath = request.getPathInfo();
		}
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
					} catch (Throwable t) { //NOPMD
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
				&& COMPRESSED_MIME_TYPES.contains(mimeType) && isCompressionAllowed()) {
			log.debug("Enabling GZIP compression for the current response.");
			return new GzipResponseStream(response);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("No compression for the current response.");
				log.debug("Accept-Encoding : " + acceptEncoding);
				log.debug("Content-type : " + mimeType);
			}

			return response.getOutputStream();
		}
	}

	private void prepareResponse(HttpServletResponse response, URL[] resources, String rawResourcePath)
			throws IOException {
		long lastModified = -1;
		int contentLength = 0;
		String mimeType = null;
		for (URL resource : resources) {
			URLConnection resourceConn = resource.openConnection();
			if (resourceConn.getLastModified() > lastModified) {
				lastModified = resourceConn.getLastModified();
			}

			String currentMimeType = servletContext.getMimeType(resource.getPath());
			if (currentMimeType == null) {
				String extension = resource.getPath().substring(resource.getPath().lastIndexOf('.'));
				currentMimeType = DEFAULT_MIME_TYPES.get(extension);
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
			} catch (IOException e) {
				/*ignore, just trying to free resources*/
			}
			try {
				resourceConn.getOutputStream().close();
			} catch (IOException e) {
				/*ignore, just trying to free resources*/
			}
		}

		response.setContentType(mimeType);
		response.setHeader(HTTP_CONTENT_LENGTH_HEADER, Long.toString(contentLength));
		response.setDateHeader(HTTP_LAST_MODIFIED_HEADER, lastModified);
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
			} catch (IOException e) {
				/*ignore, just trying to free resources*/
			}
			try {
				resourceConn.getOutputStream().close();
			} catch (IOException e) {
				/*ignore, just trying to free resources*/
			}
		}
		return lastModified;
	}

	protected URL[] getRequestResourceUrls(String rawResourcePath, HttpServletRequest request)
			throws MalformedURLException {
		String appendedPaths = request.getParameter("appended");
		// don't allow multiple resources if compression is off
		if (StringUtils.hasText(appendedPaths) && isCompressionAllowed()) {
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
		if (resourcePath.matches(PROTECTED_PATH)) {
			return false;
		}
		PathMatcher pathMatcher = new AntPathMatcher();
		for (String pattern : (allowedResourcePaths == null ? ALLOWED_RESOURCE_PATHS : allowedResourcePaths)) {
			if (pathMatcher.match(pattern, resourcePath)) {
				return true;
			}
		}
		return false;
	}
}