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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.SortType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import javax.annotation.PostConstruct;

/**
 * Hibernate layer model.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
@Transactional(rollbackFor = { Exception.class })
public class HibernateLayer extends HibernateLayerUtil implements VectorLayer, VectorLayerAssociationSupport {

	private final Logger log = LoggerFactory.getLogger(HibernateLayer.class);

	private FeatureModel featureModel;

	/**
	 * When parsing dates from filters, this model must know how to parse these strings into Date objects before
	 * transforming them into Hibernate criteria.
	 */
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private FilterService filterService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private CoordinateReferenceSystem crs;

	private int srid;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public FeatureModel getFeatureModel() {
		return this.featureModel;
	}

	@Override
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		super.setLayerInfo(layerInfo);
		if (null != featureModel) {
			featureModel.setLayerInfo(getLayerInfo());
		}
	}

	@PostConstruct
	private void postConstruct() throws GeomajasException {
		crs = geoService.getCrs(getLayerInfo().getCrs());
		srid = geoService.getSridFromCrs(crs);
	}

	public VectorLayerInfo getLayerInfo() {
		return super.getLayerInfo();
	}

	public boolean isCreateCapable() {
		return true;
	}

	public boolean isUpdateCapable() {
		return true;
	}

	public boolean isDeleteCapable() {
		return true;
	}

	public void setFeatureModel(FeatureModel featureModel) throws LayerException {
		this.featureModel = featureModel;
		if (null != getLayerInfo()) {
			featureModel.setLayerInfo(getLayerInfo());
		}
		filterService.registerFeatureModel(featureModel);
	}

	/**
	 * This implementation does not support the 'offset' and 'maxResultSize' parameters.
	 */
	public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
		try {
			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(getFeatureInfo().getDataSourceName());
			if (filter != null) {
				if (filter != Filter.INCLUDE) {
					CriteriaVisitor visitor = new CriteriaVisitor((HibernateFeatureModel) getFeatureModel(),
							dateFormat);
					Criterion c = (Criterion) filter.accept(visitor, criteria);
					if (c != null) {
						criteria.add(c);
					}
				}
			}

			// Sorting of elements.
			if (getFeatureInfo().getSortAttributeName() != null) {
				if (SortType.ASC.equals(getFeatureInfo().getSortType())) {
					criteria.addOrder(Order.asc(getFeatureInfo().getSortAttributeName()));
				} else {
					criteria.addOrder(Order.desc(getFeatureInfo().getSortAttributeName()));
				}
			}

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<?> list = criteria.list();
			return list.iterator();
		} catch (HibernateException he) {
			throw new HibernateLayerException(he, ExceptionCode.HIBERNATE_LOAD_FILTER_FAIL, getFeatureInfo()
					.getDataSourceName(), filter.toString());
		}
	}

	public Object create(Object feature) throws LayerException {
		// force the srid value
		enforceSrid(feature);
		
		// Replace associations with persistent versions:
		// Map<String, Object> attributes = featureModel.getAttributes(feature);
		setPersistentAssociations(feature);
		Session session = getSessionFactory().getCurrentSession();
		session.save(feature); // do not replace feature by managed object !

		// Set the original detached associations back where they belong:
		// @TODO THIS SHOULD ONLY RESTORE ASSOCIATIONS NOT ALL ATTRIBUTES (fails for complex attributes (ddd/ddd)
		// featureModel.setAttributes(feature, attributes);
		return feature;
	}

	public Object saveOrUpdate(Object feature) throws LayerException {
		// force the srid value
		enforceSrid(feature);

		String id = getFeatureModel().getId(feature);
		Object result = null;
		if (read(id) == null) {
			result = create(feature);
		} else {
			update(feature);
			result = read(id);
		}
		getSessionFactory().getCurrentSession().flush();
		return result;
	}

	public void delete(String featureId) throws LayerException {
		Session session = getSessionFactory().getCurrentSession();
		Object persistent = session.get(getFeatureInfo().getDataSourceName(), (Serializable) ConvertUtils.convert(
				featureId, getEntityMetadata().getIdentifierType().getReturnedClass()));
		session.delete(persistent);
		session.flush();
	}

	public Object read(String featureId) throws LayerException {
		Session session = getSessionFactory().getCurrentSession();
		return session.get(getFeatureInfo().getDataSourceName(), (Serializable) ConvertUtils.convert(featureId,
				getEntityMetadata().getIdentifierType().getReturnedClass()));
	}

	public void update(Object feature) throws LayerException {
		Object persistent = read(getFeatureModel().getId(feature));
		Map<String, Attribute> attributes = featureModel.getAttributes(feature);
		// replace all modified attributes by their new values
		featureModel.setAttributes(persistent, attributes);
		featureModel.setGeometry(feature, featureModel.getGeometry(feature));
	}

	public Envelope getBounds() throws LayerException {
		return getBounds(filterCreator.createTrueFilter());
	}

	/**
	 * Retrieve the bounds of the specified features.
	 * 
	 * @param filter
	 *            filter which needs to be applied
	 * @return the bounds of the specified features
	 */
	public Envelope getBounds(Filter filter) throws LayerException {
		// Envelope bounds = getBoundsDb(filter);
		// if (bounds == null)
		// bounds = getBoundsLocal(filter);
		// return bounds;

		// @TODO getBoundsDb cannot handle hibernate Formula fields
		return getBoundsLocal(filter);
	}

	public List<Attribute<?>> getAttributes(String attributeName, Filter filter) throws LayerException {
		log.debug("creating iterator for attribute {} and filter: {}", attributeName, filter);
		AttributeInfo attributeInfo = null;
		for (AttributeInfo info : getFeatureInfo().getAttributes()) {
			if (info.getName().equals(attributeName)) {
				attributeInfo = info;
				break;
			}
		}

		String objectName = getObjectName(attributeName);
		if (objectName == null) {
			throw new HibernateLayerException(ExceptionCode.HIBERNATE_ATTRIBUTE_TYPE_PROBLEM, attributeName);
		}
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(objectName);
		CriteriaVisitor visitor = new CriteriaVisitor((HibernateFeatureModel) getFeatureModel(), dateFormat);
		Criterion c = (Criterion) filter.accept(visitor, null);
		if (c != null) {
			criteria.add(c);
		}
		List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();
		for (Object object : criteria.list()) {
			try {
				attributes.add(converterService.toDto(object, attributeInfo));
			} catch (GeomajasException e) {
				throw new HibernateLayerException(ExceptionCode.HIBERNATE_ATTRIBUTE_TYPE_PROBLEM, attributeName);
			}
		}
		return attributes;
	}

	// -------------------------------------------------------------------------
	// Extra getters and setters:
	// -------------------------------------------------------------------------

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Enforces the correct srid on incoming features.
	 */
	private void enforceSrid(Object feature) throws LayerException {
		Geometry geom = getFeatureModel().getGeometry(feature);
		if (null != geom) {
			geom.setSRID(srid);
			getFeatureModel().setGeometry(feature, geom);
		}
	}

	/**
	 * Bounds are calculated locally, can use any filter, but slower than native.
	 * 
	 * @param filter
	 *            filter which needs to be applied
	 * @return the bounds of the specified features
	 * @throws LayerException
	 *             oops
	 */
	private Envelope getBoundsLocal(Filter filter) throws LayerException {
		try {
			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(getFeatureInfo().getDataSourceName());
			CriteriaVisitor visitor = new CriteriaVisitor((HibernateFeatureModel) getFeatureModel(), dateFormat);
			Criterion c = (Criterion) filter.accept(visitor, criteria);
			if (c != null) {
				criteria.add(c);
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<?> features = criteria.list();
			Envelope bounds = new Envelope();
			for (Object f : features) {
				Envelope geomBounds = getFeatureModel().getGeometry(f).getEnvelopeInternal();
				if (!geomBounds.isNull()) {
					bounds.expandToInclude(geomBounds);
				}
			}
			return bounds;
		} catch (HibernateException he) {
			throw new HibernateLayerException(he, ExceptionCode.HIBERNATE_LOAD_FILTER_FAIL, getFeatureInfo()
					.getDataSourceName(), filter.toString());
		}
	}

	private String getObjectName(String attributeName) {
		for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
			if (attribute.getName().equals(attributeName)) {
				if (attribute instanceof AssociationAttributeInfo) {
					AssociationAttributeInfo association = (AssociationAttributeInfo) attribute;
					return association.getName();
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * The idea here is to replace association objects with their persistent counterparts. This has to happen just
	 * before the saving to database. We have to keep the persistent objects inside the HibernateLayer package. Never
	 * let them out, because that way we'll invite exceptions. @TODO This method is not recursive!
	 * 
	 * @param feature
	 *            feature to persist
	 * @throws LayerException
	 *             oops
	 */
	@SuppressWarnings("unchecked")
	private void setPersistentAssociations(Object feature) throws LayerException {
		try {
			Map<String, Attribute> attributes = featureModel.getAttributes(feature);
			for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
				// We're looping over all associations:
				if (attribute instanceof AssociationAttributeInfo) {
					String name = ((AssociationAttributeInfo) attribute).getFeature().getDataSourceName();
					Object value = attributes.get(name);
					if (value != null) {
						// Find the association's meta-data:
						Session session = getSessionFactory().getCurrentSession();
						AssociationAttributeInfo aso = (AssociationAttributeInfo) attribute;
						ClassMetadata meta = getSessionFactory().getClassMetadata(aso.getName());

						AssociationType asoType = aso.getType();
						if (asoType == AssociationType.MANY_TO_ONE) {
							// Many-to-one:
							Serializable id = meta.getIdentifier(value, EntityMode.POJO);
							if (id != null) { // We can only replace it, if it has an ID:
								value = session.load(aso.getName(), id);
								getEntityMetadata().setPropertyValue(feature, name, value, EntityMode.POJO);
							}
						} else if (asoType == AssociationType.ONE_TO_MANY) {
							// One-to-many - value is a collection:

							// Get the reflection property name:
							String refPropName = null;
							Type[] types = meta.getPropertyTypes();
							for (int i = 0; i < types.length; i++) {
								String name1 = types[i].getName();
								String name2 = feature.getClass().getCanonicalName();
								if (name1.equals(name2)) {
									// Only for circular references?
									refPropName = meta.getPropertyNames()[i];
								}
							}

							// Instantiate a new collection:
							Object[] array = (Object[]) value;
							Type type = getEntityMetadata().getPropertyType(name);
							CollectionType colType = (CollectionType) type;
							Collection<Object> col = (Collection<Object>) colType.instantiate(0);

							// Loop over all detached values:
							for (int i = 0; i < array.length; i++) {
								Serializable id = meta.getIdentifier(array[i], EntityMode.POJO);
								if (id != null) {
									// Existing values: need replacing!
									Object persistent = session.load(aso.getName(), id);
									String[] props = meta.getPropertyNames();
									for (String prop : props) {
										if (!(prop.equals(refPropName))) {
											Object propVal = meta.getPropertyValue(array[i], prop, EntityMode.POJO);
											meta.setPropertyValue(persistent, prop, propVal, EntityMode.POJO);
										}
									}
									array[i] = persistent;
								} else if (refPropName != null) {
									// Circular reference to the feature itself:
									meta.setPropertyValue(array[i], refPropName, feature, EntityMode.POJO);
								} else {
									// New values:
									// do nothing...it can stay a detached value. Better hope for cascading.
								}
								col.add(array[i]);
							}
							getEntityMetadata().setPropertyValue(feature, name, col, EntityMode.POJO);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new HibernateLayerException(e, ExceptionCode.HIBERNATE_ATTRIBUTE_SET_FAILED, getFeatureInfo()
					.getDataSourceName());
		}
	}
}