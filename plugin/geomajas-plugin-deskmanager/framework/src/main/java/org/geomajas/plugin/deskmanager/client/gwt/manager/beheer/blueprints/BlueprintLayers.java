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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.blueprints;

import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.LayerSelectPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintSelectionHandler;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayerTreeNodeResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class BlueprintLayers extends VLayout implements WoaEventHandler, BlueprintSelectionHandler {

	private BlueprintDto blueprint;

	private LayerSelectPanel layerSelect;

	private LayerTreeNodeDto systemLayerTreeNode;

	public BlueprintLayers() {
		super(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

//		// ----------------------------------------------------------
//
//		CommService.getSystemLayerTreeNode(new DataCallback<GetSystemLayerTreeNodeResponse>() {
//
//			public void execute(GetSystemLayerTreeNodeResponse result) {
//				systemLayerTreeNode = result.getSystemLayerTreeNode();
//			}
//		});
//
//		// ----------------------------------------------------------

		layerSelect = new LayerSelectPanel();
		layerSelect.setDisabled(true);
		layerSelect.setWidth100();
		layerSelect.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle("Datalagen");
		group.addMember(layerSelect);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setBlueprint(BlueprintDto newBluePrint) {
		this.blueprint = newBluePrint;
		CommService.getSystemLayerTreeNode(new DataCallback<GetSystemLayerTreeNodeResponse>() {

			public void execute(GetSystemLayerTreeNodeResponse result) {
				systemLayerTreeNode = result.getSystemLayerTreeNode();
				layerSelect.clearValues();
				if (blueprint != null) {
					if (blueprint.getLayerTree() == null) {
						blueprint.setLayerTree(new LayerTreeDto());
					}
					LayerTreeNodeDto target = blueprint.getLayerTree().getRootNode();
					boolean hasLayerTree = false;
					if (blueprint.getUserApplicationName() != null) {
						hasLayerTree = UserApplicationRegistry.getInstance().get(blueprint.getUserApplicationName())
								.hasLayerTree();
					}
					layerSelect.setValues(systemLayerTreeNode, target, !(blueprint.isPublic()), hasLayerTree);
				}
			}
		});

	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		layerSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		LayerTreeDto lt = blueprint.getLayerTree();
		if (lt == null) {
			lt = new LayerTreeDto();
			blueprint.setLayerTree(lt);
		}
		lt.setRootNode(layerSelect.getValues());
		layerSelect.setDisabled(true);
		CommService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_LAYERTREE);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setBlueprint(blueprint);
		layerSelect.setDisabled(true);
		return true;
	}

	public void onBlueprintSelectionChange(BlueprintEvent bpe) {
		setBlueprint(bpe.getBlueprint());
	}

}
