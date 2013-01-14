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

package org.geomajas.example.gwt.client.samples;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;

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

		Img logo = new Img("[ISOMORPHIC]/geomajas/geomajas_logo.png");
		logo.setWidth(600);
		logo.setHeight(220);
		logo.setMargin(30);
		logo.setLayoutAlign(Alignment.CENTER);

		HTMLPane pane = new HTMLPane();
		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(currentLocale)) {
			pane.setContentsURL("[ISOMORPHIC]/geomajas/example/pages/intro.html");
		} else if ("nl".equals(currentLocale)) {
			pane.setContentsURL("[ISOMORPHIC]/geomajas/example/pages/intro_nl.html");
		}

		layout.addMember(logo);
		layout.addMember(pane);

		return layout;
	}

	public String getDescription() {
		return null;
	}

	public String getSourceFileName() {
		return "IntroductionTab.txt";
	}

	public String[] getConfigurationFiles() {
		return null;
	}

	public String ensureUserLoggedIn() {
		return null;
	}
}
