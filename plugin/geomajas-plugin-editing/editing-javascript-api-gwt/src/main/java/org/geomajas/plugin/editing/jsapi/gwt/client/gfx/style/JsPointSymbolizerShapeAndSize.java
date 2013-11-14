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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx.style;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.gwt.client.gfx.PointSymbolizerShapeAndSize;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * JavaScript wrapper of {@link org.geomajas.plugin.editing.gwt.client.gfx.PointSymbolizerShapeAndSize}.
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 *
 */
@Export("PointSymbolizerShapeAndSize")
@ExportPackage("org.geomajas.plugin.editing.gfx.style")
@Api(allMethods = true)
public class JsPointSymbolizerShapeAndSize implements ExportOverlay<PointSymbolizerShapeAndSize> {

	/**
	 * Get the shape type of the point symbolizer. Can be a square or circle.
	 *
	 * @return The shape type as a string.
	 */
	@ExportInstanceMethod
	public static String getShape(PointSymbolizerShapeAndSize instance) {
		switch (instance.getShape()) {
			case SQUARE:
				return "square";
			case CIRCLE:
				return "circle";
			default:
				return "unknown";
		}
	}

	/**
	 * Set the shape type of the point symbolizer. Can be a square (default) or circle.
	 *
	 * @param shape The shape type as a string.
	 */
	@ExportInstanceMethod
	public static void setShape(PointSymbolizerShapeAndSize instance, String shape) {
		if ("circle".equals(shape)) {
			instance.setShape(PointSymbolizerShapeAndSize.Shape.CIRCLE);
		} else if ("square".equals(shape)) {
			instance.setShape(PointSymbolizerShapeAndSize.Shape.SQUARE);
		} else {
			//default value
			instance.setShape(PointSymbolizerShapeAndSize.Shape.SQUARE);
		}
	}

	/**
	 * Set size of the point symbolizer.
	 *
	 * @param size
	 */
	public void setSize(int size) {

	}

	/**
	 * Get size of the point symbolizer.
	 *
	 * @return size
	 */
	public int getSize() {
		return 0;
	}

}
