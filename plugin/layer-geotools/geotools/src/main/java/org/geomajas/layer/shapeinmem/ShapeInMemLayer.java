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

package org.geomajas.layer.shapeinmem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.annotation.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.geotools.DataStoreFactory;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Layer model for handling shape files in memory.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @since 1.7.1
 */
@Api
public class ShapeInMemLayer extends FeatureSourceRetriever implements VectorLayer {

	private final Map<String, SimpleFeature> features = new ConcurrentHashMap<String, SimpleFeature>();

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converterService;

	private CoordinateReferenceSystem crs;

	private String url;

	private String id;
	
	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * Set the id for this layer.
	 *
	 * @param id layer id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Set the url for the shape file.
	 *
	 * @param url shape file url
	 * @throws LayerException file cannot be accessed
	 * @since 1.7.1
	 */
	@Api
	public void setUrl(String url) throws LayerException {
		try {
			this.url = url;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("url", url);
			DataStore store = DataStoreFactory.create(params);
			setDataStore(store);
		} catch (IOException ioe) {
			throw new LayerException(ioe, ExceptionCode.INVALID_SHAPE_FILE_URL, url);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * This implementation does not support the 'offset' and 'maxResultSize' parameters.
	 */
	public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
		List<SimpleFeature> filteredList = new ArrayList<SimpleFeature>();
		for (SimpleFeature feature : features.values()) {
			if (filter.evaluate(feature)) {
				filteredList.add(feature);
				if (filteredList.size() == maxResultSize) {
					break;
				}
			}
		}
		return filteredList.iterator();
	}

	@Override
	public Envelope getBounds() throws LayerException {
		return getBounds(Filter.INCLUDE);
	}

	/**
	 * Retrieve the bounds of the specified features.
	 *
	 * @param filter filter
	 * @return the bounds of the specified features
	 * @throws LayerException cannot read features
	 */
	public Envelope getBounds(Filter filter) throws LayerException {
		try {
			FeatureCollection<SimpleFeatureType, SimpleFeature> fc = getFeatureSource().getFeatures(filter);
			return fc.getBounds();
		} catch (IOException ioe) {
			throw new LayerException(ioe, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	@Override
	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	@Override
	public Object create(Object feature) throws LayerException {
		String id = featureModel.getId(feature);
		if (id != null && !features.containsKey(id)) {
			SimpleFeature realFeature = asFeature(feature);
			features.put(id, realFeature);
			return realFeature;
		}
		return null;
	}

	@Override
	public Object read(String featureId) throws LayerException {
		if (!features.containsKey(featureId)) {
			throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
		} else {
			return features.get(featureId);
		}
	}

	@Override
	public Object saveOrUpdate(Object feature) throws LayerException {
		if (!features.containsKey(getFeatureModel().getId(feature))) {
			return create(feature);
		} else {
			// Nothing to do
			return feature;
		}
	}

	@Override
	public void delete(String featureId) throws LayerException {
		features.remove(featureId);
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Finish initializing the layer.
	 *
	 * @throws LayerException oops
	 */
	@PostConstruct
	protected void initFeatures() throws LayerException {
		crs = geoService.getCrs2(layerInfo.getCrs());
		try {
			setFeatureSourceName(layerInfo.getFeatureInfo().getDataSourceName());
			featureModel = new ShapeInMemFeatureModel(getDataStore(), layerInfo.getFeatureInfo().getDataSourceName(),
					geoService.getSridFromCrs(layerInfo.getCrs()), converterService);
			featureModel.setLayerInfo(layerInfo);
			FeatureCollection<SimpleFeatureType, SimpleFeature> col = getFeatureSource().getFeatures();
			FeatureIterator<SimpleFeature> iterator = col.features();
			int lastIndex = 0;
			while (iterator.hasNext()) {
				SimpleFeature feature = iterator.next();
				String id = featureModel.getId(feature);
				features.put(id, feature);
				int intId = Integer.parseInt(id.substring(id.lastIndexOf('.') + 1));
				if (intId > lastIndex) {
					lastIndex = intId;
				}
			}
			iterator.close();
			((ShapeInMemFeatureModel) featureModel).setNextId(++lastIndex);
		} catch (NumberFormatException nfe) {
			throw new LayerException(nfe, ExceptionCode.FEATURE_MODEL_PROBLEM, url);
		} catch (MalformedURLException e) {
			throw new LayerException(e, ExceptionCode.INVALID_SHAPE_FILE_URL, url);
		} catch (IOException ioe) {
			throw new LayerException(ioe, ExceptionCode.CANNOT_CREATE_LAYER_MODEL, url);
		} catch (GeomajasException ge) {
			throw new LayerException(ge, ExceptionCode.CANNOT_CREATE_LAYER_MODEL, url);
		}
	}
	
}