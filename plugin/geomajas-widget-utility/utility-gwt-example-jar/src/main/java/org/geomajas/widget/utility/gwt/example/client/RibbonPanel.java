/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.utility.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonBarLayout;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonTabLayout;
import org.geomajas.widget.utility.gwt.example.client.i18n.WidgetUtilityMessages;

/**
 * Sample showing the ribbon.
 *
 * @author Joachim Van der Auwera
 */
public class RibbonPanel extends SamplePanel {

	public static final String TITLE = "WidgetUtilityRibbon";

	public static final WidgetUtilityMessages MESSAGES = GWT.create(WidgetUtilityMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new RibbonPanel();
		}
	};

	/**
	 * Get view panel.
	 *
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout main = new VLayout(5);
		main.setWidth100();
		main.setHeight100();

		ToolStrip banner = new ToolStrip();
		banner.setBorder("none");
		banner.setSize("100%", "53");
		banner.setBackgroundColor("#337428");
		banner.setBackgroundImage("[ISOMORPHIC]/images/geomajas_logo.png");
		banner.setBackgroundRepeat(BkgndRepeat.NO_REPEAT);
		main.addMember(banner);

		TabSet tabs = new TabSet();

		Tab tab1 = new Tab("Default Ribbon");
		tab1.setPane(getExample1());
		tabs.addTab(tab1);

		Tab tab2 = new Tab("Tabbed Ribbon");
		tab2.setPane(getExample2());
		tabs.addTab(tab2);

		Tab tab3 = new Tab("Custom Ribbon Widgets");
		tab3.setPane(getExample3());
		tabs.addTab(tab3);

		Tab tab4 = new Tab("Custom Ribbon Style");
		tab4.setPane(getExample4());
		tabs.addTab(tab4);

		Tab tab5 = new Tab("Microsoft Ribbon Style");
		tab5.setPane(getExample5());
		tabs.addTab(tab5);

		main.addMember(tabs);
		main.draw();

		return main;
	}

	private Canvas getExample1() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);

		MapWidget mapWidget = new MapWidget("mapGuwOsm", "appGuw");
		final RibbonBarLayout ribbonBar = new RibbonBarLayout(mapWidget, "appGuw", "guwRibbonBar1");
		ribbonBar.setSize("100%", "94px");

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		ToolStripButton btn1 = new ToolStripButton("Toggle group title");
		btn1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ribbonBar.setShowGroupTitles(!ribbonBar.isShowGroupTitles());
			}
		});
		toolStrip.addButton(btn1);
		layout.addMember(toolStrip);
		layout.addMember(ribbonBar);
		layout.addMember(mapWidget);
		return layout;
	}

	private Canvas getExample2() {
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

	private Canvas getExample3() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);

		MapWidget mapWidget = new MapWidget("mapGuwCountries", "appGuw");
		final RibbonBarLayout ribbon = new RibbonBarLayout(mapWidget, "appGuw", "guwRibbonBar2");
		ribbon.setSize("100%", "80px");

		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}

	private Canvas getExample4() {
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

	private Canvas getExample5() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);
		MapWidget mapWidget = new MapWidget("mapGuwMsOsm", "appGuw");
		final RibbonTabLayout ribbon = new RibbonTabLayout(mapWidget, "appGuw", "guwRibbon4");
		//ribbon.setRibbonBarMembersMargin(0);
		ribbon.setStyleName("msRibbon");
		ribbon.setSize("100%", "120px");

		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.ribbonDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[]{"classpath:org/geomajas/widget/utility/gwt/example/ribbon.xml",
				"classpath:org/geomajas/widget/utility/gwt/example/appGuw.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
