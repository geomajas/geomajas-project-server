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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.geotools.data.DataAccess;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Request-scoped object that synchronizes Geotools transactions with Spring transactions. The Geotools transactions
 * simply follow the Spring transaction lifecycle.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component("GeoToolsTransactionSynchronization")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
public class GeoToolsTransactionSynchronization extends TransactionSynchronizationAdapter {

	private final Logger log = LoggerFactory.getLogger(GeoToolsTransactionSynchronization.class);

	private final List<IteratorInfo> iterators = new ArrayList<IteratorInfo>();

	private Map<DataAccess<SimpleFeatureType, SimpleFeature>, Transaction> transactions
		= new HashMap<DataAccess<SimpleFeatureType, SimpleFeature>, Transaction>();

	/**
	 * Get the list of iterators.
	 * 
	 * @return list of iterators
	 */
	public List<IteratorInfo> getIterators() {
		return iterators;
	}

	/**
	 * Add an iterator.
	 * 
	 * @param iterator iterator
	 */
	public void addIterator(FeatureIterator<SimpleFeature> iterator) {
		this.iterators.add(new IteratorInfo(iterator));
	}

	/**
	 * Called at construction time. Make sure that the target object is only accessed when the
	 * {@link TransactionSynchronizationManager} has already started. This will be the case when accessing the object in
	 * a transactional method.
	 */
	@PostConstruct
	public void init() {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(this);
		} else {
			log.warn("Could not synchronize with transaction synchronization manager");
		}
	}

	@PreDestroy
	public void destroy() {
		// clean up iterators
		for (IteratorInfo info : iterators) {
			info.close();
		}
		iterators.clear();
	}

	/**
	 * Synchronize the geotools transaction with the platform transaction, if such a transaction is active.
	 * 
	 * @param featureStore
	 * @param dataSource
	 */
	public void synchTransaction(SimpleFeatureStore featureStore) {
		// check if transaction is active, otherwise do nothing (auto-commit mode)
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			DataAccess<SimpleFeatureType, SimpleFeature> dataStore = featureStore.getDataStore();
			if (!transactions.containsKey(dataStore)) {
				Transaction transaction = null;
				if (dataStore instanceof JDBCDataStore) {
					JDBCDataStore jdbcDataStore = (JDBCDataStore) dataStore;
					transaction = jdbcDataStore.buildTransaction(DataSourceUtils
							.getConnection(jdbcDataStore.getDataSource()));
				} else {
					transaction = new DefaultTransaction();
				}
				transactions.put(dataStore, transaction);
			}
			featureStore.setTransaction(transactions.get(dataStore));
		}
	}

	/**
	 * Called when the transaction manager has committed/rolled back. We should copy this behavior at the geotools
	 * level, except for the JDBC case, which has already been handled.
	 */
	@Override
	public void afterCompletion(int status) {
		// clean up transactions that aren't managed by Spring
		for (DataAccess<SimpleFeatureType, SimpleFeature> dataStore : transactions.keySet()) {
			// we must manage transaction ourselves for non-JDBC !
			if (!(dataStore instanceof JDBCDataStore)) {
				Transaction transaction = transactions.get(dataStore);
				try {
					switch (status) {
						case TransactionSynchronization.STATUS_COMMITTED:
							transaction.commit();
							break;
						case TransactionSynchronization.STATUS_ROLLED_BACK:
							transaction.rollback();
							break;
						case TransactionSynchronization.STATUS_UNKNOWN:
					}
				} catch (IOException e) {
					try {
						transaction.rollback();
					} catch (IOException e1) {
						log.debug("Could not rollback geotools transaction", e1);
					}
				} finally {
					try {
						transaction.close();
					} catch (IOException e) {
						log.debug("Could not close geotools transaction", e);
					}
				}
			}
		}
		transactions.clear();
	}

	/**
	 * Container object to manage iterators.
	 * 
	 * @author Jan De Moerloose
	 */
	private static class IteratorInfo {

		private final FeatureIterator<SimpleFeature> iterator;

		/**
		 * Create a {@link IteratorInfo}.
		 * 
		 * @param iterator iterator to maintain
		 */
		public IteratorInfo(FeatureIterator<SimpleFeature> iterator) {
			this.iterator = iterator;
		}

		/**
		 * Close the iterator.
		 */
		public void close() {
			iterator.close();
		}
	}

}
