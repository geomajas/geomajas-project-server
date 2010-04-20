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

package org.geomajas.configuration;

import org.geomajas.global.Api;

import java.io.Serializable;

/**
 * Base information which is shared between all attributes.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class AttributeBaseInfo implements Serializable {

	private static final long serialVersionUID = 152L;
	private String name;
	private boolean editable;

	/**
	 * Get name of the attribute.
	 * <p/>
	 * This is also the name which is used to identify the attribute at the data level and
	 * is assumed to be unique across the attributes for the feature.
	 * <p/>
	 * It depends on the layer whether an attribute name is case dependent or independent.
	 *
	 * @return attribute name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the attribute.
	 * <p/>
	 * This is also the name which is used to identify the attribute at the data level and
	 * is assumed to be unique across the attributes for the feature.
	 * <p/>
	 * It depends on the layer whether an attribute name is case dependend or independent.
	 *
	 * @param name attribute name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Indicates whether the layer is capable of of storing values at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @return does the layer have to capability of writing this attribute?
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Set whether the layer has the capability of writing values for this attribute at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @param editable "editable" capability for attribute
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}