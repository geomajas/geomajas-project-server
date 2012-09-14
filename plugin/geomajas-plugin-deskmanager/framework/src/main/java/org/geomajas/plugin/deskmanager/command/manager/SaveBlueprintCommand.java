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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.LayerTree;
import org.geomajas.plugin.deskmanager.domain.LayerTreeNode;
import org.geomajas.plugin.deskmanager.domain.LayerView;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GroupService;
import org.geomajas.plugin.deskmanager.service.common.LayerTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Oliver May
 * 
 */
@Component(SaveBlueprintRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class SaveBlueprintCommand implements Command<SaveBlueprintRequest, BlueprintResponse> {

	private final Logger log = LoggerFactory.getLogger(SaveBlueprintCommand.class);

	@Autowired
	private DtoConverterService dtoService;

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private LayerTreeService layerTreeService;

	@Autowired
	private GroupService groupService;

	public void execute(SaveBlueprintRequest request, BlueprintResponse response) throws Exception {
		try {
			if (request.getBlueprint() == null) {
				response.getErrorMessages().add("Geen blauwdruk opgegeven ??");
			} else if (request.getSaveWhat() < 1) {
				response.getErrorMessages().add("Niets te bewaren ??");
			} else {
				Blueprint target = blueprintService.getBlueprintById(request.getBlueprint().getId());
				if (target == null) {
					response.getErrorMessages().add(
							"Geen blauwdruk gevonden voor id: " + request.getBlueprint().getId()
									+ " (Nieuwe blauwdruk?)");
				} else {
					Blueprint source = dtoService.fromDto(request.getBlueprint());

					if ((SaveBlueprintRequest.SAVE_SETTINGS & request.getSaveWhat()) > 0) {
						copySettings(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_GROUPS & request.getSaveWhat()) > 0) {
						copyGroups(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_LAYERTREE & request.getSaveWhat()) > 0) {
						copyLayerTree(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO & request.getSaveWhat()) > 0) {
						copyWidgetInfo(source, target);
					}

					blueprintService.saveOrUpdateBlueprint(target);
					response.setBlueprint(dtoService.toDto(target, false));
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij opslaan blauwdruk: " + e.getMessage());
			log.error("fout bij opslaan blauwdruk.", e);
		}
	}

	public BlueprintResponse getEmptyCommandResponse() {
		return new BlueprintResponse();
	}

	// ----------------------------------------------------------

	private void copySettings(Blueprint source, Blueprint target) throws Exception {
		target.setActive(source.isActive());
		target.setUserApplicationKey(source.getUserApplicationKey());
		target.setLimitToLoketTerritory(source.isLimitToCreatorTerritory());
		target.setLimitToUserTerritory(source.isLimitToUserTerritory());
		target.setLokettenActive(source.isLokettenActive());
		target.setName(source.getName());
		target.setPublic(source.isPublic());
	}

	private void copyWidgetInfo(Blueprint source, Blueprint target) throws Exception {
		target.getApplicationClientWidgetInfos().putAll(source.getApplicationClientWidgetInfos());
		target.getMainMapClientWidgetInfos().putAll(source.getMainMapClientWidgetInfos());
		target.getOverviewMapClientWidgetInfos().putAll(source.getOverviewMapClientWidgetInfos());
		
	}

	private void copyGroups(Blueprint source, Blueprint target) throws Exception {
		List<Territory> toDelete = new ArrayList<Territory>();
		List<Territory> toAdd = new ArrayList<Territory>();
		for (Territory g : target.getTerritories()) {
			if (!source.getTerritories().contains(g)) {
				toDelete.add(g);
			}
		}
		if (toDelete.size() > 0) {
			target.getTerritories().removeAll(toDelete);
		}
		for (Territory g : source.getTerritories()) {
			if (!target.getTerritories().contains(g)) {
				toAdd.add(g);
			}
		}
		for (Territory discon : toAdd) {
			Territory conn = groupService.getById(discon.getId());
			if (conn != null) {
				target.getTerritories().add(conn);
			} else {
				log.warn("Territory not found !? (id: " + discon.getId());
			}
		}
	}

	private void copyLayerTree(Blueprint source, Blueprint target) throws Exception {
		// -- check LayerTree
		if (source.getLayerTree() == null && target.getLayerTree() == null) {
			return;

		} else if (target.getLayerTree() == null && source.getLayerTree() != null) {
			target.setLayerTree(new LayerTree());
			layerTreeService.saveOrUpdateLayerTree(target.getLayerTree());
			source.getLayerTree().getRootNode().setId(target.getLayerTree().getRootNode().getId());

		} else if (target.getLayerTree() != null && source.getLayerTree() == null) {
			LayerTree lt = target.getLayerTree();
			target.setLayerTree(null);
			layerTreeService.deleteLayerTree(lt);
			return;
		}

		final LayerTreeNode sourceRootNode = source.getLayerTree().getRootNode();
		final LayerTreeNode targetRootNode = target.getLayerTree().getRootNode();
		final Map<Long, LayerTreeNode> olduns = new HashMap<Long, LayerTreeNode>();
		final Set<LayerTreeNode> useduns = new HashSet<LayerTreeNode>();
		final Map<LayerTreeNode, LayerTreeNode> mappings = new HashMap<LayerTreeNode, LayerTreeNode>();
		mappings.put(sourceRootNode, targetRootNode);

		// find all nodes, will be rebuilt later on
		targetRootNode.visit(new LayerTree.LayerTreeNodeVisitor() {

			public void visit(LayerTreeNode node) {
				olduns.put(node.getId(), node);
			}
		});

		// detach children
		for (LayerTreeNode ltn : olduns.values()) {
			if (!ltn.isLeaf()) {
				ltn.getChildren().clear();
			}
			ltn.setParentNode(null);
		}

		// rebuild tree, use olduns where possible
		sourceRootNode.visit(new LayerTree.LayerTreeNodeVisitor() {

			public void visit(LayerTreeNode node) {
				LayerTreeNode target;
				if (node.getId() != null) {
					// existing
					target = olduns.get(node.getId());
					if (target == null) {
						throw new RuntimeException(
								"Could not copy LayerTree (Should have persisted node ?! (did you set id manually ?)");
					}
					useduns.add(target);
					copyLayerTreeNode(node, target);
				} else {
					// new
					target = new LayerTreeNode();
					copyLayerTreeNode(node, target);
				}
				mappings.put(node, target);

				if (node.getParentNode() != null) {
					target.setParentNode(mappings.get(node.getParentNode()));
					target.getParentNode().getChildren().add(target);
				}
			}
		});

		// delete obsolete nodes (can't use cascade, then reparenting doesn't work)
		olduns.values().removeAll(useduns);
		for (LayerTreeNode ltn : olduns.values()) {
			layerTreeService.deleteLayerTreeNode(ltn);
		}

		// persist (needed for new items, won't be persisted otherwise)
		layerTreeService.saveOrUpdateLayerTree(target.getLayerTree());
	}

	/**
	 * Only the node, not the children!
	 */
	private void copyLayerTreeNode(LayerTreeNode source, LayerTreeNode target) {
		target.setExpanded(source.isExpanded());
		target.setLeaf(source.isLeaf());
		target.setName(source.getNodeName());
		target.setClientLayerId(source.getClientLayerId());
		target.setPublicLayer(source.isPublicLayer());

		if (source.getView() == null && target.getView() != null) {
			target.setView(null);
			return;
		} else if (source.getView() != null && target.getView() == null) {
			target.setView(new LayerView());
		}
		copyLayerView(source.getView(), target.getView());
	}

	private void copyLayerView(LayerView source, LayerView target) {
		if (source == null) {
			return;
		}
		target.setDefaultVisible(source.isDefaultVisible());
		target.setLabel(source.getLabel());
		target.setMaximumScale(source.getMaximumScale());
		target.setMinimumScale(source.getMinimumScale());
		target.setShowInLegend(source.isShowInLegend());
	}
}
