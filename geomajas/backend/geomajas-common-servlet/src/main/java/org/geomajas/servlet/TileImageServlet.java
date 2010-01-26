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

import org.geomajas.cache.CacheException;
import org.geomajas.cache.CacheService;
import org.geomajas.cache.TileCacheService;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p> This servlet will retrieve raster images for rendering tiles. To do so, it uses a caching system to speed up the
 * process. </p>
 *
 * @author Pieter De Graef
 */
public class TileImageServlet extends HttpServlet {

	private static final long serialVersionUID = 5396873956988773365L;

	private final Logger log = LoggerFactory.getLogger(TileImageServlet.class);

	private static final String PARAM_USE_CACHE = "useCache";

	private static final String PARAM_BASE_PATH = "cacheBasePath";

	private static final String PARAM_MAXIMUM_SIZE = "cacheMaximumSize";

	private static final String DEFAULT_CACHE_DIRECTORY = "WEB-INF/cache";

	private static long TOTAL_TIME;

	private static int COUNT;

	private Calendar calendar;

	private TileCacheService service;

	private CacheService cacheService;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		calendar = new GregorianCalendar();

		String basePath = config.getInitParameter(PARAM_BASE_PATH);
		String maximumSize = config.getInitParameter(PARAM_MAXIMUM_SIZE);
		String useCacheStr = config.getInitParameter(PARAM_USE_CACHE);
		boolean useCache = false;
		if (useCacheStr != null && useCacheStr.equalsIgnoreCase("true")) {
			useCache = true;
		}

		if (basePath == null) {
			basePath = DEFAULT_CACHE_DIRECTORY;
			File file = new File(config.getServletContext().getRealPath(basePath));
			if (useCache && !file.exists()) {
				boolean res = file.mkdir();
				if (!res) {
					log.warn("TileImageServlet could not create cache directory " + file.getAbsolutePath());
				}
			}
		}
		ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext(config);
		cacheService = applicationContext.getBean("cache.CacheService", CacheService.class);

		File baseDirectory;
		try {
			baseDirectory = cacheService.findCacheDirectory(config, basePath);
		} catch (CacheException e1) {
			try {
				log.error(e1.getMessage());
				baseDirectory = cacheService.findCacheDirectory(config, DEFAULT_CACHE_DIRECTORY);
			} catch (CacheException e) {
				baseDirectory = null;
			}
		}

		ApplicationService runtime =
				applicationContext.getBean("service.ApplicationService", ApplicationService.class);
		runtime.setTileCacheDirectory(baseDirectory.getAbsolutePath());
		try {
			runtime.setTileCacheMaximumSize(Integer.parseInt(maximumSize));
		} catch (NumberFormatException nfe) {
			log.warn("cacheMaximumSize {} cannot be converted to integer", maximumSize);
		}
		runtime.setTileCacheEnabled(useCache);

		service = applicationContext.getBean("internal.cache.TileCacheService", TileCacheService.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		long start = System.currentTimeMillis();

		RenderContent content;
		try {
			content = service.get(cacheService.createRenderContent(cacheService.getTileParameters(request)));
		} catch (CacheException e) {
			content = null;
		}

		if (content != null) {
			// int maxTime = 3600 * 24 * 365; // 1 year
			int maxTime = 10;
			response.setHeader("Cache-Control", "max-age=" + maxTime);
			response.setHeader("Expires", createExpiresString(maxTime));
			response.setContentType("image/png");
			response.setStatus(200);

			response.getOutputStream().write(content.getContent());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} else {
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html");
			response.setStatus(500);
		}

		long time = System.currentTimeMillis() - start;
		COUNT++;
		TOTAL_TIME += time;
		log.info("Image rendering time: " + time + ", average: " + (TOTAL_TIME / COUNT));
	}

	public String createExpiresString(int seconds) {
		calendar.setTimeInMillis(System.currentTimeMillis() + seconds * 1000);
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));		
		return format.format(calendar.getTime());
	}
}