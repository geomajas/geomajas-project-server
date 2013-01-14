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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * <p>
 * Painter implementation for geometries.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class GeometryPainter implements Painter {

	/**
	 * Return the class-name of the type of object this painter can paint.
	 * 
	 * @return Return the class-name as a string.
	 */
	public String getPaintableClassName() {
		return GfxGeometry.class.getName();
	}

	/**
	 * The actual painting function. Draws the circles with the object's id.
	 * 
	 * @param paintable
	 *            A {@link org.geomajas.gwt.client.gfx.paintable.Text} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		if (paintable != null) {
			GfxGeometry gfxGeometry = (GfxGeometry) paintable;
			Geometry geometry = gfxGeometry.getGeometry();
			ShapeStyle shapeStyle = gfxGeometry.getStyle();
			if (geometry instanceof LineString) {
				context.getVectorContext().drawLine(group, gfxGeometry.getId(), (LineString) geometry, shapeStyle);
			} else if (geometry instanceof MultiLineString) {
				MultiLineString m = (MultiLineString) geometry;
				String gfxId = gfxGeometry.getId();
				GraphicsContext gc = context.getVectorContext();
				for (int i = 0; i < m.getNumGeometries(); i++) {
					gc.drawLine(group, gfxId + "." + i, (LineString) m.getGeometryN(i), shapeStyle);
				}
			} else if (geometry instanceof Polygon) {
				context.getVectorContext().drawPolygon(group, gfxGeometry.getId(), (Polygon) geometry, shapeStyle);
			} else if (geometry instanceof MultiPolygon) {
				MultiPolygon m = (MultiPolygon) geometry;
				String gfxId = gfxGeometry.getId();
				GraphicsContext gc = context.getVectorContext();
				for (int i = 0; i < m.getNumGeometries(); i++) {
					gc.drawPolygon(group, gfxId + "." + i, (Polygon) m.getGeometryN(i), shapeStyle);
				}
			} else if (geometry instanceof Point) {
				context.getVectorContext().drawSymbolDefinition(group, gfxGeometry.getId() + ".def",
						gfxGeometry.getSymbolInfo(), shapeStyle, null);
				context.getVectorContext().drawSymbol(group, gfxGeometry.getId(), geometry.getCoordinate(), shapeStyle,
						gfxGeometry.getId() + ".def");
			} else if (geometry instanceof MultiPoint) {
				Coordinate[] coordinates = geometry.getCoordinates();
				String gfxId = gfxGeometry.getId();
				GraphicsContext gc = context.getVectorContext();
				String styleTypeDef = gfxGeometry.getId() + ".def";
				context.getVectorContext().drawSymbolDefinition(group, styleTypeDef, gfxGeometry.getSymbolInfo(),
						shapeStyle, null);
				for (int i = 0; i < coordinates.length; i++) {
					gc.drawSymbol(group, gfxId + "." + i, coordinates[i], shapeStyle, styleTypeDef);
				}
			}
		}
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It
	 * the object does not exist, nothing will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		GfxGeometry gfxGeometry = (GfxGeometry) paintable;
		Geometry geom = gfxGeometry.getGeometry();
		if (geom instanceof Point || geom instanceof MultiPoint) {
			context.getVectorContext().deleteElement(group, gfxGeometry.getId() + ".def");
		}
		if (geom instanceof MultiPolygon || geom instanceof MultiLineString || geom instanceof MultiPoint) {
			GraphicsContext gc = context.getVectorContext();
			String gfxId = gfxGeometry.getId();
			for (int i = 0; i < geom.getNumGeometries(); i++) {
				gc.deleteElement(group, gfxId + "." + i);
			}
		} else {
			context.getVectorContext().deleteElement(group, gfxGeometry.getId());
		}
	}
}
