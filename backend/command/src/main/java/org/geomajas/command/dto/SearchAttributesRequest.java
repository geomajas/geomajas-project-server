/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.feature.SearchAttributes";

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
