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

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.domain.dto.MailAddressDto;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
/**
 * 
 * @author Oliver May
 *
 */
public class MailGrid extends ListGrid {

	// public static final String FLD_ID = "id";
	private static final String FLD_NAME = "name";

	private static final String FLD_EMAIL = "email";

	private static final String FLD_OBJECT = "object";

	private ListGridRecord rollOverRecord;

	private HLayout rollOverCanvas;

	private boolean changed;

	public MailGrid() {
		super();
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);
		setCanEdit(true);
		setEditEvent(ListGridEditEvent.CLICK);
		setEditByCell(true);

		addEditCompleteHandler(new EditCompleteHandler() {

			public void onEditComplete(EditCompleteEvent event) {
				changed = true;
			}
		});

		RegExpValidator emailValidator = new RegExpValidator();
		emailValidator.setErrorMessage("Geen geldig email adres");
		emailValidator.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");

		// -- Fields ------------------------------------------------

		ListGridField name = new ListGridField(FLD_NAME, "Naam");
		name.setWidth("*");
		name.setType(ListGridFieldType.TEXT);
		name.setRequired(true);

		ListGridField email = new ListGridField(FLD_EMAIL, "Email adres");
		email.setType(ListGridFieldType.TEXT);
		email.setWidth("*");
		email.setRequired(true);
		email.setValidators(emailValidator);

		setFields(name, email);
		setSortField(0);
		setSortDirection(SortDirection.ASCENDING);

		// ----------------------------------------------------------

		clearData();
	}

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverRecord = this.getRecord(rowNum);

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(22);
			rollOverCanvas.setHeight(22);

			ImgButton deleteImg = new ImgButton();
			deleteImg.setShowDown(false);
			deleteImg.setShowRollOver(false);
			deleteImg.setLayoutAlign(Alignment.CENTER);
			deleteImg.setSrc(WidgetLayout.iconRemove);
			deleteImg.setPrompt("Verwijder email adres");
			deleteImg.setHeight(16);
			deleteImg.setWidth(16);
			deleteImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					SC.ask("Verwijderen", "Email adres \"" + rollOverRecord.getAttribute("name") + "\" verwijderen?",
							new BooleanCallback() {

								public void execute(Boolean value) {
									if (value) {
										removeData(rollOverRecord);
										changed = true;
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

	public void setData(List<MailAddressDto> data) {
		setShowEmptyMessage(false);
		setData(new ListGridRecord[] {});
		if (data != null) {
			for (MailAddressDto m : data) {
				addData(toGridRecord(m));
			}
		}
		markForRedraw();
		changed = false;
	}

	public List<MailAddressDto> getData() {
		List<MailAddressDto> res = new ArrayList<MailAddressDto>();
		for (Record r : getDataAsRecordList().toArray()) {
			res.add(toMailAddress(r));
		}
		return res;
	}

	public boolean hasChanged() {
		return changed;
	}

	// -------------------------------------------------

	private void clearData() {
		setData(new ListGridRecord[] {});
		setShowEmptyMessage(true);
		setEmptyMessage("<i>Adressen worden ingelezen... <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");
		markForRedraw();
		changed = false;
	}

	private ListGridRecord toGridRecord(MailAddressDto mail) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_NAME, mail.getName());
		record.setAttribute(FLD_EMAIL, mail.getEmail());
		record.setAttribute(FLD_OBJECT, mail);
		return record;
	}

	private MailAddressDto toMailAddress(Record r) {
		MailAddressDto mail = (MailAddressDto) r.getAttributeAsObject(FLD_OBJECT);
		if (mail == null) {
			mail = new MailAddressDto();
		}

		mail.setName(r.getAttribute(FLD_NAME));
		mail.setEmail(r.getAttribute(FLD_EMAIL));
		return mail;
	}
}
