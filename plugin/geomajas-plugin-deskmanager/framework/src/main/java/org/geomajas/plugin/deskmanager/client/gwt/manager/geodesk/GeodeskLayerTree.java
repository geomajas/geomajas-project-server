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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.LayerTreeSelectPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Oliver May
 *
 */
public class GeodeskLayerTree extends VLayout implements WoaEventHandler {

	private GeodeskDto loket;

	private LayerTreeSelectPanel layerTreeSelect;

	private LayerTreeNodeDto systemLayerTreeNode;

	private boolean loading;

	private boolean deferredEnable;

	public GeodeskLayerTree() {
		super(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		// ----------------------------------------------------------
		// get personal layers

//		CommService.getSystemLayerTreeNode(new DataCallback<GetSystemLayerTreeNodeResponse>() {
//
//			public void execute(GetSystemLayerTreeNodeResponse result) {
//				systemLayerTreeNode = result.getSystemLayerTreeNode();
//			}
//		});

		// ----------------------------------------------------------

		layerTreeSelect = new LayerTreeSelectPanel();
		layerTreeSelect.setDisabled(true);
		layerTreeSelect.setWidth100();
		layerTreeSelect.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle("Datalagen");
		group.addMember(layerTreeSelect);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setGeodesk(final GeodeskDto newLoket) {
		loket = newLoket;
//		CommService.getSystemLayerTreeNode(new DataCallback<GetSystemLayerTreeNodeResponse>() {
//
//			public void execute(GetSystemLayerTreeNodeResponse result) {
//				systemLayerTreeNode = result.getSystemLayerTreeNode();
//				layerTreeSelect.clearValues();
//				if (loket != null) {
//					if (loket.getLayerTree() == null) {
//						loket.setLayerTree(new LayerTreeDto());
//					}
//
//					loading = true;
//					CommService.getBlueprint(loket.getBlueprint().getId(), new DataCallback<BlueprintDto>() {
//
//						public void execute(BlueprintDto result) {
//							LayerTreeNodeDto source = mergeNodes(result.getLayerTree());
//							LayerTreeNodeDto target = loket.getLayerTree().getRootNode();
//							boolean hasLayerTree = false;
//							if (loket.getBlueprint().getUserApplicationName() != null) {
//								hasLayerTree = UserApplicationRegistry.getInstance()
//										.get(loket.getBlueprint().getUserApplicationName()).hasLayerTree();
//							}
//							layerTreeSelect.setValues(source, target, !(loket.isPublic()), hasLayerTree);
//							loading = false;
//							if (deferredEnable) {
//								layerTreeSelect.setDisabled(false);
//								deferredEnable = false;
//							}
//						}
//					});
//				}
//			}
//		});
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		if (loading) {
			NotificationWindow.showInfoMessage("Lagen worden nog geladen, even geduld.");
			deferredEnable = true;
		} else {
			layerTreeSelect.setDisabled(false);
		}
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
//		LayerTreeDto lt = loket.getLayerTree();
//		if (lt == null) {
//			lt = new LayerTreeDto();
//			loket.setLayerTree(lt);
//		}
//		lt.setRootNode(layerTreeSelect.getValues());
//		layerTreeSelect.setDisabled(true);
//		CommService.saveGeodesk(loket, SaveGeodeskRequest.SAVE_LAYERS);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		layerTreeSelect.setDisabled(true);
		setGeodesk(loket);
		return true;
	}

	// -------------------------------------------------

	private LayerTreeNodeDto mergeNodes(LayerTreeDto tree) {
		if (tree == null || tree.getRootNode() == null) {
			return systemLayerTreeNode;
		} else if (systemLayerTreeNode.getChildren().size() == 0) {
			return tree.getRootNode();
		} else {
			LayerTreeNodeDto rootNode = tree.getRootNode();
			LayerTreeNodeDto blueprintNode = new LayerTreeNodeDto("Lagen uit Blauwdruk", false);
			blueprintNode.setExpanded(true);
			for (LayerTreeNodeDto n : tree.getRootNode().getChildren()) {
				blueprintNode.getChildren().add(n);
				n.setParentNode(blueprintNode);
			}
			rootNode.getChildren().clear();
			rootNode.getChildren().add(blueprintNode);
			blueprintNode.setParentNode(rootNode);

			LayerTreeNodeDto otherNode = new LayerTreeNodeDto("Andere Lagen", false);
			otherNode.setExpanded(false);
			LayerTreeNodeDto sysNode = systemLayerTreeNode.clone(); // clone for reparenting
			for (LayerTreeNodeDto n : sysNode.getChildren()) {
				if (!blueprintNode.contains(n)) {
					otherNode.getChildren().add(n);
					n.setParentNode(otherNode);
				}
			}
			if (otherNode.getChildren().size() > 0) {
				rootNode.getChildren().add(otherNode);
				otherNode.setParentNode(rootNode);
			}

			return rootNode;
		}
	}
}
