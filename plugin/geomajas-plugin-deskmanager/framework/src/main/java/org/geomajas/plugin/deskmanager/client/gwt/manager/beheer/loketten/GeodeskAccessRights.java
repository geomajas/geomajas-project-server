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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.loketten;

import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.GroupTreeGrid;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GeodeskAccessRights extends VLayout implements WoaEventHandler {

	private GeodeskDto geodesk;

	private GroupTreeGrid groupSelect;

	public GeodeskAccessRights() {
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
		group.setGroupTitle("Gebruiksrechten");
		group.addMember(groupSelect);
		group.setOverflow(Overflow.AUTO);

		// ----------------------------------------------------------

		CommService.getGroups(new DataCallback<List<TerritoryDto>>() {

			public void execute(List<TerritoryDto> result) {
				groupSelect.setGroups(result);
			}
		});

		// ----------------------------------------------------------

		addMember(group);
	}

	public void setGeodesk(GeodeskDto loket) {
		this.geodesk = loket;
		if (loket != null) {
			groupSelect.setValues(loket.getTerritories());
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
		geodesk.setGroups(groupSelect.getValues());
		CommService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_GROUPS);
		groupSelect.setDisabled(true);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setGeodesk(geodesk);
		groupSelect.setDisabled(true);
		return true;
	}
}
