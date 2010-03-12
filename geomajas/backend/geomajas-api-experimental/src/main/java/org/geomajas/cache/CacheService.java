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
package org.geomajas.cache;

import org.geomajas.cache.store.RenderContent;

import java.util.Map;

/**
 * ...
 *
 * @author Joachim Van der Auwera
 */
public interface CacheService {

	String PARAM_LAYER_ID = "layerId";

	String PARAM_STYLE_ID = "styleId";

	String PARAM_CRS = "crs";

	String PARAM_X = "x";

	String PARAM_Y = "y";

	String PARAM_TILELEVEL = "tileLevel";

	String PARAM_SCALE = "scale";

	String PARAM_ORIG_X = "origX"; // not yet used

	String PARAM_ORIG_Y = "origY"; // not yet used

	String PARAM_FILTER = "filter"; // not yet used

	String PARAM_PAINT_GEOMETRIES = "paintGeometries";

	String PARAM_PAINT_LABELS = "paintLabels";

	/**
	 * Retrieve the necessary parameters form a <code>HttpServletRequest</code>
	 * object.
	 *
	 * @param request servlet request
	 * @return tile parameters map
	 * @throws CacheException This exception is thrown if any parsing goes wrong.
	 */
	//Map<String, Object> getTileParameters(HttpServletRequest request) throws CacheException;

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
	String createCacheId(Map<String, Object> parameters);

	/**
	 * Try to find a suitable directory for caching.
	 *
	 * @param config servlet config
	 * @param basePath base path
	 * @return cache directory
	 * @throws CacheException oops
	 */
	//File findCacheDirectory(ServletConfig config, String basePath) throws CacheException;

	/**
	 * Create a render content for the cache.
	 *
	 * @param parameters parameters from the servlet request
	 * @throws CacheException oops
	 */
	RenderContent createRenderContent(Map<String, Object> parameters) throws CacheException;
}
