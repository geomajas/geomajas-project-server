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
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.widget.MapWidget;

/**
 * Actual painter for the {@link org.geomajas.smartgwt.client.map.MapModel} object. Prepares some groups, and sets the
 * correct transformations. Also prepares a group for objects that are to be painted in world space. (the MapModel's
 * WorldSpacePaintables)
 * 
 * @author Pieter De Graef
 */
public class MapModelPainter implements Painter {

	private MapWidget mapWidget;

	public MapModelPainter(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	}

	// -------------------------------------------------------------------------
	// Painter implementation:
	// -------------------------------------------------------------------------

	public String getPaintableClassName() {
		return MapModel.class.getName();
	}

	/**
	 * The actual painting function. Draws the basic groups.
	 * 
	 * @param paintable
	 *            A {@link org.geomajas.smartgwt.client.map.MapModel} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		// Group for objects in raster space
		context.getRasterContext().drawGroup(null, mapWidget.getGroup(MapWidget.RenderGroup.RASTER),
				mapWidget.getMapModel().getMapView()

				.getPanToViewTranslation());

		// Group for objects in vector space
		context.getVectorContext().drawGroup(null, mapWidget.getGroup(MapWidget.RenderGroup.VECTOR),
				mapWidget.getMapModel().getMapView().getPanToViewTranslation());

		// Group for objects in world space
		context.getVectorContext().drawGroup(null, mapWidget.getGroup(MapWidget.RenderGroup.WORLD),
				mapWidget.getMapModel().getMapView().getPanToViewTranslation());

		// Group for objects in screen space
		context.getVectorContext().drawGroup(null, mapWidget.getGroup(MapWidget.RenderGroup.SCREEN));
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist, nothing
	 * will be done.
	 * 
	 * @param paintable
	 *            The MapModel
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		context.getRasterContext().deleteGroup(mapWidget.getGroup(MapWidget.RenderGroup.RASTER));
		context.getVectorContext().deleteGroup(mapWidget.getGroup(MapWidget.RenderGroup.VECTOR));
		context.getVectorContext().deleteGroup(mapWidget.getGroup(MapWidget.RenderGroup.WORLD));
		context.getVectorContext().deleteGroup(mapWidget.getGroup(MapWidget.RenderGroup.SCREEN));
	}
}
