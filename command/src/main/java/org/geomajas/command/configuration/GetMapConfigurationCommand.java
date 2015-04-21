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
package org.geomajas.command.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.annotation.Api;
import org.geomajas.command.CommandHasRequest;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.ServerSideOnlyInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This command fetches, and returns the initial application configuration for a specific MapWidget.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @author Jan Venstermans
 * @since 1.6.0
 */
@Api
@Component()
public class GetMapConfigurationCommand
		implements CommandHasRequest<GetMapConfigurationRequest, GetMapConfigurationResponse> {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SecurityContext securityContext;

	@Override
	public GetMapConfigurationRequest getEmptyCommandRequest() {
		return new GetMapConfigurationRequest();
	}

	@Override
	public GetMapConfigurationResponse getEmptyCommandResponse() {
		return new GetMapConfigurationResponse();
	}

	@Override
	public void execute(GetMapConfigurationRequest request, GetMapConfigurationResponse response) throws Exception {
		if (null == request.getApplicationId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "applicationId");
		}
		String mapId = request.getMapId();
		if (null == mapId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapId");
		}

		ClientApplicationInfo application = context.getBean(request.getApplicationId(), ClientApplicationInfo.class);
		for (ClientMapInfo map : application.getMaps()) {
			if (mapId.equals(map.getId())) {
				response.setMapInfo(securityClone(map));
			}
		}
		if (response.getMapInfo() == null) {
			throw new GeomajasException(ExceptionCode.MAP_NOT_FOUND, request.getMapId(), request.getApplicationId());
		}
	}

	public ClientMapInfo securityClone(ClientMapInfo original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		ClientMapInfo client = new ClientMapInfo();
		client.setBackgroundColor(original.getBackgroundColor());
		client.setCrs(original.getCrs());
		client.setDisplayUnitType(original.getDisplayUnitType());
		client.setId(original.getId());
		client.setInitialBounds(original.getInitialBounds());
		client.setPreferredPixelsPerTile(original.getPreferredPixelsPerTile());
		List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
		client.setLayers(layers);
		for (ClientLayerInfo layer : original.getLayers()) {
			ClientLayerInfo clientLayer = securityClone(layer);
			if (null != clientLayer) {
				layers.add(clientLayer);
			}
		}
		client.setLayerTree(securityClone(original.getLayerTree()));
		client.setLineSelectStyle(original.getLineSelectStyle());
		client.setMaxBounds(original.getMaxBounds());
		client.setViewBoundsLimitOption(original.getViewBoundsLimitOption());
		client.setScaleConfiguration(original.getScaleConfiguration());
		client.setPanButtonsEnabled(original.isPanButtonsEnabled());
		client.setPixelLength(original.getPixelLength());
		client.setPointSelectStyle(original.getPointSelectStyle());
		client.setPolygonSelectStyle(original.getPolygonSelectStyle());
		client.setPrecision(original.getPrecision());
		client.setScaleBarEnabled(original.isScaleBarEnabled());
		client.setToolbar(securityClone(original.getToolbar()));
		client.setUnitLength(original.getUnitLength());
		if (!(original.getUserData() instanceof ServerSideOnlyInfo)) {
			client.setUserData(original.getUserData());
		}
		client.setWidgetInfo(securityClone(original.getWidgetInfo()));
		return client;
	}

	/**
	 * Clone a widget info map considering what may be copied to the client.
	 *
	 * @param widgetInfo widget info map
	 * @return cloned copy including only records which are not {@link ServerSideOnlyInfo}
	 */
	public Map<String, ClientWidgetInfo> securityClone(Map<String, ClientWidgetInfo> widgetInfo) {
		Map<String, ClientWidgetInfo> res = new HashMap<String, ClientWidgetInfo>();
		for (Map.Entry<String, ClientWidgetInfo> entry : widgetInfo.entrySet()) {
			ClientWidgetInfo value = entry.getValue();
			if (!(value instanceof ServerSideOnlyInfo)) {
				res.put(entry.getKey(), value);
			}
		}
		return res;
	}

	/**
	 * Clone a {@link org.geomajas.configuration.LayerInfo#extraInfo} considering what may be copied to the client.
	 *
	 * @param extraInfoMap map of extra info
	 * @return cloned copy including only records which are not {@link ServerSideOnlyInfo}
	 */
	public Map<String, LayerExtraInfo> securityCloneLayerExtraInfo(Map<String, LayerExtraInfo> extraInfoMap) {
		Map<String, LayerExtraInfo> res = new HashMap<String, LayerExtraInfo>();
		for (Map.Entry<String, LayerExtraInfo> entry : extraInfoMap.entrySet()) {
			LayerExtraInfo value = entry.getValue();
			if (!(value instanceof ServerSideOnlyInfo)) {
				res.put(entry.getKey(), value);
			}
		}
		return res;
	}

	/**
	 * Clone layer information considering what may be copied to the client.
	 *
	 * @param original layer info
	 * @return cloned copy including only allowed information
	 */
	public ClientLayerInfo securityClone(ClientLayerInfo original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		ClientLayerInfo client = null;
		String layerId = original.getServerLayerId();
		if (securityContext.isLayerVisible(layerId)) {
			client = (ClientLayerInfo) SerializationUtils.clone(original);
			client.setWidgetInfo(securityClone(original.getWidgetInfo()));
			client.getLayerInfo().setExtraInfo(securityCloneLayerExtraInfo(original.getLayerInfo().getExtraInfo()));
			if (client instanceof ClientVectorLayerInfo) {
				ClientVectorLayerInfo vectorLayer = (ClientVectorLayerInfo) client;
				// set statuses
				vectorLayer.setCreatable(securityContext.isLayerCreateAuthorized(layerId));
				vectorLayer.setUpdatable(securityContext.isLayerUpdateAuthorized(layerId));
				vectorLayer.setDeletable(securityContext.isLayerDeleteAuthorized(layerId));
				// filter feature info
				FeatureInfo featureInfo = vectorLayer.getFeatureInfo();
				List<AttributeInfo> originalAttr = featureInfo.getAttributes();
				List<AttributeInfo> filteredAttr = new ArrayList<AttributeInfo>();
				featureInfo.setAttributes(filteredAttr);
				for (AttributeInfo ai : originalAttr) {
					if (securityContext.isAttributeReadable(layerId, null, ai.getName())) {
						filteredAttr.add(ai);
					}
				}
			}
		}
		return client;
	}

	public ClientLayerTreeInfo securityClone(ClientLayerTreeInfo original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		// the data is explicitly copied as this assures the security is considered when copying.
		ClientLayerTreeInfo client = new ClientLayerTreeInfo();
		client.setId(original.getId());
		client.setTools(securityClone(original.getTools()));
		client.setTreeNode(securityClone(original.getTreeNode()));
		return client;
	}

	public ClientLayerTreeNodeInfo securityClone(ClientLayerTreeNodeInfo original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		ClientLayerTreeNodeInfo client = new ClientLayerTreeNodeInfo();
		client.setLabel(original.getLabel());
		client.setExpanded(original.isExpanded());
		List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
		client.setLayers(layers);
		for (ClientLayerInfo layer : original.getLayers()) {
			ClientLayerInfo copy = securityClone(layer);
			if (null != copy) {
				layers.add(copy);
			}
		}
		List<ClientLayerTreeNodeInfo> nodes = new ArrayList<ClientLayerTreeNodeInfo>();
		client.setTreeNodes(nodes);
		for (ClientLayerTreeNodeInfo node : original.getTreeNodes()) {
			ClientLayerTreeNodeInfo copy = securityClone(node);
			if (null != copy) {
				nodes.add(copy);
			}
		}
		if (layers.size() > 0 || nodes.size() > 0) {
			return client;
		}
		return null;
	}

	public ClientToolbarInfo securityClone(ClientToolbarInfo original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		ClientToolbarInfo client = new ClientToolbarInfo();
		client.setId(original.getId());
		client.setTools(securityClone(original.getTools()));
		return client;
	}

	public List<ClientToolInfo> securityClone(List<ClientToolInfo> original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		List<ClientToolInfo> tools = new ArrayList<ClientToolInfo>();
		for (ClientToolInfo tool : original) {
			if (securityContext.isToolAuthorized(tool.getToolId())) {
				tools.add(tool);
			}
		}
		return tools;
	}
}
