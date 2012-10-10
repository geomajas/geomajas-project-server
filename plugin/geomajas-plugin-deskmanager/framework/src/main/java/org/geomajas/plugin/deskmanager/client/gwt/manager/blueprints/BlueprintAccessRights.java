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

import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.GroupTreeGrid;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class BlueprintAccessRights extends VLayout implements WoaEventHandler, BlueprintSelectionHandler {
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private BlueprintDto blueprint;

	private GroupTreeGrid groupSelect;

	public BlueprintAccessRights() {
		super(5);

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		groupSelect = new GroupTreeGrid();
		groupSelect.setDisabled(true);
		groupSelect.setWidth100();
		groupSelect.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.geodeskAccessRightsFormGroup());
		group.addMember(groupSelect);
		group.setOverflow(Overflow.AUTO);

		// ----------------------------------------------------------

		ManagerCommandService.getGroups(new DataCallback<List<TerritoryDto>>() {

			public void execute(List<TerritoryDto> result) {
				groupSelect.setGroups(result);
			}
		});

		// ----------------------------------------------------------

		addMember(group);
	}

	public void setBlueprint(BlueprintDto blueprint) {
		this.blueprint = blueprint;
		if (blueprint != null) {
			groupSelect.setValues(blueprint.getTerritories());
		} else {
			groupSelect.setValues(null);
		}
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		groupSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		blueprint.setGroups(groupSelect.getValues());
		ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_TERRITORIES);
		groupSelect.setDisabled(true);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setBlueprint(blueprint);
		groupSelect.setDisabled(true);
		return true;
	}
	
	public void onBlueprintSelectionChange(BlueprintEvent bpe) {
		setBlueprint(bpe.getBlueprint());
	}

}
