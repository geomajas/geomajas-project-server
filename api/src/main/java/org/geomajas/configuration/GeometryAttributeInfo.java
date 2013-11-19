/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration;

import org.geomajas.annotation.Api;

/**
 * Geometric attribute configuration information.
 *
 * @author Joachim Van der Auwera 
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GeometryAttributeInfo extends AttributeBaseInfo implements EditableAttributeInfo {

	private static final long serialVersionUID = 152L;

	private boolean editable;

	/**
	 * Indicates whether the layer is capable of of changing the geometry value for a feature.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the geometry, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 * <p/>
	 * Note that when editable is false, it will be impossible to create new features.
	 *
	 * @return does the layer have to capability of writing this attribute?
	 * @since 1.10.0
	 */
	@SuppressWarnings("deprecation")
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Set whether the layer is capable of of changing the geometry value for a feature.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the geometry, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 * <p/>
	 * Note that when editable is false, it will be impossible to create new features.
	 *
	 * @param editable "editable" capability for attribute
	 * @since 1.10.0
	 */
	@SuppressWarnings("deprecation")
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
