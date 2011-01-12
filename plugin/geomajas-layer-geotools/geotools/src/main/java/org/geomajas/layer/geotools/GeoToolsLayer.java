/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.shapeinmem.FeatureSourceRetriever;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Geotools layer model.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.7.1
 */
@Api
@Transactional(rollbackFor = { Exception.class })
public class GeoToolsLayer extends FeatureSourceRetriever implements VectorLayer {

	private final Logger log = LoggerFactory.getLogger(GeoToolsLayer.class);

	private FilterFactory filterFactory;

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	private String url;

	private String dbtype;

	private List<Parameter> parameters;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private GeoService geoService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired(required = false)
	private GeoToolsTransactionManager transactionManager;

	@Autowired
	private DtoConverterService converterService;

	private CoordinateReferenceSystem crs;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

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
		this.layerInfo = layerInfo;
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

	public void setDataStore(DataStore dataStore) throws LayerException {
		super.setDataStore(dataStore);
	}

	@PostConstruct
	public void initFeatures() throws LayerException {
		if (null == layerInfo) {
			return;
		}
		crs = geoService.getCrs2(layerInfo.getCrs());
		setFeatureSourceName(layerInfo.getFeatureInfo().getDataSourceName());
		try {
			if (null == getDataStore()) {
				Map<String, String> params = new HashMap<String, String>();
				if (null != url) {
					params.put("url", url);
				}
				if (null != dbtype) {
					params.put("dbtype", dbtype);
				}
				if (null != parameters) {
					for (Parameter parameter : parameters) {
						params.put(parameter.getName(), parameter.getValue());
					}
				}
				DataStore store = DataStoreFactory.create(params);
				setDataStore(store);
			}
			if (null == getDataStore()) {
				return;
			}
			this.filterFactory = CommonFactoryFinder.getFilterFactory(null);
			this.featureModel = new GeoToolsFeatureModel(getDataStore(),
					layerInfo.getFeatureInfo().getDataSourceName(), geoService.getSridFromCrs(layerInfo.getCrs()),
					converterService);
			featureModel.setLayerInfo(layerInfo);
		} catch (IOException ioe) {
			throw new LayerException(ExceptionCode.INVALID_SHAPE_FILE_URL, url);
		}
	}

	public Object create(Object feature) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store =
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			FeatureCollection<SimpleFeatureType, SimpleFeature> col = DataUtilities
					.collection(new SimpleFeature[] { (SimpleFeature) feature });
			if (transactionManager != null) {
				store.setTransaction(transactionManager.getTransaction());
			}
			try {
				store.addFeatures(col);
			} catch (IOException ioe) {
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
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
			if (transactionManager != null) {
				store.setTransaction(transactionManager.getTransaction());
			}
			List<AttributeDescriptor> descriptors = new ArrayList<AttributeDescriptor>();
			Map<String, Attribute> attrMap = getFeatureModel().getAttributes(feature);
			List<Object> values = new ArrayList<Object>();
			for (String name : attrMap.keySet()) {
				descriptors.add(store.getSchema().getDescriptor(name));
				values.add(attrMap.get(name).getValue());
			}

			try {
				store.modifyFeatures(descriptors.toArray(new AttributeDescriptor[descriptors.size()]),
						values.toArray(), filter);
				store.modifyFeatures(store.getSchema().getGeometryDescriptor(), getFeatureModel().getGeometry(feature),
						filter);
				log.debug("Updated feature {} in {}", filter.getIDs().iterator().next(), getFeatureSourceName());
			} catch (IOException ioe) {
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
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
			if (transactionManager != null) {
				store.setTransaction(transactionManager.getTransaction());
			}
			try {
				store.removeFeatures(filter);
				if (log.isDebugEnabled()) {
					log.debug("Deleted feature {} in {}", filter.getIDs().iterator().next(), getFeatureSourceName());
				}
			} catch (IOException ioe) {
				throw new LayerException(ioe, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
			}
		} else {
			log.error("Don't know how to delete from " + getFeatureSourceName() + ", class "
					+ source.getClass().getName() + " does not implement FeatureStore");
			throw new LayerException(ExceptionCode.DELETE_NOT_IMPLEMENTED, getFeatureSourceName(), source.getClass()
					.getName());
		}
	}

	public Object saveOrUpdate(Object feature) throws LayerException {
		if (exists(getFeatureModel().getId(feature))) {
			update(feature);
		} else {
			feature = create(feature);
		}
		return feature;
	}

	public Object read(String featureId) throws LayerException {
		Identifier identifier = new FeatureIdImpl(featureId);
		Id filter = filterFactory.id(Collections.singleton(identifier));
		Iterator<?> iterator = getElements(filter, 0, 0);
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
		}
	}

	public Envelope getBounds() throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			if (transactionManager != null) {
				store.setTransaction(transactionManager.getTransaction());
			}
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures();
			Iterator<SimpleFeature> it = fc.iterator();
			if (transactionManager != null) {
				transactionManager.addIterator(fc, it);
			}
			return fc.getBounds();
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

	/**
	 * Retrieve the bounds of the specified features.
	 * 
	 * @return the bounds of the specified features
	 */
	public Envelope getBounds(Filter filter) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			if (transactionManager != null) {
				store.setTransaction(transactionManager.getTransaction());
			}
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures(filter);
			Iterator<SimpleFeature> it = fc.iterator();
			if (transactionManager != null) {
				transactionManager.addIterator(fc, it);
			}
			return fc.getBounds();
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.LAYER_MODEL_IO_EXCEPTION);
		}
	}

	/**
	 * This implementation does not support the 'offset' and 'maxResultSize' parameters.
	 */
	public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
		FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource();
		if (source instanceof FeatureStore<?, ?>) {
			FeatureStore<SimpleFeatureType, SimpleFeature> store = 
				(FeatureStore<SimpleFeatureType, SimpleFeature>) source;
			if (transactionManager != null) {
				store.setTransaction(transactionManager.getTransaction());
			}
		}
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures(filter);
			Iterator<SimpleFeature> it = fc.iterator();
			if (transactionManager != null) {
				transactionManager.addIterator(fc, it);
			}
			return it;
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

	public FeatureModel getFeatureModel() {
		return this.featureModel;
	}

	private boolean exists(String featureId) throws LayerException {
		Identifier identifier = new FeatureIdImpl(featureId);
		Id filter = filterFactory.id(Collections.singleton(identifier));
		Iterator<?> iterator = getElements(filter, 0, 0);
		return iterator.hasNext();
	}

}