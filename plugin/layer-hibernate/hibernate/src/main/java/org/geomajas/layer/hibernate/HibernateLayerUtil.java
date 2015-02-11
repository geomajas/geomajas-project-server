/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * General Hibernate ClassMetadata and SessionFactory provision. Package visibility only.
 * 
 * @author Pieter De Graef
 */
class HibernateLayerUtil {

	public static final String XPATH_SEPARATOR = "/";
	public static final String SEPARATOR = ".";
	public static final String SEPARATOR_REGEXP = "\\.";

	private SessionFactory sessionFactory;

	private ClassMetadata entityMetadata;

	private VectorLayerInfo layerInfo;

	/**
	 * Get feature info.
	 *
	 * @return feature info
	 */
	public FeatureInfo getFeatureInfo() {
		return layerInfo.getFeatureInfo();
	}

	/***
	 * Get layer configuration.
	 *
	 * @return layer info
	 */
	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Set the layer configuration.
	 *
	 * @param layerInfo layer info
	 * @throws LayerException oops
	 */
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		if (null != sessionFactory) {
			setSessionFactory(sessionFactory);
		}
	}

	//-------------------------------------------------------------------------
	// Class specific functions:
	//-------------------------------------------------------------------------

	/**
	 * Retrieve the Hibernate ClassMetadata.
	 * 
	 * @return hibernate meta data
	 * @throws HibernateLayerException
	 *             Throws an exception if the initialization of the meta data went wrong. In other words if the
	 *             Hibernate Configuration is not correct.
	 */
	public ClassMetadata getEntityMetadata() throws HibernateLayerException {
		if (null == entityMetadata) {
			throw new HibernateLayerException(ExceptionCode.HIBERNATE_NO_META_DATA);
		}
		return entityMetadata;
	}

	/**
	 * Return the class of one of the properties of another class from which the Hibernate metadata is given.
	 * 
	 * @param meta
	 *            The parent class to search a property in.
	 * @param propertyName
	 *            The name of the property in the parent class (provided by meta)
	 * @return Returns the class of the property in question.
	 * @throws HibernateLayerException
	 *             Throws an exception if the property name could not be retrieved.
	 */
	protected Class<?> getPropertyClass(ClassMetadata meta, String propertyName) throws HibernateLayerException {
		// try to assure the correct separator is used
		propertyName = propertyName.replace(XPATH_SEPARATOR, SEPARATOR);

		if (propertyName.contains(SEPARATOR)) {
			String directProperty = propertyName.substring(0, propertyName.indexOf(SEPARATOR));
			try {
				Type prop = meta.getPropertyType(directProperty);
				if (prop.isCollectionType()) {
					CollectionType coll = (CollectionType) prop;
					prop = coll.getElementType((SessionFactoryImplementor) sessionFactory);
				}
				ClassMetadata propMeta = sessionFactory.getClassMetadata(prop.getReturnedClass());
				return getPropertyClass(propMeta, propertyName.substring(propertyName.indexOf(SEPARATOR) + 1));
			} catch (HibernateException e) {
				throw new HibernateLayerException(e, ExceptionCode.HIBERNATE_COULD_NOT_RESOLVE, propertyName,
						meta.getEntityName());
			}
		} else {
			try {
				return meta.getPropertyType(propertyName).getReturnedClass();
			} catch (HibernateException e) {
				throw new HibernateLayerException(e, ExceptionCode.HIBERNATE_COULD_NOT_RESOLVE, propertyName,
						meta.getEntityName());
			}
		}
	}

	/**
	 * Return the Hibernate SessionFactory.
	 * 
	 * @return session factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Set session factory.
	 *
	 * @param sessionFactory session factory
	 * @throws HibernateLayerException could not get class metadata for data source
	 */
	public void setSessionFactory(SessionFactory sessionFactory) throws HibernateLayerException {
		try {
			this.sessionFactory = sessionFactory;
			if (null != layerInfo) {
				entityMetadata = sessionFactory.getClassMetadata(layerInfo.getFeatureInfo().getDataSourceName());
			}
		} catch (Exception e) { // NOSONAR
			throw new HibernateLayerException(e, ExceptionCode.HIBERNATE_NO_SESSION_FACTORY);
		}
	}
}