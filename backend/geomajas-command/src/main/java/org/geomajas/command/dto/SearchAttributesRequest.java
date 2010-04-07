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
package org.geomajas.command.dto;

import org.geomajas.command.LayerIdCommandRequest;

/**
 * Request object for {@link org.geomajas.command.feature.SearchAttributesCommand}.
 * 
 * @author Pieter De Graef
 */
public class SearchAttributesRequest extends LayerIdCommandRequest {

	private static final long serialVersionUID = 154L;

	private String attributeName;

	private String filter;

	// Constructors:

	public SearchAttributesRequest() {
	}

	public SearchAttributesRequest(String layerId, String attributeName) {
		setLayerId(layerId);
		this.attributeName = attributeName;
	}

	public SearchAttributesRequest(String layerId, String attributeName, String filter) {
		setLayerId(layerId);
		this.attributeName = attributeName;
		this.filter = filter;
	}

	// Getters and setters:

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
