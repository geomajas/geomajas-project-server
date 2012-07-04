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
package org.geomajas.layer.geotools;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * Holder for GeoTools transactions.
 * 
 * @author Jan De Moerloose
 */
public class GeoToolsTransactionHolder extends ResourceHolderSupport {

	private final List<IteratorInfo> iterators = new ArrayList<IteratorInfo>();
	
	// use object id as handle
	private GeotoolsTransaction transaction = new GeotoolsTransaction(this.toString());
	
	/**
	 * Get the list of iterators.
	 *
	 * @return list of iterators
	 */
	public List<IteratorInfo> getIterators() {
		return iterators;
	}

	/**
	 * Get transaction.
	 *
	 * @return transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * Is transaction active?
	 *
	 * @return true when transaction is active
	 */
	public boolean isTransactionActive() {
		return !transaction.isClosed();
	}

	/**
	 * Add an iterator.
	 *
	 * @param iterator iterator
	 */
	public void addIterator(FeatureIterator<SimpleFeature> iterator) {
		this.iterators.add(new IteratorInfo(iterator));
	}

	@Override
	public void clear() {
		super.clear();
		for (IteratorInfo info : iterators) {
			info.close();
		}
		if (transaction != null) {
			transaction.close();
		}
		iterators.clear();
		transaction = null;
	}

	/**
	 * Transaction which can be tested for closed state.
	 * 
	 * @author Jan De Moerloose
	 */
	public static class GeotoolsTransaction extends DefaultTransaction {

		private boolean closed;
		
		GeotoolsTransaction(String handle) {
			super(handle);
		}

		@Override
		public synchronized void close() {
			super.close();
			closed = true;
		}

		/**
		 * Is transaction already already closed?
		 *
		 * @return true when transaction is closed
		 */
		public boolean isClosed() {
			return closed;
		}

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
