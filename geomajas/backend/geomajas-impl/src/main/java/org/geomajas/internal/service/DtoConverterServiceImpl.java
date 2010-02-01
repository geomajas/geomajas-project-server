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

package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.internal.layer.feature.ClippedInternalFeature;
import org.geomajas.internal.layer.feature.EditableFeature;
import org.geomajas.internal.layer.feature.RenderedFeatureImpl;
import org.geomajas.internal.layer.tile.InternalRasterTile;
import org.geomajas.internal.layer.tile.InternalVectorTile;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.Tile;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.service.BboxService;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Default implementation of DTO converter.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
@Component()
public class DtoConverterServiceImpl implements DtoConverterService {

	@Autowired
	private BboxService bboxService;

	private ConvertUtilsBean cub = new ConvertUtilsBean();

	// -------------------------------------------------------------------------
	// Attribute conversion:
	// -------------------------------------------------------------------------

	/**
	 * Converts a DTO attribute into a generic attribute object.
	 * 
	 * @param attribute
	 *            The DTO attribute.
	 * @return The server side attribute representation. As we don't know at this point what kind of object the
	 *         attribute is (that's a problem for the <code>FeatureModel</code>), we return an <code>Object</code>.
	 */
	public Object toObject(Attribute attribute) {
		if (attribute instanceof PrimitiveAttribute<?>) {
			return toPrimitiveObject((PrimitiveAttribute<?>) attribute);
		} else if (attribute instanceof AssociationAttribute) {
			return toAssociationObject((AssociationAttribute) attribute);
		} else {
			throw new IllegalArgumentException("AttributeConverter does not support conversion of " + attribute);
		}
	}

	/**
	 * Converts a server-side attribute object into a DTO attribute.
	 * 
	 * @param object
	 *            The attribute value.
	 * @param info
	 *            The attribute definition from the configuration.
	 * @return Returns a DTO attribute.
	 */
	public Attribute toDto(Object object, AttributeInfo info) {
		if (info instanceof PrimitiveAttributeInfo) {
			return toPrimitiveDto(object, (PrimitiveAttributeInfo) info);
		} else if (info instanceof AssociationAttributeInfo) {
			return toAssociationDto(object, (AssociationAttributeInfo) info);
		} else {
			throw new IllegalArgumentException("AttributeConverter does not support conversion of attribute info "
					+ info);
		}
	}

	// -------------------------------------------------------------------------
	// Private methods - Attribute conversion:
	// -------------------------------------------------------------------------

	private Attribute toAssociationDto(Object value, AssociationAttributeInfo associationAttributeInfo) {
		// TODO: implement
		return null;
	}

	private Attribute toPrimitiveDto(Object value, PrimitiveAttributeInfo info) {
		switch (info.getType()) {
			case BOOLEAN:
				return new BooleanAttribute((Boolean) convertToClass(value, Boolean.class));
			case SHORT:
				return new ShortAttribute((Short) convertToClass(value, Short.class));
			case INTEGER:
				return new IntegerAttribute((Integer) convertToClass(value, Integer.class));
			case LONG:
				return new LongAttribute((Long) convertToClass(value, Long.class));
			case FLOAT:
				return new FloatAttribute((Float) convertToClass(value, Float.class));
			case DOUBLE:
				return new DoubleAttribute((Double) convertToClass(value, Double.class));
			case CURRENCY:
				return new CurrencyAttribute((String) convertToClass(value, String.class));
			case STRING:
				return new StringAttribute(value == null ? null : value.toString());
			case DATE:
				return new DateAttribute((Date) value);
			case URL:
				return new UrlAttribute((String) value);
			case IMGURL:
				return new ImageUrlAttribute((String) value);
		}
		throw new IllegalArgumentException("Cannot create primitive attribute of type " + info);
	}

	private Object toAssociationObject(AssociationAttribute primitiveAttribute) {
		// TODO: implement
		return null;
	}

	private Object toPrimitiveObject(PrimitiveAttribute<?> primitive) {
		return primitive.getValue();
	}

	private Object convertToClass(Object object, Class<?> c) {
		if (object == null) {
			return null;
		} else if (c.isInstance(object)) {
			return object;
		}
		return cub.convert(object.toString(), c);
	}

	// -------------------------------------------------------------------------
	// Feature conversion:
	// -------------------------------------------------------------------------

	/**
	 * Convert the server side feature to a DTO feature that can be sent to the client.
	 * 
	 * @param feature
	 *            The server-side feature representation.
	 * @return Returns the DTO feature.
	 */
	public Feature toDto(InternalFeature feature) {
		if (feature == null) {
			return null;
		}
		Feature dto = new Feature(feature.getId());
		FeatureInfo info = feature.getLayer().getLayerInfo().getFeatureInfo();
		dto.setAttributes(toDto(feature.getAttributes(), info));
		dto.setLabel(feature.getLabel());
		dto.setGeometry(toDto(feature.getGeometry()));
		if (feature.getStyleInfo() != null) {
			dto.setStyleId((int) feature.getStyleInfo().getId());
		}

		if (feature instanceof ClippedInternalFeature) {
			ClippedInternalFeature vFeature = (ClippedInternalFeature) feature;
			dto.setClipped(vFeature.isClipped());
		} else if (feature instanceof EditableFeature) {
			EditableFeature eFeature = (EditableFeature) feature;
			dto.setEditable(eFeature.isEditable());
		}
		return dto;
	}

