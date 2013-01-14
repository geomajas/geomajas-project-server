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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Contains two layertreepanels which are used to make a selection (target) of some layers (source).
 * 
 * @author Kristof Heirwegh
 */
public class LayerSelectPanel extends HLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	
	private LayerListGrid sourceLayersGrid;

	private LayerListGrid targetLayersGrid;

	public LayerSelectPanel() {
		super(10);

		sourceLayersGrid = new LayerListGrid(MESSAGES.layerSelectAvailableLayers(), false);
		targetLayersGrid = new LayerListGrid(MESSAGES.layerSelectSelectedLayers(), true);
		targetLayersGrid.setEmptyMessage(MESSAGES.layerSelectSelectedLayersTooltip());

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				targetLayersGrid.transferSelectedData(sourceLayersGrid);
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sourceLayersGrid.transferSelectedData(targetLayersGrid);
			}
		});

		Img help = new Img(DeskmanagerIcon.HELP_ICON, 24, 24);
		help.setTooltip(MESSAGES.layerSelectPanelHelpText());
		help.setHoverWidth(350);
		help.setShowDisabled(false);
		help.setShowDown(false);

		VLayout buttons = new VLayout(10);
		buttons.addMember(add);
		buttons.addMember(remove);
		buttons.addMember(new LayoutSpacer());
		buttons.addMember(help);

		addMember(sourceLayersGrid);
		addMember(buttons);
		addMember(targetLayersGrid);
	}

	public void clearValues() {
		sourceLayersGrid.selectAllRecords();
		sourceLayersGrid.removeSelectedData();

		targetLayersGrid.selectAllRecords();
		targetLayersGrid.removeSelectedData();
	}

	/**
	 * Set data for the LayerSelectPanel.
	 * 
	 * @param sourceLayers the available layers from the blueprint or userapplication
	 * @param userLayers the available layers for the user
	 * @param selectedLayers the layers already selected
	 * @param isPublic whether to include public layers.
	 */
	public void setValues(List<LayerDto> sourceLayers, List<LayerDto> userLayers, List<LayerDto> selectedLayers, 
			boolean isPublic) {
		clearValues();
		if (sourceLayers != null) {
			//Reverse order
			ListIterator<LayerDto> li = sourceLayers.listIterator(sourceLayers.size());
			while (li.hasPrevious()) {
				LayerDto layer = li.previous();
				if (isPublic && !layer.getLayerModel().isPublic()) {
					// Ignore layer
				} else {
					if (selectedLayers == null || !selectedLayers.contains(layer)) {
						ListGridRecord record = new ListGridRecord();
						if (layer.getClientLayerInfo() != null) {
							record.setAttribute(LayerListGrid.FLD_NAME, layer.getClientLayerInfo().getLabel());
						} else if (layer.getReferencedLayerInfo() != null) {
							record.setAttribute(LayerListGrid.FLD_NAME, layer.getReferencedLayerInfo().getLabel());
						}
						record.setAttribute(LayerListGrid.FLD_PUBLIC, layer.getLayerModel().isPublic());
						record.setAttribute(LayerListGrid.FLD_OBJECT, layer);
						sourceLayersGrid.addData(record);
					}
				}
			}
		}
		
		if (userLayers != null) {
			//Reverse order
			ListIterator<LayerDto> li = userLayers.listIterator(userLayers.size());
			while (li.hasPrevious()) {
				LayerDto layer = li.previous();
				if (isPublic && !layer.getLayerModel().isPublic()) {
					// Ignore layer
				} else {
					if ( (selectedLayers == null || !selectedLayers.contains(layer)) &&   
							(sourceLayers == null || !sourceLayers.contains(layer)) ) {
						ListGridRecord record = new ListGridRecord();
						if (layer.getClientLayerInfo() != null) {
							record.setAttribute(LayerListGrid.FLD_NAME, layer.getClientLayerInfo().getLabel());
						} else if (layer.getReferencedLayerInfo() != null) {
							record.setAttribute(LayerListGrid.FLD_NAME, layer.getReferencedLayerInfo().getLabel());
						}
						record.setAttribute(LayerListGrid.FLD_PUBLIC, layer.getLayerModel().isPublic());
						record.setAttribute(LayerListGrid.FLD_OBJECT, layer);
						record.setAttribute(LayerListGrid.FLD_USER, true);
						sourceLayersGrid.addData(record);
					}
				}
			}
		}
		
		if (selectedLayers != null) {
			//Reverse order
			ListIterator<LayerDto> li = selectedLayers.listIterator(selectedLayers.size());
			while (li.hasPrevious()) {
				LayerDto layer = li.previous();
				ListGridRecord record = new ListGridRecord();
				if (layer.getClientLayerInfo() != null) {
					record.setAttribute(LayerListGrid.FLD_NAME, layer.getClientLayerInfo().getLabel());
				} else if (layer.getReferencedLayerInfo() != null) {
					record.setAttribute(LayerListGrid.FLD_NAME, layer.getReferencedLayerInfo().getLabel());
				}
				record.setAttribute(LayerListGrid.FLD_PUBLIC, layer.getLayerModel().isPublic());
				record.setAttribute(LayerListGrid.FLD_OBJECT, layer);
				targetLayersGrid.addData(record);
			}
		} else {
			
		}
	}

	public List<LayerDto> getValues() {
		// reverse order!
		List<LayerDto> selectedLayers = new ArrayList<LayerDto>();
		for (int i = targetLayersGrid.getRecords().length - 1 ; i >= 0 ; i--) {
			ListGridRecord record = targetLayersGrid.getRecord(i);
			LayerDto layer = (LayerDto) record.getAttributeAsObject(LayerListGrid.FLD_OBJECT);
			if (!selectedLayers.contains(layer)) {
				selectedLayers.add(layer);
			}
		}
		return selectedLayers;
	}
}
