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

package org.geomajas.configuration;

import org.geomajas.annotation.Api;

/**
 * Base information which is shared between all attributes.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 * @deprecated use {@link AbstractAttributeInfo} instead
 */
@Deprecated
@Api(allMethods = true)
public class AttributeBaseInfo extends AbstractAttributeInfo {

	private static final long serialVersionUID = 152L;

	@Override
	public String getName() { // NOSONAR override needed for @Api
		return super.getName();
	}

	@Override
	public void setName(String name) { // NOSONAR override needed for @Api
		super.setName(name);
	}

	/**
	 * Indicates whether the layer is capable of of storing values at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @return does the layer have to capability of writing this attribute?
	 * @deprecated use {@link AbstractEditableAttributeInfo@isEditable} or {@link GeometryAttributeInfo@isEditable}
	 */
	@Deprecated
	public boolean isEditable() {
		return false;
	}

	/**
	 * Set whether the layer has the capability of writing values for this attribute at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @param editable "editable" capability for attribute
	 * @deprecated use {@link AbstractEditableAttributeInfo@setEditable} or {@link GeometryAttributeInfo@setEditable}
	 */
	@Deprecated
	public void setEditable(boolean editable) {
		// don't do anything, deprecated
	}
}