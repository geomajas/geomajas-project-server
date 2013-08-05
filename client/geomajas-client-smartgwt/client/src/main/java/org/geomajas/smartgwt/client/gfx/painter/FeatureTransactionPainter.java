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

package org.geomajas.smartgwt.client.gfx.painter;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.gfx.GraphicsContext;
import org.geomajas.smartgwt.client.gfx.MapContext;
import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.Painter;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPoint;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.smartgwt.client.spatial.geometry.Point;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderGroup;

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
		context.getVectorContext().drawGroup(mapWidget.getGroup(RenderGroup.VECTOR), featureTransaction);
		for (int i = 0; i < features.length; i++) {
			Geometry geometry = mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToPan(
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
	 * Delete a {@link Paintable} object from the given {@link org.geomajas.smartgwt.client.gfx.GraphicsContext}. It
	 * the object does not exist,
	 * nothing will be done.
	 *
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
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
