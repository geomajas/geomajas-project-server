/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.shapeinmem.FeatureSourceRetriever;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;

/**
 * GeoTools layer model.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @author Kristof Heirwegh
 * @since 1.7.1
 */
@Api
public class GeoToolsLayer extends FeatureSourceRetriever implements VectorLayer {

	private final Logger log = LoggerFactory.getLogger(GeoToolsLayer.class);

	private static final long DEFAULT_COOLDOWN_TIME = 60000; // millis

	// WARNING this may change when using a different GeoTools library version
	private static final String MAGIC_STRING_LIBRARY_MISSING = "No datastore found. Possible causes are "
			+ "missing factory or missing library for your datastore (e.g. database driver).";

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	private String url;

	private String dbtype;

	private List<Parameter> parameters;

	private DataSource dataSource;

	@Autowired
	private FilterService filterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoToolsTransactionSynchronization transactionSynchronization;

	private CoordinateReferenceSystem crs;

	private String id;

	private boolean featureModelUsable;

	private long lastInitFeaturesRetry;

	private long cooldownTimeBetweenInitializationRetries = DEFAULT_COOLDOWN_TIME;

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Set the id for this layer.
	 * 
	 * @param id layer id
	 * @since 1.8.0
	 */
	@Api
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Set the URL for the case when the data source is a shape file.
	 * <p/>
	 * You cal also use "classpath" as protocol if the shape file is stored in the classpath.
	 * <p/>
	 * Important note: the shape file data store (specifically the indexing code) is not thread safe, so it should not
	 * be used for writing.
	 * 
	 * @param url shape file url
	 * @since 1.8.0
	 */
	@Api
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Set database type. Useful when the data store is a database.
	 * 
	 * @param dbtype database type
	 * @since 1.8.0
	 */
	@Api
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	/**
	 * Get the data source used by this layer (optional and only for database layers). This is the data source that is
	 * passed to the GeoTools data store.
	 * 
	 * @return the data source or null if not set
	 * @since 1.10.0
	 */
	@Api
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Set the data source used by this layer. This optional property can be used to bind a layers to an external data
	 * source. Alternatively, one can pass the JNDI name of a data source as a normal parameter.
	 * 
	 * @param dataSource the data source
	 * @since 1.10.0
	 */
	@Api
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Set additional parameters for the GeoTools data store.
	 * 
	 * @param parameters parameter list
	 * @since 1.8.0
	 */
	@Api
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * The time to wait between initialization retries in case the service is unavailable.
	 * 
	 * @param cooldownTimeBetweenInitializationRetries cool down time in milliseconds
	 * @since 1.8.0
	 */
	@Api
	public void setCooldownTimeBetweenInitializationRetries(long cooldownTimeBetweenInitializationRetries) {
		this.cooldownTimeBetweenInitializationRetries = cooldownTimeBetweenInitializationRetries;
	}

