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

package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api
public final class WidgetLayout {

	private static int marginSmall = 5;
	private static int marginLarge = 10;

	private static String legendBackgroundColor = "#FFFFFF";
	private static int legendRasterRowHeight = 20;
	private static int legendVectorRowHeight = 21;
	private static String legendRasterIcon = "geomajas/osgeo/layer-raster.png";
	private static int legendRasterIconWidth = 16;
	private static int legendRasterIconHeight = 16;
	private static int legendLabelIndent = 20;

	private WidgetLayout() {
		// do not allow instantiation.
	}

	public static int getMarginSmall() {
		return marginSmall;
	}

	public static void setMarginSmall(int marginSmall) {
		WidgetLayout.marginSmall = marginSmall;
	}

	public static int getMarginLarge() {
		return marginLarge;
	}

	public static void setMarginLarge(int marginLarge) {
		WidgetLayout.marginLarge = marginLarge;
	}

	public static String getLegendBackgroundColor() {
		return legendBackgroundColor;
	}

	public static void setLegendBackgroundColor(String legendBackgroundColor) {
		WidgetLayout.legendBackgroundColor = legendBackgroundColor;
	}

	public static int getLegendRasterRowHeight() {
		return legendRasterRowHeight;
	}

	public static void setLegendRasterRowHeight(int legendRasterRowHeight) {
		WidgetLayout.legendRasterRowHeight = legendRasterRowHeight;
	}

	public static int getLegendVectorRowHeight() {
		return legendVectorRowHeight;
	}

	public static void setLegendVectorRowHeight(int legendVectorRowHeight) {
		WidgetLayout.legendVectorRowHeight = legendVectorRowHeight;
	}

	public static String getLegendRasterIcon() {
		return legendRasterIcon;
	}

	public static void setLegendRasterIcon(String legendRasterIcon) {
		WidgetLayout.legendRasterIcon = legendRasterIcon;
	}

	public static int getLegendRasterIconWidth() {
		return legendRasterIconWidth;
	}

	public static void setLegendRasterIconWidth(int legendRasterIconWidth) {
		WidgetLayout.legendRasterIconWidth = legendRasterIconWidth;
	}

	public static int getLegendRasterIconHeight() {
		return legendRasterIconHeight;
	}

	public static void setLegendRasterIconHeight(int legendRasterIconHeight) {
		WidgetLayout.legendRasterIconHeight = legendRasterIconHeight;
	}

	public static int getLegendLabelIndent() {
		return legendLabelIndent;
	}

	public static void setLegendLabelIndent(int legendLabelIndent) {
		WidgetLayout.legendLabelIndent = legendLabelIndent;
	}
}
