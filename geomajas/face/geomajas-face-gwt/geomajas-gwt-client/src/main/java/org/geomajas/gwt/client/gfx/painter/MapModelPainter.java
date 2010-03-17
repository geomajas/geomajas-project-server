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

import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.widget.MapWidget;

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

	public void paint(Paintable paintable, GraphicsContext graphics) {
		MapModel mapModel = (MapModel) paintable;

		// Group for objects in pan space
		graphics.drawGroup(null, mapModel.getMapGroup(), 
				mapWidget.getMapModel().getMapView().getPanToViewTranslation());

		// Group for objects in world space
		graphics.drawGroup(null, mapModel.getWorldGroup(), mapWidget.getMapModel().getMapView()
				.getWorldToViewTransformation());

		// Group for objects in screen space
		graphics.drawGroup(null, mapModel.getScreenGroup());
	}

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The MapModel
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, GraphicsContext graphics) {
		graphics.deleteGroup(paintable);
	}
}
