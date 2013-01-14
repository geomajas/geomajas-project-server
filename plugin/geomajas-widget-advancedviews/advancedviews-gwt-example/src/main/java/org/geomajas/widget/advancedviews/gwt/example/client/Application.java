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

package org.geomajas.widget.advancedviews.gwt.example.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.widget.advancedviews.client.widget.ExpandingThemeWidget;
import org.geomajas.widget.advancedviews.client.widget.ThemeWidget;
import org.geomajas.widget.advancedviews.gwt.example.client.i18n.ApplicationMessages;
import org.geomajas.widget.advancedviews.gwt.example.client.pages.AbstractTab;
import org.geomajas.widget.advancedviews.gwt.example.client.pages.FeatureListGridPage;
import org.geomajas.widget.advancedviews.gwt.example.client.pages.SearchPage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this
 * application.
 *
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private OverviewMap overviewMap;
	
	private TabSet tabSet = new TabSet();

	private List<AbstractTab> tabs = new ArrayList<AbstractTab>();

	private ApplicationMessages messages = GWT.create(ApplicationMessages.class);

	private ThemeWidget themes;
	
	private ExpandingThemeWidget exthemes;

	public Application() {
	}

	public void onModuleLoad() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// ---------------------------------------------------------------------
		// Top bar:
		// ---------------------------------------------------------------------
		ToolStrip topBar = new ToolStrip();
		topBar.setHeight(33);
		topBar.setWidth100();
		topBar.addSpacer(6);

		Img icon = new Img("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png");
		icon.setSize(24);
		topBar.addMember(icon);
		topBar.addSpacer(6);

		Label title = new Label(messages.applicationTitle("hello world"));
		title.setStyleName("appTitle");
		title.setWidth(300);
		topBar.addMember(title);
		topBar.addFill();
		topBar.addMember(new LocaleSelect("English"));

		mainLayout.addMember(topBar);

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);
		layout.setMargin(5);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapMain", "app");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		VLayout mapLayout = new VLayout();
		mapLayout.setShowResizeBar(true);
		mapLayout.setResizeBarTarget("tabs");
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("65%");
		tabSet.setTabBarPosition(Side.TOP);
		tabSet.setWidth100();
		tabSet.setHeight("35%");
		tabSet.setID("tabs");

		VLayout leftLayout = new VLayout();
		leftLayout.setShowEdges(true);
		leftLayout.addMember(mapLayout);
		leftLayout.addMember(tabSet);

		layout.addMember(leftLayout);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setShowEdges(true);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("300px", "100%");

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack originalSectionStack = new SectionStack();
		originalSectionStack.setShowEdges(true);
		originalSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		originalSectionStack.setCanReorderSections(true);
		originalSectionStack.setCanResizeSections(false);
		originalSectionStack.setSize("250px", "100%");

		// Overview map layout:
		SectionStackSection osection1 = new SectionStackSection("Overview map");
		osection1.setExpanded(true);
		overviewMap = new OverviewMap("mapOverview", "app", map, false, true);
		osection1.addItem(overviewMap);
		originalSectionStack.addSection(osection1);

		// LayerTree layout:
		SectionStackSection osection2 = new SectionStackSection("Layer tree");
		osection2.setExpanded(true);
		LayerTree lt = new LayerTree(map);
		osection2.addItem(lt);
		originalSectionStack.addSection(osection2);

		// Legend layout:
		SectionStackSection osection3 = new SectionStackSection("Legend");
		osection3.setExpanded(true);
		Legend l = new Legend(map.getMapModel());
		osection3.addItem(l);
		originalSectionStack.addSection(osection3);

		// Putting the right side layouts together:
		layout.addMember(originalSectionStack);

		// ---------------------------------------------------------------------
		// Bottom left: Add tabs here:
		// ---------------------------------------------------------------------
		FeatureListGridPage page1 = new FeatureListGridPage(map);
		addTab(new SearchPage(map, tabSet, page1.getTable()));
		addTab(page1);

		// ---------------------------------------------------------------------
		// Theme
		// ---------------------------------------------------------------------
		themes = new ThemeWidget(map);
		themes.setParentElement(map);
		themes.setSnapTo("BL");
		themes.setSnapOffsetTop(-50);
		themes.setSnapOffsetLeft(10);
		themes.setWidth(150);

		exthemes = new ExpandingThemeWidget(map);
		exthemes.setParentElement(map);
		exthemes.setSnapTo("BR");
		exthemes.setSnapOffsetTop(-50);
		exthemes.setSnapOffsetLeft(-20);
		exthemes.setWidth(160);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();
		
		// Install a loading screen
		// This only works if the application initially shows a map with at least 1 vector layer:
		LoadingScreen loadScreen = new LoadingScreen(map, "Simple GWT application using Geomajas "
				+ Geomajas.getVersion());
		loadScreen.draw();

		// Then initialize:
		initialize();
		
		// -- Filter layer to show filterIcon
		map.getMapModel().addMapModelHandler(new MapModelHandler() {
			public void onMapModelChange(MapModelEvent event) {
				VectorLayer countries = map.getMapModel().getVectorLayer("clientLayerCountries");
				countries.setFilter("NAME NOT like 'France'");
			}
		});
	}

	private void addTab(AbstractTab tab) {
		tabSet.addTab(tab);
		tabs.add(tab);
	}

	private void initialize() {
		overviewMap.setHeight(200);

		for (AbstractTab tab : tabs) {
			tab.initialize();
		}
	}
}
