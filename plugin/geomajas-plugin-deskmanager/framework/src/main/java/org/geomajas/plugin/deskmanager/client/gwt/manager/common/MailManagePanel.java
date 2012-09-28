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

import java.util.List;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.dto.MailAddressDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Manages a set of mailaddresses, doesn't care / know where they come from, that's up to you.
 * 
 * @author Kristof Heirwegh
 */
public class MailManagePanel extends VLayout {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private IButton buttonNew;

	private MailGrid grid;

	public MailManagePanel() {
		super(10);

		grid = new MailGrid();

		buttonNew = new IButton(MESSAGES.mailManageNewButtonText());
		buttonNew.setIcon(WidgetLayout.iconAdd);
		buttonNew.setAutoFit(true);
		buttonNew.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				grid.startEditingNew();
			}
		});

		addMember(grid);
		addMember(buttonNew);
	}

	/**
	 * @param values
	 * @param type new items are set to this type
	 */
	public void setValues(List<MailAddressDto> values) {
		grid.setData(values);
	}

	public List<MailAddressDto> getValues() {
		return grid.getData();
	}

	public boolean hasChanged() {
		return grid.hasChanged();
	}

	public boolean hasErrors() {
		return grid.hasErrors();
	}
}
