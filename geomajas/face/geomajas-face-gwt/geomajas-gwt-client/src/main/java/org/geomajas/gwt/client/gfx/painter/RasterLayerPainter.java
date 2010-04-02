/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.widget.MapContext;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

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
		context.getRasterContext().drawGroup(mapWidget.getGroup(RenderGroup.RASTER), layer); // layer.getDefaultStyle???

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
