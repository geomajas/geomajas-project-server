/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import java.util.List;
import java.util.Map.Entry;

import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.GeoService;
import org.geomajas.service.LayerInvalidationService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Container class which contains runtime information about the parameters and other information for Geomajas. Values
 * are injected using Spring.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class ConfigurationServiceImpl implements ConfigurationService {

	private final Logger log = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

	@Autowired(required = false)
	private List<LayerInvalidationService> layerInvalidationServices;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private GeoService geoService;

	/** {@inheritDoc} */
	public VectorLayer getVectorLayer(String id) {
		return getBeanOrNull(id, VectorLayer.class);
	}

	/** {@inheritDoc} */
	public RasterLayer getRasterLayer(String id) {
		return getBeanOrNull(id, RasterLayer.class);
	}

	/** {@inheritDoc} */
	public Layer<?> getLayer(String id) {
		return getBeanOrNull(id, Layer.class);
	}

	/** {@inheritDoc} */
	public ClientMapInfo getMap(String mapId, String applicationId) {
		ClientApplicationInfo application = getBeanOrNull(applicationId, ClientApplicationInfo.class);
		if (application != null) {
			for (ClientMapInfo map : application.getMaps()) {
				if (mapId.equals(map.getId())) {
					return map;
				}
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	public CoordinateReferenceSystem getCrs(String crs) throws LayerException {
		return geoService.getCrs2(crs);
	}

	/** {@inheritDoc} */
	public void invalidateLayer(String layerId) throws GeomajasException {
		Layer layer = null;
		if (null != layerId) {
			layer = getLayer(layerId);
		}
		invalidateLayer(layer);
	}
	
	/**
	 * Get a bean from the application context. Returns null if the bean does not exist.
	 * @param name name of bean
	 * @param requiredType type of bean
	 * @return the bean or null
	 */
	private <T> T getBeanOrNull(String name, Class<T> requiredType) {
		if (name == null || !applicationContext.containsBean(name)) {
			return null;
		} else {
			try {
				return applicationContext.getBean(name, requiredType);
			} catch (BeansException be) {
				log.error("Error during getBeanOrNull, not rethrown, " + be.getMessage(), be);
				return null;
			}
		}
	}

	private void invalidateLayer(Layer layer) {
		if (null != layerInvalidationServices) {
			for (LayerInvalidationService service : layerInvalidationServices) {
				try {
					service.invalidateLayer(layer);
					if (null != layer) {
						service.invalidateLayer(null);
					}
				} catch (GeomajasException ge) {
					log.error("Error during invalidateLayer, not rethrown, " + ge.getMessage(), ge);
				}
			}
		}
	}

	/** {@inheritDoc} */
	public void invalidateAllLayers() throws GeomajasException {
		for (Entry<String, Layer> entry : applicationContext.getBeansOfType(Layer.class).entrySet()) {
			invalidateLayer(entry.getValue());
		}
	}

	/** {@inheritDoc} */
	public <TYPE extends LayerExtraInfo> TYPE getLayerExtraInfo(LayerInfo layerInfo, Class<TYPE> type) {
		return getLayerExtraInfo(layerInfo, type.getName(), type);
	}

	/** {@inheritDoc} */
	public <TYPE extends LayerExtraInfo> TYPE getLayerExtraInfo(LayerInfo layerInfo, String key, Class<TYPE> type) {
		Object obj = layerInfo.getExtraInfo().get(key);
		if (null != obj && type.isAssignableFrom(obj.getClass())) {
			return (TYPE) obj;
		}
		return null;
	}
	
}
