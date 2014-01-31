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

package org.geomajas.service;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FontStyleInfo;

/**
 * Utility functions for calculating text and font related parameters server-side. These parameters can of course be
 * calculated more accurately on the displaying device itself, but unfortunately there is no support for this in browser
 * environments.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public interface TextService {

	/**
	 * Returns the dimensions of a text string rendered with the specified font style.
	 * 
	 * @param text the string
	 * @param fontStyle the font style
	 * @return dimension object containing the width and height
	 */
	Rectangle2D getStringBounds(String text, FontStyleInfo fontStyle);
	
	/**
	 * Convert the specified font style to an AWT font.
	 * 
	 * @param fontStyle
	 *            the font style
	 * @return the AWT font
	 */
	Font getFont(FontStyleInfo fontStyle);

}
