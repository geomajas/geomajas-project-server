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

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
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
 *
 */
public class ChooseUserAppNameWindow extends Window {

	private static final int FORMITEM_WIDTH = 300;

	private DataCallback<String> callback;

	private DynamicForm form;

	private IButton save;

	private SelectItem userappName;

	/**
	 * @param callback returns userAppName or null if cancelled.
	 */
	public ChooseUserAppNameWindow(DataCallback<String> callback) {
		this.callback = callback;

		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle("Selecteer Gebruikstoepassing");
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
		userappName.setRequiredMessage("Gelieve een gebruikstoepassing te selecteren.");
		userappName.setTitle("Selecteer een Gebruikstoepassing");
		userappName.setTooltip("<nobr>De Gebruikstoepassing die gebruikt zal worden"
				+ " voor loketten gebaseerd op deze blauwdruk.</nobr>");
		userappName.setValueMap(UserApplicationRegistry.getInstance().getLoketNames());

		form.setFields(userappName);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		save = new IButton("Aanmaken");
		save.setIcon(WidgetLayout.iconAdd);
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton("Annuleren");
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
				callback.execute(userappName.getValueAsString());
			}
			destroy();
		}
	}
}
