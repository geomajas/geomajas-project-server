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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.plugin.jsapi.client.map.ViewPort;
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
@Export("ViewPort")
@ExportPackage("org.geomajas.jsapi.map")
public class ViewPortImpl implements ViewPort, Exportable {

	private MapView mapView;

	private ZoomOption zoomOption = ZoomOption.LEVEL_FIT;

	public ViewPortImpl() {
	}

	/**
	 * TODO.
	 * 
	 * @param mapView
	 */
	public ViewPortImpl(MapView mapView) {
		this.mapView = mapView;
	}

	/** {@inheritDoc} */
	public void applyPosition(Coordinate coordinate) {
		mapView.setCenterPosition(coordinate);
	}

	/** {@inheritDoc} */
	public void applyScale(final double newScale) {
		mapView.setCurrentScale(newScale, zoomOption);
	}

	/** {@inheritDoc} */
	public void applyBounds(final Bbox bounds) {
		org.geomajas.gwt.client.spatial.Bbox bbox = new org.geomajas.gwt.client.spatial.Bbox(bounds.getX(),
				bounds.getY(), bounds.getWidth(), bounds.getHeight());
		mapView.applyBounds(bbox, zoomOption);
	}

	/** {@inheritDoc} */
	public Bbox getBounds() {
		org.geomajas.gwt.client.spatial.Bbox box = mapView.getBounds();
		return new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	/** {@inheritDoc} */
	public Coordinate getPosition() {
		return new Coordinate(mapView.getBounds().getCenterPoint());
	}

	/** {@inheritDoc} */
	public double getScale() {
		return mapView.getCurrentScale();
	}

	/** {@inheritDoc} */
	public Bbox getMaximumBounds() {
		org.geomajas.gwt.client.spatial.Bbox box = mapView.getMaxBounds();
		return new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	/** {@inheritDoc} */
	public void applyScale(double scale, Coordinate rescalePoint) {
		mapView.setCurrentScale(scale, zoomOption, rescalePoint);
	}

	/** {@inheritDoc} */
	public Coordinate transform(Coordinate coordinate, String from, String to) {
		if (from.equalsIgnoreCase(to)) {
			return coordinate;
		}

		org.geomajas.geometry.Coordinate transformed;
		if (from.equalsIgnoreCase("screen") && to.equalsIgnoreCase("world")) {
			transformed = mapView.getWorldViewTransformer().viewToWorld(coordinate);
			return new Coordinate(transformed.getX(), transformed.getY());
		} else if (from.equalsIgnoreCase("world") && to.equalsIgnoreCase("screen")) {
			transformed = mapView.getWorldViewTransformer().worldToView(coordinate);
			return new Coordinate(transformed.getX(), transformed.getY());
		}
		return coordinate;
	}
}