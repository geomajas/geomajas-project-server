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
package org.geomajas.layer.hibernate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.LayerException;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.ExceptionCode;
import org.geomajas.service.GeoService;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * FeatureModel implementation for the HibernateLayer.
 * </p>
 * 
 * @author Jan De Moerloose, Pieter De Graef
 */
public class HibernateFeatureModel extends HibernateLayerUtil implements FeatureModel {

	private final Logger log = LoggerFactory.getLogger(HibernateFeatureModel.class);

	@Autowired
	private GeoService geoService;
	
	private int srid;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	@Override
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		super.setLayerInfo(layerInfo);
		srid = geoService.getSridFromCrs(layerInfo.getCrs());
	}

	// -------------------------------------------------------------------------
	// FeatureModel implementation:
	// -------------------------------------------------------------------------

	public Object getAttribute(Object feature, String name) throws LayerException {
		try {
			return getAttributeRecursively(feature, name);
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.HIBERNATE_ATTRIBUTE_GET_FAILED, name, feature.toString());
		}
	}

	public Map<String, Object> getAttributes(Object feature) throws LayerException {
		try {
			Map<String, Object> attribs = new HashMap<String, Object>();
			for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
				String name = attribute.getName();
				if (!name.equals(getGeometryAttributeName())) {
					Object value = this.getAttribute(feature, name);
					attribs.put(name, value);
				}
			}
			return attribs;
		} catch (Exception e) {
			log.error("Getting all attributes failed ", e);
			throw new LayerException(ExceptionCode.HIBERNATE_ATTRIBUTE_ALL_GET_FAILED, feature);
		}
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Object obj = getAttribute(feature, getFeatureInfo().getGeometryType().getName());
		if (obj == null) {
			return null;
		} else if (Geometry.class.isAssignableFrom(obj.getClass())) {
			Geometry geom = (Geometry) obj;
			return (Geometry) geom.clone();
		} else {
			throw new LayerException(ExceptionCode.PROPERTY_IS_NOT_GEOMETRY, getFeatureInfo().getGeometryType()
					.getName());
		}
	}

	public String getGeometryAttributeName() throws LayerException {
		return getFeatureInfo().getGeometryType().getName();
	}

	public String getId(Object feature) throws LayerException {
		Object id = getEntityMetadata().getIdentifier(feature, EntityMode.POJO);
		return (id == null ? null : id.toString());
	}

	public int getSrid() {
		return srid;
	}

	public Object newInstance() throws LayerException {
		try {
			return getEntityMetadata().instantiate(null, EntityMode.POJO);
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.HIBERNATE_CANNOT_CREATE_POJO,
					getFeatureInfo().getDataSourceName());
		}
	}

	public Object newInstance(String id) throws LayerException {
		try {
			Serializable ser = (Serializable) ConvertUtils.convert(id, getEntityMetadata()
					.getIdentifierType().getReturnedClass());
			return getEntityMetadata().instantiate(ser, EntityMode.POJO);
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.HIBERNATE_CANNOT_CREATE_POJO,
					getFeatureInfo().getDataSourceName());
		}
	}

	public Class<?> getSuperClass() throws LayerException {
		/*
		if (info.getExtends() != null) {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				if (null == loader) loader = this.getClass().getClassLoader();
				return loader.loadClass(info.getExtends());
			} catch (ClassNotFoundException e) {
				throw new LayerException(e, ExceptionCode.SUPERCLASS_NOT_FOUND, info.getExtends());
			}
		} else {
			return null;
		}
		*/
		return null;
	}

	/**
	 * Does not support many-to-one and one-to-many....
	 */
	public void setAttributes(Object feature, Map<String, Object> attributes) throws LayerException {
		for (Entry<String, Object> entry : attributes.entrySet()) {
			setAttributeRecursively(feature, null, entry.getKey(), entry.getValue());
		}
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		try {
			String name = getFeatureInfo().getGeometryType().getName();

			NameValuePair pair = getPropertyNameValuePair(feature, name);
			name = pair.getName();
			Object realFeature = pair.getValue();

			getSessionFactory().getClassMetadata(realFeature.getClass()).setPropertyValue(realFeature, name, geometry,
					EntityMode.POJO);
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.GEOMETRY_SET_FAILED, e, getFeatureInfo().getDataSourceName());
		}
	}

	public boolean canHandle(Object feature) {
		try {
			return getEntityMetadata().getEntityName().equals(feature.getClass().getName());
		} catch (HibernateLayerException e) {
			return false;
		}
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * ??? 
	 *
	 * @param feature
	 * @param name
	 * @return
	 *
	 * @deprecated ???
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private NameValuePair getPropertyNameValuePair(Object feature, String name) {
		// return getPropertyNameValuePairRecursive(feature, name);

		String[] props = name.split(SEPARATOR_REGEXP);
		Object tempFeature = feature;
		for (int i = 0; i < props.length - 1; i++) {

			// Detect if the property is a collection
			String[] list = props[i].split("[\\[\\]]");

			Class clazz = tempFeature.getClass();
			ClassMetadata cmd = null;
			try {
				cmd = getSessionFactory().getClassMetadata(clazz);
				tempFeature = cmd.getPropertyValue(tempFeature, list[0], EntityMode.POJO);
			} catch (HibernateException e) {
				log.error(e.getMessage(), e);
			}

			// If a number is given, get n'th element out of collection
			if (list.length > 1) {
				int n = new Integer(list[1]);

				try {
					Iterator it = ((Collection<Object>) tempFeature).iterator();
					tempFeature = null;
					for (int j = 0; j <= n && it.hasNext(); j++) {
						tempFeature = it.next();
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return new NameValuePair(props[props.length - 1], tempFeature);

	}

	/**
	 * A recursive getAttribute method. In case a one-to-many is passed, an array will be returned.
	 * 
	 * @param feature
	 *            The feature wherein to search for the attribute
	 * @param name
	 *            The attribute's full name. (can be attr1.attr2)
	 * @return Returns the value. In case a one-to-many is passed along the way, an array will be returned.
	 * @throws LayerException oops
	 */
	private Object getAttributeRecursively(Object feature, String name) throws LayerException {
		if (feature == null) {
			return null;
		}
		// Split up properties: the first and the rest.
		String[] properties = name.split(SEPARATOR_REGEXP, 2);
		Object tempFeature = feature;

		// If the first property is the identifier:
		if (properties[0].equals(getFeatureInfo().getIdentifier().getName())) {
			tempFeature = getId(feature);
		} else {
			// Get attribute through meta-data info:
			ClassMetadata meta = getSessionFactory().getClassMetadata(feature.getClass());

			if (meta == null) { // In case of a lazy loaded object
				String tmp = feature.toString();
				int pos = tmp.indexOf("@");
				if (pos <= 0) {
					throw new LayerException(ExceptionCode.CANNOT_DETERMINE_CLASS_FOR_FEATURE, feature);
				}
				meta = getSessionFactory().getClassMetadata(tmp.substring(0, pos));
			}
			if (meta == null) {
				// an embedded object ?
				try {
					tempFeature = PropertyUtils.getProperty(feature, properties[0]);
				} catch (Exception e) {
					// we are out of options...
				}
			} else {
				// The normal way: ask the meta-data
				tempFeature = meta.getPropertyValue(feature, properties[0], EntityMode.POJO);
			}
		}

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

	/**
	 * TODO Do we allow an ID to be set by the setAttributes method? I suggest not.(PDG)
	 * 
	 * @param parent
	 * @param parentAttribute
	 * @param propertyName
	 * @param baseValue
	 * @throws LayerException
	 */
	private void setAttributeRecursively(Object parent, AttributeInfo parentAttribute, String propertyName,
			Object baseValue) throws LayerException {
		Object value = baseValue;
		if (parent == null) {
			return;
		}

		// Split up properties: the first and the rest.
		String[] properties = propertyName.split(SEPARATOR_REGEXP, 2);

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
			for (AttributeInfo attr : association.getFeatureInfo().getAttributes()) {
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
				PrimitiveAttributeInfo prim = (PrimitiveAttributeInfo) attribute;
				value = castPrimitiveValue(prim.getType(), value);
				ClassMetadata cmd = getMetadata(parent.getClass());
				if (cmd != null) {
					cmd.setPropertyValue(parent, properties[0], value, EntityMode.POJO);
				} else {
					// not an entity, try bean
					try {
						PropertyUtils.setSimpleProperty(parent, properties[0], value);
					} catch (Throwable t) {
						throw new HibernateLayerException(t, ExceptionCode.HIBERNATE_ATTRIBUTE_SET_FAILED,
								attribute.getName(), value.toString());
					}
				}
			} else if (attribute instanceof AssociationAttributeInfo) {

				// In case the attribute is an association:
				AssociationAttributeInfo aso = (AssociationAttributeInfo) attribute;
				if (properties.length == 1) {
					// If the first property is the only one: set the entire complex object:
					String name = attribute.getName();
					String asoType = aso.getType().value();
					if ("many-to-one".equals(asoType)) {
						getMetadata(parent.getClass()).setPropertyValue(parent, name, value, EntityMode.POJO);
					} else if ("one-to-many".equals(asoType)) {
						Type type = getMetadata(parent.getClass()).getPropertyType(name);
						if (type.isCollectionType() && value instanceof Object[]) {
							Object[] array = (Object[]) value;
							copyArrayToPersistentCollection(array, aso, parent,
									(Collection<?>) getMetadata(parent.getClass()).
											getPropertyValue(parent, name, EntityMode.POJO));
						}
					}
				} else {
					// It's a complex property name...we must go deeper:
					ClassMetadata meta = getSessionFactory().getClassMetadata(parent.getClass());
					Object newParent = meta.getPropertyValue(parent, properties[0], EntityMode.POJO);

					if (newParent instanceof Collection<?>) {
						// one-to-many: go over all entries!
						Collection<?> colParent  = (Collection<?>) newParent;
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

	@SuppressWarnings("unchecked")
	private void copyArrayToPersistentCollection(Object[] array, AssociationAttributeInfo type, Object parent,
			Collection<?> collection) throws LayerException {
		ClassMetadata childMetaData = getMetadata(type.getFeatureInfo().getDataSourceName());
		if (null == childMetaData) {
			throw new LayerException(ExceptionCode.MODEL_FEATURE_CLASS_NOT_FOUND,
					type.getFeatureInfo().getDataSourceName());
		}
		// find the parent property
		Type[] types = childMetaData.getPropertyTypes();
		String parentName = null;
		for (int i = 0; i < types.length; i++) {
			Type t = types[i];
			if (t.getReturnedClass().equals(parent.getClass())) {
				parentName = childMetaData.getPropertyNames()[i];
			}
		}
		// new and update
		Collection toAdd = new ArrayList();
		for (Object newChild : array) {
			Serializable newId = childMetaData.getIdentifier(newChild, EntityMode.POJO);
			if (newId == null) {
				toAdd.add(newChild);
				if (parentName != null) {
					childMetaData.setPropertyValue(newChild, parentName, parent, EntityMode.POJO);
				}
			} else {
				for (Object oldChild : collection) {
					Serializable oldId = childMetaData.getIdentifier(oldChild, EntityMode.POJO);
					if (oldId.equals(newId)) {
						List<AttributeInfo> attribs = type.getFeatureInfo().getAttributes();
						for (AttributeInfo attrib : attribs) {
							// TODO: write a test for this case, because it has been changed without testing.
							setAttributeRecursively(oldChild, attrib, attrib.getName(),
									childMetaData.getPropertyValue(newChild, attrib
											.getName(), EntityMode.POJO));
						}
					}
				}
			}
		}
		// delete
		Collection toDelete = new ArrayList();
		for (Object oldChild : collection) {
			Serializable oldId = childMetaData.getIdentifier(oldChild, EntityMode.POJO);
			boolean found = false;
			for (int i = 0; i < array.length; i++) {
				Object newChild = array[i];
				Serializable newId = childMetaData.getIdentifier(newChild, EntityMode.POJO);
				if (oldId.equals(newId)) {
					found = true;
				}
			}
			if (!found) {
				toDelete.add(oldChild);
			}
		}

		collection.addAll(toAdd);
		collection.removeAll(toDelete);
	}

	private Object castPrimitiveValue(PrimitiveType type, Object baseValue) {
		Object value = baseValue;
		if (value != null) {
			String svalue = value.toString();
			if ("undefined".equals(svalue) && type != PrimitiveType.STRING) {
				value = null;
			} else {
				switch (type) {
					case SHORT:
						value = new Short(value.toString());
						break;
					case INTEGER:
						value = new Integer(value.toString());
						break;
					case LONG:
						value = new Long(value.toString());
						break;
					case FLOAT:
						value = new Float(value.toString());
						break;
					case DOUBLE:
					case CURRENCY:
						value = new Double(value.toString());
						break;
					case STRING:
						value = value.toString();
						break;
					case DATE:
						if (!(value instanceof Date)) {
							value = Timestamp.valueOf(value.toString());
						}
						break;
					case URL:
						value = value.toString();
						break;
					case IMGURL:
						value = value.toString();
						break;
					case BOOLEAN:
						value = Boolean.valueOf(value.toString());
						break;
					default :
						throw new RuntimeException(
								"Invalid option for switch in HibernateFeatureModel.castPrimitiveValue " + type);
				}
			}
		}
		return value;
	}

	/**
	 * ???
	 *
	 * @deprecated ???
	 */
	@Deprecated
	private class NameValuePair {

		private String name;

		private Object value;

		public NameValuePair(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return this.name;
		}

		public Object getValue() {
			return this.value;
		}
	}
}