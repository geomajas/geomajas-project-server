/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.jsapi.map.ViewPort;
import org.geomajas.jsapi.spatial.geometry.Bbox;
import org.geomajas.jsapi.spatial.geometry.Coordinate;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exportable wrapper around {@link MapView}.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.map")
public class ViewPortImpl implements ViewPort, Exportable {

	private MapView mapView;

	private ZoomOption zoomOption = ZoomOption.LEVEL_CLOSEST;

	public ViewPortImpl() {
	}

	/**
	 * TODO.
	 * 
	 * @param mapView
	 * @since 1.0.0
	 */
	@Api
	public ViewPortImpl(MapView mapView) {
		this.mapView = mapView;
	}

	public void applyPosition(Coordinate coordinate) {
		mapView.setCenterPosition(coordinate.getCoordinate());
	}

	public void applyScale(final double newScale) {
		mapView.setCurrentScale(newScale, zoomOption);
	}

	public void applyBounds(final Bbox bounds) {
		org.geomajas.gwt.client.spatial.Bbox bbox = new org.geomajas.gwt.client.spatial.Bbox(bounds.getX(),
				bounds.getY(), bounds.getWidth(), bounds.getHeight());
		mapView.applyBounds(bbox, zoomOption);
	}

	public Bbox getBounds() {
		org.geomajas.gwt.client.spatial.Bbox box = mapView.getBounds();
		return new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	public Coordinate getPosition() {
		return new Coordinate(mapView.getBounds().getCenterPoint());
	}

	public double getScale() {
		return mapView.getCurrentScale();
	}

	public Bbox getMaximumBounds() {
		org.geomajas.gwt.client.spatial.Bbox box = mapView.getMaxBounds();
		return new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	public void applyScale(double scale, Coordinate rescalePoint) {
		mapView.setCurrentScale(scale, zoomOption, Coordinate.toGeomajasCoordinate(rescalePoint));
	}

	public Coordinate transform(Coordinate coordinate, String from, String to) {
		if (from.equalsIgnoreCase(to)) {
			return coordinate;
		}

		org.geomajas.geometry.Coordinate transformed;
		if (from.equalsIgnoreCase("screen") && to.equalsIgnoreCase("world")) {
			transformed = mapView.getWorldViewTransformer().viewToWorld(coordinate.getCoordinate());
			return new Coordinate(transformed.getX(), transformed.getY());
		} else if (from.equalsIgnoreCase("world") && to.equalsIgnoreCase("screen")) {
			transformed = mapView.getWorldViewTransformer().worldToView(coordinate.getCoordinate());
			return new Coordinate(transformed.getX(), transformed.getY());
		}
		return coordinate;
	}
}