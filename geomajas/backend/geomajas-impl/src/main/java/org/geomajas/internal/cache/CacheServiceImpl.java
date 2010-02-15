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

package org.geomajas.internal.cache;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.CacheService;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.cache.store.DefaultRenderContent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * General utility class for functions concerning caching. TODO: The filter
 * parameter is not yet used. This is quite urgent.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public final class CacheServiceImpl implements CacheService {

	/**
	 * Retrieve the necessary parameters form a <code>HttpServletRequest</code>
	 * object.
	 *
	 * @param request
	 * @return
	 * @throws org.geomajas.cache.CacheException
	 *             This exception is thrown if any parsing goes wrong.
	 */
	public Map<String, Object> getTileParameters(HttpServletRequest request) throws CacheException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			String temp = request.getParameter(PARAM_APPLICATION_ID);
			if (temp == null) {
				throw new CacheException(ExceptionCode.CACHE_NO_APPLICATION_ID);
			}
			parameters.put(PARAM_APPLICATION_ID, temp);

			temp = request.getParameter(PARAM_LAYER_ID);
			if (temp == null) {
				throw new CacheException(ExceptionCode.CACHE_NO_LAYER_ID);
			}
			parameters.put(PARAM_LAYER_ID, temp);

			parameters.put(PARAM_X, Integer.parseInt(request.getParameter(PARAM_X)));
			parameters.put(PARAM_Y, Integer.parseInt(request.getParameter(PARAM_Y)));
			parameters.put(PARAM_TILELEVEL, Integer.parseInt(request.getParameter(PARAM_TILELEVEL)));
			parameters.put(PARAM_SCALE, Double.parseDouble(request.getParameter(PARAM_SCALE)));
			parameters.put(PARAM_ORIG_X, Double.parseDouble(request.getParameter(PARAM_ORIG_X)));
			parameters.put(PARAM_ORIG_Y, Double.parseDouble(request.getParameter(PARAM_ORIG_Y)));

			temp = request.getParameter(PARAM_FILTER);
			if (temp != null && !"null".equals(temp)) {
				parameters.put(PARAM_FILTER, temp);
			}

			temp = request.getParameter(PARAM_PAINT_GEOMETRIES);
			if (temp != null && !"null".equals(temp)) {
				parameters.put(PARAM_PAINT_GEOMETRIES, Boolean.parseBoolean(temp));
			} else {
				parameters.put(PARAM_PAINT_GEOMETRIES, false);
			}

			temp = request.getParameter(PARAM_PAINT_LABELS);
			if (temp != null && !"null".equals(temp)) {
				parameters.put(PARAM_PAINT_LABELS, Boolean.parseBoolean(temp));
			} else {
				parameters.put(PARAM_PAINT_LABELS, false);
			}

			return parameters;
		} catch (CacheException e) {
			throw e;
		} catch (Exception e) {
			throw new CacheException(e, ExceptionCode.CACHE_UNEXPECTED_FAILURE);
		}
	}

	/**
	 * Create a unique ID given a set of parameters. This ID is used in the
	 * <code>RenderContent</code> and <code>MetaRenderContent</code> objects.
	 *
	 * @param parameters
	 *            The same set of parameters, acquired by the
	 *            "getTileParameters" function in this class.
	 * @return Returns a unique hash-code ID. TODO AB: This is not 100% unique,
	 *         but maybe enough for this usage. For instance
	 *         "planbatenmainMap.refgem_5_4_26_nullfalse" is 40 chars. Quote
	 *         http://mindprod.com/jgloss/hashcode.html: "equal hashCodes in
	 *         general are not sufficient to ensure Object equality. However, if
	 *         the hashCodes are not equal, you know the Objects can't possibly
	 *         be equal. Consider how many 50-character Strings there are
	 *         (6553550) and how many possible hashCodes there are (232). It
	 *         should be obvious there are way more Strings than hashCodes. So
	 *         the same hashCode has to be reused over and over for different
	 *         Strings." Here: parameter values are normally ascii and only
	 *         certain chars, assume 6440 = 2^(6*40) possibilities still much
	 *         bigger than 232) So we maybe need an extra verification step by
	 *         checking that the parameters are identical before using a cache
	 *         entry. And no caching of object that matches existing hashkey but
	 *         different parameters.
	 */
	public String createCacheId(Map<String, Object> parameters) {
		return "" + parameters.get(PARAM_APPLICATION_ID) + "_" + parameters.get(PARAM_LAYER_ID) + "_"
				+ parameters.get(PARAM_TILELEVEL) + "_" + parameters.get(PARAM_X) + "_"
				+ parameters.get(PARAM_Y) + "_" + parameters.get(PARAM_FILTER) + "_"
				+ parameters.get(PARAM_PAINT_GEOMETRIES) + "_" + parameters.get(PARAM_PAINT_LABELS) + "_"
				+ parameters.get(PARAM_ORIG_X) + "_" + parameters.get(PARAM_ORIG_Y)
				+ "_" + parameters.get(PARAM_SCALE);
	}

	/**
	 * Try to find a suitable directory for caching.
	 *
	 * @param config servlet config
	 * @param basePath base path
	 * @return cache directory
	 */
	public File findCacheDirectory(ServletConfig config, String basePath) throws CacheException {
		File file = new File(basePath);
		if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
			// If it does not exist, try and look at it as a relative directory:
			String realPath = config.getServletContext().getRealPath(basePath);
			file = new File(realPath);
			if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
				throw new CacheException(ExceptionCode.CACHE_NOT_WRITABLE, basePath);
			}
			return file;
		}
		return file;
	}

	public RenderContent createRenderContent(Map<String, Object> parameters)
			throws CacheException {
		return new DefaultRenderContent(parameters, this);
	}
}