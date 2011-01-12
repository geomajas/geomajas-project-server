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