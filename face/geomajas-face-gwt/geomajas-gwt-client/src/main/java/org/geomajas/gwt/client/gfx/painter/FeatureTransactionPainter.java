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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Paint a feature transaction.
 *
 * @author Pieter De Graef
 */
public class FeatureTransactionPainter implements Painter {

	private MapWidget mapWidget;

	private ShapeStyle pointStyle;

	private ShapeStyle edgeStyle;

	private ShapeStyle lineStringStyle;

	private ShapeStyle linearRingStyle;

	private ShapeStyle polygonStyle;

	public FeatureTransactionPainter(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		pointStyle = new ShapeStyle("#FF6600", 1, "#FFFFFF", 0, 1);
		edgeStyle = new ShapeStyle("#FFFFFF", 0, "#FFAA00", 1, 3);
		lineStringStyle = new ShapeStyle("#FFFFFF", 0, "#FFFFFF", 0, 0);
		linearRingStyle = new ShapeStyle("#FFEE00", 0, "#FFAA00", 1, 3);
		polygonStyle = new ShapeStyle("#FFCC00", 0.5f, "#FFAA00", 0, 0);
	}

	/**
	 * Return the class-name of the type of object this painter can paint.
	 *
	 * @return Return the class-name as a string.
	 */
	public String getPaintableClassName() {
		return FeatureTransaction.class.getName();
	}

	/**
	 * The actual painting function.
	 *
	 * @param paintable
	 *            A {@link FeatureTransaction} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		FeatureTransaction featureTransaction = (FeatureTransaction) paintable;

		Feature[] features = featureTransaction.getNewFeatures();
		if (features == null) {
			return;
		}
		context.getVectorContext().drawGroup(group, featureTransaction);
		for (int i = 0; i < features.length; i++) {
			Geometry geometry = mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToView(
					features[i].getGeometry());
			context.getVectorContext().drawGroup(featureTransaction, features[i]);
			if (geometry instanceof Point) {
				paint(features[i], "featureTransaction.feature" + i, (Point) geometry, context.getVectorContext());
			} else if (geometry instanceof MultiPoint) {
				paint(features[i], "featureTransaction.feature" + i, (MultiPoint) geometry, context.getVectorContext());
			} else if (geometry instanceof LineString) {
				paint(features[i], "featureTransaction.feature" + i, (LineString) geometry, context.getVectorContext());
			} else if (geometry instanceof MultiLineString) {
				paint(features[i], "featureTransaction.feature" + i, (MultiLineString) geometry, context
						.getVectorContext());
			} else if (geometry instanceof Polygon) {
				paint(features[i], "featureTransaction.feature" + i, (Polygon) geometry, context.getVectorContext());
			} else if (geometry instanceof MultiPolygon) {
				paint(features[i], "featureTransaction.feature" + i, (MultiPolygon) geometry, context
						.getVectorContext());
			}
		}
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link GraphicsContext}. It the object does not exist,
	 * nothing will be done.
	 *
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		context.getVectorContext().deleteGroup(paintable);
	}

	// -------------------------------------------------------------------------
	// Private drawing functions:
	// -------------------------------------------------------------------------

	private void paint(Object parent, String name, Point point, GraphicsContext graphics) {
		if (!point.isEmpty()) {
			paint(parent, name + ".coordinate0", point.getCoordinate(), graphics);
		}
	}
	
	private void paint(Object parent, String name, LineString lineString, GraphicsContext graphics) {
		// Draw LineString fill area:
		graphics.drawLine(parent, name + ".area", lineString, lineStringStyle);

		// Draw individual edges:
		Coordinate[] coordinates = lineString.getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			LineString edge = lineString.getGeometryFactory().createLineString(
					new Coordinate[] { coordinates[i - 1], coordinates[i] });
			graphics.drawLine(parent, name + ".edge" + i, edge, edgeStyle);
		}

		// Draw individual vertices:
		for (int i = 0; i < coordinates.length; i++) {
			graphics.drawCircle(parent, name + ".coordinate" + i, coordinates[i], 5.0f, pointStyle);
		}
	}

	private void paint(Object parent, String name, Polygon polygon, GraphicsContext graphics) {
		graphics.drawPolygon(parent, "background", polygon, polygonStyle);
		if (polygon.getExteriorRing() != null) {
			paint(parent, name + ".shell", polygon.getExteriorRing(), graphics);
		}
		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			paint(parent, name + ".hole" + i, polygon.getInteriorRingN(i), graphics);
		}
	}

	private void paint(Object parent, String name, MultiPoint multipoint, GraphicsContext graphics) {
		for (int i = 0; i < multipoint.getNumGeometries(); i++) {
			paint(parent, name + ".point" + i, (Point) multipoint.getGeometryN(i), graphics);
		}
	}

	private void paint(Object parent, String name, MultiLineString multiline, GraphicsContext graphics) {
		for (int i = 0; i < multiline.getNumGeometries(); i++) {
			paint(parent, name + ".linestring" + i, (LineString) multiline.getGeometryN(i), graphics);
		}
	}


	private void paint(Object parent, String name, MultiPolygon multipolygon, GraphicsContext graphics) {
		for (int i = 0; i < multipolygon.getNumGeometries(); i++) {
			paint(parent, name + ".polygon" + i, (Polygon) multipolygon.getGeometryN(i), graphics);
		}
	}

	private void paint(Object parent, String name, Coordinate coordinate, GraphicsContext graphics) {
		graphics.drawCircle(parent, name, coordinate, 5.0f, pointStyle);
	}


	private void paint(Object parent, String name, LinearRing linearRing, GraphicsContext graphics) {
		// Draw LineString fill area:
		graphics.drawLine(parent, name + ".area", linearRing, linearRingStyle);

		// Draw individual edges:
		Coordinate[] coordinates = linearRing.getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			LineString edge = linearRing.getGeometryFactory().createLineString(
					new Coordinate[] { coordinates[i - 1], coordinates[i] });
			graphics.drawLine(parent, name + ".edge" + i, edge, edgeStyle);
		}

		// Draw individual vertices:
		for (int i = 0; i < coordinates.length - 1; i++) {
			graphics.drawCircle(parent, name + ".coordinate" + i, coordinates[i], 5.0f, pointStyle);
		}
	}

}
