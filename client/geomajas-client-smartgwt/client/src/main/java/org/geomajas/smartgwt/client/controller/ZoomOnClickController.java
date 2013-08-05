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

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.map.MapView;
import org.geomajas.smartgwt.client.spatial.WorldViewTransformer;
import org.geomajas.smartgwt.client.widget.MapWidget;

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
		mapView.setCenterPosition(worldPosition);
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
