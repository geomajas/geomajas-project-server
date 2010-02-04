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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * Holder for GeoTools transactions.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeoToolsTransactionHolder extends ResourceHolderSupport {

	private GeotoolsTransaction transaction = new GeotoolsTransaction();

	private List<IteratorInfo> iterators = new ArrayList<IteratorInfo>();

	public List<IteratorInfo> getIterators() {
		return iterators;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public boolean isTransactionActive() {
		return !transaction.isClosed();
	}

	public void addIterator(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
			Iterator<SimpleFeature> iterator) {
		this.iterators.add(new IteratorInfo(featureCollection, iterator));
	}

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
	 * 
	 */
	public static class GeotoolsTransaction extends DefaultTransaction {

		private boolean closed;

		@Override
		public synchronized void close() {
			super.close();
			closed = true;
		}

		public boolean isClosed() {
			return closed;
		}

	}

	/**
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private static class IteratorInfo {

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
