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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.geotools.command.interceptor.GeotoolsTransactionInterceptor;
import org.geomajas.layer.geotools.postgis.NonTypedPostgisFidMapperFactory;
import org.geomajas.layer.shapeinmem.FeatureSourceRetriever;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.jdbc.JDBC1DataStore;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Geotools layer model.
 * 
 * @author check subversion
 */
public class GeotoolsLayer extends FeatureSourceRetriever implements VectorLayer {

	private static final String CLASSPATH_URL_PROTOCOL = "classpath:";

	private final Logger log = LoggerFactory.getLogger(GeotoolsLayer.class);

	private FilterFactory filterFactory;

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private GeoService geoService;

	private CoordinateReferenceSystem crs;

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	private void initCrs() throws LayerException {
		try {
			crs = CRS.decode(layerInfo.getCrs());
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(ExceptionCode.LAYER_CRS_UNKNOWN_AUTHORITY, e, layerInfo.getId(), getLayerInfo()
					.getCrs());
		} catch (FactoryException exception) {
			throw new LayerException(ExceptionCode.LAYER_CRS_PROBLEMATIC, exception, layerInfo.getId(), getLayerInfo()
					.getCrs());
		}
	}

	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		initCrs();
		setFeatureSourceName(layerInfo.getFeatureInfo().getDataSourceName());
		initFeatures();
	}

	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public boolean isCreateCapable() {
		return true;
	}

	public boolean isUpdateCapable() {
		return true;
	}

	public boolean isDeleteCapable() {
		return true;
	}

	public void setUrl(URL url) throws LayerException {
		try {
			InputStream in = url.openStream();
			if (in == null) {
				throw new IOException("File not found: " + url);
			}
			in.close();

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("url", url.toExternalForm());
			DataStore store = DataStoreFinder.getDataStore(parameters);
			if (store instanceof PostgisDataStore) {
				PostgisDataStore jdbcStore = (PostgisDataStore) store;
				jdbcStore.setFIDMapperFactory(new NonTypedPostgisFidMapperFactory(false));
			} else if (store instanceof JDBCDataStore) {
				JDBCDataStore jdbcStore = (JDBCDataStore) store;
				jdbcStore.setFIDMapperFactory(new NonTypedFidMapperFactory());
			}
			if (store instanceof JDBC1DataStore) {
				store = new ExtendedDataStore((JDBC1DataStore) store);
			}
			setDataStore(store);
		} catch (MalformedURLException e) {
			throw new LayerException(ExceptionCode.INVALID_SHAPE_FILE_URL, url);
		} catch (IOException ioe) {
			throw new LayerException(ExceptionCode.CANNOT_CREATE_LAYER_MODEL, ioe, url);
		}
	}

	public void setUrl(String url) throws LayerException {
		try {
			URL realUrl = null;
			if (url.startsWith(CLASSPATH_URL_PROTOCOL)) {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				if (null == classLoader) {
					classLoader = this.getClass().getClassLoader();
				}
				realUrl = classLoader.getResource(url.substring(CLASSPATH_URL_PROTOCOL.length()));
			}
			if (null == realUrl) {
				realUrl = new URL(url);
			}
			setUrl(realUrl);
		} catch (MalformedURLException mue) {
			throw new LayerException(ExceptionCode.INVALID_SHAPE_FILE_URL, url);
		}
	}

	@Override
	protected void setDataStore(DataStore dataStore) throws LayerException {
		super.setDataStore(dataStore);
		initFeatures();
	}

	private void initFeatures() throws LayerException {
		if (null == layerInfo || null == getDataStore()) {
			return;
		}
		this.filterFactory = CommonFactoryFinder.getFilterFactory(null);
		this.featureModel = new GeotoolsFeatureModel(getDataStore(), layerInfo.getFeatureInfo().getDataSourceName(),
				geoService.getSridFromCrs(layerInfo.getCrs()));
	}

	public Object create(Object feature) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			FeatureCollection<SimpleFeatureType, SimpleFeature> col = DataUtilities
					.collection(new SimpleFeature[] { (SimpleFeature) feature });
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
			// List<FeatureId> ids = store.addFeatures(col);
			// FeatureId newId = ids.iterator().next();
			// return read(newId.getID());
			try {
				store.addFeatures(col);
			} catch (IOException ioe) {
				throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, ioe);
			}
			return feature;
		} else {
			log.error("Don't know how to create or update " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement FeatureStore");
			throw new LayerException(ExceptionCode.CREATE_OR_UPDATE_NOT_IMPLEMENTED, getFeatureSourceName(), source
					.getClass().getName());
		}
	}

	public void update(Object feature) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			Identifier identifier = new FeatureIdImpl(getFeatureModel().getId(feature));
			Id filter = filterFactory.id(Collections.singleton(identifier));
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());

			List<AttributeDescriptor> descriptors = store.getSchema().getAttributeDescriptors();
			Map<String, Object> attrMap = getFeatureModel().getAttributes(feature);
			List<Object> attrList = new ArrayList<Object>();
			for (int i = 0; i < attrMap.size(); i++) {
				Object value = attrMap.get(descriptors.get(i).getName().toString());
				attrList.add(value);
			}

			try {
				store.modifyFeatures(descriptors.toArray(new AttributeDescriptor[descriptors.size()]), attrList
						.toArray(), filter);
				store.modifyFeatures(store.getSchema().getGeometryDescriptor(), getFeatureModel().getGeometry(feature),
						filter);
				log.debug("Updated feature {} in {}", filter.getIDs().iterator().next(), getFeatureSourceName());
			} catch (IOException ioe) {
				throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, ioe);
			}
		} else {
			log.error("Don't know how to create or update " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement FeatureStore");
			throw new LayerException(ExceptionCode.CREATE_OR_UPDATE_NOT_IMPLEMENTED, getFeatureSourceName(), source
					.getClass().getName());
		}
	}

	public void delete(String featureId) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			Identifier identifier = new FeatureIdImpl(featureId);
			Id filter = filterFactory.id(Collections.singleton(identifier));
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
			try {
				store.removeFeatures(filter);
				if (log.isDebugEnabled()) {
					log.debug("Deleted feature {} in {}", filter.getIDs().iterator().next(), getFeatureSourceName());
				}
			} catch (IOException ioe) {
				throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, ioe);
			}
		} else {
			log.error("Don't know how to delete from " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement FeatureStore");
			throw new LayerException(ExceptionCode.DELETE_NOT_IMPLEMENTED, getFeatureSourceName(), source.getClass()
					.getName());
		}
	}

	public Object saveOrUpdate(Object feature) throws LayerException {
		try {
			if (read(getFeatureModel().getId(feature)) == null) {
				return create(feature);
			} else {
				update(feature);
				return feature;
			}
		} catch (NoSuchElementException e) {
			return create(feature);
		}
	}

	public Object read(String featureId) throws LayerException {
		Identifier identifier = new FeatureIdImpl(featureId);
		Id filter = filterFactory.id(Collections.singleton(identifier));
		Iterator<?> iterator = getElements(filter);
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
	}

	public Envelope getBounds() throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures();
			Iterator<SimpleFeature> it = fc.iterator();
			GeotoolsTransactionInterceptor.addIterator(fc, it);
			return fc.getBounds();
		} catch (Throwable t) {
			if (t instanceof NullPointerException || t instanceof IllegalStateException) {
				GeotoolsTransactionInterceptor.closeTransaction();
			}
			throw new LayerException(ExceptionCode.UNEXPECTED_PROBLEM, t);
		}
	}

	/**
	 * Retrieve the bounds of the specified features.
	 * 
	 * @return the bounds of the specified features
	 */
	public Envelope getBounds(Filter queryFilter) throws LayerException {
		Filter filter = convertFilter(queryFilter);
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures(filter);
			Iterator<SimpleFeature> it = fc.iterator();
			GeotoolsTransactionInterceptor.addIterator(fc, it);
			return fc.getBounds();
		} catch (Throwable t) {
			if (t instanceof NullPointerException || t instanceof IllegalStateException) {
				GeotoolsTransactionInterceptor.closeTransaction();
			}
			throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, t);
		}
	}

	public Iterator<?> getElements(Filter queryFilter) throws LayerException {
		Filter filter = convertFilter(queryFilter);
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures(filter);
			Iterator<SimpleFeature> it = fc.iterator();
			GeotoolsTransactionInterceptor.addIterator(fc, it);

			return it;
		} catch (Throwable t) {
			if (t instanceof NullPointerException || t instanceof IllegalStateException) {
				GeotoolsTransactionInterceptor.closeTransaction();
				getElements(filter);
			}
			throw new LayerException(ExceptionCode.UNEXPECTED_PROBLEM, t);
		}
	}

	private Filter convertFilter(Filter queryFilter) {
		if (queryFilter instanceof Id) {
			Iterator<?> iterator = ((Id) queryFilter).getIdentifiers().iterator();
			List<String> identifiers = new ArrayList<String>();
			while (iterator.hasNext()) {
				identifiers.add(getFeatureSourceName() + "." + iterator.next());
			}
			return filterCreator.createFidFilter(identifiers.toArray(new String[identifiers.size()]));
		}
		return queryFilter;
	}

	public FeatureModel getFeatureModel() {
		return this.featureModel;
	}

	public Iterator<?> getObjects(String attributeName, Filter filter) throws LayerException {
		return Collections.EMPTY_LIST.iterator();
	}
}