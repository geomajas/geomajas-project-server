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

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General Hibernate ClassMetadata and SessionFactory provision. Package visibility only.
 * 
 * @author Pieter De Graef
 */
class HibernateLayerUtil {

	private final Logger log = LoggerFactory.getLogger(HibernateLayerUtil.class);

	public static final String XPATH_SEPARATOR = "/";
	public static final String SEPARATOR = ".";

	public static final String SEPARATOR_REGEXP = "\\.";

	private SessionFactory sessionFactory;

	private ClassMetadata entityMetadata;

	private VectorLayerInfo layerInfo;

	public FeatureInfo getFeatureInfo() {
		return layerInfo.getFeatureInfo();
	}

	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

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
		if (propertyName.indexOf(XPATH_SEPARATOR) > 0) {
			propertyName = propertyName.replace(XPATH_SEPARATOR, SEPARATOR);
		}
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
			} catch (Exception e) {
				throw new HibernateLayerException(ExceptionCode.HIBERNATE_COULD_NOT_RESOLVE, propertyName,
						meta.getEntityName());
			}
		} else {
			try {
				return meta.getPropertyType(propertyName).getReturnedClass();
			} catch (Exception e) {
				throw new HibernateLayerException(ExceptionCode.HIBERNATE_COULD_NOT_RESOLVE, propertyName,
						meta.getEntityName());
			}
		}
	}

	/**
	 * Search the Hibernate configuration for the metadata of a certain class.
	 * 
	 * @param clazz
	 *            The class you are searching metadata for.
	 * @return Returns the ClassMetadata object if found.
	 * @throws HibernateLayerException
	 *             Throws an exception if the metadata for the given class could not be found.
	 */
	protected ClassMetadata getMetadata(Class<?> clazz) throws HibernateLayerException {
		try {
			return sessionFactory.getClassMetadata(clazz);
		} catch (Exception e) {
			log.error("Couldn't get metadata for " + clazz.getName(), e);
			throw new HibernateLayerException(ExceptionCode.HIBERNATE_NO_META_DATA, clazz.getName());
		}
	}

	/**
	 * Search the Hibernate configuration for the metadata of a certain class.
	 * 
	 * @param name
	 *            The class name you are searching metadata for.
	 * @return Returns the ClassMetadata object if found.
	 * @throws HibernateLayerException
	 *             Throws an exception if the metadata for the given class could not be found.
	 */
	protected ClassMetadata getMetadata(String name) throws HibernateLayerException {
		try {
			return sessionFactory.getClassMetadata(name);
		} catch (Exception e) {
			log.error("Couldn't get metadata for " + name, e);
			throw new HibernateLayerException(ExceptionCode.HIBERNATE_NO_META_DATA, name);
		}
	}

	/**
	 * Return the Hibernate SessionFactory
	 * 
	 * @return session factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	public void setSessionFactory(SessionFactory sessionFactory) throws HibernateLayerException {
		try {
			this.sessionFactory = sessionFactory;
			if (null != layerInfo) {
				entityMetadata = sessionFactory.getClassMetadata(layerInfo.getFeatureInfo().getDataSourceName());
			}
		} catch (Exception e) {
			throw new HibernateLayerException(e, ExceptionCode.HIBERNATE_NO_SESSION_FACTORY);
		}
	}
}