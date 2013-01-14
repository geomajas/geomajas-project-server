/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.geotools;

import java.io.IOException;

import org.geotools.data.Transaction;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Transaction manager for GeoTools layers.
 * 
 * @author Jan De Moerloose
 */
public class GeoToolsTransactionManager extends AbstractPlatformTransactionManager {

	private static final long serialVersionUID = 190L;

	private final Logger log = LoggerFactory.getLogger(AbstractPlatformTransactionManager.class);

	/**
	 * Constructs a new GeoToolsTransactionManager.
	 */
	public GeoToolsTransactionManager() {
		super();
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
	 * @param iterator SimpleFeature iterator
	 */
	public void addIterator(FeatureIterator<SimpleFeature> iterator) {
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) TransactionSynchronizationManager
				.getResource(this);
		txHolder.addIterator(iterator);
	}

	@Override
	protected Object doGetTransaction() {
		GeoToolsTransactionObject txObject = new GeoToolsTransactionObject();
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) TransactionSynchronizationManager
				.getResource(this);
		txObject.setTransactionHolder(txHolder, false);
		return txObject;
	}

	@Override
	protected boolean isExistingTransaction(Object transaction) {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) transaction;
		return (txObject.getTransactionHolder() != null && txObject.getTransactionHolder().isTransactionActive());
	}

	/**
	 * This implementation tries to honor the timeout.
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) transaction;
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
	protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) status.getTransaction();
		txObject.setRollbackOnly();
	}

	@Override
	protected Object doSuspend(Object transaction) {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) transaction;
		txObject.setTransactionHolder(null, false);
		return TransactionSynchronizationManager.unbindResource(this);
	}

	@Override
	protected void doResume(Object transaction, Object suspendedResources) {
		GeoToolsTransactionHolder txHolder = (GeoToolsTransactionHolder) suspendedResources;
		TransactionSynchronizationManager.bindResource(this, txHolder);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) status.getTransaction();
		Transaction tx = txObject.getTransactionHolder().getTransaction();
		try {
			tx.commit();
		} catch (IOException e) {
	   		log.warn(e.getMessage(), e);
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) status.getTransaction();
		Transaction tx = txObject.getTransactionHolder().getTransaction();
		try {
			tx.rollback();
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		GeoToolsTransactionObject txObject = (GeoToolsTransactionObject) transaction;

		// Remove the connection holder from the thread, if exposed.
		if (txObject.isNewTransactionHolder()) {
			TransactionSynchronizationManager.unbindResource(this);
		}

		txObject.getTransactionHolder().clear();
	}

	/**
	 * GeoTools transaction object.
	 */
	private static class GeoToolsTransactionObject {

		private GeoToolsTransactionHolder holder;

		private boolean newTransactionHolder;
		
		private boolean rollbackOnly;

		/**
		 * Get the transaction holder.
		 *
		 * @return transaction holder
		 */
		public GeoToolsTransactionHolder getTransactionHolder() {
			return holder;
		}

		/**
		 * Set transaction holder.
		 *
		 * @param holder tx holder
		 * @param newTransactionHolder true when new
		 */
		public void setTransactionHolder(GeoToolsTransactionHolder holder, boolean newTransactionHolder) {
			this.holder = holder;
			this.newTransactionHolder = newTransactionHolder;
		}

		/**
		 * Is this a new transaction holder?
		 *
		 * @return true when transaction holder is new
		 */
		public boolean isNewTransactionHolder() {
			return newTransactionHolder;
		}

		/**
		 * Is for transaction for rollback only?
		 *
		 * @return true when tx should be rolled back
		 */
		public boolean isRollbackOnly() {
			return rollbackOnly;
		}

		/**
		 * Set rollback only state to true.
		 */
		public void setRollbackOnly() {
			this.rollbackOnly = true;
		}
	}

}
