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
package org.geomajas.extension.printing.configuration;

import org.geomajas.layermodel.hibernate.GenericHibernateDao;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * ???
 *
 * @author check subversion
 */
public class HibernatePrintTemplateDao extends GenericHibernateDao<PrintTemplate, Long> implements
		PrintTemplateDao {

	private final Logger log = LoggerFactory.getLogger(PrintTemplateDao.class);

	public HibernatePrintTemplateDao(Session session) {
		super(PrintTemplate.class, session);
	}

	@Override
	protected void setSession(Session s) {
		this.session = s;
	}

	public PrintTemplate findPrintByName(String name) throws IOException {
		PrintTemplate example = new PrintTemplate(false);
		example.setName(name);
		return findUniqueByExample(example);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllNames() throws IOException {
		try {
			Query q = getSession().createQuery("select name from PrintTemplate p");
			return (List<String>) q.list();
		} catch (HibernateException e) {
			log.error("find all print template names failed", e);
			throw new IOException("find all print template names failed");
		}
	}

}
