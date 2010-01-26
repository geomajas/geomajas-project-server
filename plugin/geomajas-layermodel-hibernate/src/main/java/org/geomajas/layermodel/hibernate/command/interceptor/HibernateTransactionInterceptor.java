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
package org.geomajas.layermodel.hibernate.command.interceptor;

import org.geomajas.command.Command;
import org.geomajas.command.CommandInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Basic Hibernate helper class, handles SessionFactory, Session and Transaction.
 * <p>
 * Uses a static initializer for the initial SessionFactory creation and holds Session and Transactions in thread local
 * variables. (original version by christian@hibernate.org)
 *
 * Added afterExecute() and beforeExecute()
 *
 * @author Jan De Moerloose
 */
@Component()
@Scope("prototype")
public class HibernateTransactionInterceptor implements CommandInterceptor {

	private final Logger log = LoggerFactory.getLogger(HibernateTransactionInterceptor.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Retrieves the current {@link Session}.
	 *
	 * @return Session
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Closes the Session local to the thread.
	 */
	public void closeSession() {
		sessionFactory.getCurrentSession().close();
	}

	/**
	 * Start a new database transaction.
	 */
	public void beginTransaction() {
		sessionFactory.getCurrentSession().beginTransaction();
	}

	/**
	 * Commit the database transaction.
	 */
	public void commitTransaction() {
		Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
		if (transaction.isActive()) {
			transaction.commit();
		}
	}

	/**
	 * Rollback the database transaction.
	 */
	public void rollbackTransaction() {
		Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
		if (transaction.isActive()) {
			transaction.rollback();
		}
	}

	public void afterExecute(Command command) {
		/*
		if (null != command && command.getResult().isError()) {
			log.debug("tx rollback");
			rollbackTransaction();
		} else {
			log.debug("tx commit");
			commitTransaction();
		}
		*/
		commitTransaction(); // temp cfr commented code above
		closeSession();
	}

	public boolean beforeExecute(Command command) {
		Session session = sessionFactory.openSession();
		ManagedSessionContext.bind(session);
		session.beginTransaction();
		log.debug("tx started");
		return true;
	}

}
