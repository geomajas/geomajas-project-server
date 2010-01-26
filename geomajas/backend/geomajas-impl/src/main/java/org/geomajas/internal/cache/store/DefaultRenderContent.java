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

package org.geomajas.internal.cache.store;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.CacheService;
import org.geomajas.cache.store.RenderContent;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The actual rendering content. It contains all the parameters that were passed
 * to the HttpServlet to uniquely build/identify a raster.
 * </p>
 *
 * @author Pieter De Graef
 */
public class DefaultRenderContent implements RenderContent {

	/**
	 * A unique ID. It is built from the given set of parameters.
	 */
	private String id;

	/**
	 * The raster's byte array content.
	 */
	private byte[] content;

	/**
	 * A set of parameters to know what raster to retrieve/build.
	 */
	private Map<String, Object> parameters;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Use a meta information object to construct a RenderContent object. Of
	 * course, there will be no real content yet.
	 */
	public DefaultRenderContent(MetaRenderContent meta) {
		this.id = meta.getId();
		parameters = new HashMap<String, Object>();
		parameters.put(CacheService.PARAM_TILELEVEL, meta.getTileLevel());
		parameters.put(CacheService.PARAM_X, meta.getX());
		parameters.put(CacheService.PARAM_Y, meta.getY());
	}

	/**
	 * Initialize this object with the HttpServletRequest. From this request,
	 * all needed information is taken to build the set of parameters, build an
	 * ID, and even acquire the application controller form the session object.
	 */
	public DefaultRenderContent(Map<String, Object> parameters, CacheService cacheService)
			throws CacheException {
		this.parameters = parameters;
		id = cacheService.createCacheId(parameters);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}