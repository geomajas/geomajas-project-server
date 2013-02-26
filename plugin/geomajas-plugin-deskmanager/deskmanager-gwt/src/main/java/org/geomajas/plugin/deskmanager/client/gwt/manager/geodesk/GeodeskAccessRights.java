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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.GroupTreeGrid;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GeodeskAccessRights extends AbstractConfigurationLayout implements GeodeskSelectionHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private GeodeskDto geodesk;

	private GroupTreeGrid groupSelect;

	public GeodeskAccessRights() {
		super();
		setMargin(5);

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

	private void setGeodesk(GeodeskDto loket) {
		this.geodesk = loket;
		if (loket != null) {
			groupSelect.setValues(loket.getTerritories());
		} else {
			groupSelect.setValues(null);
		}
		fireChangedHandler();
	}
	
	public void onGeodeskSelectionChange(GeodeskEvent geodeskEvent) {
		setGeodesk(geodeskEvent.getGeodesk());
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		groupSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		geodesk.setGroups(groupSelect.getValues());
		ManagerCommandService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_TERRITORIES);
		groupSelect.setDisabled(true);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setGeodesk(geodesk);
		groupSelect.setDisabled(true);
		return true;
	}
	

	public boolean onResetClick(ClickEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefault() {
		return true;
	}
}
