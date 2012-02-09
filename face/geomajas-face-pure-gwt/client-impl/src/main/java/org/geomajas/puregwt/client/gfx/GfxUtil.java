/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.gfx;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.controller.MapController;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Path;

import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

/**
 * Utility class concerning custom graphics rendering on the map.
 * 
 * @author Pieter De Graef
 */
public final class GfxUtil {

	@Inject
	private GfxUtil() {
	}

	/**
	 * Apply the main elements of the given style onto the shape.
	 * 
	 * @param shape
	 *            The shape in need of styling.
	 * @param style
	 *            The style to apply. Only the following elements are applied: fill color, fill opacity, stroke color,
	 *            stroke opacity and stroke width.
	 */
	public void applyStyle(Shape shape, FeatureStyleInfo style) {
		if (style.getFillColor() != null) {
			shape.setFillColor(style.getFillColor());
		}
		if (style.getFillOpacity() >= 0) {
			shape.setFillOpacity(style.getFillOpacity());
		}
		if (style.getStrokeColor() != null) {
			shape.setStrokeColor(style.getStrokeColor());
		}
		if (style.getStrokeOpacity() >= 0) {
			shape.setStrokeOpacity(style.getStrokeOpacity());
		}
		if (style.getStrokeWidth() >= 0) {
			shape.setStrokeWidth(style.getStrokeWidth());
		}
	}

	/**
	 * Apply the given controller onto the given shape.
	 * 
	 * @param shape
	 *            The shape in need of a controller.
	 * @param mapController
	 *            The controller to apply onto the shape.
	 * @return The list of registrations that allows for removing the controller again.
	 */
	public List<HandlerRegistration> applyController(Shape shape, MapController mapController) {
		List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
		registrations.add(shape.addMouseDownHandler(mapController));
		registrations.add(shape.addMouseUpHandler(mapController));
		registrations.add(shape.addMouseMoveHandler(mapController));
		registrations.add(shape.addMouseOutHandler(mapController));
		registrations.add(shape.addMouseOverHandler(mapController));
		registrations.add(shape.addMouseWheelHandler(mapController));
		registrations.add(shape.addDoubleClickHandler(mapController));
		registrations.add(shape.addDomHandler(mapController, TouchStartEvent.getType()));
		registrations.add(shape.addDomHandler(mapController, TouchEndEvent.getType()));
		registrations.add(shape.addDomHandler(mapController, TouchMoveEvent.getType()));
		registrations.add(shape.addDomHandler(mapController, TouchCancelEvent.getType()));
		return registrations;
	}

	/**
	 * Transform the given geometry into a path object that can be drawn on the map.
	 * 
	 * @param geometry
	 *            The geometry to transform.
	 * @return The quivalent path object.
	 */
	public Path toPath(Geometry geometry) {
		if (geometry != null) {
			if (Geometry.POINT.equals(geometry.getGeometryType())) {
				return toPathPoint(geometry);
			} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
				return toPathLineString(geometry);
			} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
				return toPathLinearRing(geometry);
			} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
				return toPathPolygon(geometry);
			} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
				return toPathMultiPoint(geometry);
			} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
				return toPathMultiLineString(geometry);
			} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
				return toPathMultiPolygon(geometry);
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Path toPathPoint(Geometry point) {
		if (point.getCoordinates() != null && point.getCoordinates().length == 1) {
			Coordinate first = point.getCoordinates()[0];
			return new Path((int) first.getX(), (int) first.getY());
		}
		return null;
	}

	private Path toPathLineString(Geometry lineString) {
		if (lineString.getCoordinates() != null && lineString.getCoordinates().length > 0) {
			Coordinate first = lineString.getCoordinates()[0];
			Path path = new Path((int) first.getX(), (int) first.getY());
			for (int i = 1; i < lineString.getCoordinates().length; i++) {
				Coordinate coordinate = lineString.getCoordinates()[i];
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
			return path;
		}
		return null;
	}

	private Path toPathLinearRing(Geometry linearRing) {
		if (linearRing.getCoordinates() != null && linearRing.getCoordinates().length > 0) {
			Coordinate first = linearRing.getCoordinates()[0];
			Path path = new Path((int) first.getX(), (int) first.getY());
			for (int i = 1; i < linearRing.getCoordinates().length - 1; i++) {
				Coordinate coordinate = linearRing.getCoordinates()[i];
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
			path.close();
			path.getElement().getStyle().setProperty("fillRule", "evenOdd");
			return path;
		}
		return null;
	}

	private Path toPathPolygon(Geometry polygon) {
		if (polygon.getGeometries() != null && polygon.getGeometries().length > 0) {
			Path path = toPathLinearRing(polygon.getGeometries()[0]);
			path.getElement().getStyle().setProperty("fillRule", "evenOdd");
			for (int i = 1; i < polygon.getGeometries().length; i++) {
				Geometry ring = polygon.getGeometries()[i];
				path.moveTo((int) ring.getCoordinates()[0].getX(), (int) ring.getCoordinates()[0].getY());
				for (int j = 1; j < ring.getCoordinates().length - 1; j++) {
					Coordinate coordinate = ring.getCoordinates()[j];
					path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
				}
				path.close();
			}

			// IE even-odd problem. This fix only works if no more changes are made to the Path object:
			// TODO fix this in the GwtGraphics library
			String pathStr = path.getElement().getAttribute("path");
			if (pathStr.indexOf("x e") > 0) {
				pathStr = pathStr.replaceAll("x e", "x") + " e";
				path.getElement().setAttribute("path", pathStr);
			}
			return path;
		}
		return null;
	}

	private Path toPathMultiPoint(Geometry multiPoint) {
		if (multiPoint.getGeometries() != null && multiPoint.getGeometries().length > 0) {
			Path path = toPathPoint(multiPoint.getGeometries()[0]);
			for (int i = 1; i < multiPoint.getGeometries().length; i++) {
				Geometry point = multiPoint.getGeometries()[i];
				path.moveTo(point.getCoordinates()[0].getX(), point.getCoordinates()[0].getY());
			}
			return path;
		}
		return null;
	}

	private Path toPathMultiLineString(Geometry multiLineString) {
		if (multiLineString.getGeometries() != null && multiLineString.getGeometries().length > 0) {
			Path path = toPathLineString(multiLineString.getGeometries()[0]);
			for (int i = 1; i < multiLineString.getGeometries().length; i++) {
				Geometry lineString = multiLineString.getGeometries()[i];
				path.moveTo((int) lineString.getCoordinates()[0].getX(), (int) lineString.getCoordinates()[0].getY());
				for (int j = 1; j < lineString.getCoordinates().length; j++) {
					Coordinate coordinate = lineString.getCoordinates()[j];
					path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
				}
			}
			return path;
		}
		return null;
	}

	private Path toPathMultiPolygon(Geometry multiPolygon) {
		if (multiPolygon.getGeometries() != null && multiPolygon.getGeometries().length > 0) {
			Path path = toPathPolygon(multiPolygon.getGeometries()[0]);
			for (int i = 1; i < multiPolygon.getGeometries().length; i++) {
				Geometry polygon = multiPolygon.getGeometries()[i];
				for (int j = 0; j < polygon.getGeometries().length; j++) {
					Geometry ring = polygon.getGeometries()[0];
					path.moveTo((int) ring.getCoordinates()[0].getX(), (int) ring.getCoordinates()[0].getY());
					for (int k = 1; k < ring.getCoordinates().length; k++) {
						Coordinate coordinate = ring.getCoordinates()[k];
						path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
					}
					path.close();
				}
			}
			return path;
		}
		return null;
	}
}