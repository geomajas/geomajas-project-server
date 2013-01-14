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
package org.geomajas.gwt.client.controller;

import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Controller that activates the ZoomToRectangleController to be used once.
 * 
 * @author Pieter De Graef
 */
public class ZoomToRectangleOnceController extends AbstractGraphicsController {

	public ZoomToRectangleOnceController(MapWidget mapWidget) {
		super(mapWidget);
	}

	public void onMouseUp(MouseUpEvent event) {
		mapWidget.setController(new ActualZoomToRectangleController(mapWidget));
		event.stopPropagation();
	}

	public void onMouseDown(MouseDownEvent event) {
		// Don't propagate to the active controller on the map:
		event.stopPropagation();
	}

	/**
	 * Controller the zooms to a rectangle drawn by the user, and then deactivates itself again.
	 * 
	 * @author Pieter De Graef
	 */
	private class ActualZoomToRectangleController extends org.geomajas.gwt.client.controller.ZoomToRectangleController {

		private GraphicsController keepController;

		public ActualZoomToRectangleController(MapWidget mapWidget) {
			super(mapWidget);
			keepController = mapWidget.getController();
		}

		protected void selectRectangle(Bbox worldBounds) {
			super.selectRectangle(worldBounds);
			mapWidget.setController(keepController);
		}
	}
}
