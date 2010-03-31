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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
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
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;

import javax.annotation.PostConstruct;

/**
 * Geotools layer model.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
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

	private void initCrs() throws LayerException {
		try {
			crs = CRS.decode(layerInfo.getCrs());
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_UNKNOWN_AUTHORITY, e, getId(),
					getLayerInfo().getCrs());
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_PROBLEMATIC, getId(),
					getLayerInfo().getCrs());
		}
	}

	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		initCrs();
		setFeatureSourceName(layerInfo.getFeatureInfo().getDataSourceName());
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
			this.featureModel = new GeoToolsFeatureModel(getDataStore(), layerInfo.getFeatureInfo().getDataSourceName(),
					geoService.getSridFromCrs(layerInfo.getCrs()), converterService);
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
			List<AttributeDescriptor> descriptors = store.getSchema().getAttributeDescriptors();
			Map<String, Attribute> attrMap = getFeatureModel().getAttributes(feature);
			List<Attribute> attrList = new ArrayList<Attribute>();
			for (int i = 0; i < descriptors.size(); i++) {
				Attribute value = attrMap.get(descriptors.get(i).getName().toString());
				attrList.add(value);
			}

			try {
				store.modifyFeatures(descriptors.toArray(new AttributeDescriptor[descriptors.size()]), attrList
						.toArray(), filter);
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
		Iterator<?> iterator = getElements(filter, 0, 0);
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
}