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

import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Used by LayerSelectPanel.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * @author An Buyle
 * 
 */
public class LayerListGrid extends ListGrid {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	public static final String FLD_NAME = "name";

	public static final String FLD_PUBLIC = "public";
	
	public static final String FLD_ACTIONS = "actions";
	private static final int FLD_ACTIONS_WIDTH = 60;

	public static final String FLD_OBJECT = "object";
	
	public static final String FLD_USER = "userLayer";

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	public LayerListGrid(String title, boolean editable) {
		super();
		setWidth100();
		setHeight100();
		
		if (editable) {
			setShowRollOverCanvas(true);
			setCanSort(false); // this messes things up
		} else {
			setShowRollOverCanvas(false);
		}
		setCanReorderRecords(true);
		setDragDataAction(DragDataAction.MOVE);
		setCanAcceptDroppedRecords(true);
		setCanDragRecordsOut(true);
		setSelectionType(SelectionStyle.MULTIPLE);
		setShowAllRecords(true);

		ListGridField nameFld = new ListGridField(FLD_NAME);
		nameFld.setWidth("*");
		nameFld.setTitle(title);

		ListGridField publicFld = new ListGridField(FLD_PUBLIC, MESSAGES.datalayerGridColumnPublic());
		publicFld.setType(ListGridFieldType.BOOLEAN);
		publicFld.setWidth(70);
		publicFld.setPrompt(MESSAGES.layerListGridColumnPublicTooltip());

		ListGridField userFld = new ListGridField(FLD_USER, MESSAGES.datalayerGridColumnUserLayer());
		userFld.setType(ListGridFieldType.BOOLEAN);
		userFld.setWidth(70);
		userFld.setPrompt(MESSAGES.datalayerGridColumnUserLayerTooltip());

		if (editable) {
			ListGridField actionsFld = new ListGridField(FLD_ACTIONS, MESSAGES.gridColumnActions());
			actionsFld.setType(ListGridFieldType.ICON);
			actionsFld.setWidth(FLD_ACTIONS_WIDTH);
			actionsFld.setCanEdit(false);
			setFields(nameFld, publicFld, actionsFld);
		} else {
			setFields(nameFld, publicFld, userFld);	
		}
		
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		
		rollOverRecord = getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(FLD_ACTIONS_WIDTH);
			rollOverCanvas.setHeight(22);

			ImgButton editProps = new ImgButton();
			editProps.setShowDown(false);
			editProps.setShowRollOver(false);
			editProps.setLayoutAlign(Alignment.CENTER);
			editProps.setSrc(DeskmanagerIcon.IMG_SRC_COG);
			editProps.setPrompt(MESSAGES.layerListGridConfigurate());
			editProps.setShowDisabledIcon(false);
			editProps.setHeight(16);
			editProps.setWidth(16);
			editProps.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

				public void onClick(ClickEvent event) {
					configureLayer(rollOverRecord);
				}
			});
			rollOverCanvas.addMember(editProps);
		}
		return rollOverCanvas;
	}

	private void configureLayer(final ListGridRecord record) {
		LayerDto layer = (LayerDto) record.getAttributeAsObject(FLD_OBJECT);

		LayerConfigurationWindow window = new LayerConfigurationWindow(layer, new BooleanCallback() {

			public void execute(Boolean value) {
				updateData(record);
			}
		});
		window.show();
	}
}
