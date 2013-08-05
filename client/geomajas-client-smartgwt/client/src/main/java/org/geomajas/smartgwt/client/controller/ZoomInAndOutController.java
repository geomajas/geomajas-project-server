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
package org.geomajas.smartgwt.client.controller;

import org.geomajas.smartgwt.client.map.MapView.ZoomOption;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Controller that zooms in or out on mouse up.
 * 
 * @author Pieter De Graef
 */
public class ZoomInAndOutController extends AbstractGraphicsController {

	private double delta;

	public ZoomInAndOutController(MapWidget mapWidget, double delta) {
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
