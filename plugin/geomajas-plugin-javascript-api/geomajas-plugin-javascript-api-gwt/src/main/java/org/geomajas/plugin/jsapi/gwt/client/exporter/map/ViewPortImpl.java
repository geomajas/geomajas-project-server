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
package org.geomajas.plugin.jsapi.gwt.client.exporter.map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.plugin.jsapi.client.map.ViewPort;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable facade for a map's ViewPort. The Central view port definition that determines and influences
 * the position and current view of the map.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("ViewPort")
@ExportPackage("org.geomajas.jsapi.map")
public class ViewPortImpl implements ViewPort, Exportable {

	private static final String WORLD = "world";

	private static final String SCREEN = "screen";

	private MapView mapView;

	private ZoomOption zoomOption = ZoomOption.LEVEL_FIT;

	public ViewPortImpl() {
	}

	/**
	 * Create a new ViewPort.
	 * 
	 * @param mapView
	 *            The MapView behind a map in the GWT face.
	 */
	public ViewPortImpl(MapView mapView) {
		this.mapView = mapView;
	}

	/**
	 * Re-centers the map to a new position.
	 * 
	 * @param coordinate
	 *            the new center position
	 */
	public void applyPosition(Coordinate coordinate) {
		mapView.setCenterPosition(coordinate);
	}

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 */
	public void applyScale(final double scale) {
		mapView.setCurrentScale(scale, zoomOption);
	}

	/**
	 * <p>
	 * Change the view on the map by applying a bounding box (world coordinates!). Since the width/height ratio of the
	 * bounding box may differ from that of the map, the fit is "as good as possible".
	 * </p>
	 * <p>
	 * Also this function will almost certainly change the scale on the map, so if there have been resolutions defined,
	 * it will snap to them.
	 * </p>
	 * 
	 * @param bounds
	 *            A bounding box in world coordinates that determines the view from now on.
	 */
	public void applyBounds(final Bbox bounds) {
		org.geomajas.gwt.client.spatial.Bbox bbox = new org.geomajas.gwt.client.spatial.Bbox(bounds.getX(),
				bounds.getY(), bounds.getWidth(), bounds.getHeight());
		mapView.applyBounds(bbox, zoomOption);
	}

	/**
	 * Return the currently visible bounds on the map. These bounds are expressed in the CRS of the map.
	 * 
	 * @return Returns the maps bounding box.
	 */
	public Bbox getBounds() {
		org.geomajas.gwt.client.spatial.Bbox box = mapView.getBounds();
		return new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	/**
	 * Get the current center position expressed in world space.
	 * 
	 * @return The current center position expressed in world space.
	 */
	public Coordinate getPosition() {
		return new Coordinate(mapView.getBounds().getCenterPoint());
	}

	/**
	 * Return the current scale on the map.
	 */
	public double getScale() {
		return mapView.getCurrentScale();
	}

	/**
	 * Get the maximum zooming extent that is allowed on this view port. These bounds are determined by the map
	 * configuration.
	 * 
	 * @return The maximum zooming extent that is allowed on this view port.
	 */
	public Bbox getMaximumBounds() {
		org.geomajas.gwt.client.spatial.Bbox box = mapView.getMaxBounds();
		return new Bbox(box.getX(), box.getY(), box.getWidth(), box.getHeight());
	}

	/**
	 * Apply a new scale level on the map. In case the are fixed resolutions defined on this MapView, it will
	 * automatically snap to the nearest resolution. In case the maximum extents are exceeded, it will pan to avoid
	 * this.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 * @param rescalePoint
	 *            After zooming, this point will still be on the same position in the view as before. Makes for easy
	 *            double clicking on the map without it moving away.
	 */
	public void applyScale(double scale, Coordinate rescalePoint) {
		mapView.setCurrentScale(scale, zoomOption, rescalePoint);
	}

	/**
	 * Transform the given coordinate from a certain rendering space to another.
	 * 
	 * @param coordinate
	 *            The coordinate to transform. The X and Y ordinates are expected to be expressed in the 'from'
	 *            rendering space.
	 * @param from
	 *            The rendering space that expresses the X and Y ordinates of the given coordinate. Options are:
	 *            <ul>
	 *            <li>screen</li>
	 *            <li>world</li>
	 *            </ul>
	 * @param to
	 *            The rendering space where to the coordinate should be transformed. Options are:
	 *            <ul>
	 *            <li>screen</li>
	 *            <li>world</li>
	 *            </ul>
	 * @return The transformed coordinate.
	 */
	public Coordinate transform(Coordinate coordinate, String from, String to) {
		if (from.equalsIgnoreCase(to)) {
			return coordinate;
		}

		org.geomajas.geometry.Coordinate transformed;
		if (SCREEN.equalsIgnoreCase(from) && WORLD.equalsIgnoreCase(to)) {
			transformed = mapView.getWorldViewTransformer().viewToWorld(coordinate);
			return new Coordinate(transformed.getX(), transformed.getY());
		} else if (WORLD.equalsIgnoreCase(from) && SCREEN.equalsIgnoreCase(to)) {
			transformed = mapView.getWorldViewTransformer().worldToView(coordinate);
			return new Coordinate(transformed.getX(), transformed.getY());
		}
		return coordinate;
	}
}