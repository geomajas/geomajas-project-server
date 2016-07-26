/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.bean;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityAttributeService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
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

	private final Class<?> beanClass;

	private final int srid;

	private VectorLayerInfo vectorLayerInfo;

	private final boolean wkt;

	private final EntityAttributeService entityMappingService;
	
	private final BeanEntityMapper entityMapper;

	public BeanFeatureModel(VectorLayerInfo vectorLayerInfo, int srid, EntityAttributeService entityMappingService)
			throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
		this.entityMappingService = entityMappingService;

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
		for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
			addAttribute(null, info);
		}
		entityMapper = new BeanEntityMapper();
	}

	private void addAttribute(String prefix, AbstractAttributeInfo info) {
		String name = info.getName();
		if (null != prefix) {
			name = prefix + SEPARATOR + name;
		}
		if (info instanceof AssociationAttributeInfo) {
			FeatureInfo association = ((AssociationAttributeInfo) info).getFeature();
			for (AbstractAttributeInfo assInfo : association.getAttributes()) {
				addAttribute(name, assInfo);
			}
		}
	}

	public boolean canHandle(Object feature) {
		if (feature instanceof FeatureModelAware) {
			FeatureModelAware fma = (FeatureModelAware) feature;
			return (fma.getFeatureModel() == this);
		} else {
			return beanClass.isInstance(feature);
		}
	}

	public Attribute getAttribute(Object feature, String name) throws LayerException {
		try {
			return entityMappingService.getAttribute(feature, getFeatureInfo(), entityMapper, name);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		try {
			Map<String, Attribute> attribs = new HashMap<String, Attribute>();
			for (AbstractAttributeInfo attribute : getFeatureInfo().getAttributes()) {
				String name = attribute.getName();
				if (!name.equals(getGeometryAttributeName())) {
					Attribute value = this.getAttribute(feature, name);
					attribs.put(name, value);
				}
			}
			return attribs;
		} catch (Exception e) { // NOSONAR
			throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Entity entity = entityMapper.asEntity(feature);
		Object geometry = entity.getAttribute(getGeometryAttributeName());
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
		Entity entity = entityMapper.asEntity(feature);
		Object id = entity.getId(getFeatureInfo().getIdentifier().getName());
		return id == null ? null : id.toString();
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	public Object newInstance() throws LayerException {
		try {
			Object o = beanClass.newInstance();
			if (o instanceof FeatureModelAware) {
				((FeatureModelAware) o).setFeatureModel(this);
			}
			return o;
		} catch (Throwable t) {
			throw new LayerException(t, ExceptionCode.FEATURE_MODEL_PROBLEM);
		}
	}

	public Object newInstance(String id) throws LayerException {
		try {
			Object instance = newInstance();
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
		Entity entity = entityMapper.asEntity(instance);
		entity.setAttribute(getFeatureInfo().getIdentifier().getName(), value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		entityMappingService.setAttributes(feature, getFeatureInfo(), entityMapper, (Map) attributes);
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		Entity entity = entityMapper.asEntity(feature);
		if (wkt) {
			WKTWriter writer = new WKTWriter();
			String wktStr = null;
			if (null != geometry) {
				wktStr = writer.write(geometry);
			}
			entity.setAttribute(getGeometryAttributeName(), wktStr);
		} else {
			entity.setAttribute(getGeometryAttributeName(), geometry);
		}
	}

	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		this.vectorLayerInfo = vectorLayerInfo;
	}

	public FeatureInfo getFeatureInfo() {
		return vectorLayerInfo.getFeatureInfo();
	}
	
}
