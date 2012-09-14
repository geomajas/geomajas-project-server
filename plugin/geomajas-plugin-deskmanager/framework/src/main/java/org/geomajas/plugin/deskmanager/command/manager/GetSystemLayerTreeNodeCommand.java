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
package org.geomajas.plugin.deskmanager.command.manager;

import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayerTreeNodeRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayerTreeNodeResponse;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This creates a ready to use layerTreeNode from all available (allowed) layers.
 * 
 * @author Kristof Heirwegh
 */
@Component(GetSystemLayerTreeNodeRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetSystemLayerTreeNodeCommand implements
		Command<GetSystemLayerTreeNodeRequest, GetSystemLayerTreeNodeResponse> {

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private DtoConverterService converterService;

	public void execute(GetSystemLayerTreeNodeRequest request, GetSystemLayerTreeNodeResponse response)
			throws Exception {
		LayerTreeNodeDto root = new LayerTreeNodeDto();
		root.setName("[ROOT]");
		root.setLeaf(false);
		List<LayerTreeNodeDto> ch = root.getChildren();
		for (LayerModel lm : layerModelService.getLayerModels()) {
			ch.add(toNode(lm, root));
			response.getLayerModels().put(lm.getClientLayerId(), converterService.toDto(lm, false));
		}
		response.setSystemLayerTreeNode(root);
	}

	public GetSystemLayerTreeNodeResponse getEmptyCommandResponse() {
		return new GetSystemLayerTreeNodeResponse();
	}

	// ----------------------------------------------------------

	private LayerTreeNodeDto toNode(LayerModel lm, LayerTreeNodeDto parent) throws Exception {
		LayerTreeNodeDto ltn = new LayerTreeNodeDto();
		ltn.setParentNode(parent);
		ltn.setLeaf(true);
		ltn.setClientLayerId(lm.getClientLayerId());
		ltn.setPublicLayer(lm.isPublic());
		ltn.setName(lm.getName());
		return ltn;
	}
}
