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

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.bean.BeanEntityMapper.BeanEntity;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * A simple Java beans based feature model.
 * 
 * @author Jan De Moerloose
 */
public class BeanFeatureModel implements FeatureModel {

	private final Logger log = LoggerFactory.getLogger(BeanFeatureModel.class);

	public static final String XPATH_SEPARATOR = "/";

	public static final String SEPARATOR = ".";

	public static final String SEPARATOR_REGEXP = "\\.";

	private Class<?> beanClass;

	private int srid;

	private VectorLayerInfo vectorLayerInfo;

	private boolean wkt;

	private DtoConverterService converterService;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	private BeanEntityMapper mapper;

	public BeanFeatureModel(VectorLayerInfo vectorLayerInfo, int srid, DtoConverterService converterService)
			throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
		this.converterService = converterService;

		try {
			beanClass = Class.forName(vectorLayerInfo.getFeatureInfo().getDataSourceName());
		} catch (ClassNotFoundException e) {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature class "
					+ vectorLayerInfo.getFeatureInfo().getDataSourceName() + " was not found", e);
		}
		this.srid = srid;
		PropertyDescriptor d = BeanUtils.getPropertyDescriptor(beanClass, getGeometryAttributeName());
		Class geometryClass = d.getPropertyType();
		if (Geometry.class.isAssignableFrom(geometryClass)) {
			wkt = false;
		} else if (geometryClass == String.class) {
			wkt = true;
		} else {
			throw new LayerException(ExceptionCode.FEATURE_MODEL_PROBLEM, "Feature "
					+ vectorLayerInfo.getFeatureInfo().getDataSourceName() + " has no valid geometry attribute");
		}

		FeatureInfo featureInfo = vectorLayerInfo.getFeatureInfo();
		attributeInfoMap.put(featureInfo.getIdentifier().getName(), featureInfo.getIdentifier());
		for (AttributeInfo info : featureInfo.getAttributes()) {
			addAttribute(null, info);
		}
		mapper = new BeanEntityMapper(featureInfo);
	}

	private void addAttribute(String prefix, AttributeInfo info) {
		String name = info.getName();
		if (null != prefix) {
			name = prefix + SEPARATOR + name;
		}
		attributeInfoMap.put(name, info);
		if (info instanceof AssociationAttributeInfo) {
			FeatureInfo association = ((AssociationAttributeInfo) info).getFeature();
			attributeInfoMap.put(name + SEPARATOR + association.getIdentifier().getName(), association.getIdentifier());
			for (AttributeInfo assInfo : association.getAttributes()) {
				addAttribute(name, assInfo);
			}
		}
	}

	public boolean canHandle(Object feature) {
		return beanClass.isInstance(feature);
	}

	public Attribute getAttribute(Object feature, String name) throws LayerException {
		Object attr = getAttributeRecursively(feature, name);
		name = name.replace(XPATH_SEPARATOR, SEPARATOR);
		AttributeInfo attributeInfo = attributeInfoMap.get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name);
		}
		try {
			return converterService.toDto(attr, attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		try {
			Map<String, Attribute> attribs = new HashMap<String, Attribute>();
			for (AttributeInfo attribute : getFeatureInfo().getAttributes()) {
				String name = attribute.getName();
				if (!name.equals(getGeometryAttributeName())) {
					Attribute value = this.getAttribute(feature, name);
					attribs.put(name, value);
				}
			}
			return attribs;
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Object geometry = getAttributeRecursively(feature, getGeometryAttributeName());
		if (!wkt || null == geometry) {
			log.debug("bean.getGeometry {}", geometry);
			return (Geometry) geometry;
		} else {
			try {
				WKTReader reader = new WKTReader(new GeometryFactory(new PrecisionModel(), srid));
				Geometry geom = reader.read((String) geometry);
				log.debug("bean.getGeometry {}", geom);
				return geom;
			} catch (Throwable t) {
				throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM, geometry);
			}
		}
	}

	public String getGeometryAttributeName() throws LayerException {
		return getFeatureInfo().getGeometryType().getName();
	}

	public String getId(Object feature) throws LayerException {
		Entity entity = mapper.asEntity(feature);
		Object id = entity.getId(getFeatureInfo().getIdentifier().getName());
		return id == null ? null : id.toString();
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	public Object newInstance() throws LayerException {
		try {
			return beanClass.newInstance();
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Object newInstance(String id) throws LayerException {
		try {
			Object instance = beanClass.newInstance();
			setId(instance, id);
			return instance;
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public void setId(Object instance, String id) throws LayerException {
		PrimitiveAttributeInfo pai = vectorLayerInfo.getFeatureInfo().getIdentifier();
		Object value;
		switch (pai.getType()) {
			case LONG:
				value = Long.parseLong(id);
				break;
			case STRING:
				value = id;
				break;
			default:
				throw new IllegalStateException("BeanFeatureModel only accepts String and long ids.");
		}
		Entity entity = mapper.asEntity(instance);
		entity.setPrimitiveAttribute(getFeatureInfo().getIdentifier().getName(), value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		mapper.mergeEntity(feature, (Map) attributes);
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		Entity entity = mapper.asEntity(feature);
		if (wkt) {
			WKTWriter writer = new WKTWriter();
			String wktStr = null;
			if (null != geometry) {
				wktStr = writer.write(geometry);
			}
			entity.setPrimitiveAttribute(getGeometryAttributeName(), wktStr);
		} else {
			entity.setPrimitiveAttribute(getGeometryAttributeName(), geometry);
		}
	}

	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
	}

	public FeatureInfo getFeatureInfo() {
		return vectorLayerInfo.getFeatureInfo();
	}

	/**
	 * A recursive getAttribute method. In case a one-to-many is passed, an array will be returned.
	 * 
	 * @param feature The feature wherein to search for the attribute
	 * @param name The attribute's full name. (can be attr1.attr2)
	 * @return Returns the value. In case a one-to-many is passed along the way, an array will be returned.
	 * @throws LayerException oops
	 */
	private Object getAttributeRecursively(Object feature, String name) throws LayerException {
		if (feature == null) {
			return null;
		}
		// Split up properties: the first and the rest.
		name = name.replace(XPATH_SEPARATOR, SEPARATOR);
		String[] properties = name.split(SEPARATOR_REGEXP, 2);

		// Get the first property:
		Entity entity = mapper.asEntity(feature);
		BeanEntity child = (BeanEntity) entity.getChild(properties[0]);
		Object tempFeature = child == null ? null : child.getBean();

		// Detect if the first property is a collection (one-to-many):
		if (tempFeature instanceof Collection<?>) {
			Collection<?> features = (Collection<?>) tempFeature;
			Object[] values = new Object[features.size()];
			int count = 0;
			for (Object value : features) {
				if (properties.length == 1) {
					values[count++] = value;
				} else {
					values[count++] = getAttributeRecursively(value, properties[1]);
				}
			}
			return values;
		} else { // Else first property is not a collection (one-to-many):
			if (properties.length == 1 || tempFeature == null) {
				return tempFeature;
			} else {
				return getAttributeRecursively(tempFeature, properties[1]);
			}
		}
	}

}
