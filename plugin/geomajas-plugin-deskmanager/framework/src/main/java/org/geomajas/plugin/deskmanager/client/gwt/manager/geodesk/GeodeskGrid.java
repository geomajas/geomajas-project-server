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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.BeheerConstants;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;

import com.google.gwt.user.client.Window;
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
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GeodeskGrid extends ListGrid implements GeodeskHandler, BlueprintHandler {

	public static final String FLD_ID = "id";

	private static final String FLD_NAME = "name";

	private static final String FLD_BLUEPRINT = "blueprint";

	private static final String FLD_AUTHOR = "author";

	private static final String FLD_PUBLIC = "public";

	private static final String FLD_ACTIVE = "active";

	private static final String FLD_ACTIONS = "actions";

	private static final String FLD_GEODESKID = "geodeskId";

	private static final String FLD_OBJECT = "_object_";

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	private Map<String, GeodeskDto> geodesks = new HashMap<String, GeodeskDto>();

	public GeodeskGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);

		// -- Fields --------------------------------------------------------

		ListGridField name = new ListGridField(FLD_NAME, "Naam Geodesk");
		name.setWidth("*");
		name.setType(ListGridFieldType.TEXT);

		ListGridField blueprint = new ListGridField(FLD_BLUEPRINT, "Naam Blauwdruk");
		blueprint.setType(ListGridFieldType.TEXT);
		blueprint.setWidth("*");

		ListGridField geodeskId = new ListGridField(FLD_GEODESKID, "LoketId");
		geodeskId.setType(ListGridFieldType.TEXT);
		geodeskId.setWidth("*");

		ListGridField author = new ListGridField(FLD_AUTHOR, "Auteur");
		author.setType(ListGridFieldType.TEXT);
		author.setWidth("*");
		author.setPrompt("De persoon die het laatst wijzigingen heeft aangebracht aan dit loket.");

		ListGridField publicUse = new ListGridField(FLD_PUBLIC, "Publiek");
		publicUse.setType(ListGridFieldType.BOOLEAN);
		publicUse.setWidth(70);
		publicUse
				.setPrompt("Aan: loket kan geraadpleegd worden zonder aanmelden.<br />" +
						"Uit: loket kan enkel geraadpleegd worden na aanmelden (LB of VO).");

		ListGridField active = new ListGridField(FLD_ACTIVE, "Actief");
		active.setType(ListGridFieldType.BOOLEAN);
		active.setWidth(70);
		active.setPrompt("Aan: betekent dat loket kan geraadpleegd worden.");

		ListGridField actions = new ListGridField(FLD_ACTIONS, "Acties");
		actions.setType(ListGridFieldType.TEXT);
		actions.setWidth(60);
		actions.setPrompt("Acties: Voorbeeld loket tonen en Geodesk verwijderen.");

		setFields(name, blueprint, geodeskId, author, publicUse, active, actions);
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

		Whiteboard.registerHandler((GeodeskHandler) this);
		Whiteboard.registerHandler((BlueprintHandler) this);
		readData();
		
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				String id = event.getRecord().getAttribute(FLD_GEODESKID);
				String url = GeodeskHtmlCode.createPreviewUrl(id);
				Window.open(url, "_blank", null);
			}
		});
	}

	@Override
	public void destroy() {
		Whiteboard.unregisterHandler((GeodeskHandler) this);
		Whiteboard.unregisterHandler((BlueprintHandler) this);
		super.destroy();
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(44);
			rollOverCanvas.setHeight(22);

			ImgButton previewImg = new ImgButton();
			previewImg.setShowDown(false);
			previewImg.setShowRollOver(false);
			previewImg.setLayoutAlign(Alignment.CENTER);
			previewImg.setSrc(BeheerConstants.ICON_OPENSAMPLELOKET);
			previewImg.setPrompt("Open voorbeeld loket");
			previewImg.setHeight(16);
			previewImg.setWidth(16);
			previewImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					DockableWindow window = new DockableWindow();
					window.setTitle("Geodesk: " + rollOverRecord.getAttribute(FLD_NAME));
					window.setAutoCenter(true);
					window.setWidth("90%");
					window.setHeight("90%");
					window.setSrc(GeodeskHtmlCode.createPreviewUrl(rollOverRecord.getAttribute(FLD_GEODESKID)));
					window.setContentsType("page");
					window.show();
				}
			});

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc(WidgetLayout.iconRemove);
			deleteImg.setPrompt("Verwijder loket");
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					SC.ask("Verwijderen", "Geodesk \"" + rollOverRecord.getAttribute("name")
							+ "\" verwijderen?<br /><br />Deze actie kan niet ongedaan gemaakt worden.",
							new BooleanCallback() {

								public void execute(Boolean value) {
									if (value) {
										CommService.deleteGeodesk(geodesks.get(rollOverRecord.getAttribute(FLD_ID)));
									}
								}
							});
				}
			});
			rollOverCanvas.addMember(previewImg);
			rollOverCanvas.addMember(deleteImg);
		}
		return rollOverCanvas;
	}

	// ----------------------------------------------------------

	public void readData() {
		clearData();

		setShowEmptyMessage(true);
		setEmptyMessage("<i>Geodesks worden geladen... <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");
		redraw();

		CommService.getGeodesks(new DataCallback<List<GeodeskDto>>() {

			public void execute(List<GeodeskDto> result) {
				for (GeodeskDto bp : result) {
					geodesks.put(bp.getId(), bp);
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
		geodesks.clear();
	}

	private ListGridRecord toGridRecord(GeodeskDto loket) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_ID, loket.getId());
		record.setAttribute(FLD_NAME, loket.getName());
		record.setAttribute(FLD_BLUEPRINT, loket.getBlueprint().getName());
		record.setAttribute(FLD_AUTHOR, loket.getLastEditBy());
		record.setAttribute(FLD_PUBLIC, loket.isPublic());
		record.setAttribute(FLD_ACTIVE, loket.isActive() && loket.getBlueprint().isGeodesksActive());
		record.setAttribute(FLD_ACTIONS, " ");
		record.setAttribute(FLD_GEODESKID, loket.getGeodeskId());
		record.setAttribute(FLD_OBJECT, loket);
		return record;
	}

	// -- Handlers --

	public void onGeodeskChange(GeodeskEvent le) {
		GeodeskDto old = geodesks.remove(le.getGeodesk().getId());
		if (old != null && getRecordList() != null) {
			Record oldr = getRecordList().find(FLD_ID, old.getId());
			removeData(oldr);
		}
		if (!le.isDeleted()) {
			geodesks.put(le.getGeodesk().getId(), le.getGeodesk());
			ListGridRecord record = toGridRecord(le.getGeodesk());
			addData(record);
			if (le.isNewInstance()) {
				deselectAllRecords();
				selectRecord(record);
			}
		}
	}

	public void onBlueprintChange(BlueprintEvent bpe) {
		if (!bpe.isDeleted() && !bpe.isNewInstance()) { // just changed
			for (Record r : getDataAsRecordList().toArray()) {
				GeodeskDto l = (GeodeskDto) r.getAttributeAsObject(FLD_OBJECT);
				if (l.getBlueprint().getId().equals(bpe.getBlueprint().getId())) {
					r.setAttribute(FLD_ACTIVE, l.isActive() && bpe.getBlueprint().isGeodesksActive());
					refreshRow(getRecordIndex(r));
					if (getSelectedRecord() != null && getSelectedRecord().equals(r)) {
						deselectAllRecords();
						selectRecord(r);
					}
				}
			}
		}
	}
}
