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
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.layertree.LayerTreeSelectPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.widget.layer.configuration.client.ClientLayerTreeInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class BlueprintLayerTree extends AbstractConfigurationLayout implements BlueprintSelectionHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private BlueprintDto blueprint;

	private LayerTreeSelectPanel layerTreeSelect;

	public BlueprintLayerTree() {
		super();
		setMargin(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		layerTreeSelect = new LayerTreeSelectPanel();
		layerTreeSelect.setDisabled(true);
		layerTreeSelect.setWidth100();
		layerTreeSelect.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.layerTreeFormGroup());
		group.addMember(layerTreeSelect);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setBlueprint(BlueprintDto newBluePrint) {
		this.blueprint = newBluePrint;
		layerTreeSelect.setValues(newBluePrint);
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		layerTreeSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		ClientLayerTreeInfo cli = layerTreeSelect.getValues();
		blueprint.getMainMapClientWidgetInfos().put(ClientLayerTreeInfo.IDENTIFIER, cli);
		ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setBlueprint(blueprint);
		layerTreeSelect.setDisabled(true);
		return true;
	}

	public void onBlueprintSelectionChange(BlueprintEvent bpe) {
		setBlueprint(bpe.getBlueprint());
	}

	public boolean onResetClick(ClickEvent event) {
		blueprint.getMainMapClientWidgetInfos().remove(ClientLayerTreeInfo.IDENTIFIER);
		ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
		return true;
	}

	public boolean isDefault() {
		return !blueprint.getMainMapClientWidgetInfos().containsKey(ClientLayerTreeInfo.IDENTIFIER);
	}
}
