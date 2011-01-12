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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Generic hibernate feature dao implementation.
 *
 * @param <T> feature object type
 * @param <ID> id attribute type
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public abstract class GenericHibernateDao<T, ID extends Serializable> implements GenericDao<T, ID> {

	private final Logger log = LoggerFactory.getLogger(GenericHibernateDao.class);

	private Class<T> persistentClass;
	protected Session session;

	protected GenericHibernateDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public GenericHibernateDao(Class<T> persistentClass, Session session) {
		this.persistentClass = persistentClass;
		this.session = session;
	}

	protected abstract void setSession(Session s);

	protected Session getSession() {
		if (session == null) {
			throw new IllegalStateException("Session has not been set on DAO before usage");
		}
		return session;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock) throws IOException {
		try {
			T entity;
			if (lock) {
				entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
			} else {
				entity = (T) getSession().load(getPersistentClass(), id);
			}

			return entity;
		} catch (HibernateException e) {
			log.error("find by id failed for id = " + id, e);
			throw new IOException("find by id failed for id = " + id);
		}
	}

	public List<T> findAll() throws IOException {
		try {
			return findByCriteria();
		} catch (HibernateException e) {
			log.error("find all failed", e);
			throw new IOException("find all failed");
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String[] excludeProperty) throws IOException {
		try {
			Criteria crit = getSession().createCriteria(getPersistentClass());
			Example example = Example.create(exampleInstance);
			for (String exclude : excludeProperty) {
				example.excludeProperty(exclude);
			}
			crit.add(example);
			return crit.list();
		} catch (HibernateException e) {
			log.error("find by example failed", e);
			throw new IOException("find by example failed");
		}
	}

	@SuppressWarnings("unchecked")
	public T findUniqueByExample(T exampleInstance) throws IOException {
		try {
			Criteria crit = getSession().createCriteria(getPersistentClass());
			Example example = Example.create(exampleInstance);
			crit.add(example);
			return (T) crit.uniqueResult();
		} catch (HibernateException e) {
			log.error("find unique by example failed", e);
			throw new IOException("find unique by example failed");
		}
	}

	public T makePersistent(T entity) throws IOException {
		try {
			getSession().saveOrUpdate(entity);
			return entity;
		} catch (HibernateException e) {
			log.error("make persistent failed", e);
			throw new IOException("make persistent failed");
		}
	}

	public void makeTransient(T entity) throws IOException {
		try {
			getSession().delete(entity);
		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new IOException("delete failed");
		}
	}

	public void merge(T entity) throws IOException {
		try {
			getSession().merge(entity);
		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new IOException("delete failed");
		}
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

}