/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.map.feature;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.AttributeUtil;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

/**
 * <p>
 * Definition of an object belonging to a {@link VectorLayer}. Simply put, a feature is an object with a unique ID
 * within it's layer, a list of alpha-numerical attributes and a geometry.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
public class Feature implements Paintable, Cloneable {

	/** Unique identifier */
	private String id;

	/** Reference to the layer. */
	private VectorLayer layer;

	/** Map of this feature's attributes. */
	private Map<String, Attribute> attributes;

	/** This feature's geometry. */
	private Geometry geometry;

	/** The identifier of this feature's style. */
	private String styleId;

	/** Coordinate for the label. */
	private Coordinate labelPosition;

	/** is the feature clipped ? */
	private boolean clipped;

	/**
	 * Is it allowed for the user in question to edit this feature?
	 */
	private boolean updatable;

	/**
	 * Is it allowed for the user in question to delete this feature?
	 */
	private boolean deletable;

	private String crs;

	// Constructors:

	public Feature() {
		this(null);
	}

	public Feature(String id, VectorLayer layer) {
		this(layer);
		this.id = id;
	}

	public Feature(VectorLayer layer) {
		this.layer = layer;
		this.geometry = null;
		this.styleId = null;
		this.labelPosition = null;
		this.clipped = false;
		setUpdatable(true);
		setDeletable(true);
	}

	public Feature(org.geomajas.layer.feature.Feature dto, VectorLayer layer) {
		this(layer);
		if (null != dto) {
			attributes = dto.getAttributes();
			id = dto.getId();
			geometry = GeometryConverter.toGwt(dto.getGeometry());
			styleId = dto.getStyleId();
			crs = dto.getCrs();
			setUpdatable(dto.isUpdatable());
			setDeletable(dto.isDeletable());
		}
	}

	// Paintable implementation:

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	public String getId() {
		return id;
	}

	// Class specific functions:

	public Feature clone() {
		Feature feature = new Feature(this.layer);
		if (null != attributes) {
			feature.attributes = new HashMap<String, Attribute>();
			for (Entry<String, Attribute> entry : attributes.entrySet()) {
				feature.attributes.put(entry.getKey(), (Attribute<?>) entry.getValue().clone());
			}
		}
		feature.clipped = clipped;
		feature.labelPosition = labelPosition;
		feature.layer = layer;
		if (null != geometry) {
			feature.geometry = (Geometry) geometry.clone();
		}
		feature.id = id;
		feature.styleId = styleId;
		feature.crs = crs;
		feature.deletable = deletable;
		feature.updatable = updatable;
		return feature;
	}

	public Object getAttributeValue(String attributeName) {
		Attribute attribute = getAttributes().get(attributeName);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}
	
	public void setBooleanAttribute(String name, Boolean value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof BooleanAttribute)) {
			throw new IllegalStateException("Cannot set boolean value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((BooleanAttribute) attribute).setValue(value);
	}

	public void setCurrencyAttribute(String name, String value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof CurrencyAttribute)) {
			throw new IllegalStateException("Cannot set currency value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((CurrencyAttribute) attribute).setValue(value);
	}

	public void setDateAttribute(String name, Date value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof DateAttribute)) {
			throw new IllegalStateException("Cannot set date value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((DateAttribute) attribute).setValue(value);
	}

	public void setDoubleAttribute(String name, Double value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof DoubleAttribute)) {
			throw new IllegalStateException("Cannot set double value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((DoubleAttribute) attribute).setValue(value);
	}

	public void setFloatAttribute(String name, Float value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof FloatAttribute)) {
			throw new IllegalStateException("Cannot set float value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((FloatAttribute) attribute).setValue(value);
	}

	public void setImageUrlAttribute(String name, String value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof ImageUrlAttribute)) {
			throw new IllegalStateException("Cannot set imageUrl value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((ImageUrlAttribute) attribute).setValue(value);
	}

	public void setIntegerAttribute(String name, Integer value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof IntegerAttribute)) {
			throw new IllegalStateException("Cannot set integer value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((IntegerAttribute) attribute).setValue(value);
	}

	public void setLongAttribute(String name, Long value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof LongAttribute)) {
			throw new IllegalStateException("Cannot set boolean value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((LongAttribute) attribute).setValue(value);
	}

	public void setShortAttribute(String name, Short value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof ShortAttribute)) {
			throw new IllegalStateException("Cannot set short value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((ShortAttribute) attribute).setValue(value);
	}

	public void setStringAttribute(String name, String value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof StringAttribute)) {
			throw new IllegalStateException("Cannot set boolean value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((StringAttribute) attribute).setValue(value);
	}

	public void setUrlAttribute(String name, String value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof UrlAttribute)) {
			throw new IllegalStateException("Cannot set url value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((UrlAttribute) attribute).setValue(value);
	}

	public void setManyToOneAttribute(String name, AssociationValue value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof ManyToOneAttribute)) {
			throw new IllegalStateException("Cannot set manyToOne value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((ManyToOneAttribute) attribute).setValue(value);
	}

	public void addOneToManyValue(String name, AssociationValue value) {
		Attribute attribute = getAndCreateAttributes().get(name);
		if (!(attribute instanceof OneToManyAttribute)) {
			throw new IllegalStateException("Cannot set oneToMany value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		OneToManyAttribute oneToMany = (OneToManyAttribute) attribute;
		if (oneToMany.getValue() == null) {
			oneToMany.setValue(new ArrayList<AssociationValue>());
		}
		oneToMany.getValue().add(value);
	}

	/**
	 * Transform this object into a DTO feature.
	 * 
	 * @return dto for this feature
	 */
	public org.geomajas.layer.feature.Feature toDto() {
		org.geomajas.layer.feature.Feature dto = new org.geomajas.layer.feature.Feature();
		dto.setAttributes(attributes);
		dto.setClipped(clipped);
		dto.setId(id);
		dto.setGeometry(GeometryConverter.toDto(geometry));
		dto.setCrs(crs);
		return dto;
	}

	public String getLabel() {
		String attributeName = layer.getLayerInfo().getNamedStyleInfo().getLabelStyle().getLabelAttributeName();
		Object attributeValue = getAttributeValue(attributeName);
		return attributeValue == null ? "null" : attributeValue.toString();
	}

	// Getters and setters:

	/**
	 * Crs as (optionally) set by the backend.
	 * 
	 * @return crs for this feature
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Get the attributes map, throws exception when it needs to be lazy loaded.
	 * 
	 * @return attributes map
	 * @throws IllegalStateException
	 *             attributes not present because of lazy loading
	 */
	public Map<String, Attribute> getAttributes() throws IllegalStateException {
		if (null == attributes) {
			throw new IllegalStateException("Attributes not available, use LazyLoader.");
		}
		return attributes;
	}

	/**
	 * Get the attributes map, create if it is not loaded yet.
	 * 
	 * @return attributes map
	 * @throws IllegalStateException
	 *             attributes not present because of lazy loading
	 */
	private Map<String, Attribute> getAndCreateAttributes()  {
		if (null == attributes) {
			attributes = new HashMap<String, Attribute>();
			if (layer != null) {
				// Create empty attributes:
				for (AttributeInfo attrInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
					attributes.put(attrInfo.getName(), AttributeUtil.createEmptyAttribute(attrInfo));
				}
			}
		}
		return attributes;
	}

	/**
	 * Set the attributes map.
	 * 
	 * @param attributes
	 *            attributes map
	 */
	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Check whether the attributes are already available or should be lazy loaded.
	 * 
	 * @return true when attributes are available
	 */
	public boolean isAttributesLoaded() {
		return attributes != null;
	}

	/**
	 * Get the feature's geometry, throws exception when it needs to be lazy loaded.
	 * 
	 * @return geometry
	 * @throws IllegalStateException
	 *             attributes not present because of lazy loading
	 */
	public Geometry getGeometry() throws IllegalStateException {
		if (null == geometry) {
			throw new IllegalStateException("Geometry not available, use LazyLoader.");
		}
		return geometry;
	}

	/**
	 * Set the geometry.
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Check whether the geometry is already available or should be lazy loaded.
	 * 
	 * @return true when geometry are available
	 */
	public boolean isGeometryLoaded() {
		return geometry != null;
	}

	public boolean isSelected() {
		return layer.isFeatureSelected(getId());
	}

	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Is the logged in user allowed to edit this feature?
	 * 
	 * @return true when edit/update is allowed for this feature
	 */
	public boolean isUpdatable() {
		return updatable;
	}

	/**
	 * Set whether the logged in user is allowed to edit/update this feature.
	 * 
	 * @param editable
	 *            true when edit/update is allowed for this feature
	 */
	public void setUpdatable(boolean editable) {
		this.updatable = editable;
	}

	/**
	 * Is the logged in user allowed to delete this feature?
	 * 
	 * @return true when delete is allowed for this feature
	 */
	public boolean isDeletable() {
		return deletable;
	}

	/**
	 * Set whether the logged in user is allowed to delete this feature.
	 * 
	 * @param deletable
	 *            true when deleting this feature is allowed
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

}
