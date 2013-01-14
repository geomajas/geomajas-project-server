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

package org.geomajas.widget.layer.gwt.example.client;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.widget.layer.client.widget.CombinedLayertree;
import org.geomajas.widget.layer.gwt.example.client.i18n.ApplicationMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private OverviewMap overviewMap;

	private CombinedLayertree layerTree;

	private ApplicationMessages messages = GWT.create(ApplicationMessages.class);

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
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setShowEdges(true);

		layout.addMember(mapLayout);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setShowEdges(true);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("300px", "100%");

		// LayerTree layout:
		SectionStackSection section2 = new SectionStackSection("Layer tree with legend");
		section2.setExpanded(true);
		layerTree = new CombinedLayertree(map);
		section2.addItem(layerTree);
		sectionStack.addSection(section2);

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

		// Legend layout:
		SectionStackSection osection3 = new SectionStackSection("Legend");
		osection3.setExpanded(true);
		Legend l = new Legend(map.getMapModel());
		osection3.addItem(l);
		originalSectionStack.addSection(osection3);

		// Putting the right side layouts together:
		layout.addMember(originalSectionStack);

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

	private void initialize() {
		overviewMap.setHeight(200);
	}
}
