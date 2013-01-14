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

package org.geomajas.widget.searchandfilter.gwt.example.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.plugin.staticsecurity.client.Authentication;
import org.geomajas.widget.searchandfilter.client.widget.attributesearch.AttributeSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.FreeDrawingSearch;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchPanel;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.GeometricSearchPanelCreator;
import org.geomajas.widget.searchandfilter.client.widget.geometricsearch.SelectionSearch;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;
import org.geomajas.widget.searchandfilter.client.widget.search.CombinedSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchHandler;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;
import org.geomajas.widget.searchandfilter.client.widget.searchfavourites.SearchFavouritesListCreator;
import org.geomajas.widget.searchandfilter.gwt.example.client.i18n.ApplicationMessages;
import org.geomajas.widget.searchandfilter.gwt.example.client.pages.AbstractTab;
import org.geomajas.widget.searchandfilter.gwt.example.client.pages.MultiFeatureListGridPage;
import org.geomajas.widget.searchandfilter.gwt.example.client.pages.SearchPage;

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

	private OverviewMap overviewMap;

	private MapWidget mapWidget;

	private MultiFeatureListGrid featureListGrid;

	private Legend legend;

	private TabSet tabSet = new TabSet();

	private List<AbstractTab> tabs = new ArrayList<AbstractTab>();

	private ApplicationMessages messages = GWT.create(ApplicationMessages.class);

	private ToolStrip topBar;

	public Application() {
	}

	public void onModuleLoad() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// ---------------------------------------------------------------------
		// Top bar:
		// ---------------------------------------------------------------------
		topBar = new ToolStrip();
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
		mapWidget = new MapWidget("mapMain", "app");
		final Toolbar toolbar = new Toolbar(mapWidget);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		VLayout mapLayout = new VLayout();
		mapLayout.setShowResizeBar(true);
		mapLayout.setResizeBarTarget("tabs");
		mapLayout.addMember(toolbar);
		mapLayout.addMember(mapWidget);
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
		overviewMap = new OverviewMap("mapOverview", "app", mapWidget, false, true);
		section1.addItem(overviewMap);
		sectionStack.addSection(section1);

		// LayerTree layout:
		SectionStackSection section2 = new SectionStackSection("Layer tree");
		section2.setExpanded(true);
		LayerTree layerTree = new LayerTree(mapWidget);
		section2.addItem(layerTree);
		sectionStack.addSection(section2);

		// Legend layout:
		SectionStackSection section3 = new SectionStackSection("Legend");
		section3.setExpanded(true);
		legend = new Legend(mapWidget.getMapModel());
		section3.addItem(legend);
		sectionStack.addSection(section3);

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		// ---------------------------------------------------------------------
		// Bottom left: Add tabs here:
		// ---------------------------------------------------------------------
		MultiFeatureListGridPage page1 = new MultiFeatureListGridPage(mapWidget);
		featureListGrid = page1.getTable();
		SearchPage page2 = new SearchPage("Search features", mapWidget);
		addTab(page2);
		addTab(page1);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();

		// Install a loading screen
		// This only works if the application initially shows a map with at
		// least 1 vector layer:
		LoadingScreen loadScreen = new LoadingScreen(mapWidget, "Simple GWT application using Geomajas "
				+ Geomajas.getVersion());
		loadScreen.draw();

		// Then initialize:
		Authentication.getInstance().login("luc", "luc", null);
		initialize();
	}

	private void addTab(AbstractTab tab) {
		tabSet.addTab(tab);
		tabs.add(tab);
	}

	private void initialize() {
		legend.setHeight(200);
		overviewMap.setHeight(200);

		// ---------------------------------------------------------------------
		// Create Searchpanels
		// ---------------------------------------------------------------------
		SearchWidgetRegistry.initialize(mapWidget, featureListGrid);
		SearchWidgetRegistry.put(new AttributeSearchCreator());
		SearchWidgetRegistry.put(new CombinedSearchCreator());
		SearchWidgetRegistry.put(new SearchFavouritesListCreator());
		SearchWidgetRegistry.put(new GeometricSearchCreator(new GeometricSearchPanelCreator() {
			public GeometricSearchPanel createInstance(MapWidget mapWidget) {
				GeometricSearchPanel gsp = new GeometricSearchPanel(mapWidget);
				gsp.addSearchMethod(new SelectionSearch());
				gsp.addSearchMethod(new FreeDrawingSearch());
				return gsp;
			}
		}));

		// -- Show the grid after new result has been retrieved
		SearchWidgetRegistry.addSearchHandler(new SearchHandler() {
			public void onSearchStart(SearchEvent event) {
			}

			public void onSearchDone(SearchEvent event) {
				// handled by featureListGrid, no need for us to do something
			}

			public void onSearchEnd(SearchEvent event) {
				if (!(featureListGrid.isShowDetailsOnSingleResult() && event.isSingleResult())) {
					tabSet.selectTab(1);
				}
			}
		});

		for (AbstractTab tab : tabs) {
			tab.initialize();
		}
	}
}
