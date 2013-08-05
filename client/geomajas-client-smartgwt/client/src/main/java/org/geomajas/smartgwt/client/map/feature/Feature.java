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

package org.geomajas.smartgwt.client.map.feature;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.AttributeUtil;
import org.geomajas.smartgwt.client.util.GeometryConverter;
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
 * Definition of an object belonging to a {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}. A feature is an
 * object with a unique ID within it's layer, a list of alpha-numerical attributes and a geometry.
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
	private Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	/** Are the attributes lazy Loaded yet? */
	private boolean attributesLoaded;
	
	/** This feature's geometry. */
	private Geometry geometry;

	/** The identifier of this feature's style. */
	private String styleId;

	/** Label text. */
	private String label = ""; // new features have no label yet, empty string to avoid npe !

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

	/** No-arguments constructor. */
	public Feature() {
		this((org.geomajas.layer.feature.Feature) null, null);
	}

	/**
	 * Construct feature with given id for given layer.
	 *
	 * @param id feature id
	 * @param layer layer
	 */
	public Feature(String id, VectorLayer layer) {
		this(layer);
		this.id = id;
	}

	/**
	 * Construct a feature for given layer.
	 *
	 * @param layer layer
	 */
	public Feature(VectorLayer layer) {
		this((org.geomajas.layer.feature.Feature) null, layer);
	}

	/**
	 * Construct a feature based from a feature DTO for given layer.
	 *
	 * @param dto feature dto
	 * @param layer layer
	 */
	public Feature(org.geomajas.layer.feature.Feature dto, VectorLayer layer) {
		this.layer = layer;
		this.geometry = null;
		this.styleId = null;
		this.labelPosition = null;
		this.clipped = false;
		if (null != dto) {
			label = dto.getLabel();
			attributes = dto.getAttributes();
			attributesLoaded = true;
			id = dto.getId();
			geometry = GeometryConverter.toGwt(dto.getGeometry());
			styleId = dto.getStyleId();
			crs = dto.getCrs();
			setUpdatable(dto.isUpdatable());
			setDeletable(dto.isDeletable());
		} else {
			if (layer != null) {
				// Create empty attributes:
				for (AbstractAttributeInfo attrInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
					attributes.put(attrInfo.getName(), AttributeUtil.createEmptyAttribute(attrInfo));
				}
			}
			setUpdatable(true);
			setDeletable(true);
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

	/** Clone the object. */
	public Feature clone() { // NOSONAR overwriting clone but not using super.clone()
		Feature feature = new Feature(this.layer);
		if (null != attributes) {
			feature.attributes = new HashMap<String, Attribute>();
			for (Entry<String, Attribute> entry : attributes.entrySet()) {
				feature.attributes.put(entry.getKey(), (Attribute<?>) entry.getValue().clone());
			}
		}
		feature.clipped = clipped;
		feature.labelPosition = labelPosition;
		feature.label = label;
		feature.layer = layer;
		if (null != geometry) {
			feature.geometry = (Geometry) geometry.clone();
		}
		feature.id = id;
		feature.styleId = styleId;
		feature.crs = crs;
		feature.deletable = deletable;
		feature.updatable = updatable;
		feature.attributesLoaded = attributesLoaded;
		return feature;
	}

	/**
	 * Get attribute value for given attribute name.
	 *
	 * @param attributeName attribute name
	 * @return attribute value
	 */
	public Object getAttributeValue(String attributeName) {
		Attribute attribute = getAttributes().get(attributeName);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setBooleanAttribute(String name, Boolean value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof BooleanAttribute)) {
			throw new IllegalStateException("Cannot set boolean value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((BooleanAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setCurrencyAttribute(String name, String value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof CurrencyAttribute)) {
			throw new IllegalStateException("Cannot set currency value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((CurrencyAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setDateAttribute(String name, Date value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof DateAttribute)) {
			throw new IllegalStateException("Cannot set date value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((DateAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setDoubleAttribute(String name, Double value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof DoubleAttribute)) {
			throw new IllegalStateException("Cannot set double value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((DoubleAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setFloatAttribute(String name, Float value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof FloatAttribute)) {
			throw new IllegalStateException("Cannot set float value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((FloatAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setImageUrlAttribute(String name, String value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof ImageUrlAttribute)) {
			throw new IllegalStateException("Cannot set imageUrl value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((ImageUrlAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setIntegerAttribute(String name, Integer value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof IntegerAttribute)) {
			throw new IllegalStateException("Cannot set integer value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((IntegerAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setLongAttribute(String name, Long value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof LongAttribute)) {
			throw new IllegalStateException("Cannot set boolean value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((LongAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setShortAttribute(String name, Short value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof ShortAttribute)) {
			throw new IllegalStateException("Cannot set short value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((ShortAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setStringAttribute(String name, String value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof StringAttribute)) {
			throw new IllegalStateException("Cannot set boolean value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((StringAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setUrlAttribute(String name, String value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof UrlAttribute)) {
			throw new IllegalStateException("Cannot set url value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((UrlAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void setManyToOneAttribute(String name, AssociationValue value) {
		Attribute attribute = getAttributes().get(name);
		if (!(attribute instanceof ManyToOneAttribute)) {
			throw new IllegalStateException("Cannot set manyToOne value on attribute with different type, " +
					attribute.getClass().getName() + " setting value " + value);
		}
		((ManyToOneAttribute) attribute).setValue(value);
	}

	/**
	 * Set attribute value of given type.
	 *
	 * @param name attribute name
	 * @param value attribute value
	 */
	public void addOneToManyValue(String name, AssociationValue value) {
		Attribute attribute = getAttributes().get(name);
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
		dto.setLabel(getLabel());
		return dto;
	}

	/**
	 * Get feature label.
	 *
	 * @return label
	 */
	public String getLabel() {
		return label;
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
		return attributes;
	}

	/**
	 * Set the attributes map.
	 * 
	 * @param attributes
	 *            attributes map
	 */
	public void setAttributes(Map<String, Attribute> attributes) {
		if (null == attributes) {
			throw new IllegalArgumentException("Attributes should be not-null.");
		}
		this.attributes = attributes;
		this.attributesLoaded = true;
	}

	/**
	 * Check whether the attributes are already available or should be lazy loaded.
	 * 
	 * @return true when attributes are available
	 */
	public boolean isAttributesLoaded() {
		return attributesLoaded;
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

	/**
	 * Is this feature selected?
	 *
	 * @return true when feature is selected
	 */
	public boolean isSelected() {
		return layer.isFeatureSelected(getId());
	}

	/**
	 * Get the layer which contains this feature.
	 *
	 * @return layer which contains this feature
	 */
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

	/**
	 * Get the style id for this feature.
	 *
	 * @return style if
	 */
	public String getStyleId() {
		return styleId;
	}

	/**
	 * Set the style id for this feature.
	 *
	 * @param styleId style id
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

}
