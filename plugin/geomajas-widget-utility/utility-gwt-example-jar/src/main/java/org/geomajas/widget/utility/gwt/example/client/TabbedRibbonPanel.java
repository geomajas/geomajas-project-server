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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonTabLayout;
import org.geomajas.widget.utility.gwt.example.client.i18n.WidgetUtilityMessages;

/**
 * Sample showing the ribbon.
 *
 * @author Pieter De Graef
 */
public class TabbedRibbonPanel extends SamplePanel {

	public static final String TITLE = "GuwTabbedRibbon";

	public static final WidgetUtilityMessages MESSAGES = GWT.create(WidgetUtilityMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new TabbedRibbonPanel();
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

		MapWidget mapWidget = new MapWidget("mapGuwWmsCountries", "appGuw");
		final RibbonTabLayout ribbon = new RibbonTabLayout(mapWidget, "appGuw", "guwRibbon1");
		ribbon.setSize("100%", "120px");

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		ToolStripButton btn1 = new ToolStripButton("Toggle group title");
		btn1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ribbon.getRibbonBar(0).setShowGroupTitles(!ribbon.getRibbonBar(0).isShowGroupTitles());
			}
		});
		toolStrip.addButton(btn1);
		layout.addMember(toolStrip);
		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.tabbedRibbonDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {"classpath:org/geomajas/widget/utility/gwt/example/context/ribbon.xml",
				"classpath:org/geomajas/widget/utility/gwt/example/context/appGuw.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
