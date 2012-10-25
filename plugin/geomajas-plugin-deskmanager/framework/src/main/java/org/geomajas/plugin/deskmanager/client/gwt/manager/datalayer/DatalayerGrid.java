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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import java.util.List;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.LayerModelEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.LayerModelHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

/**
 * @author Kristof Heirwegh
 */
public class DatalayerGrid extends ListGrid implements LayerModelHandler {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	public static final String FLD_ID = "id";

	private static final String FLD_NAME = "name";

	private static final String FLD_PUBLIC = "public";

	private static final String FLD_OWNER = "owner";

	private static final String FLD_ACTIVE = "active";

	private static final String FLD_ACTIONS = "actions";
	private static final int FLD_ACTIONS_WIDTH = 60;

	private static final String FLD_TYPE = "type";

	private static final String FLD_OBJECT = "_object";

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	public DatalayerGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);

		// -- Fields --------------------------------------------------------

		ListGridField name = new ListGridField(FLD_NAME, MESSAGES.datalayerGridColumnLayerName());
		name.setType(ListGridFieldType.TEXT);
		name.setWidth("*");

		ListGridField owner = new ListGridField(FLD_OWNER, MESSAGES.datalayerGridColumnGroup());
		owner.setType(ListGridFieldType.TEXT);
		owner.setWidth(150);

		ListGridField type = new ListGridField(FLD_TYPE, MESSAGES.datalayerGridColumnLayerType());
		type.setType(ListGridFieldType.TEXT);
		type.setWidth(100);

		ListGridField publicUse = new ListGridField(FLD_PUBLIC, MESSAGES.datalayerGridColumnPublic());
		publicUse.setType(ListGridFieldType.BOOLEAN);
		publicUse.setWidth(70);
		publicUse.setPrompt(MESSAGES.datalayerGridColumnPublicTooltip());

		ListGridField active = new ListGridField(FLD_ACTIVE, MESSAGES.datalayerGridColumnActive());
		active.setType(ListGridFieldType.BOOLEAN);
		active.setWidth(70);
		active.setPrompt(MESSAGES.datalayerGridColumnActiveTooltip());

		ListGridField actions = new ListGridField(FLD_ACTIONS, MESSAGES.datalayerGridColumnActions());
		actions.setType(ListGridFieldType.ICON);
		actions.setWidth(FLD_ACTIONS_WIDTH);
		actions.setCanEdit(false);

		setFields(name, owner, type, publicUse, active, actions);
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

		Whiteboard.registerHandler(this);
	}

	@Override
	public void destroy() {
		Whiteboard.unregisterHandler(this);
		super.destroy();
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(FLD_ACTIONS_WIDTH);
			rollOverCanvas.setHeight(22);

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc(WidgetLayout.iconRemove);
			deleteImg.setPrompt(MESSAGES.datalayerGridActionsColumnRemoveTooltip());
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					final LayerModelDto model = (LayerModelDto) rollOverRecord.getAttributeAsObject(FLD_OBJECT);
					//Also allow to remove system layers, they will however be recreated if they still exist after 
					//server restart.
//					if (model.isReadOnly()) {
//						SC.warn(MESSAGES.datalayerGridWarnPublicCannotBeRemoved());
//					} else {
						NotificationWindow.showInfoMessage(MESSAGES.datalayerGridControlOnLayerUseBeforeRemove());
						ManagerCommandService.checkLayerModelInUse(model, new DataCallback<Boolean>() {

							public void execute(Boolean result) {
								NotificationWindow.clearMessages();
								if (result) {
									SC.warn(MESSAGES.datalayerGridCannotRemoveLayerInUse());
								} else {
									SC.ask(MESSAGES.removeTitle(), MESSAGES.datalayerGridRemoveConfirmQuestion(
											model.getName()), new BooleanCallback() {

												public void execute(Boolean value) {
													if (value) {
														ManagerCommandService.deleteLayerModel(model);
													}
												}
											});
								}
							}
						});
//					}
				}
			});
			rollOverCanvas.addMember(new LayoutSpacer());
			rollOverCanvas.addMember(deleteImg);
			rollOverCanvas.addMember(new LayoutSpacer());
		}
		return rollOverCanvas;
	}

	// ----------------------------------------------------------

	public void readData() {
		clearData();

		setShowEmptyMessage(true);
		setEmptyMessage("<i>" + MESSAGES.datalayerGridLoading() + " <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");
		redraw();

		ManagerCommandService.getLayerModels(new DataCallback<List<LayerModelDto>>() {

			public void execute(List<LayerModelDto> result) {
				for (LayerModelDto lmd : result) {
					ListGridRecord record = toGridRecord(lmd);
					addData(record);
				}
				setShowEmptyMessage(false);
				redraw();
			}
		});
	}

	void clearData() {
		deselectAllRecords();
		setData(new ListGridRecord[] {});
	}

	private ListGridRecord toGridRecord(LayerModelDto lm) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_ID, lm.getId());
		record.setAttribute(FLD_NAME, lm.getName());
		record.setAttribute(FLD_TYPE, lm.getLayerType());
		record.setAttribute(FLD_PUBLIC, lm.isPublic());
		record.setAttribute(FLD_ACTIVE, lm.isActive());
		record.setAttribute(FLD_OWNER, lm.getOwner());
		record.setAttribute(FLD_ACTIONS, " ");
		record.setAttribute(FLD_OBJECT, lm);
		return record;
	}

	public void onLayerModelChange(LayerModelEvent lme) {
		if (getRecordList() != null) {
			Record oldr = getRecordList().find(FLD_ID, lme.getLayerModel().getId());
			if (oldr != null) {
				removeData(oldr);
			}
		}
		if (!lme.isDeleted()) {
			ListGridRecord record = toGridRecord(lme.getLayerModel());
			addData(record);
			if (lme.isNewInstance()) {
				deselectAllRecords();
				selectRecord(record);
			}
		}
	}
}
