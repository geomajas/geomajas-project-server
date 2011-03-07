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

package org.geomajas.layer.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.SortType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A simple Java beans based layer model.
 * 
 * @author Jan De Moerloose
 */
public class BeanLayer implements VectorLayer, VectorLayerAssociationSupport {

	private final Logger log = LoggerFactory.getLogger(BeanLayer.class);

	// all access to this variable needs to be synchronized
	private final Map<String, Object> featuresById = new LinkedHashMap<String, Object>();

	/**
	 * The features (should be Java beans compliant)
	 */
	private List<Object> features = new ArrayList<Object>();

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	@Autowired
	private FilterService filterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converterService;

	private CoordinateReferenceSystem crs;

	protected Comparator<Object> comparator;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
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

	/**
	 * This implementation does not support the 'offset' and 'maxResultSize' parameters.
	 */
	public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
		List<Object> filteredList = new ArrayList<Object>();
		try {
			synchronized (featuresById) {
				for (Object feature : featuresById.values()) {
					if (filter.evaluate(feature)) {
						filteredList.add(feature);
					}
				}
			}
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.FILTER_EVALUATION_PROBLEM, filter, getId());
		}
		// Sorting of elements.
		if (comparator != null) {
			Collections.sort(filteredList, comparator);
		}
		if (maxResultSize > 0) {
			int fromIndex = Math.max(0, offset);
			int toIndex = Math.min(offset + maxResultSize, filteredList.size());
			toIndex = Math.max(fromIndex, toIndex);
			return filteredList.subList(fromIndex, toIndex).iterator();
		} else {
			return filteredList.iterator();
		}
	}

	public Envelope getBounds() throws LayerException {
		return getBounds(Filter.INCLUDE);
	}

	/**
	 * Retrieve the bounds of the specified features.
	 * 
	 * @return the bounds of the specified features
	 */
	public Envelope getBounds(Filter queryFilter) throws LayerException {
		Iterator<?> it = getElements(queryFilter, 0, 0);
		// start with null envelope
		Envelope bounds = new Envelope();
		while (it.hasNext()) {
			Object o = it.next();
			Geometry g = featureModel.getGeometry(o);
			bounds.expandToInclude(g.getEnvelopeInternal());
		}
		return bounds;
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		crs = geoService.getCrs2(layerInfo.getCrs());
		initFeatureModel();
		initComparator();
	}

	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public Object create(Object feature) throws LayerException {
		String id = featureModel.getId(feature);
		synchronized (featuresById) {
			if (id != null && !featuresById.containsKey(id)) {
				features.add(feature);
				featuresById.put(id, feature);
				return feature;
			} else {
				throw new IllegalStateException("BeanLayer cannot auto assign the feature id");
			}
		}
	}

	public Object read(String featureId) throws LayerException {
		synchronized (featuresById) {
			if (!featuresById.containsKey(featureId)) {
				throw new LayerException(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, featureId);
			} else {
				return featuresById.get(featureId);
			}
		}
	}

	public Object saveOrUpdate(Object feature) throws LayerException {
		synchronized (featuresById) {
			if (!featuresById.containsKey(getFeatureModel().getId(feature))) {
				return create(feature);
			} else {
				// Nothing to do
				return feature;
			}
		}
	}

	public void delete(String featureId) throws LayerException {
		synchronized (featuresById) {
			Object o = featuresById.remove(featureId);
			if (null != o) {
				features.remove(o);
			}
		}
	}

	public List<Object> getFeatures() {
		return features;
	}

	public synchronized void setFeatures(List<Object> features) throws LayerException {
		if (null != features) {
			this.features.addAll(features);
			if (null != featureModel) {
				synchronized (featuresById) {
					for (Object f : features) {
						featuresById.put(featureModel.getId(f), f);
					}
				}
			}
		}
	}

	public List<Attribute<?>> getAttributes(String attributeName, Filter filter) throws LayerException {
		log.debug("creating iterator for attribute {} and filter: {}", attributeName, filter);
		AttributeInfo attributeInfo = null;
		for (AttributeInfo info : getFeatureInfo().getAttributes()) {
			if (info.getName().equals(attributeName)) {
				attributeInfo = info;
				break;
			}
		}

		List<?> values = null;
		if (attributeInfo instanceof AssociationAttributeInfo) {
			AssociationAttributeInfo associationInfo = (AssociationAttributeInfo) attributeInfo;
			if (associationInfo.getType().equals(AssociationType.MANY_TO_ONE)) {
				values = ManyToOneAttributeBean.manyToOneValues();
			}
		}

		List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();
		for (Object object : values) {
			try {
				attributes.add(converterService.toDto(object, attributeInfo));
			} catch (GeomajasException e) {
				throw new LayerException(ExceptionCode.CONVERSION_PROBLEM, attributeName);
			}
		}
		return attributes;
	}

	protected FeatureInfo getFeatureInfo() {
		return layerInfo.getFeatureInfo();
	}

	protected synchronized void initFeatureModel() throws LayerException {
		featureModel = new BeanFeatureModel(layerInfo, geoService.getSridFromCrs(layerInfo.getCrs()), converterService);
		filterService.registerFeatureModel(featureModel);
		synchronized (featuresById) {
			for (Object f : features) {
				featuresById.put(featureModel.getId(f), f);
			}
		}
	}

	protected void initComparator() throws LayerException {
		SortType sortType = getFeatureInfo().getSortType();
		String name = getFeatureInfo().getSortAttributeName();
		if (null == name) {
			comparator = null;
		} else {
			comparator = new FeatureComparator(name, sortType);
		}
	}

	/**
	 * Compares features by a single attribute.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class FeatureComparator implements Comparator<Object> {

		private String attributeName;

		private SortType type;

		public FeatureComparator(String attributeName, SortType type) {
			this.attributeName = attributeName;
			this.type = type;
		}

		@SuppressWarnings("unchecked")
		public int compare(Object f1, Object f2) {
			try {
				Comparable attr1 = (Comparable) getFeatureModel().getAttribute(f1, attributeName).getValue();
				Comparable attr2 = (Comparable) getFeatureModel().getAttribute(f2, attributeName).getValue();
				switch (type) {
					case ASC:
						return attr1.compareTo(attr2);
					case DESC:
						return attr2.compareTo(attr1);
				}
			} catch (Throwable t) {
				// can't throw !
				log.warn("Can't compare " + getFeatureInfo().getDataSourceName() + " features for attribute "
						+ attributeName + ", exception " + t.getMessage(), t);
			}
			return 0;
		}
	}
}
