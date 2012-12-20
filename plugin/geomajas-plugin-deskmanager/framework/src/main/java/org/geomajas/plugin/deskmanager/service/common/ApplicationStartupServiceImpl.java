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
package org.geomajas.plugin.deskmanager.service.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Component
@Transactional
public class ApplicationStartupServiceImpl implements ApplicationStartupService {

	private final Logger log = LoggerFactory.getLogger(ApplicationStartupServiceImpl.class);

	@Autowired
	private LayerModelService layerModelService;

	@Autowired(required = false)
	private Map<String, ClientLayerInfo> layerMap = new LinkedHashMap<String, ClientLayerInfo>();

	@Autowired(required = false)
	private Map<String, Layer<LayerInfo>> serverLayerMap = new LinkedHashMap<String, Layer<LayerInfo>>();

	public void onStartup() {
		updateSystemLayers();
	}

	// -------------------------------------------------

	private void updateSystemLayers() {
		log.info("Updating systemlayers");
		try {
			Map<String, LayerModel> models = toMap(layerModelService.getLayerModelsInternal());
			for (ClientLayerInfo cli : layerMap.values()) {
				if (!models.containsKey(cli.getId())) {
					try {
						LayerModel lm = toLayerModel(cli);
						layerModelService.saveOrUpdateLayerModelInternal(lm);
						log.info(" - updated layermodel for: " + cli.getLabel());
					} catch (Exception e) {
						log.error("Error creating layer, invalid configuration (service not available?): "
								+ cli.getLabel());
					}
				}
			}
		} catch (Exception e) {
			log.warn("Error updating system layers: " + e.getMessage());
		}
	}

	// ---- Update ---------------------------------------------

	private Map<String, LayerModel> toMap(List<LayerModel> models) {
		Map<String, LayerModel> map = new HashMap<String, LayerModel>();
		for (LayerModel layerModel : models) {
			map.put(layerModel.getClientLayerId(), layerModel);
		}
		return map;
	}

	private LayerModel toLayerModel(ClientLayerInfo cli) {
		LayerModel lm = new LayerModel();
		org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo ecli = layerModelService.getExtraInfo(cli);

		// Get layerInfo via server layer
		LayerInfo sli = null;
		Layer<LayerInfo> layer = serverLayerMap.get(cli.getServerLayerId());
		if (null != layer) {
			sli = layer.getLayerInfo();
		}

		lm.setActive(ecli.isActive());
		lm.setClientLayerId(cli.getId());
		lm.setName(ecli.getName() == null ? cli.getLabel() : ecli.getName());
		lm.setPublic(ecli.isPublic());
		lm.setMinScale(cli.getMinimumScale());
		lm.setMaxScale(cli.getMaximumScale());
		lm.setDefaultVisible(cli.isVisible());
		lm.setReadOnly(true);

		if (null != layer && LayerType.RASTER.equals(sli.getLayerType())) {
			lm.setLayerType("Raster");
			lm.setLayerConfiguration(new RasterLayerConfiguration());
		} else if (null != layer) {
			lm.setLayerType(sli.getLayerType().getGeometryType());
			lm.setLayerConfiguration(new VectorLayerConfiguration());
		}
		return lm;
	}
}
