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

import org.geomajas.global.FutureApi;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.jsapi.spatial.geometry.Bbox;
import org.geomajas.jsapi.spatial.geometry.Coordinate;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Exportable wrapper around {@link org.geomajas.gwt.client.map.MapView}.
 * 
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.map")
public class ViewPortImpl implements Exportable {

	private org.geomajas.gwt.client.map.MapView mapView;

	private ZoomOption zoomOption = ZoomOption.LEVEL_CLOSEST;

	/**
	 * TODO.
	 * 
	 * @param mapView
	 * @since 1.0.0
	 */
	@FutureApi
	public ViewPortImpl(org.geomajas.gwt.client.map.MapView mapView) {
		this.mapView = mapView;
	}

	/**
	 * Adjust the current scale on the map by a new factor.
	 * 
	 * @param delta
	 *            Adjust the scale by factor "delta".
	 */
	public void scale(final double delta) {
		applyScale(getScale() * delta);
	}

	/**
	 * Move the view on the map. This happens by translating the camera in turn.
	 * 
	 * @param x
	 *            Translation factor along the X-axis in world space.
	 * @param y
	 *            Translation factor along the Y-axis in world space.
	 * @since 1.0.0
	 */
	@FutureApi
	public void translate(double x, double y) {
		Coordinate center = getPosition();
		center.setX(center.getX() + x);
		center.setY(center.getY() + y);

		applyPosition(center);
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
}
