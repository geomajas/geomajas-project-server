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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

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

/**
 * @author Kristof Heirwegh
 */
public class BlueprintGrid extends ListGrid implements BlueprintHandler {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	public static final String FLD_ID = "id";
	
	public static final String FLD_NAME = "name";
	
	private static final String FLD_LIMIT_TO_TERRITORY = "limitToLoketTerritory";

	public static final String FLD_PUBLIC = "public";
	
	public static final String FLD_ACTIVE = "active";
	
	public static final String FLD_ACTIONS = "actions";

	public static final String FLD_GEODESKSACTIVE = "geodesksActive";

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	private Map<String, BlueprintDto> blueprints = new HashMap<String, BlueprintDto>();

	private static final int FLD_ACTIONS_WIDTH = 60;

	public BlueprintGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);

		// -- Fields --------------------------------------------------------

		ListGridField name = new ListGridField(FLD_NAME, MESSAGES.blueprintGridColumnName());
		name.setWidth("*");
		name.setType(ListGridFieldType.TEXT);

		ListGridField limitTerritory = new ListGridField(FLD_LIMIT_TO_TERRITORY,
				MESSAGES.blueprintGridColumnLimitToTerritory());
		limitTerritory.setType(ListGridFieldType.BOOLEAN);
		limitTerritory.setWidth(140);
		limitTerritory.setPrompt(MESSAGES.settingsLimitToTerritoryAdministratorTooltip());

		ListGridField publicUse = new ListGridField(FLD_PUBLIC, MESSAGES.blueprintGridColumnPublic());
		publicUse.setType(ListGridFieldType.BOOLEAN);
		publicUse.setWidth(100);
		publicUse.setPrompt(MESSAGES.blueprintAttributePublicTooltip());

		ListGridField active = new ListGridField(FLD_ACTIVE, MESSAGES.blueprintGridColumnActiv());
		active.setType(ListGridFieldType.BOOLEAN);
		active.setWidth(100);
		active.setPrompt(MESSAGES.blueprintActivTooltip());

		ListGridField geodesksActive = new ListGridField(FLD_GEODESKSACTIVE,
				MESSAGES.blueprintAttributeGeodesksActiv());
		geodesksActive.setType(ListGridFieldType.BOOLEAN);
		geodesksActive.setWidth(135);
		geodesksActive.setPrompt(MESSAGES.blueprintAttributeGeodesksActivTooltip());

		
		ListGridField actions = new ListGridField(FLD_ACTIONS, MESSAGES.blueprintGridColumnActions());
		actions.setType(ListGridFieldType.ICON);
		actions.setWidth(FLD_ACTIONS_WIDTH);
		actions.setCanEdit(false);
		actions.setPrompt(MESSAGES.bleuprintGridColumnActionsTooltip());
		
		setFields(name, limitTerritory, publicUse, active, geodesksActive, actions);
		// initially sort on blueprint name
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

		Whiteboard.registerHandler(this);
		readData();
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
			deleteImg.setPrompt(MESSAGES.blueprintGridActionsColumnRemoveTooltip());
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					SC.ask(MESSAGES.removeTitle(), MESSAGES.blueprintRemoveConfirmQuestion(
							rollOverRecord.getAttribute(FLD_NAME)), new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								CommService.deleteBlueprint(blueprints.get(rollOverRecord.getAttribute(FLD_ID)));
							}
						}
					});
				}
			});
			rollOverCanvas.addMember(deleteImg);
		}
		return rollOverCanvas;
	}

	// ----------------------------------------------------------

	private void readData() {
		clearData();

		setShowEmptyMessage(true);
		setEmptyMessage("<i>" + MESSAGES.blueprintsLoading() + " <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");
		redraw();

		CommService.getBlueprints(new DataCallback<List<BlueprintDto>>() {

			public void execute(List<BlueprintDto> result) {
				for (BlueprintDto bp : result) {
					blueprints.put(bp.getId(), bp);
					ListGridRecord record = toGridRecord(bp);
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
		blueprints.clear();
	}

	private ListGridRecord toGridRecord(BlueprintDto blueprint) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_ID, blueprint.getId());
		record.setAttribute(FLD_NAME, blueprint.getName());
		record.setAttribute(FLD_LIMIT_TO_TERRITORY, blueprint.isLimitToCreatorTerritory());
		record.setAttribute(FLD_PUBLIC, blueprint.isPublic());
		record.setAttribute(FLD_ACTIVE, blueprint.isActive());
		record.setAttribute(FLD_GEODESKSACTIVE, blueprint.isGeodesksActive());
		return record;
	}

	public void onBlueprintChange(BlueprintEvent bpe) {
		BlueprintDto old = blueprints.remove(bpe.getBlueprint().getId());
		if (old != null && getRecordList() != null) {
			Record oldr = getRecordList().find(FLD_ID, old.getId());
			removeData(oldr);
		}
		if (!bpe.isDeleted()) {
			blueprints.put(bpe.getBlueprint().getId(), bpe.getBlueprint());
			ListGridRecord record = toGridRecord(bpe.getBlueprint());
			addData(record);
			if (bpe.isNewInstance()) {
				deselectAllRecords();
				selectRecord(record);
			}
		}
	}
}
