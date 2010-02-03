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
package org.geomajas.layermodel.geotools.command.interceptor;

import org.geomajas.command.Command;
import org.geomajas.command.CommandInterceptor;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Interceptor for Geotools transaction demarcation. Also closes Geotools collection iterators, whose life cycle is very
 * much intertwined with transaction life cycle. Iterators should be added to a thread-local list via the addIterator()
 * method.
 *
 * @author Jan De Moerloose
 *
 */
@Component
public class GeotoolsTransactionInterceptor implements CommandInterceptor {

	private static final ThreadLocal<Transaction> THREAD_TRANSACTION = new ThreadLocal<Transaction>();

	private static final ThreadLocal<List<IteratorInfo>> THREAD_ITERATORS = new ThreadLocal<List<IteratorInfo>>();

	/**
	 * Start a new database transaction.
	 */
	public static Transaction getTransaction() {
		Transaction tx = THREAD_TRANSACTION.get();
		if (tx == null) {
			Logger log = LoggerFactory.getLogger(GeotoolsTransactionInterceptor.class);
			log.debug("Starting new datastore transaction in this thread.");
			tx = new DefaultTransaction("thread");
			THREAD_TRANSACTION.set(tx);
		}
		return tx;
	}

	/**
	 * Commit the database transaction.
	 */
	public static void commitTransaction() {
		Transaction tx = THREAD_TRANSACTION.get();
		Logger log = LoggerFactory.getLogger(GeotoolsTransactionInterceptor.class);
		try {
			log.debug("Committing datastore transaction of this thread.");
			if (tx != null) {
				log.debug("Committing database transaction of this thread.");
				tx.commit();
			}
		} catch (IOException ex) {
			log.error("Could not commit, trying to roll back transaction", ex);
			rollbackTransaction();
		}
	}

	/**
	 * Rollback the database transaction.
	 */
	public static void rollbackTransaction() {
		Transaction tx = THREAD_TRANSACTION.get();
		Logger log = LoggerFactory.getLogger(GeotoolsTransactionInterceptor.class);
		try {
			if (tx != null) {
				log.info("Rolling back database transaction of this thread.");
				tx.rollback();
			}
		} catch (IOException e) {
			log.error("Could not roll back transaction", e);
		}
	}

	/**
	 * add ea new collection/iterator pair.
	 */
	public static void addIterator(FeatureCollection<SimpleFeatureType, SimpleFeature> fc, Iterator<SimpleFeature> it) {
		List<IteratorInfo> iterators = (List<IteratorInfo>) THREAD_ITERATORS.get();
		Logger log = LoggerFactory.getLogger(GeotoolsTransactionInterceptor.class);
		if (iterators == null) {
			log.debug("Adding datastore iterator in this thread.");
			iterators = new ArrayList<IteratorInfo>();
			THREAD_ITERATORS.set(iterators);
		}
		iterators.add(new IteratorInfo(fc, it));
	}

	/**
	 * Closes all iterators.
	 */
	public static void closeIterators() {
		List<IteratorInfo> iterators = (List<IteratorInfo>) THREAD_ITERATORS.get();
		THREAD_ITERATORS.set(null);
		Logger log = LoggerFactory.getLogger(GeotoolsTransactionInterceptor.class);
		if (iterators != null) {
			for (IteratorInfo info : iterators) {
				log.debug("Closing datastore iterator in this thread.");
				info.close();
			}
		}
	}

	/**
	 * Closes the transaction.
	 */
	public static void closeTransaction() {
		Transaction tx = (Transaction) THREAD_TRANSACTION.get();
		Logger log = LoggerFactory.getLogger(GeotoolsTransactionInterceptor.class);
		try {
			log.debug("closing datastore transaction of this thread.");
			THREAD_TRANSACTION.set(null);
			if (tx != null) {
				log.debug("closing database transaction of this thread.");
				tx.close();
			}
		} catch (IOException ex) {
			log.error("Could not commit, trying to roll back transaction", ex);
			rollbackTransaction();
		}
	}

	public void afterExecute(Command command) {
		/*if (command.getResult().isError()) {
			rollbackTransaction();
		} else {
			commitTransaction();
		}*/
		commitTransaction(); // temp to replace commented block above
		closeIterators();
		closeTransaction();
	}

	public boolean beforeExecute(Command command) {
		return true;
	}

	/**
	 * ???
	 */
	static class IteratorInfo {

		private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;

		private Iterator<SimpleFeature> iterator;

		public IteratorInfo(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
				Iterator<SimpleFeature> iterator) {
			this.featureCollection = featureCollection;
			this.iterator = iterator;
		}

		public void close() {
			featureCollection.close(iterator);
		}
	}
}
