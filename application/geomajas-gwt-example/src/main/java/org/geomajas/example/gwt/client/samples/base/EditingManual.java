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

package org.geomajas.example.gwt.client.samples.base;

import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Simple editing manual to assist users in the editing showcases.
 * 
 * @author Pieter De Graef
 */
public class EditingManual extends TabSet {

	private static final String CREATE_FEATURE_PAGE = "[ISOMORPHIC]/geomajas/example/pages/edit/create_feature.html";

	private static final String EDIT_FEATURE_PAGE = "[ISOMORPHIC]/geomajas/example/pages/edit/edit_feature.html";

	private static final String DELETE_FEATURE_PAGE = "[ISOMORPHIC]/geomajas/example/pages/edit/delete_feature.html";

	private static final String DONUT_PAGE = "[ISOMORPHIC]/geomajas/example/pages/edit/donut_polygon.html";

	public EditingManual() {
		Tab tab1 = new Tab();
		tab1.setTitle("Create a new feature");
		HTMLPane pane1 = new HTMLPane();
		pane1.setContentsURL(CREATE_FEATURE_PAGE);
		tab1.setPane(pane1);
		tab1.setCanClose(false);
		addTab(tab1);

		Tab tab2 = new Tab();
		tab2.setTitle("Edit a feature");
		HTMLPane pane2 = new HTMLPane();
		pane2.setContentsURL(EDIT_FEATURE_PAGE);
		tab2.setPane(pane2);
		tab2.setCanClose(false);
		addTab(tab2);

		Tab tab3 = new Tab();
		tab3.setTitle("Delete a feature");
		HTMLPane pane3 = new HTMLPane();
		pane3.setContentsURL(DELETE_FEATURE_PAGE);
		tab3.setPane(pane3);
		tab3.setCanClose(false);
		addTab(tab3);

		Tab tab4 = new Tab();
		tab4.setTitle("Create donut polygon");
		HTMLPane pane4 = new HTMLPane();
		pane4.setContentsURL(DONUT_PAGE);
		tab4.setPane(pane4);
		tab4.setCanClose(false);
		addTab(tab4);
	}
}
