/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.Attribute;

/**
 * Result object for {@link org.geomajas.command.feature.SearchAttributesCommand}.
 * 
 * @author Pieter De Graef
 */
public class SearchAttributesResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private List<Attribute<?>> attributes;

	// Constructors:

	public SearchAttributesResponse() {
	}

	// Getters and setters:

	public List<Attribute<?>> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute<?>> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "SearchAttributesResponse{" +
				"attributes=" + attributes +
				'}';
	}
}