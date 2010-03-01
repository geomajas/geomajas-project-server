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
package org.geomajas.layer.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * A simple Java beans based feature model.
 * 
 * @author Jan De Moerloose
 */
public class BeanFeatureModel implements FeatureModel {

	private final Logger log = LoggerFactory.getLogger(BeanFeatureModel.class);

	public static final String SEPARATOR_REGEXP = "\\.";

	private Class<?> beanClass;

	private int srid;

	private VectorLayerInfo vectorLayerInfo;

	private WKTReader reader;

	private WKTWriter writer;

	private boolean wkt;

	private DtoConverterService converterService;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	public BeanFeatureModel(VectorLayerInfo vectorLayerInfo, int srid, DtoConverterService converterService)
			throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
		this.converterService = converterService;

		try {
			beanClass = Class.forName(vectorLayerInfo.getFeatureInfo().getDataSourceName());
		} catch (ClassNotFoundException e) {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature class "
					+ vectorLayerInfo.getFeatureInfo().getDataSourceName() + " was not found");
		}
		this.srid = srid;
		reader = new WKTReader(new GeometryFactory(new PrecisionModel(), srid));
		writer = new WKTWriter();
		PropertyDescriptor d = BeanUtils.getPropertyDescriptor(beanClass, getGeometryAttributeName());
		Class geometryClass = d.getPropertyType();
		if (Geometry.class.isAssignableFrom(geometryClass.getClass())) {
			wkt = false;
		} else if (geometryClass == String.class) {
			wkt = true;
		} else {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature "
					+ vectorLayerInfo.getFeatureInfo().getDataSourceName() + " has no valid geometry attribute");
		}

		FeatureInfo featureInfo = vectorLayerInfo.getFeatureInfo();
		attributeInfoMap.put(featureInfo.getIdentifier().getName(), featureInfo.getIdentifier());
		for (AttributeInfo info : featureInfo.getAttributes()) {
			attributeInfoMap.put(info.getName(), info);
		}
	}

	public boolean canHandle(Object feature) {
		return beanClass.isInstance(feature);
	}

	public Attribute getAttribute(Object feature, String name) throws LayerException {
		Object attr = getAttributeRecursively(feature, name);
		AttributeInfo attributeInfo = attributeInfoMap.get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name);
		}
		try {
			return converterService.toDto(attr, attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		try {
			Map<String, Attribute> attribs = new HashMap<String, Attribute>();
			for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
				String name = attribute.getName();
				if (!name.equals(getGeometryAttributeName())) {
					Attribute value = this.getAttribute(feature, name);
					attribs.put(name, value);
				}
			}
			return attribs;
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Object geometry = getAttributeRecursively(feature, getGeometryAttributeName());
		if (!wkt || null == geometry) {
			log.debug("bean.getGeometry {}", geometry);
			return (Geometry) geometry;
		} else {
			try {
				Geometry geom = reader.read((String) geometry);
				log.debug("bean.getGeometry {}", geom);
				return geom;
			} catch (Throwable t) {
				throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM, geometry);
			}
		}
	}

	public String getGeometryAttributeName() throws LayerException {
		return getFeatureInfo().getGeometryType().getName();
	}

	public String getId(Object feature) throws LayerException {
		return getAttributeRecursively(feature, getFeatureInfo().getIdentifier().getName()).toString();
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	public Object newInstance() throws LayerException {
		try {
			return beanClass.newInstance();
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Object newInstance(String id) throws LayerException {
		try {
			Object instance = beanClass.newInstance();
			PrimitiveAttributeInfo pai = vectorLayerInfo.getFeatureInfo().getIdentifier();
			Object value;
			switch (pai.getType()) {
				case LONG:
					value = Long.parseLong(id);
					break;
				case STRING:
					value = id;
					break;
				default:
					throw new IllegalStateException("BeanFeatureModel only accepts String and long ids.");
			}
			writeProperty(instance, value, getFeatureInfo().getIdentifier().getName());
			return instance;
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	/**
	 * Does not support many-to-one and one-to-many....
	 */
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		for (Entry<String, Attribute> entry : attributes.entrySet()) {
			setAttributeRecursively(feature, null, entry.getKey(), entry.getValue());
		}
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		if (wkt) {
			String wktStr = writer.write(geometry);
			writeProperty(feature, wktStr, getGeometryAttributeName());
		} else {
			writeProperty(feature, geometry, getGeometryAttributeName());
		}
	}

	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
	}

	public FeatureInfo getFeatureInfo() {
		return vectorLayerInfo.getFeatureInfo();
	}

	/**
	 * A recursive getAttribute method. In case a one-to-many is passed, an array will be returned.
	 * 
	 * @param feature
	 *            The feature wherein to search for the attribute
	 * @param name
	 *            The attribute's full name. (can be attr1.attr2)
	 * @return Returns the value. In case a one-to-many is passed along the way, an array will be returned.
	 * @throws LayerException
	 *             oops
	 */
	private Object getAttributeRecursively(Object feature, String name) throws LayerException {
		if (feature == null) {
			return null;
		}
		// Split up properties: the first and the rest.
		String[] properties = name.split(SEPARATOR_REGEXP, 2);

		// Get the first property:
		Object tempFeature = readProperty(feature, properties[0]);

		// Detect if the first property is a collection (one-to-many):
		if (tempFeature instanceof Collection<?>) {
			Collection<?> features = (Collection<?>) tempFeature;
			Object[] values = new Object[features.size()];
			int count = 0;
			for (Object value : features) {
				if (properties.length == 1) {
					values[count++] = value;
				} else {
					values[count++] = getAttributeRecursively(value, properties[1]);
				}
			}
			return values;
		} else { // Else first property is not a collection (one-to-many):
			if (properties.length == 1 || tempFeature == null) {
				return tempFeature;
			} else {
				return getAttributeRecursively(tempFeature, properties[1]);
			}
		}
	}

	private void setAttributeRecursively(Object parent, AttributeInfo parentAttribute, String property, Object value)
			throws LayerException {
		if (parent == null) {
			return;
		}

		// Split up properties: the first and the rest.
		String[] properties = property.split(SEPARATOR_REGEXP, 2);

		// Search for the attribute definition for the first property:
		AttributeInfo attribute = null;
		if (parentAttribute == null) {
			// If no parent was given, we start from the FeatureInfo:
			for (AttributeInfo attr : getFeatureInfo().getAttributes()) {
				if (attr.getName().equals(properties[0])) {
					attribute = attr;
					break;
				}
			}
		} else if (parentAttribute instanceof AssociationAttributeInfo) {
			// If the parent is an association:
			AssociationAttributeInfo association = (AssociationAttributeInfo) parentAttribute;
			for (AttributeInfo attr : association.getFeature().getAttributes()) {
				if (attr.getName().equals(properties[0])) {
					attribute = attr;
					break;
				}
			}
		} else if (parentAttribute instanceof PrimitiveAttributeInfo) {
			// In case a primitive attribute was passed along, then it cannot be the parent attribute.
			attribute = parentAttribute;
		}

		// Setting an attribute is only allowed if the attribute is editable:
		if (attribute != null && attribute.isEditable()) {

			// If the attribute is primitive, simply set the value.
			if (attribute instanceof PrimitiveAttributeInfo) {
				writeProperty(parent, value, properties[0]);
			} else if (attribute instanceof AssociationAttributeInfo) {
				// In case the attribute is an association:
				AssociationAttributeInfo aso = (AssociationAttributeInfo) attribute;
				if (properties.length == 1) {
					// If the first property is the only one: set the entire complex object:
					String name = attribute.getName();
					switch (aso.getType()) {
						case MANY_TO_ONE:
							// how to get hold of the may-to-one ???
							// writeProperty(parent, value, name); @todo make this work, currently causes failures 
							break;
						case ONE_TO_MANY:
							if (value instanceof Object[]) {
								Object[] array = (Object[]) value;
								Collection<?> old = (Collection<?>) readProperty(parent, name);
								copyArrayToPersistentCollection(array, aso, parent, old);
							}
							break;
					}
				} else {
					// It's a complex property name...we must go deeper:
					Object newParent = readProperty(parent, properties[0]);
					if (newParent instanceof Collection<?>) {
						// one-to-many: go over all entries!
						Collection<?> colParent = (Collection<?>) newParent;
						for (Object parentEntry : colParent) {
							setAttributeRecursively(parentEntry, aso, properties[1], value);
						}
					} else {
						// many-to-one:
						setAttributeRecursively(newParent, aso, properties[1], value);
					}
				}
			}
		}
	}

	private void copyArrayToPersistentCollection(Object[] array, AssociationAttributeInfo aso, Object parent,
			Collection collection) throws LayerException {
		// find collection id name
		String identifier = aso.getFeature().getIdentifier().getName();
		// find the parent property
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(parent.getClass(), aso.getName());
		PropertyDescriptor[] childProps = BeanUtils.getPropertyDescriptors(pd.getPropertyType());
		String parentName = null;
		for (PropertyDescriptor p : childProps) {
			if (p.getWriteMethod() != null) {
				if (parent.getClass().isAssignableFrom(p.getPropertyType())) {
					parentName = p.getName();
				}
			}
		}
		// make a temporary map of the old children
		Map<Object, Object> childMap = new HashMap<Object, Object>();
		for (Object old : collection) {
			childMap.put(readProperty(old, identifier), old);
		}

		Set newOrUpdate = new HashSet();
		// new and update
		for (Object newChild : array) {
			Object id = readProperty(newChild, identifier);
			newOrUpdate.add(id);
			if (!childMap.containsKey(id)) {
				// new
				collection.add(newChild);
				if (parentName != null) {
					writeProperty(newChild, parent, parentName);
				}
			} else {
				// update
				Object oldChild = childMap.get(id);
				List<AttributeInfo> attribs = aso.getFeature().getAttributes();
				for (AttributeInfo attrib : attribs) {
					Object newAttrib = readProperty(newChild, attrib.getName());
					writeProperty(oldChild, newAttrib, attrib.getName());
				}
			}
		}
		// delete
		Collection toDelete = new ArrayList();
		for (Object old : collection) {
			Object id = readProperty(old, identifier);
			if (!newOrUpdate.contains(id)) {
				toDelete.add(old);
			}
		}
		collection.removeAll(toDelete);
	}

	private Object readProperty(Object feature, String name) throws LayerException {
		if (feature != null) {
			PropertyDescriptor d = BeanUtils.getPropertyDescriptor(feature.getClass(), name);
			if (d != null && d.getReadMethod() != null) {
				BeanUtils.getPropertyDescriptor(feature.getClass(), name);
				Method m = d.getReadMethod();
				if (!Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
					m.setAccessible(true);
				}
				Object value;
				try {
					value = m.invoke(feature);
				} catch (Throwable t) {
					throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
				}
				return value;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private void writeProperty(Object feature, Object wvalue, String name) throws LayerException {
		Object value = wvalue;
		if (value instanceof Attribute) {
			value = ((Attribute) value).getValue();
		}
		if (feature != null) {
			PropertyDescriptor d = BeanUtils.getPropertyDescriptor(feature.getClass(), name);
			if (d != null && d.getWriteMethod() != null) {
				Method m = d.getWriteMethod();
				if (!Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
					m.setAccessible(true);
				}
				try {
					m.invoke(feature, value);
				} catch (Throwable t) {
					throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
				}
			}
		}
	}

}
