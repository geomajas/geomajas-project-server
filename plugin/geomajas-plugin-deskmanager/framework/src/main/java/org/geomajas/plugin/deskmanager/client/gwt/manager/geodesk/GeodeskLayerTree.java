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

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.layertree.LayerTreeSelectPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.widget.layer.configuration.client.ClientLayerTreeInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Oliver May
 * 
 */
public class GeodeskLayerTree extends VLayout implements WoaEventHandler, GeodeskSelectionHandler {
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private GeodeskDto geodesk;

	private LayerTreeSelectPanel layerTreeSelect;

	public GeodeskLayerTree() {
		super(5);

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

	public void onGeodeskSelectionChange(GeodeskEvent geodeskEvent) {
		setGeodesk(geodeskEvent.getGeodesk());
	}
	
	private void setGeodesk(final GeodeskDto newLoket) {
		geodesk = newLoket;
		layerTreeSelect.setValues(geodesk);
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		layerTreeSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		ClientLayerTreeInfo cli = layerTreeSelect.getValues();
		geodesk.getMainMapClientWidgetInfos().put(ClientLayerTreeInfo.IDENTIFIER, cli);
		ManagerCommandService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_CLIENTWIDGETINFO);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		layerTreeSelect.setDisabled(true);
		setGeodesk(geodesk);
		return true;
	}
}
