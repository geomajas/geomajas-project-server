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

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

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
 * 
 */
public class LayerListGrid extends ListGrid {

	public static final String FLD_NAME = "name";

	public static final String FLD_PUBLIC = "public";

	public static final String FLD_OBJECT = "object";

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

		ListGridField publicFld = new ListGridField(FLD_PUBLIC, "Publiek");
		publicFld.setType(ListGridFieldType.BOOLEAN);
		publicFld.setWidth(90);
		publicFld.setPrompt("Aan: laag kan geraadpleegd worden in een publiek loket.");

		setFields(nameFld, publicFld);
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(22);
			rollOverCanvas.setHeight(22);

			ImgButton editProps = new ImgButton();
			editProps.setShowDown(false);
			editProps.setShowRollOver(false);
			editProps.setLayoutAlign(Alignment.CENTER);
			editProps.setSrc(DeskmanagerIcon.IMG_SRC_COG);
			editProps.setPrompt("Configureren");
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
