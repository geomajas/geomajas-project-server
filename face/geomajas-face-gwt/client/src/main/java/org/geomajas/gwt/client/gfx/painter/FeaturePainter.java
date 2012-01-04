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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

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

	public FeaturePainter() {
	}

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
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		if (paintable != null) {
			Feature feature = (Feature) paintable;
			WorldViewTransformer worldViewTransformer = feature.getLayer().getMapModel().getMapView()
					.getWorldViewTransformer();
			Geometry geometry = worldViewTransformer.worldToPan(feature.getGeometry());
			ShapeStyle style = createStyleForFeature(feature);
			PaintableGroup selectionGroup = feature.getLayer().getSelectionGroup();
			context.getVectorContext().drawGroup(selectionGroup, feature);
			String name = feature.getLayer().getId() + "-" + feature.getId();

			if (geometry instanceof LineString) {
				context.getVectorContext().drawLine(feature, name, (LineString) geometry, style);
			} else if (geometry instanceof MultiLineString) {
				MultiLineString m = (MultiLineString) geometry;
				for (int i = 0; i < m.getNumGeometries(); i++) {
					context.getVectorContext().drawLine(feature, name + "." + i, (LineString) m.getGeometryN(i), style);
				}
			} else if (geometry instanceof Polygon) {
				context.getVectorContext().drawPolygon(feature, name, (Polygon) geometry, style);
			} else if (geometry instanceof MultiPolygon) {
				MultiPolygon m = (MultiPolygon) geometry;
				for (int i = 0; i < m.getNumGeometries(); i++) {
					context.getVectorContext().drawPolygon(feature, name + "." + i, (Polygon) m.getGeometryN(i), style);
				}
			} else if (geometry instanceof Point) {
				if (hasImageSymbol(feature)) {
					context.getVectorContext().drawSymbol(feature, name, geometry.getCoordinate(), null,
							feature.getStyleId() + "-selection");
				} else {
					context.getVectorContext().drawSymbol(feature, name, geometry.getCoordinate(), style,
							feature.getStyleId());
				}
			} else if (geometry instanceof MultiPoint) {
				Coordinate[] coordinates = geometry.getCoordinates();
				if (hasImageSymbol(feature)) {
					for (int i = 0; i < coordinates.length; i++) {
						context.getVectorContext().drawSymbol(feature, name + "." + i, coordinates[i], null,
								feature.getStyleId() + "-selection");
					}
				} else {
					for (int i = 0; i < coordinates.length; i++) {
						context.getVectorContext().drawSymbol(feature, name + "." + i, coordinates[i], style,
								feature.getStyleId());
					}
				}
			}
		}
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist, nothing will
	 * be done.
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
		if (feature != null && feature.getStyleId() != null) {
			for (FeatureStyleInfo style : feature.getLayer().getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
				if (feature.getStyleId().equals(style.getStyleId())) {
					styleInfo = style;
					break;
				}
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

	private boolean hasImageSymbol(Feature feature) {
		String styleId = feature.getStyleId();
		for (FeatureStyleInfo style : feature.getLayer().getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
			if (style.getStyleId().equals(styleId)) {
				return style.getSymbol() != null && style.getSymbol().getImage() != null;
			}
		}
		return false;
	}
}
