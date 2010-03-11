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
package org.geomajas.gwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.transform.WorldViewTransformer;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Controller for zooming when clicking.
 *
 * @author Joachim Van der Auwera
 */
public class ZoomOnClickController extends AbstractGraphicsController {

	private double zoomFactor;

	public ZoomOnClickController(MapWidget mapWidget, double zoomFactor) {
		super(mapWidget);
		this.zoomFactor = zoomFactor;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		MapView mapView = mapWidget.getMapModel().getMapView();
		WorldViewTransformer transformer = mapView.getWorldViewTransformer();
		Coordinate viewPosition = getScreenPosition(event);
		Coordinate worldPosition = transformer.viewToWorld(viewPosition);
		mapView.getCamera().setPosition(worldPosition);
		mapView.scale(zoomFactor, MapView.ZoomOption.LEVEL_CHANGE);
	}

	/**
	 * Set zoom factor. Should be a positive number, &gt;1 for zooming in, &lt;1 for zooming out.
	 *
	 * @param zoomFactor zoom factor
	 */
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
}
