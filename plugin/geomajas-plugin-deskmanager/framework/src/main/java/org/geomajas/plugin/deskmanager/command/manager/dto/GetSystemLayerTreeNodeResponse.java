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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GetSystemLayerTreeNodeResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private LayerTreeNodeDto systemLayerTreeNode;

	private Map<String, LayerModelDto> layerModels = new HashMap<String, LayerModelDto>();

	public LayerTreeNodeDto getSystemLayerTreeNode() {
		return systemLayerTreeNode;
	}

	public void setSystemLayerTreeNode(LayerTreeNodeDto systemLayerTreeNode) {
		this.systemLayerTreeNode = systemLayerTreeNode;
	}

	public Map<String, LayerModelDto> getLayerModels() {
		return layerModels;
	}

	public void setLayerModels(Map<String, LayerModelDto> layerModels) {
		this.layerModels = layerModels;
	}

}
