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

package org.geomajas.gwt.client.samples;

import org.geomajas.gwt.client.samples.base.SamplePanel;

import com.google.gwt.i18n.client.LocaleInfo;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Show introduction message.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class IntroductionTab extends SamplePanel {

	public IntroductionTab() {
		super();
	}

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		Img logo = new Img("[ISOMORPHIC]/geomajas/temp/geomajas_logo.png");
		logo.setWidth(600);
		logo.setHeight(160);
		logo.setLayoutAlign(Alignment.CENTER);

		HTMLPane pane = new HTMLPane();
		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(currentLocale)) {
			pane.setContentsURL("[ISOMORPHIC]/geomajas/example/pages/list.html");
		} else if ("nl".equals(currentLocale)) {
			pane.setContentsURL("[ISOMORPHIC]/geomajas/example/pages/list_nl.html");
		}

		layout.addMember(logo);
		layout.addMember(pane);

		return layout;
	}

	public String getDescription() {
		return null;
	}

	public String getSourceFileName() {
		return "org.geomajas.gwt.client.samples.IntroductionTab.txt";
	}

	public String[] getConfigurationFiles() {
		return null;
	}

	public String ensureUserLoggedIn() {
		return null;
	}
}
