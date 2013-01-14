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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

/**
 * Actual painter for the {@link MapModel} object. Prepares some groups, and sets the correct transformations. Also
 * prepares a group for objects that are to be painted in world space. (the MapModel's WorldSpacePaintables)
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
	 *            A {@link org.geomajas.gwt.client.map.MapModel} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		// Group for objects in raster space
		context.getRasterContext().drawGroup(null, mapWidget.getGroup(RenderGroup.RASTER),
				mapWidget.getMapModel().getMapView()

				.getPanToViewTranslation());

		// Group for objects in vector space
		context.getVectorContext().drawGroup(null, mapWidget.getGroup(RenderGroup.VECTOR),
				mapWidget.getMapModel().getMapView().getPanToViewTranslation());

		// Group for objects in world space
		context.getVectorContext().drawGroup(null, mapWidget.getGroup(RenderGroup.WORLD),
				mapWidget.getMapModel().getMapView().getPanToViewTranslation());

		// Group for objects in screen space
		context.getVectorContext().drawGroup(null, mapWidget.getGroup(RenderGroup.SCREEN));
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
		context.getRasterContext().deleteGroup(mapWidget.getGroup(RenderGroup.RASTER));
		context.getVectorContext().deleteGroup(mapWidget.getGroup(RenderGroup.VECTOR));
		context.getVectorContext().deleteGroup(mapWidget.getGroup(RenderGroup.WORLD));
		context.getVectorContext().deleteGroup(mapWidget.getGroup(RenderGroup.SCREEN));
	}
}
