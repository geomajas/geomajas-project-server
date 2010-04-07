/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.action.menu;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Menu item that tells the user something about the Geomajas technology.
 * 
 * @author Pieter De Graef
 */
public class AboutAction extends MenuAction {

	/**
	 * @param mapWidget
	 *            The map on this menu action is used.
	 */
	public AboutAction() {
		super(I18nProvider.getGlobal().aboutMenuTitle(), "[ISOMORPHIC]/geomajas/tips.png");
	}

	public void onClick(MenuItemClickEvent event) {
		VLayout layout = new VLayout();
		layout.setPadding(10);
		Img logo = new Img("[ISOMORPHIC]/geomajas/temp/geomajas_logo.png");
		layout.addMember(logo);
		HTMLFlow flow = new HTMLFlow("<h2>Geomajas " + Geomajas.getVersion() + "</h2>" + "<p>"
				+ I18nProvider.getGlobal().aboutCopyRight() + "</p>" + "<p>" + I18nProvider.getGlobal().aboutVisit()
				+ ": <a href='http://www.geomajas.org/'>http://www.geomajas.org/</a></p>");
		layout.addMember(flow);

		Window window = new Window();
		window.setTitle(I18nProvider.getGlobal().aboutMenuTitle());
		window.setWidth(400);
		window.setHeight(250);
		window.setAutoCenter(true);
		window.setPadding(10);
		window.addItem(layout);
		window.draw();
	}
}
