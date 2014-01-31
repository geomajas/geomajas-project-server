/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.ConvertUtils;
import org.geomajas.annotation.Api;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.SortType;
import org.geomajas.configuration.VectorLayerInfo;
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
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

	@Autowired
	private ApplicationContext applicationContext;

	private CoordinateReferenceSystem crs;

	private int srid;

	private String id;

	private boolean useLazyFeatureConversion = true;

	@Override
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

	@Override
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	@Override
	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	@Override
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

	/**
	 * Finish initializing the layer.
	 *
	 * @throws GeomajasException oops
	 */
	@PostConstruct
	@SuppressWarnings("unused")
	protected void postConstruct() throws GeomajasException {
		crs = geoService.getCrs2(getLayerInfo().getCrs());
		srid = geoService.getSridFromCrs(crs);
		filterService.registerFeatureModel(featureModel);
		if (null == featureModel) {
			HibernateFeatureModel hibernateFeatureModel = applicationContext.getBean(HibernateFeatureModel.class);
			hibernateFeatureModel.setSessionFactory(getSessionFactory());
			hibernateFeatureModel.setLayerInfo(getLayerInfo());
			featureModel = hibernateFeatureModel;
		}

	}

	@Override
	public boolean isCreateCapable() {
		return true;
	}

	@Override
	public boolean isUpdateCapable() {
		return true;
	}

	@Override
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

	@Override
	public Object create(Object feature) throws LayerException {
		// force the srid value
		enforceSrid(feature);
		Session session = getSessionFactory().getCurrentSession();
		session.save(feature);
		return feature;
	}

	@Override
	public Object saveOrUpdate(Object feature) throws LayerException {
		// force the srid value
		enforceSrid(feature);
		Session session = getSessionFactory().getCurrentSession();
		// using merge to allow detached objects, although Geomajas avoids them
		return session.merge(feature);
	}

	@Override
	public void delete(String featureId) throws LayerException {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(getFeature(featureId));
		session.flush();
	}

	@Override
	public Object read(String featureId) throws LayerException {
		Object object = getFeature(featureId);
		if (object == null) {
			throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
		}
		return object;
	}

	/**
	 * Update a feature object in the Hibernate session.
	 *
	 * @param feature feature object
	 * @throws LayerException oops
	 */
	public void update(Object feature) throws LayerException {
		Session session = getSessionFactory().getCurrentSession();
		session.update(feature);
	}

	@Override
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

	@Override
	public List<Attribute<?>> getAttributes(String attributeName, Filter filter) throws LayerException {
		if (attributeName == null) {
			throw new HibernateLayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, (Object) null);
		}
		AssociationAttributeInfo attributeInfo = getRecursiveAttributeInfo(attributeName, getFeatureInfo()
				.getAttributes());
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

	/**
	 * Get the date format.
	 *
	 * @return date format
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * Set the date format.
	 *
	 * @param dateFormat date format
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Is the result set scrollable?
	 *
	 * @return true when result set is scrollable
	 */
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
	private static class ScrollIterator implements Iterator {

		private final ScrollableResults sr;

		private boolean hasNext;

		/**
		 * Create a {@link ScrollIterator}.
		 *
		 * @param sr scrollable result set
		 */
		public ScrollIterator(ScrollableResults sr) {
			this.sr = sr;
			hasNext = sr.first();
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Object next() {
			Object o = sr.get(0);
			hasNext = sr.next();
			return o;
		}

		@Override
		public void remove() {
			// TODO the alternative (default) version with list allows remove(),
			// but this will only remove it from the list, not from db, so maybe we should
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

	private Object getFeature(String featureId) throws HibernateLayerException {
		Session session = getSessionFactory().getCurrentSession();
		return session.get(getFeatureInfo().getDataSourceName(), (Serializable) ConvertUtils.convert(featureId,
				getEntityMetadata().getIdentifierType().getReturnedClass()));
	}
	
	
	private AssociationAttributeInfo getRecursiveAttributeInfo(String name, List<AttributeInfo> infos) {
		for (AttributeInfo attributeInfo : infos) {
			if (attributeInfo instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo associationAttributeInfo = (AssociationAttributeInfo) attributeInfo;
				if (name.equals(attributeInfo.getName())) {
					return associationAttributeInfo;
				} else if (name.startsWith(attributeInfo.getName())) {
					String childName = name.substring(attributeInfo.getName().length() + 1);
					return getRecursiveAttributeInfo(childName, associationAttributeInfo.getFeature().getAttributes());
				}
			}
		}
		return null;
	}

}