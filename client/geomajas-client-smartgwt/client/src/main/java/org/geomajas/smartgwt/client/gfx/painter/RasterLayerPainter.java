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
package org.geomajas.smartgwt.client.gfx.painter;

import org.geomajas.smartgwt.client.gfx.MapContext;
import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.Painter;
import org.geomajas.smartgwt.client.map.layer.RasterLayer;
import org.geomajas.smartgwt.client.widget.MapWidget;

/**
 * Paints a raster layer.
 * 
 * @author Jan De Moerloose
 */
public class RasterLayerPainter implements Painter {

	private MapWidget mapWidget;

	public RasterLayerPainter(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	}
	
	public String getPaintableClassName() {
		return RasterLayer.class.getName();
	}

	/**
	 * The actual painting function. Draws the groups.
	 * 
	 * @param paintable
	 *            A {@link RasterLayer} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		RasterLayer layer = (RasterLayer) paintable;

		// Create the needed groups in the correct order:
		context.getRasterContext().drawGroup(mapWidget.getGroup(MapWidget.RenderGroup.RASTER), layer);
		// layer.getDefaultStyle???

		// Check layer visibility:
		if (layer.isShowing()) {
			context.getRasterContext().unhide(layer);
		} else {
			context.getRasterContext().hide(layer);
		}

	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist, nothing
	 * will be done.
	 * 
	 * @param paintable
	 *            The raster layer
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		context.getRasterContext().deleteGroup(paintable);
	}

}
