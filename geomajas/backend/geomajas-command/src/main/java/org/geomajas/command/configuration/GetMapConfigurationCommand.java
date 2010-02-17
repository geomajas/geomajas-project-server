/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.command.configuration;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.command.Command;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This command fetches, and returns the initial application configuration for a specific MapWidget.
 * 
 * @author Pieter De Graef
 */
@Component()
public class GetMapConfigurationCommand implements Command<GetMapConfigurationRequest, GetMapConfigurationResponse> {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SecurityContext securityContext;

	public GetMapConfigurationResponse getEmptyCommandResponse() {
		return new GetMapConfigurationResponse();
	}

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
		client.setMaximumScale(original.getMaximumScale());
		client.setPanButtonsEnabled(original.isPanButtonsEnabled());
		client.setPixelLength(original.getPixelLength());
		client.setPointSelectStyle(original.getPointSelectStyle());
		client.setPolygonSelectStyle(original.getPolygonSelectStyle());
		client.setPrecision(original.getPrecision());
		client.setResolutions(original.getResolutions());
		client.setResolutionsRelative(original.isResolutionsRelative());
		client.setScaleBarEnabled(original.isScaleBarEnabled());
		client.setToolbar(securityClone(original.getToolbar()));
		client.setUnitLength(original.getUnitLength());
		return client;
	}

	public ClientLayerInfo securityClone(ClientLayerInfo original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		ClientLayerInfo client = null;
		String layerId = original.getId();
		if (securityContext.isLayerVisible(layerId)) {
			client = (ClientLayerInfo) SerializationUtils.clone(original);
			if (client instanceof ClientVectorLayerInfo) {
				ClientVectorLayerInfo vectorLayer = (ClientVectorLayerInfo) client;
				vectorLayer.setCreatable(securityContext.isLayerCreateAuthorized(layerId));
				vectorLayer.setUpdatable(securityContext.isLayerUpdateAuthorized(layerId));
				vectorLayer.setDeletable(securityContext.isLayerDeleteAuthorized(layerId));
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
		return original;
	}

	public List<ClientToolInfo> securityClone(List<ClientToolInfo> original) {
		// the data is explicitly copied as this assures the security is considered when copying.
		if (null == original) {
			return null;
		}
		List<ClientToolInfo> tools = new ArrayList<ClientToolInfo>();
		for (ClientToolInfo tool : original) {
			if (securityContext.isToolAuthorized(tool.getId())) {
				tools.add(tool);
			}
		}
		return tools;
	}
}
