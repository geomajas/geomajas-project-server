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
package org.geomajas.layer.hibernate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * FeatureModel implementation for the HibernateLayer.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class HibernateFeatureModel extends HibernateLayerUtil implements FeatureModel {

	private final Logger log = LoggerFactory.getLogger(HibernateFeatureModel.class);

	@Autowired
	private GeoService geoService;

	private int srid;

	@Autowired
	private DtoConverterService converterService;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	@Override
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		super.setLayerInfo(layerInfo);
		srid = geoService.getSridFromCrs(layerInfo.getCrs());

		FeatureInfo featureInfo = layerInfo.getFeatureInfo();
		attributeInfoMap.put(featureInfo.getIdentifier().getName(), featureInfo.getIdentifier());
		for (AttributeInfo info : featureInfo.getAttributes()) {
			attributeInfoMap.put(info.getName(), info);
		}
	}

	// -------------------------------------------------------------------------
	// FeatureModel implementation:
	// -------------------------------------------------------------------------

	public Attribute<?> getAttribute(Object feature, String name) throws LayerException {
		try {
			name = fixName(name);
			AttributeInfo attributeInfo = getRecursiveAttributeInfo(name, null);
			if (null == attributeInfo) {
				// Only return beans for which an attribute has been configured.
				throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name);
			}
			return converterService.toDto(getAttributeRecursively(feature, name), attributeInfo);
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.HIBERNATE_ATTRIBUTE_GET_FAILED, name, feature.toString());
		}
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		try {
			Map<String, Attribute> attribs = new HashMap<String, Attribute>();
			for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
				String name = attribute.getName();
				if (!name.equals(getGeometryAttributeName())) {
					Attribute value = getAttribute(feature, name);
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
		Object obj = getAttributeRecursively(feature, getGeometryAttributeName());
		if (obj == null) {
			return null;
		} else if (Geometry.class.isAssignableFrom(obj.getClass())) {
			Geometry geom = (Geometry) obj;
			return (Geometry) geom.clone();
		} else {
			throw new LayerException(ExceptionCode.PROPERTY_IS_NOT_GEOMETRY, getGeometryAttributeName());
		}
	}

	public String getGeometryAttributeName() throws LayerException {
		return fixName(getFeatureInfo().getGeometryType().getName());
	}

	public String getId(Object feature) throws LayerException {
		Object id = getEntityMetadata().getIdentifier(feature,
				(SessionImplementor) getSessionFactory().getCurrentSession());
		return (id == null ? null : id.toString());
	}

	public int getSrid() {
		return srid;
	}

	public Object newInstance() throws LayerException {
		try {
			return getEntityMetadata().instantiate(null, (SessionImplementor) getSessionFactory().getCurrentSession());
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.HIBERNATE_CANNOT_CREATE_POJO, getFeatureInfo()
					.getDataSourceName());
		}
	}

	public Object newInstance(String id) throws LayerException {
		try {
			Serializable ser = (Serializable) ConvertUtils.convert(id, getEntityMetadata().getIdentifierType()
					.getReturnedClass());
			return getEntityMetadata().instantiate(ser, (SessionImplementor) getSessionFactory().getCurrentSession());
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.HIBERNATE_CANNOT_CREATE_POJO, getFeatureInfo()
					.getDataSourceName());
		}
	}

	/**
	 * Does not support many-to-one and one-to-many....
	 */
	@SuppressWarnings("rawtypes")
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		if (feature == null) {
			throw new NullPointerException("Feature can't be null when setting attributes.");
		}
		for (Entry<String, Attribute> entry : attributes.entrySet()) {
			Object value = null;
			if (null != entry.getValue()) {
				value = entry.getValue().getValue();
			}
			String attributeName = fixName(entry.getKey());
			AttributeInfo attributeInfo = getRecursiveAttributeInfo(attributeName, null);
			if (attributeInfo == null) {
				// Only return beans for which an attribute has been configured.
				throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, attributeName);
			} else if (attributeInfo.isEditable()) {
				setAttributeRecursively(feature, attributeName, value, attributeInfo);
			}
		}
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		try {
			setPropertyRecursively(feature, getGeometryAttributeName(), geometry);
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

	private String fixName(String name) {
		return name.replace(HibernateLayerUtil.XPATH_SEPARATOR, HibernateLayerUtil.SEPARATOR);
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
		Object tempFeature;

		// If the first property is the identifier:
		if (properties[0].equals(getFeatureInfo().getIdentifier().getName())) {
			tempFeature = getId(feature);
		} else {
			tempFeature = getSimpleProperty(feature, properties[0]);
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
	 * Get a direct property from a bean.<br/>
	 * TODO Give the bean solution priority to using the ClassMetadata. But this requires tests with lazy loaded
	 * properties.
	 * 
	 * @param bean
	 *            The bean to try and get the property from.
	 * @param property
	 *            The name of the property. Must be a simple name. No separators like (properyA.propertyB).
	 * @return Returns the actual property value.
	 * @throws HibernateLayerException
	 *             oops.
	 */
	private Object getSimpleProperty(Object bean, String property) throws HibernateLayerException {
		ClassMetadata meta = getSessionFactory().getClassMetadata(bean.getClass());

		if (meta == null) { // Check if the object is a lazy loaded object:
			String tmp = bean.toString();
			int pos = tmp.indexOf("@");
			if (pos <= 0) {
				throw new HibernateLayerException(ExceptionCode.CANNOT_DETERMINE_CLASS_FOR_FEATURE, bean);
			}
			meta = getSessionFactory().getClassMetadata(tmp.substring(0, pos));
		}
		if (meta == null) { // Check if the object is an embedded object:
			try {
				return PropertyUtils.getProperty(bean, property);
			} catch (Exception e) {
				throw new HibernateLayerException(e, ExceptionCode.ATTRIBUTE_UNKNOWN, bean);
			}
		} else { // The normal way: ask the meta-data
			return getBeanProperty(meta, bean, property);
		}
	}

	private Object getBeanProperty(ClassMetadata meta, Object bean, String property) throws HibernateLayerException {
		try {
			return meta.getPropertyValue(bean, property, EntityMode.POJO);
		} catch (Exception e) {
			if (meta.getClass().getSuperclass() != null) {
				// Property might be defined in the parent class. The problem is that this parent may not be an entity.
				// Therefore we switch back to using bean methods:
				try {
					return PropertyUtils.getProperty(bean, property);
				} catch (Exception e2) {
					throw new HibernateLayerException(e, ExceptionCode.ATTRIBUTE_UNKNOWN, bean);
				}
			}
			throw new HibernateLayerException(e, ExceptionCode.ATTRIBUTE_UNKNOWN, bean);
		}
	}

	/**
	 * This method will set the value of an attribute. This automatically means that it assumes the attribute exists.
	 * 
	 * @param parent
	 * @param parentAttribute
	 * @param propertyName
	 * @param baseValue
	 * @throws LayerException
	 */
	@SuppressWarnings("unchecked")
	private void setAttributeRecursively(Object bean, String propertyName, Object value, AttributeInfo info)
			throws LayerException {
		String[] properties = propertyName.split(SEPARATOR_REGEXP, 2);

		if (properties.length == 1) {
			// Final depth; apply the value:
			if (info instanceof AssociationAttributeInfo && value != null) {

				// Applying the value for a many-to-one or one-to-many attribute:
				AssociationAttributeInfo association = (AssociationAttributeInfo) info;
				if (association.getType().equals(AssociationType.MANY_TO_ONE)) {
					// Many-to-one: expect an object of type AssociationValue:
					if (value instanceof AssociationValue) {
						setAssociationValue(bean, propertyName, (AssociationValue) value, association);
					} else {
						throw new IllegalArgumentException("The value of a many-to-one should be of type"
								+ " AssociationValue.");
					}
				} else if (association.getType().equals(AssociationType.ONE_TO_MANY)) {
					// One-to-many: Expect a list of AssociationValues:
					Type type = getMetadata(bean.getClass()).getPropertyType(association.getName());
					if (type.isCollectionType() && value instanceof Collection<?>) {
						// Copy the values to a persistent collection (made through Hibernate meta-data):
						Collection<Object> collection = (Collection<Object>) getMetadata(bean.getClass())
								.getPropertyValue(bean, association.getName(), EntityMode.POJO);
						copyListToPersistentCollection((List<AssociationValue>) value, association, bean, collection);
					}
				}
			} else {
				// For simple attributes; apply property as bean:
				setSimpleProperty(bean, propertyName, value);
			}
		} else {
			// Go deeper:
			Object child = getSimpleProperty(bean, properties[0]);
			setAttributeRecursively(child, properties[1], value, info);
		}
	}

	private void setSimpleProperty(Object bean, String propertyName, Object value) throws HibernateLayerException {
		try {
			if (bean instanceof Collection<?>) {
				// Setting an attribute within a one-to-many collection: apply on all members of the set.
				Iterator<?> iterator = ((Collection<?>) bean).iterator();
				while (iterator.hasNext()) {
					PropertyUtils.setSimpleProperty(iterator.next(), propertyName, value);
				}
			} else {
				PropertyUtils.setSimpleProperty(bean, propertyName, value);
			}
		} catch (Exception e) {
			throw new HibernateLayerException(e, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

	/**
	 * Set an attribute.
	 * <p/>
	 * It is not allowed to modify a id attribute.
	 * 
	 * @param parent
	 * @param parentAttribute
	 * @param propertyName
	 * @param baseValue
	 * @throws LayerException
	 *             oops
	 */
	@SuppressWarnings("unchecked")
	private void setAttributeRecursively2(Object parent, AttributeInfo parentAttribute, String propertyName,
			Object baseValue) throws LayerException {
		Object value = baseValue;
		if (parent == null) {
			return;
		}

		// try to assure the correct separator is used
		propertyName = propertyName.replace(HibernateLayerUtil.XPATH_SEPARATOR, HibernateLayerUtil.SEPARATOR);

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
			for (AttributeInfo attr : association.getFeature().getAttributes()) {
				if (attr.getName().equals(properties[0])) {
					attribute = attr;
					break;
				} else if (attr.getName().equals(propertyName)) { // A.B
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
				setPropertyRecursively(parent, propertyName, value);
			} else if (attribute instanceof AssociationAttributeInfo) {

				// In case the attribute is an association:
				AssociationAttributeInfo aso = (AssociationAttributeInfo) attribute;
				if (properties.length == 1) {

					// If the first property is the only one: set the entire complex object:
					if (aso.getType().equals(AssociationType.MANY_TO_ONE)) {
						// Many-to-one; apply the AssociationValue on the correct bean:
						setAssociationValue(parent, attribute.getName(), (AssociationValue) value, aso);
					} else if (aso.getType().equals(AssociationType.ONE_TO_MANY)) {
						// One-to-many apply the list of AssociationValues on the correct bean:
						Type type = getMetadata(parent.getClass()).getPropertyType(attribute.getName());
						if (type.isCollectionType() && value instanceof List<?>) {
							copyListToPersistentCollection(
									(List<AssociationValue>) value,
									aso,
									parent,
									(Collection<Object>) getMetadata(parent.getClass()).getPropertyValue(parent,
											attribute.getName(), EntityMode.POJO));
						}
					}
				} else {
					// It's a complex property name...we must go deeper:
					Object newParent = getSimpleProperty(parent, properties[0]);
					if (newParent instanceof Collection<?>) {
						// one-to-many: go over all entries!
						Collection<?> colParent = (Collection<?>) newParent;
						for (Object parentEntry : colParent) {
							setAttributeRecursively2(parentEntry, aso, properties[1], value);
						}
					} else {
						// many-to-one:
						setAttributeRecursively2(newParent, aso, properties[1], value);
					}
				}
			}
		}
	}

	/**
	 * Setting a value onto a POJO object.
	 * 
	 * @param parent
	 *            The POJO object.
	 * @param propertyName
	 *            Name of the property. Can be something like A.B!
	 * @param value
	 *            The primitive value to apply.
	 * @throws HibernateLayerException
	 *             oops.
	 */
	private void setPropertyRecursively(Object parent, String propertyName, Object value)
			throws HibernateLayerException {
		String[] properties = propertyName.split(SEPARATOR_REGEXP, 2);
		if (properties.length == 1) {
			// Set simple property:
			ClassMetadata meta = getMetadata(parent.getClass());
			if (meta != null) {
				meta.setPropertyValue(parent, properties[0], value, EntityMode.POJO);
			} else {
				try { // Not an entity, try bean:
					PropertyUtils.setSimpleProperty(parent, propertyName, value);
				} catch (Throwable t) {
					throw new HibernateLayerException(t, ExceptionCode.HIBERNATE_ATTRIBUTE_SET_FAILED, propertyName,
							value.toString());
				}
			}
		} else {
			// Complex property name (A.B). Find the correct child property and look deeper:
			Object newParent = getSimpleProperty(parent, properties[0]);
			setPropertyRecursively(newParent, properties[1], value);
		}
	}

	/**
	 * Setting a single AssociationValue, i.e. a ManyToOne. This method foresees the following scenario's:
	 * <ul>
	 * <li>If the value is null, than null is applied onto the parent.</li>
	 * <li>If the value contains an ID, than look into the target table for the correct value and apply it.</li>
	 * <li>If the value does not contain an ID: we assume we should cascade a persist. In other words, we will try to
	 * create a new value into the target table.</li>
	 * </ul>
	 * @throws LayerException 
	 */
	private void setAssociationValue(Object parent, String property, AssociationValue value,
			AssociationAttributeInfo attributeInfo) throws LayerException {
		// Find the correct child bean:
		ClassMetadata meta = getSessionFactory().getClassMetadata(parent.getClass());

		// Value is null; apply null:
		if (value == null) {
			meta.setPropertyValue(parent, property, null, EntityMode.POJO);
			return;
		}

		// We have an ID, so we know which object to fetch and add to the parent:
		if (value.getId() != null && value.getId().getValue() != null) {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(
					meta.getPropertyType(property).getName());
			criteria.add(Restrictions.idEq(value.getId().getValue()));
			meta.setPropertyValue(parent, property, criteria.uniqueResult(), EntityMode.POJO);
		} else {
			// If we have no ID, then we must create a new value and cascade in the referencing table.
			// Even if the parent should already have a current value, overwrite it. (or we could set it's ID to null)
			ClassMetadata beanMeta = getSessionFactory().getClassMetadata(meta.getPropertyType(property).getName());
			Object bean = beanMeta.instantiate(null, (SessionImplementor) getSessionFactory().getCurrentSession());
			meta.setPropertyValue(parent, property, bean, EntityMode.POJO);

			// map the nested attribute info's for lookup later on
			Map<String, AttributeInfo> infos = new HashMap<String, AttributeInfo>();
			for (AttributeInfo info : attributeInfo.getFeature().getAttributes()) {
				infos.put(info.getName(), info);
			}
			// Copy the individual bean properties to the bean: (this may overwrite existing values)
			ClassMetadata propertyMeta = getSessionFactory().getClassMetadata(bean.getClass());
			for (Entry<String, Attribute<?>> entry : value.getAllAttributes().entrySet()) {
				setAttributeRecursively(bean, entry.getKey(), entry.getValue().getValue(), infos.get(entry.getKey()));
				//propertyMeta.setPropertyValue(bean, entry.getKey(), entry.getValue().getValue(), EntityMode.POJO);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void copyListToPersistentCollection(List<AssociationValue> values, AssociationAttributeInfo attributeInfo,
			Object parent, Collection<Object> collection) throws LayerException {
		if (collection == null) {
			collection = Collections.EMPTY_SET;
		}

		ClassMetadata childMetaData = getMetadata(attributeInfo.getFeature().getDataSourceName());
		if (null == childMetaData) {
			throw new LayerException(ExceptionCode.MODEL_FEATURE_CLASS_NOT_FOUND, attributeInfo.getFeature()
					.getDataSourceName());
		}

		// Find the parent property (needed for setting properties):
		Type[] types = childMetaData.getPropertyTypes();
		String parentName = null;
		for (int i = 0; i < types.length; i++) {
			Type t = types[i];
			if (t.getReturnedClass().equals(parent.getClass())) {
				parentName = childMetaData.getPropertyNames()[i];
				break;
			}
		}
		
		// map the nested attribute info's for lookup later on
		Map<String, AttributeInfo> infos = new HashMap<String, AttributeInfo>();
		for (AttributeInfo info : attributeInfo.getFeature().getAttributes()) {
			infos.put(info.getName(), info);
		}

		// Search for new rows and updates:
		Collection<Object> toAdd = new ArrayList<Object>();
		for (AssociationValue newChild : values) {
			PrimitiveAttribute<?> identifier = newChild.getId();
			if (identifier == null || identifier.getValue() == null) {
				// No ID - A new row:
				Object bean = childMetaData.instantiate(null, (SessionImplementor) getSessionFactory()
						.getCurrentSession());
				ClassMetadata propertyMeta = getSessionFactory().getClassMetadata(bean.getClass());
				for (Entry<String, Attribute<?>> entry : newChild.getAllAttributes().entrySet()) {
					setAttributeRecursively(bean, entry.getKey(), entry.getValue().getValue(),
							infos.get(entry.getKey()));
					//propertyMeta.setPropertyValue(bean, entry.getKey(), , EntityMode.POJO);
				}
				toAdd.add(bean);
				if (parentName != null) {
					// Set the parent onto the child bean. OneToMany on one side is ManyToOne on the other side.
					childMetaData.setPropertyValue(bean, parentName, parent, EntityMode.POJO);
				}
			} else {
				// Update of an existing row - loop over all sub-attributes of the OneToMany object:
				Serializable newId = (Serializable) newChild.getId().getValue();
				for (Object oldChild : collection) {
					Serializable oldId = childMetaData.getIdentifier(oldChild, (SessionImplementor) getSessionFactory()
							.getCurrentSession());
					if (oldId.equals(newId)) {
						List<AttributeInfo> attribs = attributeInfo.getFeature().getAttributes();
						for (AttributeInfo attrib : attribs) {
							Object baseValue = newChild.getAllAttributes().get(attrib.getName()).getValue();
							setAttributeRecursively2(oldChild, attrib, attrib.getName(), baseValue);
						}
					}
				}
			}
		}

		// Search for rows that are not present in the new value list
		// They should be deleted in the persistent collection as well.
		Collection<Object> toDelete = new ArrayList<Object>();
		for (Object oldChild : collection) {
			Serializable oldId = childMetaData.getIdentifier(oldChild, (SessionImplementor) getSessionFactory()
					.getCurrentSession());
			boolean found = false;
			for (AssociationValue newChild : values) {
				if (newChild.getId() != null && newChild.getId().getValue() != null) {
					Serializable newId = (Serializable) newChild.getId().getValue();
					if (oldId.equals(newId)) {
						found = true;
					}
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
					case URL:
					case IMGURL:
						value = value.toString();
						break;
					case DATE:
						if (!(value instanceof Date)) {
							value = Timestamp.valueOf(value.toString());
						}
						break;
					case BOOLEAN:
						value = Boolean.valueOf(value.toString());
						break;
					default:
						throw new RuntimeException(
								"Invalid option for switch in HibernateFeatureModel.castPrimitiveValue " + type);
				}
			}
		}
		return value;
	}

	private AttributeInfo getRecursiveAttributeInfo(String name, AssociationAttributeInfo parentInfo) {
		// Top level:
		if (parentInfo == null) {
			// Check if the name can be found immediately (most cases):
			AttributeInfo attrInfo = attributeInfoMap.get(name);
			if (attrInfo != null) {
				return attrInfo;
			}

			// Check if there is an association attribute that matches the start of 'name':
			for (AttributeInfo attr : attributeInfoMap.values()) {
				if (attr instanceof AssociationAttributeInfo && name.startsWith(attr.getName())) {
					String childName = name.substring(attr.getName().length() + 1);
					return getRecursiveAttributeInfo(childName, (AssociationAttributeInfo) attr);
				}
			}
		} else {
			// Recursive search: search within parent attributes:
			for (AttributeInfo childInfo : parentInfo.getFeature().getAttributes()) {
				if (childInfo.getName().equals(name)) {
					return childInfo;
				} else if (childInfo instanceof AssociationAttributeInfo && name.startsWith(childInfo.getName())) {
					// The API does not yet support this though...
					String childName = name.substring(childInfo.getName().length() + 1);
					return getRecursiveAttributeInfo(childName, (AssociationAttributeInfo) childInfo);
				}
			}
		}

		return null;
	}
}