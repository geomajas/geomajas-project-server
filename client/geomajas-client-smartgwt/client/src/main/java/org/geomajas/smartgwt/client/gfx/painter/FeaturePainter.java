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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.gfx.MapContext;
import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.PaintableGroup;
import org.geomajas.smartgwt.client.gfx.Painter;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.spatial.WorldViewTransformer;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;

/**
 * Painter implementation for {@link org.geomajas.smartgwt.client.map.feature.Feature}s.
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

	/** No-arguments constructor for GWT. */
	public FeaturePainter() {
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
	 *            A {@link org.geomajas.smartgwt.client.gfx.paintable.Text} object.
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

			switch (geometry.getLayerType()) {
				case LINESTRING:
					context.getVectorContext().drawLine(feature, name, (LineString) geometry, style);
					break;
				case MULTILINESTRING:
					MultiLineString mls = (MultiLineString) geometry;
					for (int i = 0; i < mls.getNumGeometries(); i++) {
						context.getVectorContext().drawLine(feature, name + "." + i,
								(LineString) mls.getGeometryN(i), style);
					}
					break;
				case POLYGON:
					context.getVectorContext().drawPolygon(feature, name, (Polygon) geometry, style);
					break;
				case MULTIPOLYGON:
					MultiPolygon mp = (MultiPolygon) geometry;
					for (int i = 0; i < mp.getNumGeometries(); i++) {
						context.getVectorContext().drawPolygon(feature, name + "." + i,
								(Polygon) mp.getGeometryN(i), style);
					}
					break;
				case POINT:
					if (hasImageSymbol(feature)) {
						context.getVectorContext().drawSymbol(feature, name, geometry.getCoordinate(), null,
								feature.getStyleId() + "-selection");
					} else {
						context.getVectorContext().drawSymbol(feature, name, geometry.getCoordinate(), style,
								feature.getStyleId());
					}
					break;
				case MULTIPOINT:
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
					break;
				default:
					throw new IllegalStateException("Cannot draw feature with Geometry type " +
							geometry.getLayerType());
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
		ShapeStyle style = null;
		if (null != feature) {
			FeatureStyleInfo styleInfo = null;
			if (feature.getStyleId() != null) {
				String check = feature.getStyleId();
				if (null != check) {
					for (FeatureStyleInfo fsi :
							feature.getLayer().getLayerInfo().getNamedStyleInfo().getFeatureStyles()) {
						if (check.equals(fsi.getStyleId())) {
							styleInfo = fsi;
							break;
						}
					}
				}
			}
			style = new ShapeStyle(styleInfo);
	
			Geometry geometry = feature.getGeometry();
			if (null != geometry) {
				switch (geometry.getLayerType()) {
					case LINESTRING:
					case MULTILINESTRING:
						style.merge(lineSelectStyle);
						break;
					case POLYGON:
					case MULTIPOLYGON:
						style.merge(polygonSelectStyle);
						break;
					case POINT:
					case MULTIPOINT:
						style.merge(pointSelectStyle);
						break;
					default:
						throw new IllegalStateException("Cannot create style for feature with Geometry type " +
								geometry.getLayerType());
				}
			}
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
