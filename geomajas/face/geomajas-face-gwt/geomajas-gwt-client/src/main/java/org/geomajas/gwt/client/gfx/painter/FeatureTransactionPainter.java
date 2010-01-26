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
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * ???
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
	 * @param graphics
	 *            A GraphicsContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, GraphicsContext graphics) {
		FeatureTransaction featureTransaction = (FeatureTransaction) paintable;

		Feature[] features = featureTransaction.getNewFeatures();
		if (features == null) {
			return;
		}
		for (int i = 0; i < features.length; i++) {
			Geometry geometry = mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToView(
					features[i].getGeometry());
			if (geometry instanceof Point) {
				paint(featureTransaction.getId() + ".feature" + i, (Point) geometry, graphics);
			} else if (geometry instanceof LineString) {
				paint(featureTransaction.getId() + ".feature" + i, (LineString) geometry, graphics);
			} else if (geometry instanceof MultiLineString) {
				paint(featureTransaction.getId() + ".feature" + i, (MultiLineString) geometry, graphics);
			} else if (geometry instanceof Polygon) {
				paint(featureTransaction.getId() + ".feature" + i, (Polygon) geometry, graphics);
			} else if (geometry instanceof MultiPolygon) {
				paint(featureTransaction.getId() + ".feature" + i, (MultiPolygon) geometry, graphics);
			}
		}
	}

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 *
	 * @param paintable
	 *            The object to be painted.
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, GraphicsContext graphics) {
		graphics.deleteShape(paintable.getId(), false);
	}

	// -------------------------------------------------------------------------
	// Private drawing functions:
	// -------------------------------------------------------------------------

	private void paint(String id, Point point, GraphicsContext graphics) {
		if (!point.isEmpty()) {
			graphics.drawCircle(id + ".coordinate0", point.getCoordinate(), 5.0f, pointStyle);
		}
	}

	private void paint(String id, LineString lineString, GraphicsContext graphics) {
		// Draw LineString fill area:
		graphics.drawLine(id + ".area", lineString, lineStringStyle);

		// Draw individual edges:
		Coordinate[] coordinates = lineString.getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			LineString edge = lineString.getGeometryFactory().createLineString(
					new Coordinate[] {coordinates[i - 1], coordinates[i]});
			graphics.drawLine(id + ".edge" + i, edge, edgeStyle);
		}

		// Draw individual vertices:
		for (int i = 0; i < coordinates.length; i++) {
			graphics.drawCircle(id + ".coordinate" + i, coordinates[i], 5.0f, pointStyle);
		}
	}

	private void paint(String id, LinearRing linearRing, GraphicsContext graphics) {
		// Draw LineString fill area:
		graphics.drawLine(id + ".area", linearRing, linearRingStyle);

		// Draw individual edges:
		Coordinate[] coordinates = linearRing.getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			LineString edge = linearRing.getGeometryFactory().createLineString(
					new Coordinate[] {coordinates[i - 1], coordinates[i]});
			graphics.drawLine(id + ".edge" + i, edge, edgeStyle);
		}

		// Draw individual vertices:
		for (int i = 0; i < coordinates.length - 1; i++) {
			graphics.drawCircle(id + ".coordinate" + i, coordinates[i], 5.0f, pointStyle);
		}
	}

	private void paint(String id, MultiLineString multi, GraphicsContext graphics) {
		for (int i = 0; i < multi.getNumGeometries(); i++) {
			paint(id + ".lineString" + i, (LineString) multi.getGeometryN(i), graphics);
		}
	}

	private void paint(String id, Polygon polygon, GraphicsContext graphics) {
		graphics.drawPolygon(id + ".background", polygon, polygonStyle);
		if (polygon.getExteriorRing() != null) {
			paint(id + ".shell0", polygon.getExteriorRing(), graphics);
		}
		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			paint(id + ".hole" + i, polygon.getInteriorRingN(i), graphics);
		}
	}

	private void paint(String id, MultiPolygon multi, GraphicsContext graphics) {
		for (int i = 0; i < multi.getNumGeometries(); i++) {
			paint(id + ".polygon" + i, (Polygon) multi.getGeometryN(i), graphics);
		}
	}
}
