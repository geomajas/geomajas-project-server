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

package org.geomajas.gwt.client.map.feature;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Definition of an object belonging to a {@link VectorLayer}. Simply put, a feature is an object with a unique ID
 * within it's layer, a list of alpha-numerical attributes and a geometry.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Feature implements Paintable, Cloneable {

	/** Unique identifier, over layers */
	private String id;

	/** Reference to the layer. */
	private VectorLayer layer;

	/** Map of this feature's attributes. */
	private Map<String, Attribute> attributes;

	/** This feature's geometry. */
	private Geometry geometry;

	/** The identifier of this feature's style. */
	private int styleId;

	/** Coordinate for the label. */
	private Coordinate labelPosition;

	/** is the feature clipped ? */
	private boolean clipped;

	// Constructors:

	public Feature() {
		this(null);
	}

	public Feature(VectorLayer layer) {
		this(null, layer);
	}

	public Feature(org.geomajas.layer.feature.Feature dto, VectorLayer layer) {
		this.layer = layer;
		this.attributes = new HashMap<String, Attribute>();
		this.geometry = null;
		this.styleId = 1;
		this.labelPosition = null;
		this.clipped = false;
		if (dto != null) {
			attributes = dto.getAttributes();
			id = dto.getId();
			geometry = GeometryConverter.toGwt(dto.getGeometry());
			styleId = dto.getStyleId();
		}
	}

	// Paintable implementation:

	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		visitor.visit(this);
	}

	public String getId() {
		return id;
	}

	/**
	 * Id which should be used for this feature when rendered as "selected".
	 * 
	 * @return selected id
	 */
	public String getSelectionId() {
		return layer.getMapModel().getId() + "." + layer.getId() + ".selection." + getId();
	}

	public static String getFeatureIdFromSelectionId(String selectionId) {
		String[] ids = selectionId.split("\\."); // It's a regular expression, not literally.
		if (ids.length > 2) {
			return ids[ids.length - 2] + "." + ids[ids.length - 1];
		}
		return null;
	}

	// Class specific functions:

	public Feature clone() {
		Feature feature = new Feature(this.layer);
		if (null != attributes) {
			feature.attributes = new HashMap<String, Attribute>(attributes);
		}
		feature.clipped = clipped;
		feature.labelPosition = labelPosition;
		feature.layer = layer;
		if (null != geometry) {
			feature.geometry = (Geometry) geometry.clone();
		}
		feature.id = id;
		feature.styleId = styleId;
		return feature;
	}

	public Object getAttributeValue(String attributeName) {
		Attribute attribute = attributes.get(attributeName);
		if (attribute instanceof BooleanAttribute) {
			return ((BooleanAttribute) attribute).getValue();
		} else if (attribute instanceof CurrencyAttribute) {
			return ((CurrencyAttribute) attribute).getValue();
		} else if (attribute instanceof DateAttribute) {
			return ((DateAttribute) attribute).getValue();
		} else if (attribute instanceof DoubleAttribute) {
			return ((DoubleAttribute) attribute).getValue();
		} else if (attribute instanceof FloatAttribute) {
			return ((FloatAttribute) attribute).getValue();
		} else if (attribute instanceof ImageUrlAttribute) {
			return ((ImageUrlAttribute) attribute).getValue();
		} else if (attribute instanceof IntegerAttribute) {
			return ((IntegerAttribute) attribute).getValue();
		} else if (attribute instanceof LongAttribute) {
			return ((LongAttribute) attribute).getValue();
		} else if (attribute instanceof ShortAttribute) {
			return ((ShortAttribute) attribute).getValue();
		} else if (attribute instanceof StringAttribute) {
			return ((StringAttribute) attribute).getValue();
		} else if (attribute instanceof UrlAttribute) {
			return ((UrlAttribute) attribute).getValue();
		}
		return null;
	}

	/**
	 * Transform this object into a DTO feature.
	 * 
	 * @return
	 */
	public org.geomajas.layer.feature.Feature toDto() {
		org.geomajas.layer.feature.Feature dto = new org.geomajas.layer.feature.Feature();
		dto.setAttributes(attributes);
		dto.setClipped(clipped);
		dto.setId(id);
		dto.setGeometry(GeometryConverter.toDto(geometry));
		return dto;
	}

	public String getLabel() {
		String attributeName = layer.getLayerInfo().getLabelAttribute().getLabelAttributeName();
		return (String) getAttributeValue(attributeName);
	}

	// Getters and setters:

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public int getStyleId() {
		return styleId;
	}

	public void setStyleId(int styleId) {
		this.styleId = styleId;
	}

	public boolean isSelected() {
		return layer.isFeatureSelected(getId());
	}

	public VectorLayer getLayer() {
		return layer;
	}
}
