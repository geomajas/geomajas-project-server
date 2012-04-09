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
package org.geomajas.plugin.printing.configuration;

import java.io.IOException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dao proxy for stored print templates.
 *
 * @author Jan De Moerloose
 */
public class HibernatePrintTemplateDao implements PrintTemplateDao {

	@Autowired
	private SessionFactory sessionFactory;

	/** {@inheritDoc} */
	public void merge(PrintTemplate template) throws IOException {
		sessionFactory.getCurrentSession().save(template);
	}

	@SuppressWarnings("unchecked")
	public List<PrintTemplate> findAll() throws IOException {
		try {
			Query q = sessionFactory.getCurrentSession().createQuery("from PrintTemplate");
			return q.list();
		} catch (HibernateException e) {
			throw new IOException("find all print template names failed", e);
		}
	}

}
