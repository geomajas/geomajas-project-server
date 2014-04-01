/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the dynamiclayerloadservice.
 *
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

	@Autowired
	private ContextConfiguratorService configService;

	@Autowired
	private BeanDefinitionDtoConverterService converterService;

	@Autowired
	private BeanFactoryService beanFactoryService;

	@Resource(name = "postGisDatastoreParams")
	private Map<String, String> postgisDataStoreParams;

	@Resource(name = "dynamicLayersApplication")
	private ClientApplicationInfo applicationInfo;

	@Autowired
	private BeanDefinitionWriterService bser;

	// -------------------------------------------------

	public void loadDynamicLayers() {
		log.info("Loading dynamic layers");

		try {
			// -- clientside / namedstyleinfo --
			List<BeanDefinitionHolder> holders = new ArrayList<BeanDefinitionHolder>();
			List<NamedObject> objects = new ArrayList<NamedObject>();
			List<String> clientLayerIds = new ArrayList<String>();
			for (LayerModel lm : layerModelService.getDynamicLayerModelsInternal()) {
				log.info(" - creating a dynamic layer for: " + lm.getName());
				try {
					clientLayerIds.add(lm.getClientLayerId());

					//Clone layer configuration so that it doesn't get overwritten in the database.
					DynamicLayerConfiguration clonedLayerConfiguration =
							(DynamicLayerConfiguration) SerializationUtils.clone(lm.getDynamicLayerConfiguration());

					updateLayerProperties(clonedLayerConfiguration);
					objects.addAll(getClientLayerInfoObject(clonedLayerConfiguration));

					// -- serverside layer bean has to be processed separately --
					Map<String, Object> params = discoService.
							createBeanLayerDefinitionParameters(clonedLayerConfiguration);
					holders.addAll(beanFactoryService.createBeans(params));

					// Add layer to the dynamicLayersApplication for dto postprocessing
					applicationInfo.getMaps().get(0).getLayers()
							.add(clonedLayerConfiguration.getClientLayerInfo());
				} catch (Exception e) {
					log.warn("Error loading dynamic layers: " + e.getMessage());
				}
			}

			NamedObjectImpl applicationInfoNamedObject = new NamedObjectImpl(applicationInfo,
					"dynamicLayersApplication");
			objects.add(applicationInfoNamedObject);

			holders.addAll(converterService.createBeanDefinitionsByIntrospection(objects));
			activateBeans(holders, clientLayerIds);

			log.info(" - finished loading dynamic layers into context.");
		} catch (Exception e) {
			log.warn("Error activating dynamic layers: " + e.getMessage());
		}
	}

	// ---- Load -----------------------------------------------

	/**
	 * replaces params if necessary, removes styleinfos (use names)
	 */
	private void updateLayerProperties(DynamicLayerConfiguration lc) throws Exception {
		if (lc == null) {
			throw new IllegalArgumentException("Need a DynamicLayerConfiguration");
		}

		if (lc.getParameter(DynamicLayerConfiguration.PARAM_SOURCE_TYPE) != null
				&& DynamicLayerConfiguration.SOURCE_TYPE_SHAPE.equals(lc.getParameter(
						DynamicLayerConfiguration.PARAM_SOURCE_TYPE).getValue())) {

			// inject private properties for shapelayers
			// this is only used for dbtype and namespace
			for (Map.Entry<String, String> entry : postgisDataStoreParams.entrySet()) {
				Parameter p = new Parameter();
				p.setName(entry.getKey());
				p.setValue(entry.getValue());
				lc.getParameters().add(p);
			}
		}
	}

	private List<NamedObject> getClientLayerInfoObject(DynamicLayerConfiguration lc) throws Exception {
		if (lc == null) {
			throw new IllegalArgumentException("Need a DynamicLayerConfiguration");
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

}
