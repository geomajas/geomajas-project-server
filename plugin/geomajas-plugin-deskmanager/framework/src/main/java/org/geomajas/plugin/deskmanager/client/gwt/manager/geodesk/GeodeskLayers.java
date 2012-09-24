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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.LayerSelectPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayersResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Oliver May
 * 
 */
public class GeodeskLayers extends VLayout implements WoaEventHandler, GeodeskSelectionHandler {
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private GeodeskDto geodesk;

	private LayerSelectPanel layerSelect;

	public GeodeskLayers() {
		super(5);

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

	private void setGeodesk(final GeodeskDto newLoket) {
		geodesk = newLoket;
		CommService.getSystemLayers(new DataCallback<GetSystemLayersResponse>() {

			public void execute(GetSystemLayersResponse result) {
				List<LayerDto> sourceLayers = new ArrayList<LayerDto>(geodesk.getBlueprint().getMainMapLayers());
				sourceLayers.addAll(result.getLayers());
				layerSelect.setValues(sourceLayers, geodesk.getMainMapLayers(), geodesk.isPublic());
			}

		});
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		layerSelect.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		List<LayerDto> layers = layerSelect.getValues();
		geodesk.setMainMapLayers(layers);

		layerSelect.setDisabled(true);
		CommService.saveGeodesk(geodesk, SaveGeodeskRequest.SAVE_LAYERS);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		layerSelect.setDisabled(true);
		setGeodesk(geodesk);
		return true;
	}

	public void onGeodeskSelectionChange(GeodeskEvent geodeskEvent) {
		setGeodesk(geodeskEvent.getGeodesk());
	}

}