	/**
	 * Convert a DTO feature to a server-side feature.
	 * 
	 * @param dto
	 *            The DTO feature that comes from the client.
	 * @return Returns a server-side feature object.
	 */
	public InternalFeature toFeature(Feature dto) {
		if (dto == null) {
			return null;
		}
		InternalFeature feature;
		if (dto.isEditable()) {
			feature = new EditableFeature(bboxService);
			((EditableFeature) feature).setEditable(true);
		} else {
			feature = new ClippedInternalFeature(new RenderedFeatureImpl(bboxService));
			((ClippedInternalFeature) feature).setClipped(dto.isClipped());
		}
		feature.setAttributes(toFeature(dto.getAttributes()));
		feature.setId(dto.getId());
		feature.setLabel(dto.getLabel());
		feature.setGeometry(toJts(dto.getGeometry()));
		return feature;
	}

	// -------------------------------------------------------------------------
	// Private methods - Feature conversion:
	// -------------------------------------------------------------------------

	private Map<String, Attribute> toDto(Map<String, Object> attributes, FeatureInfo info) {
		HashMap<String, Attribute> map = new HashMap<String, Attribute>();
		for (AttributeInfo attributeInfo : info.getAttributes()) {
			String name = attributeInfo.getName();
			if (attributes.containsKey(name)) {
				Object attribute = attributes.get(name);
				map.put(name, toDto(attribute, attributeInfo));
			}
		}
		return map;
	}