	@Override
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Set the layer configuration.
	 * 
	 * @param layerInfo layer information
	 * @throws LayerException oops
	 * @since 1.7.1
	 */
	@Api
	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		super.setLayerInfo(layerInfo);
		this.layerInfo = layerInfo;
	}

	@Override
	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	@Override
	public boolean isCreateCapable() {
		return true;
	}

	@Override
	public boolean isUpdateCapable() {
		return true;
	}

	@Override
	public boolean isDeleteCapable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated set the data store parameters on the parameter object instead
	 */
	@Override
	@Deprecated
	public void setDataStore(DataStore dataStore) throws LayerException {
		super.setDataStore(dataStore);
	}

	/**
	 * Finish initializing the layer.
	 * 
	 * @throws LayerException oops
	 */
	@PostConstruct
	protected void initFeatures() throws LayerException {
		if (null == layerInfo) {
			return;
		}
		crs = geoService.getCrs2(layerInfo.getCrs());
		setFeatureSourceName(layerInfo.getFeatureInfo().getDataSourceName());
		try {
			if (null == super.getDataStore()) {
				Map<String, Object> params = new HashMap<String, Object>();
				if (null != url) {
					params.put(ShapefileDataStoreFactory.URLP.key, url);
				}
				if (null != dbtype) {
					params.put(JDBCDataStoreFactory.DBTYPE.key, dbtype);
				}
				if (null != parameters) {
					for (Parameter parameter : parameters) {
						params.put(parameter.getName(), parameter.getValue());
					}
				}
				if (null != dataSource) {
					params.put(JDBCDataStoreFactory.DATASOURCE.key, dataSource);
					// these are apparently required but not used
					params.put(JDBCDataStoreFactory.DATABASE.key, "some_database");
					params.put(JDBCDataStoreFactory.USER.key, "some_user");
					params.put(JDBCDataStoreFactory.PASSWD.key, "some_password");
					params.put(JDBCDataStoreFactory.HOST.key, "some host");
					params.put(JDBCDataStoreFactory.PORT.key, "0");
				}
				DataStore store = DataStoreFactory.create(params);
				super.setDataStore(store);
			}
			if (null == super.getDataStore()) {
				return;
			}
			this.featureModel = new GeoToolsFeatureModel(super.getDataStore(), layerInfo.getFeatureInfo()
					.getDataSourceName(), geoService.getSridFromCrs(layerInfo.getCrs()), converterService);
			featureModel.setLayerInfo(layerInfo);
			featureModelUsable = true;

		} catch (IOException ioe) {
			if (MAGIC_STRING_LIBRARY_MISSING.equals(ioe.getMessage())) {
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION, url);
			} else {
				featureModelUsable = false;
				log.warn("The layer could not be correctly initialized: " + getId(), ioe);
			}
		} catch (LayerException le) {
			featureModelUsable = false;
			log.warn("The layer could not be correctly initialized: " + getId(), le);
		}
	}

	@Override
	@Transactional(rollbackFor = { Throwable.class })
	public Object create(Object feature) throws LayerException {
		SimpleFeatureSource source = getFeatureSource();
		if (source instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) source;
			DefaultFeatureCollection collection = new DefaultFeatureCollection();
			collection.add((SimpleFeature) feature);
			transactionSynchronization.synchTransaction(store);
			try {
				List<FeatureId> ids = store.addFeatures(collection);
				// fetch it again to get the generated values !!!
				if (ids.size() == 1) {
					return read(ids.get(0).getID());
				}
			} catch (IOException ioe) {
				featureModelUsable = false;
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
			}
			return feature;
		} else {
			log.error("Don't know how to create or update " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement SimpleFeatureStore");
			throw new LayerException(ExceptionCode.CREATE_OR_UPDATE_NOT_IMPLEMENTED, getFeatureSourceName(), source
					.getClass().getName());
		}
	}

	/**
	 * Update an existing feature. Made package private for testing purposes.
	 * 
	 * @param feature feature to update
	 * @throws LayerException oops
	 */
	void update(Object feature) throws LayerException {
		SimpleFeatureSource source = getFeatureSource();
		if (source instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) source;
			String featureId = getFeatureModel().getId(feature);
			Filter filter = filterService.createFidFilter(new String[] { featureId });
			transactionSynchronization.synchTransaction(store);
			List<Name> names = new ArrayList<Name>();
			Map<String, Attribute> attrMap = getFeatureModel().getAttributes(feature);
			List<Object> values = new ArrayList<Object>();
			for (Map.Entry<String, Attribute> entry : attrMap.entrySet()) {
				String name = entry.getKey();
				names.add(store.getSchema().getDescriptor(name).getName());
				values.add(entry.getValue().getValue());
			}

			try {
				store.modifyFeatures(names.toArray(new Name[names.size()]), values.toArray(), filter);
				store.modifyFeatures(store.getSchema().getGeometryDescriptor().getName(), getFeatureModel()
						.getGeometry(feature), filter);
				log.debug("Updated feature {} in {}", featureId, getFeatureSourceName());
			} catch (IOException ioe) {
				featureModelUsable = false;
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
			}
		} else {
			log.error("Don't know how to create or update " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement SimpleFeatureStore");
			throw new LayerException(ExceptionCode.CREATE_OR_UPDATE_NOT_IMPLEMENTED, getFeatureSourceName(), source
					.getClass().getName());
		}
	}

	@Override
	@Transactional(rollbackFor = { Throwable.class })
	public void delete(String featureId) throws LayerException {
		SimpleFeatureSource source = getFeatureSource();
		if (source instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) source;
			Filter filter = filterService.createFidFilter(new String[] { featureId });
			transactionSynchronization.synchTransaction(store);
			try {
				store.removeFeatures(filter);
				if (log.isDebugEnabled()) {
					log.debug("Deleted feature {} in {}", featureId, getFeatureSourceName());
				}
			} catch (IOException ioe) {
				featureModelUsable = false;
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
			}
		} else {
			log.error("Don't know how to delete from " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement SimpleFeatureStore");
			throw new LayerException(ExceptionCode.DELETE_NOT_IMPLEMENTED, getFeatureSourceName(), source.getClass()
					.getName());
		}
	}

	@Override
	@Transactional(rollbackFor = { Throwable.class })
	public Object saveOrUpdate(Object feature) throws LayerException {
		if (exists(getFeatureModel().getId(feature))) {
			update(feature);
		} else {
			feature = create(feature);
		}
		return feature;
	}

	@Override
	public Object read(String featureId) throws LayerException {
		Filter filter = filterService.createFidFilter(new String[] { featureId });
		Iterator<?> iterator = getElements(filter, 0, 0);
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
		}
	}

	@Override
	public Envelope getBounds() throws LayerException {
		return getBounds(null);
	}

	@Override
	public Envelope getBounds(Filter filter) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			SimpleFeatureStore store = (SimpleFeatureStore) source;
			transactionSynchronization.synchTransaction(store);
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc;
			if (null == filter) {
				fc = source.getFeatures();
			} else {
				fc = source.getFeatures(filter);
			}
			FeatureIterator<SimpleFeature> it = fc.features();
			transactionSynchronization.addIterator(it);
			return fc.getBounds();
		} catch (Throwable t) { // NOSONAR avoid errors (like NPE) as well
			throw new LayerException(t, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This implementation does not support the 'offset' and 'maxResultSize' parameters.
	 */
	@Transactional(readOnly = true)
	public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		try {
			if (source instanceof FeatureStore<?, ?>) {
				SimpleFeatureStore store = (SimpleFeatureStore) source;
				transactionSynchronization.synchTransaction(store);
			}
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures(filter);
			FeatureIterator<SimpleFeature> it = fc.features();
			transactionSynchronization.addIterator(it);
			return new JavaIterator(it);
		} catch (Throwable t) { // NOSONAR avoid errors (like NPE) as well
			throw new LayerException(t, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

	@Override
	public FeatureModel getFeatureModel() {
		if (!featureModelUsable) {
			retryInitFeatures();
		}
		return this.featureModel;
	}

	private boolean exists(String featureId) throws LayerException {
		Filter filter = filterService.createFidFilter(new String[] { featureId });
		Iterator<?> iterator = getElements(filter, 0, 0);
		return iterator.hasNext();
	}

	/**
	 * Adapter to java iterator.
	 * 
	 * @author Jan De Moerloose
	 */
	private static class JavaIterator implements Iterator<SimpleFeature> {

		private final FeatureIterator<SimpleFeature> delegate;

		public JavaIterator(FeatureIterator<SimpleFeature> delegate) {
			this.delegate = delegate;
		}

		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		@Override
		public SimpleFeature next() {
			return delegate.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Feature removal not supported, use delete(id) instead");
		}

	}

	@Override
	public DataStore getDataStore() {
		if (!featureModelUsable) {
			retryInitFeatures();
		}
		return super.getDataStore();
	}

	private void retryInitFeatures() {
		// do not hammer the service
		long now = System.currentTimeMillis();
		if (now > lastInitFeaturesRetry + cooldownTimeBetweenInitializationRetries) {
			lastInitFeaturesRetry = now;
			try {
				log.debug("Retrying to (re-)initialize layer {}", getId());
				initFeatures();
			} catch (Exception e) { // NOSONAR
				log.warn("Failed initializing layer: ", e.getMessage());
			}
		}
	}

	@Override
	public SimpleFeatureSource getFeatureSource() throws LayerException {
		if (!featureModelUsable) {
			retryInitFeatures();
		}
		return super.getFeatureSource();
	}

}