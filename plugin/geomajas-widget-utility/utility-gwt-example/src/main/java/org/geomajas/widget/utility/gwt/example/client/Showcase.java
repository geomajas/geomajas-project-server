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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.action.DropDownButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarRadioAction;
import org.geomajas.widget.utility.gwt.client.ribbon.DropDownRibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonBarLayout;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry.RibbonColumnCreator;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonTabLayout;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * <p>
 * Showcase for the Geomajas utility widgets plug-in.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint {

	public void onModuleLoad() {
		// Add my custom ribbon columns type to the registry:
		RibbonColumnRegistry.put("MyCustomColumn", new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new MyCustomRibbonColumn(mapWidget);
			}
		});

		// TODO you can't do this if you need the mapWidget.
		final RibbonButton theAnswer = new RibbonButton(new TheAnswerAction());
		RibbonColumnRegistry.put(TheAnswerAction.IDENTIFIER, new RibbonColumnCreator() {
			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return theAnswer;
			}
		});

		// TODO get from ribbon or registry by id once implemented.
		RibbonColumnRegistry.put(ChangeStateAction.IDENTIFIER, new RibbonColumnCreator() {
			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				RibbonColumn rc = new RibbonButton(new ChangeStateAction(theAnswer));
				return rc;
			}
		});
		
		final DropDownRibbonButton zoomDropDown = new DropDownRibbonButton(new DropDownButtonAction("[ISOMORPHIC]/images/magnifying-glass.png", "Zoom", ""));
		RibbonColumnRegistry.put("ZoomDropDown", new RibbonColumnCreator() {
			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				List<ButtonAction> actions = new ArrayList<ButtonAction>();
				for (ClientToolInfo tool : tools) {
					//Copied from RibbonColumnRegistry; generic implementation later?
					ToolbarBaseAction toolbarAction = ToolbarRegistry.getToolbarAction(tool.getId(), mapWidget);
					if (toolbarAction != null) {
						ButtonAction action = null;
						if (toolbarAction instanceof ToolbarAction) {
							action = new ToolbarButtonAction(toolbarAction);
						} else if (toolbarAction instanceof ToolbarModalAction) {
							action = new ToolbarRadioAction((ToolbarModalAction) toolbarAction, "map-controller-group");
						}
						if (action != null) {
							for (Parameter parameter : tool.getParameters()) {
								action.configure(parameter.getName(), parameter.getValue());
							}
							actions.add(action);
						}
					}
				}
				zoomDropDown.prepareButtons(actions);
				return zoomDropDown;
			}
		});
		
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
	}

	private Canvas getExample1() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);

		MapWidget mapWidget = new MapWidget("mapOsm", "widget-utility");
		final RibbonBarLayout ribbonBar = new RibbonBarLayout(mapWidget, "widget-utility", "ribbon-bar-1");
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

		MapWidget mapWidget = new MapWidget("mapWmsCountries", "widget-utility");
		final RibbonTabLayout ribbon = new RibbonTabLayout(mapWidget, "widget-utility", "ribbon-1");
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

		MapWidget mapWidget = new MapWidget("mapCountries", "widget-utility");
		final RibbonBarLayout ribbon = new RibbonBarLayout(mapWidget, "widget-utility", "ribbon-bar-2");
		ribbon.setSize("100%", "80px");

		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}

	private Canvas getExample4() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);

		MapWidget mapWidget = new MapWidget("mapWms", "widget-utility");
		final RibbonBarLayout ribbon = new RibbonBarLayout(mapWidget, "widget-utility", "ribbon-bar-3");
		ribbon.setStyleName("myRibbon");
		ribbon.setSize("100%", "94px");

		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}
	
	private Canvas getExample5() {
		VLayout layout = new VLayout(5);
		layout.setPadding(5);

		MapWidget mapWidget = new MapWidget("mapMsWms", "widget-utility");
		final RibbonBarLayout ribbon = new RibbonBarLayout(mapWidget, "widget-utility", "ribbon-bar-4");
		ribbon.setStyleName("msRibbon");
		ribbon.setSize("100%", "94px");

		layout.addMember(ribbon);
		layout.addMember(mapWidget);
		return layout;
	}
}