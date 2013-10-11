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

import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Allows zooming to a selection.
 *
 * @author Joachim Van der Auwera
 */
public class ZoomToRectangleController extends AbstractRectangleController {

	public ZoomToRectangleController(MapWidget mapWidget) {
		super(mapWidget);
	}

	@Override
	protected void selectRectangle(Bbox worldBounds) {
		mapWidget.getMapModel().getMapView().applyBounds(worldBounds, MapView.ZoomOption.LEVEL_CHANGE);
	}
}
