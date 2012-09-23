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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

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

	// FIXME: i18n
	private static final String HELP_TEXT = "<b>Beide lijsten:</b><br />"
			+ "- Gebruik \"Drag &amp; Drop\" om items toe te voegen of te verwijderen, "
			+ "of selecteer een item en gebruik een van de pijltjes om het item toe te voegen of te verwijderen.<br />"
			+ "<b>Lijst Geselecteerde lagen:</b><br />" + "- Versleep items om de volgorde te wijzigen.<br />"
			+ "- Gebruik het contextmenu om mappen toe te voegen of te verwijderen.<br />"
			+ "- Gebruik de \"Roll-over\"-actie (knopje rechts van een item) om het item te wijzigen.";

	private LayerListGrid left;

	private LayerListGrid right;

	public LayerSelectPanel() {
		super(10);

		left = new LayerListGrid("Beschikbare lagen", false);
		right = new LayerListGrid("Geselecteerde lagen", true);
		right.setEmptyMessage("Geen lagen geselecteerd, de default configuratie wordt gebruikt.");

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				right.transferSelectedData(left);
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				left.transferSelectedData(right);
			}
		});

		Img help = new Img(DeskmanagerIcon.HELP_ICON, 24, 24);
		help.setTooltip(HELP_TEXT);
		help.setHoverWidth(350);
		help.setShowDisabled(false);
		help.setShowDown(false);

		VLayout buttons = new VLayout(10);
		buttons.addMember(add);
		buttons.addMember(remove);
		buttons.addMember(new LayoutSpacer());
		buttons.addMember(help);

		addMember(left);
		addMember(buttons);
		addMember(right);
	}

	public void clearValues() {
		left.selectAllRecords();
		left.removeSelectedData();

		right.selectAllRecords();
		right.removeSelectedData();
	}

	public void setValues(List<LayerDto> availableLayers, List<LayerDto> selectedLayers, boolean isPublic) {
		clearValues();
		if (availableLayers != null) {
			//Reverse order
			ListIterator<LayerDto> li = availableLayers.listIterator(availableLayers.size());
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
						left.addData(record);
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
				right.addData(record);
			}
		}
	}

	public List<LayerDto> getValues() {
		List<LayerDto> selectedLayers = new ArrayList<LayerDto>();
		for (ListGridRecord record : right.getRecords()) {
			LayerDto layer = (LayerDto) record.getAttributeAsObject(LayerListGrid.FLD_OBJECT);
			if (!selectedLayers.contains(layer)) {
				selectedLayers.add(layer);
			}
		}
		return selectedLayers;
	}
}
