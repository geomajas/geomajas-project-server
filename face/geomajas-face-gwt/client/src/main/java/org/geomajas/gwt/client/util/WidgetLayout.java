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

	/** Small margin width. */
	public static int marginSmall = 5;
	/** Large margin width. */
	public static int marginLarge = 10;

	/** Background colour for the legend widget. */
	public static String legendBackgroundColor = "#FFFFFF";
	/** Row height for raster legend info. */
	public static int legendRasterRowHeight = 20;
	/** Row height for vector legend info. */
	public static int legendVectorRowHeight = 21;
	/** Icon to use for raster legend image, relative to isomorphic dir. */
	public static String legendRasterIcon = "geomajas/osgeo/layer-raster.png";
	/** Icon width for raster legend image. */
	public static int legendRasterIconWidth = 16;
	/** Icon height for raster legend image. */
	public static int legendRasterIconHeight = 16;
	/** Indent for legend labels (relative to the margin used). */
	public static int legendLabelIndent = 20;

	private WidgetLayout() {
		// do not allow instantiation.
	}

}