	private Map<String, Object> toFeature(Map<String, Attribute> attributes) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (String name : attributes.keySet()) {
			map.put(name, toObject(attributes.get(name)));
		}
		return map;
	}

	// -------------------------------------------------------------------------
	// Geometry conversion:
	// -------------------------------------------------------------------------

	/**
	 * Takes in a JTS geometry, and creates a new DTO geometry from it.
	 * 
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 */
	public Geometry toDto(com.vividsolutions.jts.geom.Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		int srid = geometry.getSRID();
		int precision = -1;
		PrecisionModel precisionmodel = geometry.getPrecisionModel();
		if (!precisionmodel.isFloating()) {
			precision = (int) Math.log10(precisionmodel.getScale());
		}

		Geometry dto = null;
		if (geometry instanceof Point) {
			dto = new Geometry("Point", srid, precision);
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof LinearRing) {
			dto = new Geometry("LinearRing", srid, precision);
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof LineString) {
			dto = new Geometry("LineString", srid, precision);
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof Polygon) {
			dto = new Geometry("Polygon", srid, precision);
			Polygon polygon = (Polygon) geometry;
			Geometry[] geometries = new Geometry[polygon.getNumInteriorRing() + 1];
			for (int i = 0; i < geometries.length; i++) {
				if (i == 0) {
					geometries[i] = toDto(polygon.getExteriorRing());
				} else {
					geometries[i] = toDto(polygon.getInteriorRingN(i - 1));
				}
			}
			dto.setGeometries(geometries);
		} else if (geometry instanceof MultiPoint) {
			dto = new Geometry("MultiPoint", srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiLineString) {
			dto = new Geometry("MultiLineString", srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiPolygon) {
			dto = new Geometry("MultiPolygon", srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		}

		return dto;
	}

	/**
	 * Takes in a DTO geometry, and converts it into a JTS geometry.
	 * 
	 * @param geometry
	 *            The DTO geometry to convert into a JTS geometry.
	 * @return Returns a JTS geometry.
	 */
	public com.vividsolutions.jts.geom.Geometry toJts(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		int srid = geometry.getSrid();
		int precision = geometry.getPrecision();
		PrecisionModel model;
		if (precision == -1) {
			model = new PrecisionModel(PrecisionModel.FLOATING);
		} else {
			model = new PrecisionModel(Math.pow(10, precision));
		}
		GeometryFactory factory = new GeometryFactory(model, srid);
		com.vividsolutions.jts.geom.Geometry jts = null;

		String geometryType = geometry.getGeometryType();
		if ("Point".equals(geometryType)) {
			jts = factory.createPoint(convertCoordinates(geometry)[0]);
		} else if ("LinearRing".equals(geometryType)) {
			jts = factory.createLinearRing(convertCoordinates(geometry));
		} else if ("LineString".equals(geometryType)) {
			jts = factory.createLineString(convertCoordinates(geometry));
		} else if ("Polygon".equals(geometryType)) {
			LinearRing exteriorRing = (LinearRing) toJts(geometry.getGeometries()[0]);
			LinearRing[] interiorRings = new LinearRing[geometry.getGeometries().length - 1];
			for (int i = 0; i < interiorRings.length; i++) {
				interiorRings[i] = (LinearRing) toJts(geometry.getGeometries()[i + 1]);
			}
			jts = factory.createPolygon(exteriorRing, interiorRings);
		} else if ("MultiPoint".equals(geometryType)) {
			Point[] points = new Point[geometry.getGeometries().length];
			jts = factory.createMultiPoint((Point[]) convertGeometries(geometry, points));
		} else if ("MultiLineString".equals(geometryType)) {
			LineString[] lineStrings = new LineString[geometry.getGeometries().length];
			jts = factory.createMultiLineString((LineString[]) convertGeometries(geometry, lineStrings));
		} else if ("MultiPolygon".equals(geometryType)) {
			Polygon[] polygons = new Polygon[geometry.getGeometries().length];
			jts = factory.createMultiPolygon((Polygon[]) convertGeometries(geometry, polygons));
		}

		return jts;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from JTS to DTO:
	// -------------------------------------------------------------------------

	private Coordinate[] convertCoordinates(com.vividsolutions.jts.geom.Geometry geometry) {
		Coordinate[] coordinates = new Coordinate[geometry.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new Coordinate(geometry.getCoordinates()[i].x, geometry.getCoordinates()[i].y);
		}
		return coordinates;
	}

	private Geometry[] convertGeometries(com.vividsolutions.jts.geom.Geometry geometry) {
		Geometry[] geometries = new Geometry[geometry.getNumGeometries()];
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toDto(geometry.getGeometryN(i));
		}
		return geometries;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from DTO to JTS:
	// -------------------------------------------------------------------------

	private com.vividsolutions.jts.geom.Coordinate[] convertCoordinates(Geometry geometry) {
		com.vividsolutions.jts.geom.Coordinate[] coordinates = new com.vividsolutions.jts.geom.Coordinate[geometry
				.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new com.vividsolutions.jts.geom.Coordinate(geometry.getCoordinates()[i].getX(), geometry
					.getCoordinates()[i].getY());
		}
		return coordinates;
	}

	private com.vividsolutions.jts.geom.Geometry[] convertGeometries(Geometry geometry,
			com.vividsolutions.jts.geom.Geometry[] geometries) {
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toJts(geometry.getGeometries()[i]);
		}
		return geometries;
	}

	// -------------------------------------------------------------------------
	// Tile conversion:
	// -------------------------------------------------------------------------

	/**
	 * Convert a server-side tile representations into a DTO tile.
	 * 
	 * @param tile
	 *            The server-side representation of a tile.
	 * @return Returns the DTO version that can be sent to the client.
	 */
	public Tile toDto(InternalTile tile) {
		if (tile != null) {
			if (tile instanceof InternalVectorTile) {
				InternalVectorTile vTile = (InternalVectorTile) tile;
				VectorTile dto = new VectorTile();
				updateBasicTile(tile, dto);
				dto.setFeatureFragment(vTile.getFeatureFragment());
				dto.setLabelFragment(vTile.getLabelFragment());
				return dto;
			} else if (tile instanceof InternalRasterTile) {
				InternalRasterTile rTile = (InternalRasterTile) tile;
				RasterTile dto = new RasterTile();
				updateBasicTile(tile, dto);
				dto.setFeatureImage(rTile.getFeatureImage());
				dto.setLabelImage(rTile.getLabelImage());
				return dto;
			} else {
				Tile dto = new Tile();
				updateBasicTile(tile, dto);
				return dto;
			}
		}
		return null;
	}

	private void updateBasicTile(InternalTile source, Tile target) {
		target.setClipped(source.isClipped());
		target.setCode(source.getCode());
		target.setCodes(source.getCodes());
		target.setScreenHeight(source.getScreenHeight());
		target.setScreenWidth(source.getScreenWidth());
		target.setTileHeight(source.getTileHeight());
		target.setTileWidth(source.getTileWidth());
		List<Feature> features = new ArrayList<Feature>();
		for (InternalFeature feature : source.getFeatures()) {
			features.add(toDto(feature));
		}
		target.setFeatures(features);
	}

	// -------------------------------------------------------------------------
	// Bounding box conversion:
	// -------------------------------------------------------------------------

	/**
	 * Convert a <code>Bbox</code> to a JTS envelope.
	 * 
	 * @param bbox
	 *            Geomajas <code>Bbox</code>
	 * @return JTS envelope
	 */
	public Envelope toEnvelope(Bbox bbox) {
		return new Envelope(bbox.getX(), bbox.getMaxX(), bbox.getY(), bbox.getMaxY());
	}

	/**
	 * Convert JTS envelope into a <code>Bbox</code>.
	 * 
	 * @param envelope
	 *            JTS envelope
	 * @return Geomajas <code>Bbox</code>
	 */
	public Bbox fromEnvelope(Envelope envelope) {
		return new Bbox(envelope.getMinX(), envelope.getMinY(), envelope.getWidth(), envelope.getHeight());
	}

}
