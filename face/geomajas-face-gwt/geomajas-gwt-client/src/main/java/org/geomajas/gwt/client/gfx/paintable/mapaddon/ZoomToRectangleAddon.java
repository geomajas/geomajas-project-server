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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.controller.ZoomToRectangleController;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.types.Cursor;

/**
 * Map addon for zooming to a rectangle on the map.
 * 
 * @author Pieter De Graef
 */
public class ZoomToRectangleAddon extends MapAddon {

	private MapWidget map;

	private boolean firstTime = true;

	public ZoomToRectangleAddon(String id, MapWidget map) {
		super(id, 20, 20);
		this.map = map;
	}

	// -------------------------------------------------------------------------
	// MapAddon implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint this zoom to rectangle button!
	 */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		map.getVectorContext().drawGroup(group, this);

		Coordinate c = getUpperLeftCorner();
		map.getVectorContext().drawImage(this, "zoom-rect-img",
				Geomajas.getIsomorphicDir() + "geomajas/mapaddon/zoom_rectangle.png",
				new Bbox(c.getX(), c.getY(), 20, 20), new PictureStyle(1));

		if (firstTime) {
			map.getVectorContext().setController(this, "zoom-rect-img", new ActivateRectangleController(map),
					Event.MOUSEEVENTS);
			map.getVectorContext().setCursor(this, "zoom-rect-img", Cursor.POINTER.getValue());
		}
		firstTime = false;
	}

	public void onDraw() {
	}

	public void onRemove() {
	}

	/**
	 * Controller that activates the ZoomToRectangleController to be used once.
	 * 
	 * @author Pieter De Graef
	 */
	private class ActivateRectangleController extends AbstractGraphicsController {

		protected ActivateRectangleController(MapWidget mapWidget) {
			super(mapWidget);
		}

		public void onMouseUp(MouseUpEvent event) {
			mapWidget.setController(new ZoomToRectangleOnceController(map));
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			// Don't propagate to the active controller on the map:
			event.stopPropagation();
		}
	}

	/**
	 * Controller the zooms to a rectangle drawn by the user, and then deactivates itself again.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectangleOnceController extends ZoomToRectangleController {

		public ZoomToRectangleOnceController(MapWidget mapWidget) {
			super(mapWidget);
		}

		protected void selectRectangle(Bbox worldBounds) {
			super.selectRectangle(worldBounds);
			map.setController(null);
		}
	}
}
