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

package org.geomajas.smartgwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.Geomajas;
import org.geomajas.smartgwt.client.controller.AbstractGraphicsController;
import org.geomajas.smartgwt.client.controller.GraphicsController;
import org.geomajas.smartgwt.client.controller.ZoomToRectangleController;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.style.PictureStyle;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.widget.MapWidget;

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

		private GraphicsController keepController;

		public ZoomToRectangleOnceController(MapWidget mapWidget) {
			super(mapWidget);
			keepController = mapWidget.getController();
		}

		protected void selectRectangle(Bbox worldBounds) {
			super.selectRectangle(worldBounds);
			map.setController(keepController);
		}
	}
}
