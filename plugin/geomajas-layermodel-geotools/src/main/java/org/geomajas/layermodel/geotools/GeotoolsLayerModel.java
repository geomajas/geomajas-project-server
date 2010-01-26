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
package org.geomajas.layermodel.geotools;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerModel;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layermodel.geotools.command.interceptor.GeotoolsTransactionInterceptor;
import org.geomajas.layermodel.geotools.postgis.NonTypedPostgisFidMapperFactory;
import org.geomajas.layermodel.shapeinmem.FeatureSourceRetriever;
import org.geomajas.service.BboxService;
import org.geomajas.service.FilterCreator;
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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

/**
 * Geotools layer model.
 *
 * @author check subversion
 */
@Component
@Scope("prototype")
public class GeotoolsLayerModel extends FeatureSourceRetriever implements LayerModel {

	private static final String CLASSPATH_URL_PROTOCOL = "classpath:";

	private final Logger log = LoggerFactory.getLogger(GeotoolsLayerModel.class);

	private FilterFactory filterFactory;

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	private Filter defaultFilter;

	@Autowired
	private BboxService bboxService;

	@Autowired
	private FilterCreator filterCreator;

	@Autowired
	private GeoService geoService;

	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		setFeatureSourceName(layerInfo.getFeatureInfo().getDataSourceName());
		initFeatures();
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
		} catch (LayerException le) {
			throw le;
		} catch (GeomajasException ge) {
			throw new LayerException(ExceptionCode.CANNOT_CREATE_LAYER_MODEL, ge, url);
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
					.collection(new SimpleFeature[] {(SimpleFeature) feature});
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
//			List<FeatureId> ids = store.addFeatures(col);
//			FeatureId newId = ids.iterator().next();
//			return read(newId.getID());
			try {
				store.addFeatures(col);
			} catch (IOException ioe) {
				throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, ioe);
			}
			return feature;
		} else {
			log.error("Don't know how to create or update " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement FeatureStore");
			throw new LayerException(ExceptionCode.CREATE_OR_UPDATE_NOT_IMPLEMENTED, getFeatureSourceName(),
					source.getClass().getName());
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
				Object value = attrMap.get(descriptors.get(i).getName());
				attrList.add(value);
			}

			try {
				store.modifyFeatures(descriptors.toArray(new AttributeDescriptor[descriptors.size()]),
						attrList.toArray(), filter);
				store.modifyFeatures(store.getSchema().getGeometryDescriptor(), getFeatureModel().getGeometry(feature),
						filter);
				log.debug("Updated feature {} in {}", filter.getIDs().iterator().next(), getFeatureSourceName());
			} catch (IOException ioe) {
				throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, ioe);
			}
		} else {
			log.error("Don't know how to create or update " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement FeatureStore");
			throw new LayerException(ExceptionCode.CREATE_OR_UPDATE_NOT_IMPLEMENTED, getFeatureSourceName(),
					source.getClass().getName());
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
			throw new LayerException(ExceptionCode.DELETE_NOT_IMPLEMENTED, getFeatureSourceName(),
					source.getClass().getName());
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

	public Bbox getBounds() throws LayerException {
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

			return bboxService.fromEnvelope(fc.getBounds());
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
	public Bbox getBounds(Filter queryFilter) throws LayerException {
		Filter filter = queryFilter;
		if (defaultFilter != null) {
			filter = filterCreator.createLogicFilter(filter, "AND", defaultFilter);
		}
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

			return bboxService.fromEnvelope(fc.getBounds());
		} catch (Throwable t) {
			if (t instanceof NullPointerException || t instanceof IllegalStateException) {
				GeotoolsTransactionInterceptor.closeTransaction();
			}
			throw new LayerException(ExceptionCode.LAYER_MODEL_IO_EXCEPTION, t);
		}
	}

	public Iterator<?> getElements(Filter queryFilter) throws LayerException {
		Filter filter = queryFilter;
		Filter realFilter = filter;
		if (defaultFilter != null) {
			filter = filterCreator.createLogicFilter(filter, "AND", defaultFilter);
		}
		if (filter instanceof Id) {
			Iterator<?> iterator = ((Id) filter).getIdentifiers().iterator();
			List<String> identifiers = new ArrayList<String>();
			while (iterator.hasNext()) {
				identifiers.add(getFeatureSourceName() + "." + iterator.next());
			}
			realFilter = filterCreator.createFidFilter(identifiers.toArray(new String[identifiers.size()]));
		}

		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store =
					(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			store.setTransaction(GeotoolsTransactionInterceptor.getTransaction());
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures(realFilter);
			Iterator<SimpleFeature> it = fc.iterator();
			GeotoolsTransactionInterceptor.addIterator(fc, it);

			return it;
		} catch (Throwable t) {
			if (t instanceof NullPointerException || t instanceof IllegalStateException) {
				GeotoolsTransactionInterceptor.closeTransaction();
				getElements(realFilter);
			}
			throw new LayerException(ExceptionCode.UNEXPECTED_PROBLEM, t);
		}
	}

	public FeatureModel getFeatureModel() {
		return this.featureModel;
	}

	public Iterator<?> getObjects(String attributeName, Filter filter) throws LayerException {
		return Collections.EMPTY_LIST.iterator();
	}

	public Filter getDefaultFilter() {
		return defaultFilter;
	}

	public void setDefaultFilter(Filter defaultFilter) {
		this.defaultFilter = defaultFilter;
	}
}