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
				context.getVectorContext().drawLine(group, gfxGeometry.getId(), (LineString) m.getGeometryN(0),
						shapeStyle);
			} else if (geometry instanceof Polygon) {
				context.getVectorContext().drawPolygon(group, gfxGeometry.getId(), (Polygon) geometry, shapeStyle);
			} else if (geometry instanceof MultiPolygon) {
				MultiPolygon m = (MultiPolygon) geometry;
				context.getVectorContext().drawPolygon(group, gfxGeometry.getId(), (Polygon) m.getGeometryN(0),
						shapeStyle);
			} else if (geometry instanceof Point) {
				context.getVectorContext().drawSymbol(group, gfxGeometry.getId(), geometry.getCoordinate(),
						shapeStyle, null);
			} else if (geometry instanceof MultiPoint) {
				Coordinate[] coordinates = geometry.getCoordinates();
				for (Coordinate coordinate : coordinates) {
					context.getVectorContext().drawSymbol(group, gfxGeometry.getId(), coordinate, shapeStyle, null);
				}
			}
		}
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist,
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
		GfxGeometry gfxGeometry = (GfxGeometry) paintable;
		context.getVectorContext().deleteElement(group, gfxGeometry.getId());
	}
}
