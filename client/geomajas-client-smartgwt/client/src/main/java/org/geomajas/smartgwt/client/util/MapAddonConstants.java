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
package org.geomajas.smartgwt.client.util;

import org.geomajas.annotation.Api;

/**
 * Constants for the map addons.
 * 
 * @author Emiel Ackermann
 * @since 1.10.0
 */
@Api
public abstract class MapAddonConstants {
	
	private MapAddonConstants() {
	}
	
	/**
	 * Addon button dimension, which also is used to calculate all the other dimensions in this class. 
	 * Change this value to adjust them all.
	 */
	// CHECKSTYLE VISIBILITY MODIFIER: OFF
	public static Integer buttonDia = 36;
	// CHECKSTYLE VISIBILITY MODIFIER: ON
	public static final Integer ADDON_ICON_DIA = buttonDia * 2 / 3;
	public static final Integer ADDON_MARGIN = (buttonDia - ADDON_ICON_DIA) / 2;
	public static final Integer PAN_DIA = buttonDia * 2 - ADDON_MARGIN;
	public static final Integer PAN_MARGIN = Math.round(PAN_DIA * 0.32f); 
	public static final Integer PAN_ARROW_DIA = Math.round(PAN_DIA * 0.36f);
}
