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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 *
 */
public class ChooseBlueprintWindow extends Window {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final int FORMITEM_WIDTH = 300;

	private DataCallback<String> callback;

	private DynamicForm form;

	private IButton save;

	private SelectItem blueprints;

	/**
	 * @param layer
	 * @param callback returns blueprintId or null if cancelled.
	 */
	public ChooseBlueprintWindow(DataCallback<String> callback) {
		this.callback = callback;

		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle(MESSAGES.chooseBlueprintTitle());
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		// setShowModalMask(true);

		form = new DynamicForm();
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setTitleOrientation(TitleOrientation.TOP);

		blueprints = new SelectItem();
		blueprints.setRequired(true);
		blueprints.setWidth(FORMITEM_WIDTH);
		blueprints.setRequiredMessage(MESSAGES.chooseBlueprintRequired());
		blueprints.setTitle(MESSAGES.chooseBlueprintTitle());
		blueprints.setTooltip("<nobr>" + MESSAGES.chooseBlueprintTooltip() + "</nobr>");
		blueprints.setDisabled(true);
		blueprints.setValue(MESSAGES.chooseBlueprintLoading());

		form.setFields(blueprints);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		save = new IButton(MESSAGES.chooseBlueprintCreate());
		save.setIcon(WidgetLayout.iconAdd);
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.setAutoFit(true);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});

		buttons.addMember(save);
		buttons.addMember(cancel);

		// ----------------------------------------------------------

		VLayout vl = new VLayout(10);
		vl.setMargin(10);
		vl.addMember(form);
		vl.addMember(buttons);
		addItem(vl);

		// ----------------------------------------------------------

		ManagerCommandService.getBlueprints(new DataCallback<List<BlueprintDto>>() {

			public void execute(List<BlueprintDto> result) {
				if (result.size() > 0) {
					LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
					for (BlueprintDto blueprintDto : result) {
						if (blueprintDto.isActive()) {
							// only needed for superadmin as list isn't filtered there
							valueMap.put(blueprintDto.getId(), blueprintDto.getName());
						}
					}
					blueprints.setValueMap(valueMap);
					blueprints.clearValue();
					blueprints.setDisabled(false);
				} else {
					SC.say(MESSAGES.chooseBlueprinWarnNoBlueprints());
					save.setDisabled(true);
				}
			}
		});
	}

	private void cancelled() {
		hide();
		if (callback != null) {
			callback.execute(null);
		}
		destroy();
	}

	private void saved() {
		if (form.validate()) {
			hide();
			if (callback != null) {
				callback.execute(blueprints.getValueAsString());
			}
			destroy();
		}
	}
}
