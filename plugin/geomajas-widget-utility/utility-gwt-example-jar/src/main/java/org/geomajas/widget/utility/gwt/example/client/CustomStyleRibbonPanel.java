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

package org.geomajas.widget.utility.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonBarLayout;
import org.geomajas.widget.utility.gwt.example.client.i18n.WidgetUtilityMessages;

/**
 * Sample showing the ribbon.
 *
 * @author Pieter De Graef
 */
public class CustomStyleRibbonPanel extends SamplePanel {

	public static final String TITLE = "GuwCustomStyleRibbon";

	public static final WidgetUtilityMessages MESSAGES = GWT.create(WidgetUtilityMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CustomStyleRibbonPanel();
		}
	};

	/**
	 * Get view panel.
	 *
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);

		MapWidget mapWidget = new MapWidget("mapGuwWms", "appGuw");
		final RibbonBarLayout ribbon = new RibbonBarLayout(mapWidget, "appGuw", "guwRibbonBar3");
		ribbon.setStyleName("myRibbon");
		ribbon.setSize("100%", "94px");

		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.customStyleRibbonDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {"classpath:org/geomajas/widget/utility/gwt/example/ribbon.xml",
				"classpath:org/geomajas/widget/utility/gwt/example/appGuw.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
