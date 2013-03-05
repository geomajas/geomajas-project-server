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

package org.geomajas.plugin.printing.client.util;

import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class PrintingLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Blank "waiting" image. */
	public static String iconWaitBlank = "[ISOMORPHIC]/geomajas/plugin/printing/pleasewait-blank.gif";
	/** Moving "waiting" image. */
	public static String iconWaitMoving = "[ISOMORPHIC]/geomajas/plugin/printing/pleasewait.gif";
	/** Wait image width. */
	public static int iconWaitWidth = 214;
	/** Wait image height. */
	public static int iconWaitHeight = 15;

	/** Width for the print preferences window. */
	public static String printPreferencesWidth = "400";
	/** Height for the print preferences window. */
	public static String printPreferencesHeight = "330";
	/** Width for the resolution selector in the print preferences window. */
	public static String printPreferencesResolutionWidth = "250";
	/** Height for the resolution selector in the  print preferences window. */
	public static String printPreferencesResolutionHeight = "30";

	/** X margin for the default template. */
	public static double templateMarginX = 20;
	/** Y margin for the default template. */
	public static double templateMarginY = 20;
	/** Width of the north arrow in the template. */
	public static double templateNorthArrowWidth = 10;
	/** Font family for the legend in the template. */
	public static String templateDefaultFontFamily = "Dialog";
	/** Font style for the legend in the template. */
	public static String templateDefaultFontStyle = "Italic";
	/** Font size for the legend in the template. */
	public static double templateDefaultFontSize = 14;
	/** Background colour for the legend in the template. */
	public static String templateDefaultBackgroundColor = "0xFFFFFF";
	/** Border colour for the legend in the template. */
	public static String templateDefaultBorderColor = "0x000000";
	/** Font colour for the legend in the template. */
	public static String templateDefaultColor = "0x000000";
	/** Should the default template include a scale bar? */
	public static boolean templateIncludeScaleBar = true;
	/** Should the default template include a legend? */
	public static boolean templateIncludeLegend = true;
	/** Should the default template include a north arrow? */
	public static boolean templateIncludeNorthArrow = true;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private PrintingLayout() {
		// do not allow instantiation.
	}

}
