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
package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Transaction manager for GeoTools layers.
 * 
 * @author Jan De Moerloose
 */
public class GeoToolsTransactionManager extends AbstractPlatformTransactionManager {

	/**
	 * Constructs a new GeoToolsTransactionManager.
	 */
	public GeoToolsTransactionManager() {
		setNestedTransactionAllowed(false);
	}

	/**
	 * Returns the current GeoTools transaction.
	 * 
	 * @return the current GeoTools transaction or null if no active transaction.
	 */
	public Transaction getTransaction() {
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) TransactionSynchronizationManager
				.getResource(this);
		if (txHolder != null && txHolder.isTransactionActive()) {
			return txHolder.getTransaction();
		} else {
			return null;
		}
	}

	/**
	 * Adds the specified collection and iterator. This method should be called when opening a feature collection to
	 * allow proper connection/transaction/lock cleanup in the case of exceptions.
	 * 
	 * @param featureCollection feature collection
	 * @param iterator SimpleFeature iterator
	 */
	public void addIterator(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
			Iterator<SimpleFeature> iterator) {
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) TransactionSynchronizationManager
				.getResource(this);
		txHolder.addIterator(featureCollection, iterator);
	}

	@Override
	protected Object doGetTransaction() {
		GeotoolsTransactionObject txObject = new GeotoolsTransactionObject();
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) TransactionSynchronizationManager
				.getResource(this);
		txObject.setTransactionHolder(txHolder, false);
		return txObject;
	}

	@Override
	protected boolean isExistingTransaction(Object transaction) {
		GeotoolsTransactionObject txObject = (GeotoolsTransactionObject) transaction;
		return (txObject.getTransactionHolder() != null && txObject.getTransactionHolder().isTransactionActive());
	}

	/**
	 * This implementation tries to honor the timeout.
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		GeotoolsTransactionObject txObject = (GeotoolsTransactionObject) transaction;
		if (txObject.getTransactionHolder() == null || 
				txObject.getTransactionHolder().isSynchronizedWithTransaction()) {
			txObject.setTransactionHolder(new GeoToolsTransactionHolder(), true);
		}

		txObject.getTransactionHolder().setSynchronizedWithTransaction(true);

		int timeout = determineTimeout(definition);
		if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
			txObject.getTransactionHolder().setTimeoutInSeconds(timeout);
		}

		// Bind the session holder to the thread.
		if (txObject.isNewTransactionHolder()) {
			TransactionSynchronizationManager.bindResource(this, txObject.getTransactionHolder());
		}
	}

	@Override
	protected Object doSuspend(Object transaction) {
		GeotoolsTransactionObject txObject = (GeotoolsTransactionObject) transaction;
		txObject.setTransactionHolder(null, false);
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) TransactionSynchronizationManager
				.unbindResource(this);
		return txHolder;
	}

	@Override
	protected void doResume(Object transaction, Object suspendedResources) {
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) suspendedResources;
		TransactionSynchronizationManager.bindResource(this, txHolder);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		GeotoolsTransactionObject txObject = (GeotoolsTransactionObject) status.getTransaction();
		Transaction tx = txObject.getTransactionHolder().getTransaction();
		try {
			tx.commit();
		} catch (IOException e) {

		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		GeotoolsTransactionObject txObject = (GeotoolsTransactionObject) status.getTransaction();
		Transaction tx = txObject.getTransactionHolder().getTransaction();
		try {
			tx.rollback();
		} catch (IOException e) {

		}
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		GeotoolsTransactionObject txObject = (GeotoolsTransactionObject) transaction;

		// Remove the connection holder from the thread, if exposed.
		if (txObject.isNewTransactionHolder()) {
			TransactionSynchronizationManager.unbindResource(this);
		}

		txObject.getTransactionHolder().clear();
	}

	/**
	 * Geotools transaction object.
	 */
	public class GeotoolsTransactionObject {

		private GeoToolsTransactionHolder holder;

		private boolean newTransactionHolder;

		public GeoToolsTransactionHolder getTransactionHolder() {
			return holder;
		}

		public void setTransactionHolder(GeoToolsTransactionHolder holder, boolean newTransactionHolder) {
			this.holder = holder;
			this.newTransactionHolder = newTransactionHolder;
		}

		public boolean isNewTransactionHolder() {
			return newTransactionHolder;
		}
	}

}
