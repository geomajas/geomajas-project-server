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

package org.geomajas.gwt.client.action.toolbar.parameter;

import org.geomajas.annotation.Api;

/**
 * Layout values for a button in ribbon or drop down panel.
 *
 * @author Emiel Ackermann
 * @since 1.11.0
 */
@Api(allMethods = true)
public enum ButtonLayoutStyle {
	/**
	 * Button layout, which is the same as a RibbonButton
	 * in a ToolbarActionList; icon (16px) on the left and title on the right.
	 */
	ICON_AND_TITLE,
	/**
	 * Button layout consisting of an icon (24px) on the
	 * left and the title and description on the right, the title on top of the description.
	 */
	ICON_TITLE_AND_DESCRIPTION
}
