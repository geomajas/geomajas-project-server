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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.configuration.GetMapConfigurationCommand;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.GeodeskLayout;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.LayerTreeNode;
import org.geomajas.plugin.deskmanager.domain.LayerView;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.runtimeconfig.service.Rewirable;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;

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

	@Autowired(required = false)
	private Map<String, ClientLayerInfo> layerMap = new LinkedHashMap<String, ClientLayerInfo>();

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService convertorService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private GetMapConfigurationCommand mapConfigurationCommand;

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private SessionFactory session;
	
	@Autowired
	private GeoService geoService;
	
	@Autowired
	private VectorLayerService layerService;

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

			ClientApplicationInfo appInfo = clone(userApplicationInfo.getApplicationInfo());
			Blueprint blueprint = blueprintService.getBlueprintByIdInternal(geodesk.getBlueprint().getId());
			if (includeMaps) {
				ClientMapInfo mainMap = null;
				ClientMapInfo overviewMap = null;
				for (ClientMapInfo map : appInfo.getMaps()) {
					if (GeodeskLayout.MAPMAIN_ID.equals(map.getId())) {
						mainMap = map;
					}
					if (GeodeskLayout.MAPOVERVIEW_ID.equals(map.getId())) {
						overviewMap = map;
					}
				}

				// Build layertree
				LayerTreeNode root = null;
				LayerTreeNode blueprintRoot = null;

				if (geodesk.getLayerTree() != null && geodesk.getLayerTree().getRootNode() != null) {
					root = geodesk.getLayerTree().getRootNode();
					if (blueprint.getLayerTree() != null && blueprint.getLayerTree().getRootNode() != null) {
						blueprintRoot = blueprint.getLayerTree().getRootNode();
					}
				} else if (blueprint.getLayerTree() != null && blueprint.getLayerTree().getRootNode() != null) {
					root = blueprint.getLayerTree().getRootNode();
				}

				if (root != null) {
					// -- clear the original map --
					mainMap.getLayers().clear();
					mainMap.getLayerTree().getTreeNode().getTreeNodes().clear();
					mainMap.getLayerTree().getTreeNode().getLayers().clear();

					// -- rebuild map --
					for (LayerTreeNode ltn : root.getChildren()) {
						addLayers(mainMap.getCrs(), mainMap.getLayers(), mainMap.getLayerTree().getTreeNode(),
								blueprintRoot, ltn);
					}
				}

				// Set bounds
				if (geodesk.mustFilterByCreatorTerritory()) {
					try {
						Bbox bounds = GeometryService.getBounds(convertorService.toDto(geodesk.getOwner()
								.getGeometry()));
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
			log.warn("Fout bij aanmaken configuratie: " + e.getMessage());
			return null;
		}
	}

	// -------------------------------------------------

	private void addLayers(String mapCrs, List<ClientLayerInfo> clientLayers, ClientLayerTreeNodeInfo nodeInfo,
			LayerTreeNode blueprintRoot, LayerTreeNode node) throws Exception {
		if (node.isLeaf()) {
			// -- create layer
			LayerView lv = findLayerViewForNode(blueprintRoot, node);
			LayerModel model = layerModelService.getLayerModelByClientLayerIdInternal(node.getClientLayerId());
			ClientLayerInfo cli = createClientLayerInfo(mapCrs, node, lv, model);
			if (cli != null) {
				if (configurationService.getLayer(cli.getServerLayerId()) != null) {
					clientLayers.add(0, cli);
					if (node.isShowInLegend(lv, model, cli)) {
						nodeInfo.getLayers().add(cli);
					}
				} else {
					log.warn("Server Layer not found! (Client: " + node.getClientLayerId() + ", Server: "
							+ cli.getServerLayerId() + ")");
				}
			} else {
				log.warn("Client Layer not found! (" + node.getClientLayerId() + ")");
			}
		} else {
			// -- create node
			ClientLayerTreeNodeInfo cltni = createClientLayerTreeNodeInfo(node);
			nodeInfo.getTreeNodes().add(cltni);
			for (LayerTreeNode ltn : node.getChildren()) {
				addLayers(mapCrs, clientLayers, cltni, blueprintRoot, ltn);
			}
		}
	}

	/**
	 * Can be null!! (there is no blueprint, or blueprint also didn't customize)
	 * 
	 * @param blueprintNode
	 * @param node
	 * @return
	 */
	private LayerView findLayerViewForNode(LayerTreeNode blueprintNode, LayerTreeNode node) {
		if (!node.isLeaf() || blueprintNode == null) {
			return null;
		}
		if (blueprintNode.isLeaf() && blueprintNode.getClientLayerId().equals(node.getClientLayerId())) {
			return blueprintNode.getView();
		} else {
			for (LayerTreeNode ltn : blueprintNode.getChildren()) {
				LayerView lv = findLayerViewForNode(ltn, node);
				if (lv != null) {
					return lv;
				}
			}
		}
		return null;
	}

	private ClientLayerTreeNodeInfo createClientLayerTreeNodeInfo(LayerTreeNode node) {
		ClientLayerTreeNodeInfo cltni = new ClientLayerTreeNodeInfo();
		cltni.setExpanded(node.isExpanded());
		cltni.setLabel(node.getNodeName());
		return cltni;
	}

	// only leafs!
	private ClientLayerInfo createClientLayerInfo(String mapCrs, LayerTreeNode node, LayerView view, LayerModel model) {
		if (node == null) {
			return null;
		}
		ClientLayerInfo cli = layerMap.get(node.getClientLayerId());

		if (cli == null) {
			cli = (ClientLayerInfo) applicationContext.getBean(node.getClientLayerId());
		}

		if (cli != null) {
			cli = clone(cli);
			Layer<?> serverLayer = (Layer<?>) applicationContext.getBean(cli.getServerLayerId());
			try {
				Envelope serverEnvelope = convertorService.toInternal(serverLayer.getLayerInfo().getMaxExtent());
				Crs crs = layerService.getCrs(serverLayer);
				cli.setMaxExtent(convertorService.toDto(geoService.transform(serverEnvelope, crs,
						geoService.getCrs2(mapCrs))));
			} catch (Exception e) {
				// fall back to all, server will do the check...
				cli.setMaxExtent(Bbox.ALL);
			}
			cli.setLayerInfo(serverLayer.getLayerInfo());
			cli.setLabel(node.getName());
			if (node.getMaximumScale(view, model, null) != null) {
				cli.setMaximumScale(node.getMaximumScale(view, model, null));
			}
			if (node.getMinimumScale(view, model, null) != null) {
				cli.setMinimumScale(node.getMinimumScale(view, model, null));
			}
			if (node.isDefaultVisible(view, model, null) != null) {
				cli.setVisible(node.isDefaultVisible(view, model, null));
			}
			if (cli instanceof ClientVectorLayerInfo) {
				updateClientVectorLayerInfo((ClientVectorLayerInfo) cli, node);
			}
			return cli;
		} else {
			log.warn("Could not find ClientLayerInfo: " + node.getClientLayerId());
		}
		return null;
	}

	private void updateClientVectorLayerInfo(ClientVectorLayerInfo cvli, LayerTreeNode node) {
		// TODO update layerstyles
		// cvli.getNamedStyleInfo().setSldStyleName(node.getStyleUuid());
	}

	// -------------------------------------------------

	private ClientApplicationInfo clone(ClientApplicationInfo cai) {
		return XmlConverterService.toClientApplicationInfo(XmlConverterService.toXml(cai));
	}

	private ClientLayerInfo clone(ClientLayerInfo cli) {
		return XmlConverterService.toClientLayerInfo(XmlConverterService.toXml(cli));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.deskmanager.common.server.service.LoketConfigurationService
	 * #createClonedConfiguration(org.geomajas.plugin.deskmanager.common .server.domain.configuration.Loket, boolean)
	 */
	public ClientApplicationInfo createClonedGeodeskConfiguration(Geodesk loket, boolean includeMaps) {
		ClientApplicationInfo loketConfig = createGeodeskConfiguration(loket, true);
		List<ClientMapInfo> cloned = new ArrayList<ClientMapInfo>();
		for (ClientMapInfo map : loketConfig.getMaps()) {
			cloned.add(mapConfigurationCommand.securityClone(map));
		}
		loketConfig.getMaps().clear();
		loketConfig.getMaps().addAll(cloned);

		return loketConfig;
	}
}
