/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld;

/**
 * SLD constants utility class.
 * 
 * @author An Buyle
 */
public interface SldConstant {

	String STROKE_OPACITY = "stroke-opacity";

	String STROKE = "stroke";

	String STROKE_WIDTH = "stroke-width";

	String FILL = "fill";

	String FILL_OPACITY = "fill-opacity";

	// OGC 02-070: : If the "stroke" CssParameter element is absent, the (default) color is defined to
	// be black ("#000000")
	// "stroke-opacity" CssParameter: The default value is 1.0 (opaque).
	// "stroke-width" CssParameter: The default is 1.0 (pixel).

	int DEFAULT_STROKE_OPACITY_PERCENTAGE = 100;

	int DEFAULT_STROKE_WIDTH_FOR_LINE = 1; // in pixels, the default is 1.0.

	String DEFAULT_FILL_FOR_LINE = "#000000"; // The default fill color for a lineSymbolizer (black)

	// OGC 02-070: The default if neither an ExternalGraphic nor a Mark is specified is to use the default
	// mark of a "square" with a 50%-gray (#808080) fill and a black border, with a size of 6 pixels,
	String DEFAULT_WKNAME_FOR_MARKER = "square"; // The default WellKnownName is "square"

	String DEFAULT_FILL_FOR_MARKER = "#808080"; // The default fill color for a marker

	// pointSymbolizer: 50%-gray (#808080)

	int DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER = 100; // default value in SLD is "1.0"

	int DEFAULT_SIZE_MARKER = 6; // in pixels

	int DEFAULT_STROKE_WIDTH = 1; // in pixels, the default is 1.0.

	int DEFAULT_ROTATION_MARKER = 0;

	String DEFAULT_ROTATION_MARKER_AS_STRING = "0";

	// OGC 02-070: the default value for the fill color in the polygon symbolizer context is 50% gray (value "#808080")
	String DEFAULT_FILL_FOR_POLYGON = "#808080";

	int DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON = 100; // default value in SLD is "1.0"

}