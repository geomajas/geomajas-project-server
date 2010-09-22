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
package org.geomajas.plugin.printing.configuration;

import java.io.IOException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dao proxy for stored print templates.
 *
 * @author Jan De Moerloose
 */
public class HibernatePrintTemplateDao implements
		PrintTemplateDao {

	private final Logger log = LoggerFactory.getLogger(PrintTemplateDao.class);

	@Autowired
	private SessionFactory sessionFactory;


	public void merge(PrintTemplate template) throws IOException {
		sessionFactory.getCurrentSession().save(template);
	}

	public List<PrintTemplate> findAll() throws IOException {
		try {
			Query q = sessionFactory.getCurrentSession().createQuery("from PrintTemplate");
			return q.list();
		} catch (HibernateException e) {
			log.error("find all print template names failed", e);
			throw new IOException("find all print template names failed");
		}
	}


	

}
