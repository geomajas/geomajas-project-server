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
 * @since 1.0.0
 *
 */
@Export("ShapeStyle")
@ExportPackage("org.geomajas.plugin.editing.gfx.style")
@Api(allMethods = true)
public interface JsShapeStyle extends ExportOverlay<ShapeStyle> {

	String getFillColor() ;

	void setFillColor(String fillColor) ;

	float getFillOpacity() ;

	void setFillOpacity(float fillOpacity) ;

	String getStrokeColor() ;

	void setStrokeColor(String strokeColor) ;

	float getStrokeOpacity() ;

	void setStrokeOpacity(float strokeOpacity) ;

	float getStrokeWidth() ;

	void setStrokeWidth(float strokeWidth) ;

}
