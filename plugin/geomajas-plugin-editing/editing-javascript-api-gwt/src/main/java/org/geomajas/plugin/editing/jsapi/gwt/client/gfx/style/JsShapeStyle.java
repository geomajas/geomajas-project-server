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
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * JavaScript wrapper of {@link org.geomajas.gwt.client.gfx.style.ShapeStyle}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @since 1.0.0
 *
 */
@Export("ShapeStyle")
@ExportPackage("org.geomajas.plugin.editing.gfx.style")
@Api(allMethods = true)
public interface JsShapeStyle extends ExportOverlay<ShapeStyle> {

	/**
	 * Gets the fill color.
	 *
	 * @return fill color
	 */
	String getFillColor() ;

	/**
	 * Sets the fill color.
	 *
	 * @param fillColor
	 */
	void setFillColor(String fillColor) ;

	/**
	 * Gets the fill opacity.
	 *
	 * @return fill opacity
	 */
	float getFillOpacity() ;

	/**
	 * Sets the fill opacity.
	 *
	 * @param fillOpacity
	 */
	void setFillOpacity(float fillOpacity) ;

	/**
	 * Gets the stroke color.
	 *
	 * @return stroke color
	 */
	String getStrokeColor() ;

	/**
	 * Sets the stroke color.
	 *
	 * @param strokeColor
	 */
	void setStrokeColor(String strokeColor) ;

	/**
	 * Gets the stroke opacity.
	 *
	 * @return stroke opacity
	 */
	float getStrokeOpacity() ;

	/**
	 * Sets the stroke opacity.
	 *
	 * @param strokeOpacity
	 */
	void setStrokeOpacity(float strokeOpacity) ;

	/**
	 * Gets the stroke width.
	 *
	 * @return stroke width
	 */
	float getStrokeWidth() ;

	/**
	 * Sets the stroke width.
	 *
	 * @param strokeWidth
	 */
	void setStrokeWidth(float strokeWidth) ;

}
