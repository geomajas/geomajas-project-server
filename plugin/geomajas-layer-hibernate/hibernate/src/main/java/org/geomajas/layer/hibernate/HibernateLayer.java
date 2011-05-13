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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.ConvertUtils;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.SortType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.VectorLayerLazyFeatureConversionSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.engine.SessionImplementor;
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

/**
 * Hibernate layer model.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Kristof Heirwegh
 * @since 1.7.1
 */
@Api
@Transactional(rollbackFor = { Exception.class })
public class HibernateLayer extends HibernateLayerUtil implements VectorLayer, VectorLayerAssociationSupport,
		VectorLayerLazyFeatureConversionSupport {

	private final Logger log = LoggerFactory.getLogger(HibernateLayer.class);

	private FeatureModel featureModel;

	/**
	 * Should the result be retrieved as a scrollable resultset? Your database(driver) needs to support this.
	 */
	private boolean scrollableResultSet;

	/**
	 * When parsing dates from filters, this model must know how to parse these strings into Date objects before
	 * transforming them into Hibernate criteria.
	 */
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private FilterService filterService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private CoordinateReferenceSystem crs;

	private int srid;

	private String id;

	private boolean useLazyFeatureConversion = true;

	public String getId() {
		return id;
	}

	/**
	 * Set the layer id.
	 * 
	 * @param id
	 *            layer id
	 * @since 1.8.0
	 */
	@Api
	public void setId(String id) {
		this.id = id;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	public boolean useLazyFeatureConversion() {
		return useLazyFeatureConversion;
	}

	/**
	 * Configure whether lazy feature conversion should be enabled for this layer. Default is true.
	 * 
	 * @param useLazyFeatureConversion
	 *            use lazy feature conversion?
	 * @since 1.8.0
	 */
	@Api
	public void setUseLazyFeatureConversion(boolean useLazyFeatureConversion) {
		this.useLazyFeatureConversion = useLazyFeatureConversion;
	}

	/**
	 * Set the layer configuration.
	 * 
	 * @param layerInfo
	 *            layer information
	 * @throws LayerException
	 *             oops
	 * @since 1.7.1
	 */
	@Api
	@Override
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		super.setLayerInfo(layerInfo);
		if (null != featureModel) {
			featureModel.setLayerInfo(getLayerInfo());
		}
	}

	@PostConstruct
	@SuppressWarnings("unused")
	private void postConstruct() throws GeomajasException {
		crs = geoService.getCrs2(getLayerInfo().getCrs());
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

	/**
	 * Set the featureModel.
	 * 
	 * @param featureModel
	 *            feature model
	 * @throws LayerException
	 *             problem setting the feature model
	 * @since 1.8.0
	 */
	@Api
	public void setFeatureModel(FeatureModel featureModel) throws LayerException {
		this.featureModel = featureModel;
		if (null != getLayerInfo()) {
			featureModel.setLayerInfo(getLayerInfo());
		}
		filterService.registerFeatureModel(featureModel);
	}

	/**
	 * Set the session factory for creating Hibernate sessions.
	 * 
	 * @param sessionFactory
	 *            session factory
	 * @throws HibernateLayerException
	 *             factory could not be set
	 * @since 1.8.0
	 */
	@Api
	public void setSessionFactory(SessionFactory sessionFactory) throws HibernateLayerException {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * This implementation does not support the 'offset' parameter. The maxResultSize parameter is not used (limiting
	 * the result needs to be done after security {@link org.geomajas.internal.layer.vector.GetFeaturesEachStep}). If
	 * you expect large results to be returned enable scrollableResultSet to retrieve only as many records as needed.
	 */
	public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
		try {
			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(getFeatureInfo().getDataSourceName());
			if (filter != null) {
				if (filter != Filter.INCLUDE) {
					CriteriaVisitor visitor = new CriteriaVisitor((HibernateFeatureModel) featureModel, dateFormat);
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

			if (isScrollableResultSet()) {
				return (Iterator<?>) new ScrollIterator(criteria.scroll());
			} else {
				List<?> list = criteria.list();
				return list.iterator();
			}
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
		// @TODO THIS SHOULD ONLY RESTORE ASSOCIATIONS NOT ALL ATTRIBUTES (fails
		// for complex attributes (ddd/ddd)
		// featureModel.setAttributes(feature, attributes);
		return feature;
	}

	public Object saveOrUpdate(Object feature) throws LayerException {
		// force the srid value
		enforceSrid(feature);
		String id = getFeatureModel().getId(feature);
		if (getFeature(id) != null) {
			update(feature);
		} else {
			feature = create(feature);
		}
		return feature;
	}

	public void delete(String featureId) throws LayerException {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(getFeature(featureId));
		session.flush();
	}

	public Object read(String featureId) throws LayerException {
		Object object = getFeature(featureId);
		if (object == null) {
			throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
		}
		return object;
	}

	public void update(Object feature) throws LayerException {
		Object persistent = read(getFeatureModel().getId(feature));
		Map<String, Attribute> attributes = featureModel.getAttributes(feature);
		// replace all modified attributes by their new values
		featureModel.setAttributes(persistent, attributes);
		featureModel.setGeometry(feature, featureModel.getGeometry(feature));
	}

	public Envelope getBounds() throws LayerException {
		return getBounds(filterService.createTrueFilter());
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
		AssociationAttributeInfo attributeInfo = null;
		for (AttributeInfo info : getFeatureInfo().getAttributes()) {
			if (info.getName().equals(attributeName) && info instanceof AssociationAttributeInfo) {
				attributeInfo = (AssociationAttributeInfo) info;
				break;
			}
		}
		if (attributeInfo == null) {
			throw new HibernateLayerException(ExceptionCode.HIBERNATE_ATTRIBUTE_TYPE_PROBLEM, attributeName);
		}

		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(attributeInfo.getFeature().getDataSourceName());
		CriteriaVisitor visitor = new CriteriaVisitor((HibernateFeatureModel) getFeatureModel(), dateFormat);
		if (filter != null) {
			Criterion c = (Criterion) filter.accept(visitor, null);
			if (c != null) {
				criteria.add(c);
			}
		}
		List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();
		for (Object object : criteria.list()) {
			try {
				attributes.add(converterService.toDto(object, attributeInfo));
			} catch (GeomajasException e) {
				throw new HibernateLayerException(e, ExceptionCode.HIBERNATE_ATTRIBUTE_TYPE_PROBLEM, attributeName);
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

	public boolean isScrollableResultSet() {
		return scrollableResultSet;
	}

	/**
	 * <p>
	 * Should the result be retrieved as a scrollable resultset? Your database(driver) needs to support this.
	 * </p>
	 * 
	 * @param scrollableResultSet
	 *            true when a scrollable resultset should be used
	 * @since 1.8.0
	 */
	@Api
	public void setScrollableResultSet(boolean scrollableResultSet) {
		this.scrollableResultSet = scrollableResultSet;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * A wrapper around a Hibernate {@link ScrollableResults}
	 * 
	 * ScrollableResults are annoying they run 1 step behind an iterator...
	 */
	@SuppressWarnings("rawtypes")
	private class ScrollIterator implements Iterator {

		private final ScrollableResults sr;

		private boolean hasnext;

		public ScrollIterator(ScrollableResults sr) {
			this.sr = sr;
			hasnext = sr.first();
		}

		public boolean hasNext() {
			return hasnext;
		}

		public Object next() {
			Object o = sr.get(0);
			hasnext = sr.next();
			return o;
		}

		public void remove() {
			// TODO the alternative (default) version with list allows remove(),
			// but this will
			// only remove it from the list, not from db, so maybe we should
			// just ignore instead of throwing an exception
			throw new HibernateException("Unsupported operation: You cannot remove records this way.");
		}
	}

	/**
	 * Enforces the correct srid on incoming features.
	 * 
	 * @param feature
	 *            object to enforce srid on
	 * @throws LayerException
	 *             problem getting or setting srid
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

	/**
	 * The idea here is to replace association objects with their persistent counterparts. This has to happen just
	 * before the saving to database. We have to keep the persistent objects inside the HibernateLayer package. Never
	 * let them out, because that way we'll invite exceptions.
	 * 
	 * @TODO This method is not recursive!
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
							Serializable id = meta.getIdentifier(value, (SessionImplementor) session);
							if (id != null) { // We can only replace it, if it
								// has an ID:
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
								Serializable id = meta.getIdentifier(array[i], (SessionImplementor) getSessionFactory()
										.getCurrentSession());
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

	private Object getFeature(String featureId) throws HibernateLayerException {
		Session session = getSessionFactory().getCurrentSession();
		return session.get(getFeatureInfo().getDataSourceName(), (Serializable) ConvertUtils.convert(featureId,
				getEntityMetadata().getIdentifierType().getReturnedClass()));
	}
}