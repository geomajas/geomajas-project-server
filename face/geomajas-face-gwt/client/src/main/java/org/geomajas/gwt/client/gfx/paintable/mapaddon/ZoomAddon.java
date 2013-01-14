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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.types.Cursor;

/**
 * Map addon for zooming buttons in the upper left corner.
 * 
 * @author Pieter De Graef
 */
public class ZoomAddon extends MapAddon {

	private MapWidget map;

	private boolean firstTime = true;

	public ZoomAddon(String id, MapWidget map) {
		super(id, 20, 60);
		this.map = map;
	}

	// -------------------------------------------------------------------------
	// MapAddon implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint this pan button!
	 */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		map.getVectorContext().drawGroup(group, this);

		Coordinate c = getUpperLeftCorner();
		map.getVectorContext().drawImage(this, "bg", Geomajas.getIsomorphicDir() + "geomajas/mapaddon/zoombg.png",
				new Bbox(c.getX(), c.getY(), 20, 60), new PictureStyle(1));

		map.getVectorContext().drawImage(this, "plus", Geomajas.getIsomorphicDir() + "geomajas/mapaddon/zoomPlus.png",
				new Bbox(c.getX(), c.getY(), 20, 20), new PictureStyle(1));

		map.getVectorContext().drawImage(this, "minus",
				Geomajas.getIsomorphicDir() + "geomajas/mapaddon/zoomMinus.png",
				new Bbox(c.getX(), c.getY() + 40, 20, 20), new PictureStyle(1));

		map.getVectorContext().drawImage(this, "max", Geomajas.getIsomorphicDir() + "geomajas/mapaddon/maxextent.png",
				new Bbox(c.getX(), c.getY() + 20, 20, 20), new PictureStyle(1));

		if (firstTime) {
			map.getVectorContext().setController(this, "plus", new ZoomController(map, 2), Event.MOUSEEVENTS);
			map.getVectorContext().setCursor(this, "plus", Cursor.POINTER.getValue());

			map.getVectorContext().setController(this, "minus", new ZoomController(map, 0.5), Event.MOUSEEVENTS);
			map.getVectorContext().setCursor(this, "minus", Cursor.POINTER.getValue());

			map.getVectorContext().setController(this, "max", new MaxExtentController(map), Event.MOUSEEVENTS);
			map.getVectorContext().setCursor(this, "max", Cursor.POINTER.getValue());
		}
		firstTime = false;
	}

	public void onDraw() {
	}

	public void onRemove() {
	}

	/**
	 * Controller that zooms in or out on mouse up.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomController extends AbstractGraphicsController {

		private double delta;

		protected ZoomController(MapWidget mapWidget, double delta) {
			super(mapWidget);
			this.delta = delta;
		}

		public void onMouseUp(MouseUpEvent event) {
			mapWidget.getMapModel().getMapView().scale(delta, ZoomOption.LEVEL_CHANGE);
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			// Don't propagate to the active controller on the map:
			event.stopPropagation();
		}
	}

	/**
	 * Controller that zooms to the maximum extent on mouse up.
	 * 
	 * @author Pieter De Graef
	 */
	private class MaxExtentController extends AbstractGraphicsController {

		protected MaxExtentController(MapWidget mapWidget) {
			super(mapWidget);
		}

		public void onMouseUp(MouseUpEvent event) {
			Bbox max = mapWidget.getMapModel().getMapView().getMaxBounds();
			mapWidget.getMapModel().getMapView().applyBounds(max, ZoomOption.LEVEL_FIT);
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			// Don't propagate to the active controller on the map:
			event.stopPropagation();
		}
	}
}
