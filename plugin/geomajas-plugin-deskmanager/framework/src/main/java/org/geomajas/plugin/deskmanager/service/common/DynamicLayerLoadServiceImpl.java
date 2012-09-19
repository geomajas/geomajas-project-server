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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;
import org.geomajas.plugin.deskmanager.configuration.client.ExtraClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService.NamedObject;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionWriterService;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionWriterServiceImpl;
import org.geomajas.plugin.runtimeconfig.service.BeanFactoryService;
import org.geomajas.plugin.runtimeconfig.service.ContextConfiguratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Component
@Transactional(readOnly = true)
public class DynamicLayerLoadServiceImpl implements DynamicLayerLoadService {

	private final Logger log = LoggerFactory.getLogger(DynamicLayerLoadServiceImpl.class);

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private DiscoveryService discoService;

	@Autowired(required = false)
	private Map<String, ClientLayerInfo> layerMap = new LinkedHashMap<String, ClientLayerInfo>();

	@Autowired(required = false)
	private Map<String, Layer<LayerInfo>> serverLayerMap = new LinkedHashMap<String, Layer<LayerInfo>>();

	@Autowired
	private ContextConfiguratorService configService;

	@Autowired
	private BeanDefinitionDtoConverterService converterService;

	@Autowired
	private BeanFactoryService beanFactoryService;

	@Resource(name = "postGisDatastoreParams")
	private Map<String, String> postgisDataStoreParams;

	@Autowired
	private BeanDefinitionWriterService bser;

	@PostConstruct
	public void onApplicationStart() {
		updateSystemLayers();
		loadDynamicLayers();
	}

	// -------------------------------------------------

	private void updateSystemLayers() {
		log.info("Updating systemlayers");
		try {
			List<ClientLayerInfo> layers = getSystemLayers();
			Map<String, LayerModel> models = toMap(layerModelService.getLayerModelsInternal());
			for (ClientLayerInfo cli : layers) {
				if (!models.containsKey(cli.getId())) {
					try {
						LayerModel lm = toLayerModel(cli);
						layerModelService.saveOrUpdateLayerModelInternal(lm);
						log.info(" - added a new layermodel for: " + cli.getLabel());
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

	public void loadDynamicLayers() {
		log.info("Loading dynamic layers");
		try {
			// -- clientside / namedstyleinfo --
			List<BeanDefinitionHolder> holders = new ArrayList<BeanDefinitionHolder>();
			List<NamedObject> objects = new ArrayList<NamedObject>();
			List<String> clientLayerIds = new ArrayList<String>();
			for (LayerModel lm : layerModelService.getDynamicLayerModelsInternal()) {
				log.info(" - creating a dynamic layer for: " + lm.getClientLayerId());
				clientLayerIds.add(lm.getClientLayerId());

				updateLayerProperties(lm.getLayerConfiguration());
				objects.addAll(getClientLayerInfoObject(lm.getLayerConfiguration()));

				// -- serverside layer bean has to be processed separately --
				Map<String, Object> params = discoService.createBeanLayerDefinitionParameters(lm
						.getLayerConfiguration());
				holders.addAll(beanFactoryService.createBeans(params));
			}

			holders.addAll(converterService.createBeanDefinitionsByIntrospection(objects));
			activateBeans(holders, clientLayerIds);

			log.info(" - finished loading dynamic layers into context.");
		} catch (Exception e) {
			log.warn("Error loading dynamic layers: " + e.getMessage());
		}
	}

	// ---- Load -----------------------------------------------

	/**
	 * replaces params if necessary, removes styleinfos (use names)
	 */
	private void updateLayerProperties(LayerConfiguration lc) throws Exception {
		if (lc == null) {
			throw new IllegalArgumentException("Need a LayerConfiguration");
		}

		if (LayerConfiguration.SOURCE_TYPE_SHAPE.equals(lc.getParameter(LayerConfiguration.PARAM_SOURCE_TYPE)
				.getValue())) {
			lc.getParameters().clear();

			// inject private properties for shapelayers
			for (Entry<String, String> entry : postgisDataStoreParams.entrySet()) {
				Parameter p = new Parameter();
				p.setName(entry.getKey());
				p.setValue(entry.getValue());
				lc.getParameters().add(p);
			}
		}
	}

	private List<NamedObject> getClientLayerInfoObject(LayerConfiguration lc) throws Exception {
		if (lc == null) {
			throw new IllegalArgumentException("Need a LayerConfiguration");
		}
		List<NamedObject> objects = new ArrayList<BeanDefinitionDtoConverterService.NamedObject>();

		if (lc.getServerLayerInfo() instanceof VectorLayerInfo) {
			NamedStyleInfo nsi = ((VectorLayerInfo) lc.getServerLayerInfo()).getNamedStyleInfos().get(0);
			objects.add(new NamedObjectImpl(nsi, nsi.getName()));
		}

		objects.add(new NamedObjectImpl(lc.getClientLayerInfo(), lc.getClientLayerInfo().getId()));

		return objects;
	}

	private void activateBeans(List<BeanDefinitionHolder> defs, List<String> clientLayerIds) throws Exception {
		if (defs.size() > 0) {
			if (log.isDebugEnabled()) {
				// -- write config out to XML --
				((BeanDefinitionWriterServiceImpl) bser).setBaseResource(new FileSystemResource("."));
				bser.persist("_dynamicConfig_", defs);
			}

			configService.configureBeanDefinitions(defs);
		}
	}

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private static class NamedObjectImpl implements NamedObject {

		private final Object o;

		private final String name;

		public NamedObjectImpl(Object o, String name) {
			this.o = o;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Object getObject() {
			return o;
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
		ExtraClientLayerInfo ecli = layerModelService.getExtraInfo(cli);

		//Get layerInfo via server layer
		LayerInfo sli = null;
		Layer<LayerInfo> layer = serverLayerMap.get(cli.getServerLayerId());
		if (null != layer) {
			sli = layer.getLayerInfo();
		}
		
		lm.setActive(ecli.isActive());
		lm.setClientLayerId(cli.getId());
		lm.setName(ecli.getName() == null ? cli.getLabel() : ecli.getName());
		lm.setPublic(ecli.isPublicLayer());
		lm.setMinScale(cli.getMinimumScale());
		lm.setMaxScale(cli.getMaximumScale());
		lm.setDefaultVisible(cli.isVisible());
		lm.setShowInLegend(ecli.isShowInLegend());
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

	/**
	 * filter internal layers when necessary (mask/overview layers)
	 */
	private List<ClientLayerInfo> getSystemLayers() {
		if (layerMap == null || layerMap.size() == 0) {
			return new ArrayList<ClientLayerInfo>();
		}
		List<ClientLayerInfo> res = new ArrayList<ClientLayerInfo>();

		for (ClientLayerInfo cli : layerMap.values()) {
			if (!layerModelService.getExtraInfo(cli).isSystemLayer()) {
				res.add(cli);
			}
		}
		return res;
	}
}
