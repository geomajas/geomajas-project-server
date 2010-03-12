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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.spatial.transform.WorldViewTransformer;

/**
 * Painter implementation for {@link Feature}s.
 * 
 * @author Joachim Van der Auwera
 */
public class FeaturePainter implements Painter {

	private ShapeStyle pointSelectStyle;

	private ShapeStyle lineSelectStyle;

	private ShapeStyle polygonSelectStyle;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public FeaturePainter(ShapeStyle pointSelectStyle, ShapeStyle lineSelectStyle, ShapeStyle polygonSelectStyle) {
		this.pointSelectStyle = pointSelectStyle;
		this.lineSelectStyle = lineSelectStyle;
		this.polygonSelectStyle = polygonSelectStyle;
	}

	// -------------------------------------------------------------------------
	// Painter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Return the class-name of the type of object this painter can paint.
	 * 
	 * @return Return the class-name as a string.
	 */
	public String getPaintableClassName() {
		return Feature.class.getName();
	}

	/**
	 * The actual painting function. Draws the circles with the object's id.
	 * 
	 * @param paintable
	 *            A {@link org.geomajas.gwt.client.gfx.paintable.Text} object.
	 * @param graphics
	 *            A GraphicsContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, GraphicsContext graphics) {
		if (paintable != null) {
			Feature feature = (Feature) paintable;
			WorldViewTransformer worldViewTransformer = feature.getLayer().getMapModel().getMapView()
					.getWorldViewTransformer();
			Geometry geometry = worldViewTransformer.worldToPan(feature.getGeometry());
			ShapeStyle style = createStyleForFeature(feature);
			PaintableGroup selectionGroup = feature.getLayer().getSelectionGroup();
			graphics.drawGroup(selectionGroup, feature);
			String name = feature.getLayer().getId() + "-" + feature.getId();
			if (geometry instanceof LineString) {
				graphics.drawLine(feature, name, (LineString) geometry, style);
			} else if (geometry instanceof MultiLineString) {
				MultiLineString m = (MultiLineString) geometry;
				graphics.drawLine(feature, name, (LineString) m.getGeometryN(0), style);
			} else if (geometry instanceof Polygon) {
				graphics.drawPolygon(feature, name, (Polygon) geometry, style);
			} else if (geometry instanceof MultiPolygon) {
				MultiPolygon m = (MultiPolygon) geometry;
				graphics.drawPolygon(feature, name, (Polygon) m.getGeometryN(0), style);
			} else if (geometry instanceof Point) {
				graphics.drawSymbol(feature, name, geometry.getCoordinate(), style, null);
			}
		}
	}

	public void deleteShape(Paintable paintable, GraphicsContext graphics) {
		Feature feature = (Feature) paintable;
		PaintableGroup selectionGroup = feature.getLayer().getSelectionGroup();
		graphics.deleteElement(selectionGroup, feature.getId());
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public ShapeStyle getLineSelectStyle() {
		return lineSelectStyle;
	}

	public void setLineSelectStyle(ShapeStyle lineSelectStyle) {
		this.lineSelectStyle = lineSelectStyle;
	}

	public ShapeStyle getPointSelectStyle() {
		return pointSelectStyle;
	}

	public void setPointSelectStyle(ShapeStyle pointSelectStyle) {
		this.pointSelectStyle = pointSelectStyle;
	}

	public ShapeStyle getPolygonSelectStyle() {
		return polygonSelectStyle;
	}

	public void setPolygonSelectStyle(ShapeStyle polygonSelectStyle) {
		this.polygonSelectStyle = polygonSelectStyle;
	}

	// Private methods:

	private ShapeStyle createStyleForFeature(Feature feature) {
		FeatureStyleInfo styleInfo = null;
		for (FeatureStyleInfo style : feature.getLayer().getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
			if (feature.getStyleId() == style.getIndex()) {
				styleInfo = style;
				break;
			}
		}
		ShapeStyle style = new ShapeStyle(styleInfo);

		if (feature.getGeometry() instanceof LineString || feature.getGeometry() instanceof MultiLineString) {
			style.merge(lineSelectStyle);
		} else if (feature.getGeometry() instanceof Polygon || feature.getGeometry() instanceof MultiPolygon) {
			style.merge(polygonSelectStyle);
		} else if (feature.getGeometry() instanceof Point || feature.getGeometry() instanceof MultiPoint) {
			style.merge(pointSelectStyle);
		}
		return style;
	}
}
