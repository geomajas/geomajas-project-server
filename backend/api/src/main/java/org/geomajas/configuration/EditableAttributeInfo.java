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
 * ...
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public interface EditableAttributeInfo {

	/**
	 * Indicates whether the layer is capable of of storing values at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @return does the layer have to capability of writing this attribute?
	 */
	boolean isEditable();

	/**
	 * Set whether the layer has the capability of writing values for this attribute at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @param editable "editable" capability for attribute
	 */
	void setEditable(boolean editable);
}
