/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.LayerSelectPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class BlueprintLayers extends AbstractConfigurationLayout implements BlueprintSelectionHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private BlueprintDto blueprint;

	private LayerSelectPanel layerSelect;

	public BlueprintLayers() {
		super();
		setMargin(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		layerSelect = new LayerSelectPanel();
		layerSelect.setDisabled(true);
		layerSelect.setWidth100();
		layerSelect.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.datalagenGroup());
		group.addMember(layerSelect);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setBlueprint(BlueprintDto newBluePrint) {
		this.blueprint = newBluePrint;
		ManagerCommandService.getLayers(new DataCallback<GetClientLayersResponse>() {

			public void execute(GetClientLayersResponse result) {
				layerSelect.setValues(null, result.getLayers(), blueprint.getMainMapLayers(),
						blueprint.isPublic());
				fireChangedHandler();
			}

		});
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		layerSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		layerSelect.setDisabled(true);
		List<LayerDto> layers = layerSelect.getValues();
		blueprint.setMainMapLayers(layers);
		ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_LAYERS);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		layerSelect.setDisabled(true);
		setBlueprint(blueprint);
		return true;
	}

	public void onBlueprintSelectionChange(BlueprintEvent bpe) {
		setBlueprint(bpe.getBlueprint());
	}

	public boolean onResetClick(ClickEvent event) {
		blueprint.getMainMapLayers().clear();
		ManagerCommandService.saveBlueprint(blueprint, SaveGeodeskRequest.SAVE_LAYERS);
		return true;
	}

	public boolean isDefault() {
		return true;
//		return blueprint.getMainMapLayers() == null || blueprint.getMainMapLayers().isEmpty();
	}
}
