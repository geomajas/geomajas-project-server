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

package org.geomajas.widget.featureinfo.gwt.example.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.widget.featureinfo.client.widget.factory.WidgetFactory;
import org.geomajas.widget.featureinfo.gwt.example.client.customfeatureinfowidgets.
			CustomCountriesFeatureInfoCanvasBuilder;
import org.geomajas.widget.featureinfo.gwt.example.client.i18n.ApplicationMessages;
import org.geomajas.widget.featureinfo.gwt.example.client.pages.AbstractTab;
import org.geomajas.widget.featureinfo.gwt.example.client.pages.FeatureListGridPage;
import org.geomajas.widget.featureinfo.gwt.example.client.pages.SearchPage;

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
 * Entry point and main class for GWT application. This class defines the layout
 * and functionality of this application.
 * 
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private static final ApplicationMessages MESSAGES = GWT.create(ApplicationMessages.class);
	private static final String CUSTOM_COUNTRIES_FEATURE_DETAIL_INFO_BUILDER_KEY =
			"SampleCustomCountriesFeatureDetail";

	private OverviewMap overviewMap;

	private Legend legend;

	private TabSet tabSet = new TabSet();

	private List<AbstractTab> tabs = new ArrayList<AbstractTab>();

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

		Label title = new Label(MESSAGES.applicationTitle("Feature info"));
		title.setStyleName("appTitle");
		title.setWidth(300);
		topBar.addMember(title);
		topBar.addFill();
		topBar.addMember(new LocaleSelect("Nederlands"));

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
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

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
		sectionStack.setSize("250px", "100%");

		// Overview map layout:
		SectionStackSection section1 = new SectionStackSection("Overview map");
		section1.setExpanded(true);
		overviewMap = new OverviewMap("mapOverview", "app", map, false, true);
		section1.addItem(overviewMap);
		sectionStack.addSection(section1);

		// LayerTree layout:
		SectionStackSection section2 = new SectionStackSection("Layer tree");
		section2.setExpanded(true);
		LayerTree layerTree = new LayerTree(map);
		section2.addItem(layerTree);
		sectionStack.addSection(section2);

		// Legend layout:
		SectionStackSection section3 = new SectionStackSection("Legend");
		section3.setExpanded(true);
		legend = new Legend(map.getMapModel());
		section3.addItem(legend);
		sectionStack.addSection(section3);

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		// ---------------------------------------------------------------------
		// Bottom left: Add tabs here:
		// ---------------------------------------------------------------------
		FeatureListGridPage page1 = new FeatureListGridPage(map);
		addTab(new SearchPage(map, tabSet, page1.getTable()));
		addTab(page1);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();

		// Install a loading screen
		// This only works if the application initially shows a map with at
		// least 1 vector layer:
		LoadingScreen loadScreen = new LoadingScreen(map, "Simple GWT application using Geomajas "
				+ Geomajas.getVersion());
		loadScreen.draw();
		
		map.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				VectorLayer layerSmallPopul = map.getMapModel().getVectorLayer(
						"clientLayerCountriesSmallPopul");
				//layer.setFilter("NAME like '%e%'");
				if (layerSmallPopul != null) {
					layerSmallPopul.setFilter("PEOPLE <= 50000000");
				}

				VectorLayer layerLargePopul = map.getMapModel().getVectorLayer(
						"clientLayerCountriesLargePopul");
				//layer.setFilter("NAME like '%e%'");
				if (layerLargePopul != null) {
					layerLargePopul.setFilter("PEOPLE > 50000000");
				}
			}
		});
		// Then initialize:
		initialize();
	}

	private void addTab(AbstractTab tab) {
		tabSet.addTab(tab);
		tabs.add(tab);
	}

	private void initialize() {
		registerWidgetBuilders();

		legend.setHeight(200);
		overviewMap.setHeight(200);

		for (AbstractTab tab : tabs) {
			tab.initialize();
		}
	}

	private void registerWidgetBuilders() {
		WidgetFactory.put(CUSTOM_COUNTRIES_FEATURE_DETAIL_INFO_BUILDER_KEY,
				new CustomCountriesFeatureInfoCanvasBuilder());
	}
}
