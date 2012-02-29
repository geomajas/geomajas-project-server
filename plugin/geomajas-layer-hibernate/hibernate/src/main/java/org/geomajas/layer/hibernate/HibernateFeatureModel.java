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
package org.geomajas.layer.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityAttributeService;
import org.geomajas.layer.entity.EntityMapper;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.hibernate.HibernateEntityMapper.HibernateEntity;
import org.geomajas.service.GeoService;
import org.hibernate.engine.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Geometry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>
 * FeatureModel implementation for the HibernateLayer.
 * </p>
 * <p> Values are read/written according to the Hibernate access type.</p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
@Scope("prototype")
@Component()
public class HibernateFeatureModel extends HibernateLayerUtil implements FeatureModel {

	@Autowired
	private GeoService geoService;

	private int srid;

	@Autowired
	private EntityAttributeService entityMappingService;

	private EntityMapper entityMapper;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	@Override
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		super.setLayerInfo(layerInfo);
		srid = geoService.getSridFromCrs(layerInfo.getCrs());
		entityMapper = new HibernateEntityMapper(getSessionFactory());
	}

	/** {@inheritDoc} */
	public Attribute getAttribute(Object feature, String name) throws LayerException {
		try {
			return entityMappingService.getAttribute(feature, getFeatureInfo(), entityMapper, name);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	/** {@inheritDoc} */
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
		} catch (Exception e) { // NOSONAR
			throw new LayerException(e, ExceptionCode.HIBERNATE_ATTRIBUTE_ALL_GET_FAILED, feature);
		}
	}

	/** {@inheritDoc} */
	public String getId(Object feature) throws LayerException {
		Entity entity = entityMapper.asEntity(feature);
		Object id = entity.getId(getFeatureInfo().getIdentifier().getName());
		return id == null ? null : id.toString();
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		entityMappingService.setAttributes(feature, getFeatureInfo(), entityMapper, (Map) attributes);
	}

	/** {@inheritDoc} */
	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		Entity entity = entityMapper.asEntity(feature);
		entity.setAttribute(getGeometryAttributeName(), geometry);
	}

	/** {@inheritDoc} */
	public Object newInstance() throws LayerException {
		try {
			return getEntityMetadata().instantiate(null, (SessionImplementor) getSessionFactory().getCurrentSession());
		} catch (Exception e) { // NOSONAR
			throw new LayerException(e, ExceptionCode.HIBERNATE_CANNOT_CREATE_POJO, getFeatureInfo()
					.getDataSourceName());
		}
	}

	/** {@inheritDoc} */
	public Object newInstance(String id) throws LayerException {
		try {
			Serializable ser = (Serializable) ConvertUtils.convert(id, getEntityMetadata().getIdentifierType()
					.getReturnedClass());
			return getEntityMetadata().instantiate(ser, (SessionImplementor) getSessionFactory().getCurrentSession());
		} catch (Exception e) { // NOSONAR
			throw new LayerException(e, ExceptionCode.HIBERNATE_CANNOT_CREATE_POJO, getFeatureInfo()
					.getDataSourceName());
		}
	}

	/** {@inheritDoc} */
	public int getSrid() {
		return srid;
	}

	/** {@inheritDoc} */
	public String getGeometryAttributeName() throws LayerException {
		return fixName(getFeatureInfo().getGeometryType().getName());
	}

	/** {@inheritDoc} */
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
	 * @param feature The feature wherein to search for the attribute
	 * @param name The attribute's full name. (can be attr1.attr2)
	 * @return Returns the value. In case a one-to-many is passed along the way, an array will be returned.
	 * @throws LayerException oops
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
			Entity entity = entityMapper.asEntity(feature);
			HibernateEntity child = (HibernateEntity) entity.getChild(properties[0]);
			tempFeature = child == null ? null : child.getObject();
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

}
