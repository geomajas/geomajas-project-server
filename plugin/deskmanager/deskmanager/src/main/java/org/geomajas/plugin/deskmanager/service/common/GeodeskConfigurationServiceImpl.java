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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.command.common.GetMapConfigurationCommand;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.ClientLayer;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.runtimeconfig.service.Rewirable;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.StyleService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Component
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GeodeskConfigurationServiceImpl implements GeodeskConfigurationService, Rewirable {

	private final Logger log = LoggerFactory.getLogger(GeodeskConfigurationServiceImpl.class);

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private Map<String, UserApplicationInfo> userApplications;

	@Autowired
	private DtoConverterService convertorService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private GetMapConfigurationCommand mapConfigurationCommand;

	@Autowired
	private StyleService styleService;

	@Autowired
	private SessionFactory session;

	// -------------------------------------------------

	private UserApplicationInfo getUserApplicationInfo(String key) {
		for (UserApplicationInfo uai : userApplications.values()) {
			if (uai.getKey().equals(key)) {
				return uai;
			}
		}
		return null;
	}

	public ClientApplicationInfo createGeodeskConfiguration(Geodesk geodesk, boolean includeMaps) {
		try {
			geodesk = (Geodesk) session.getCurrentSession().merge(geodesk);
			UserApplicationInfo userApplicationInfo = getUserApplicationInfo(geodesk.getBlueprint()
					.getUserApplicationKey());

			ClientApplicationInfo appInfo = (ClientApplicationInfo) SerializationUtils.clone(userApplicationInfo
					.getApplicationInfo());
			BaseGeodesk blueprint = blueprintService.getBlueprintByIdInternal(geodesk.getBlueprint().getId());
			if (includeMaps) {
				ClientMapInfo mainMap = null;
				ClientMapInfo overviewMap = null;
				for (ClientMapInfo map : appInfo.getMaps()) {
					if (GdmLayout.MAPMAIN_ID.equals(map.getId())) {
						mainMap = map;
					}
					if (GdmLayout.MAPOVERVIEW_ID.equals(map.getId())) {
						overviewMap = map;
					}
				}

				if (!geodesk.getMainMapLayers().isEmpty()) {
					mainMap.getLayers().clear();
					mainMap.getLayers().addAll(addLayers(geodesk.getMainMapLayers(), blueprint.getMainMapLayers()));
				} else if (!blueprint.getMainMapLayers().isEmpty()) {
					mainMap.getLayers().clear();
					mainMap.getLayers().addAll(addLayers(blueprint.getMainMapLayers(), null));
				}

				if (!geodesk.getOverviewMapLayers().isEmpty()) {
					overviewMap.getLayers().clear();
					overviewMap.getLayers().addAll(
							addLayers(geodesk.getOverviewMapLayers(), blueprint.getOverviewMapLayers()));
				} else if (!blueprint.getOverviewMapLayers().isEmpty()) {
					overviewMap.getLayers().clear();
					overviewMap.getLayers().addAll(addLayers(blueprint.getOverviewMapLayers(), null));
				}

				// Set bounds
				if (geodesk.mustFilterByCreatorTerritory()) {
					try {
						Bbox bounds = GeometryService.getBounds(convertorService
								.toDto(geodesk.getOwner().getGeometry()));
						mainMap.setMaxBounds(bounds);
						mainMap.setInitialBounds(bounds);
						overviewMap.setMaxBounds(bounds);
						overviewMap.setInitialBounds(bounds);
					} catch (GeomajasException e) {
						log.warn("Unable to apply loket territory bounds, falling back to default. {}",
								e.getLocalizedMessage());
					}
				} else if (geodesk.mustFilterByUserTerritory()) {
					try {
						Bbox bounds = GeometryService.getBounds(convertorService
								.toDto(((DeskmanagerSecurityContext) securityContext).getTerritory().getGeometry()));
						mainMap.setMaxBounds(bounds);
						mainMap.setInitialBounds(bounds);
						overviewMap.setMaxBounds(bounds);
						overviewMap.setInitialBounds(bounds);
					} catch (GeomajasException e) {
						log.warn("Unable to apply loket territory bounds, falling back to default. {}",
								e.getLocalizedMessage());
					}
				}

				// Custom configurations for main map (order matters, overrides)
				mainMap.getWidgetInfo().putAll(userApplicationInfo.getMainMapWidgetInfos());
				mainMap.getWidgetInfo().putAll(blueprint.getMainMapClientWidgetInfos());
				mainMap.getWidgetInfo().putAll(geodesk.getMainMapClientWidgetInfos());

				overviewMap.getWidgetInfo().putAll(userApplicationInfo.getOverviewMapWidgetInfos());
				overviewMap.getWidgetInfo().putAll(blueprint.getOverviewMapClientWidgetInfos());
				overviewMap.getWidgetInfo().putAll(geodesk.getOverviewMapClientWidgetInfos());

			} else {
				appInfo.getMaps().clear(); // no maps here
			}

			// -- custom widget configurations
			appInfo.getWidgetInfo().putAll(userApplicationInfo.getApplicationWidgetInfos());
			appInfo.getWidgetInfo().putAll(blueprint.getApplicationClientWidgetInfos());
			appInfo.getWidgetInfo().putAll(geodesk.getApplicationClientWidgetInfos());

			return appInfo;

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Error creating configuration: " + e.getMessage());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.deskmanager.common.server.service.LoketConfigurationService
	 * #createClonedConfiguration(org.geomajas.plugin.deskmanager.common .server.domain.configuration.Loket, boolean)
	 */
	public ClientApplicationInfo createClonedGeodeskConfiguration(Geodesk loket, boolean includeMaps) {
		ClientApplicationInfo loketConfig = createGeodeskConfiguration(loket, includeMaps);
		List<ClientMapInfo> cloned = new ArrayList<ClientMapInfo>();
		if (includeMaps) {
			for (ClientMapInfo map : loketConfig.getMaps()) {
				cloned.add(mapConfigurationCommand.securityClone(map));
			}
			loketConfig.getMaps().clear();
			loketConfig.getMaps().addAll(cloned);
		}

		return loketConfig;
	}

	private ClientLayerInfo createLayer(ClientLayer geodeskLayer, ClientLayer blueprintLayer) {
		ClientLayerInfo serverCli = null;
		ClientLayerInfo targetCli = null;
		Layer<?> serverLayer = null;

		if (geodeskLayer != null && geodeskLayer.getLayerModel() != null) {
			try {
				serverCli = (ClientLayerInfo) SerializationUtils.clone((ClientLayerInfo) applicationContext
						.getBean(geodeskLayer.getLayerModel().getClientLayerId()));
				boolean isVectorLayer = serverCli instanceof ClientVectorLayerInfo;
				serverLayer = (Layer<?>) applicationContext.getBean(serverCli.getServerLayerId());

				// Override layerInfo from server layer
				serverCli.setLayerInfo((LayerInfo) SerializationUtils.clone((LayerInfo) serverLayer.getLayerInfo()));
				if (isVectorLayer) {
					ClientVectorLayerInfo cvli = (ClientVectorLayerInfo) serverCli;
					cvli.setFeatureInfo(((VectorLayerInfo) cvli.getLayerInfo()).getFeatureInfo());
				}
				targetCli = serverCli;

				// Override with clientLayerInfo if it is set
				if (geodeskLayer.getClientLayerInfo() != null) {
					targetCli = geodeskLayer.getClientLayerInfo();
					
					// Set layerInfo and max extent from server configuration.
					targetCli.setLayerInfo(serverLayer.getLayerInfo());
					targetCli.setMaxExtent(serverCli.getMaxExtent());

					// Register the style if a vectorlayer.
					if (isVectorLayer) {
						NamedStyleInfo nsi = ((ClientVectorLayerInfo) targetCli).getNamedStyleInfo();
						log.debug("Registering style for layer: " + targetCli.getLabel());
						nsi.setName(styleService.registerStyle(targetCli.getServerLayerId(), nsi));
					}
				}

				// Finally set the widget configuration as a see trough.
				// Widget configurations set in the configuration
				Map<String, ClientWidgetInfo> clientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
				clientWidgetInfos.putAll(serverCli.getWidgetInfo());
				// Widget info set on the layer
				if (geodeskLayer != null && geodeskLayer.getLayerModel() != null) {
					clientWidgetInfos.putAll(geodeskLayer.getLayerModel().getWidgetInfo());
				}
				// Widget info set on the blueprint
				if (blueprintLayer != null) {
					clientWidgetInfos.putAll(blueprintLayer.getWidgetInfo());
				}
				// Widget info set on the geodesk layer
				if (geodeskLayer != null) {
					clientWidgetInfos.putAll(geodeskLayer.getWidgetInfo());
				}
				targetCli.setWidgetInfo(clientWidgetInfos);

				return targetCli;

			} catch (NoSuchBeanDefinitionException e) {
				// Ignore, error message later
			}
		}

		return null;
	}

	private List<ClientLayerInfo> addLayers(List<ClientLayer> geodeskLayers, List<ClientLayer> blueprintLayers) {
		List<ClientLayerInfo> clientLayers = new ArrayList<ClientLayerInfo>();
		for (ClientLayer geodeskLayer : geodeskLayers) {
			ClientLayerInfo cli;
			if (blueprintLayers != null) {
				cli = createLayer(geodeskLayer, getLayer(blueprintLayers, geodeskLayer.getLayerModel().getId()));
			} else {
				cli = createLayer(geodeskLayer, getLayer(Collections.<ClientLayer>emptyList(), 
						geodeskLayer.getLayerModel().getId()));
			}
			// Add the layer
			if (cli != null) {
				clientLayers.add(cli);
			} else {
				log.error("Unknown client layer info for " + geodeskLayer.getId());
			}
		}
		return clientLayers;
	}

	/**
	 * Helper method to fetch a single layer from the layer list.
	 * 
	 * @param layers
	 *            the list of layers
	 * @param id
	 *            the layer id to fetch
	 * @return the layer, null if not found
	 */
	private static ClientLayer getLayer(List<ClientLayer> layers, String id) {
		if (layers != null) {
			for (ClientLayer layer : layers) {
				if (layer.getLayerModel().getId().equals(id)) {
					return layer;
				}
			}
		}
		return null;
	}
}
