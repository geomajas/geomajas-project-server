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
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import java.util.HashMap;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Window that asks for information when creating a new blueprint.
 * 
 * @author Oliver May
 *
 */
public class ChooseUserAppNameWindow extends Window {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final int FORMITEM_WIDTH = 300;

	private DataCallback<HashMap<String, Object>> callback;

	private DynamicForm form;

	private IButton save;

	private SelectItem userappName;

	private CheckboxItem publik;

	private TextItem blueprintName;

	/**
	 * @param dataCallback returns userAppName or null if cancelled.
	 */
	public ChooseUserAppNameWindow(DataCallback<HashMap<String, Object>> dataCallback) {
		this.callback = dataCallback;

		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle(MESSAGES.chooseAppTitle());
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);

		form = new DynamicForm();
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setTitleOrientation(TitleOrientation.TOP);

		userappName = new SelectItem();
		userappName.setRequired(true);
		userappName.setWidth(FORMITEM_WIDTH);
		userappName.setRequiredMessage(MESSAGES.chooseAppRequired());
		userappName.setTitle(MESSAGES.chooseAppTitle());
		userappName.setTooltip("<nobr>" + MESSAGES.chooseAppTooltip() + "</nobr>");
		userappName.setValueMap(UserApplicationRegistry.getInstance().getUserApplicationNames());

		publik = new CheckboxItem();
		publik.setRequired(true);
		publik.setTitle(MESSAGES.blueprintSettingsPublic());

		blueprintName = new TextItem();
		blueprintName.setRequired(true);
		blueprintName.setTitle(MESSAGES.blueprintSettingsNameBlueprint());
		blueprintName.setValue(MESSAGES.newBlueprintButtonText());

		form.setFields(userappName, publik, blueprintName);
		
		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		save = new IButton(MESSAGES.chooseAppCreate());
		save.setIcon(WidgetLayout.iconAdd);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
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
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("userApplication", userappName.getValueAsString());
				map.put("public", publik.getValue());
				map.put("name", blueprintName.getValueAsString());
				
				callback.execute(map);
			}
			destroy();
		}
	}
}
