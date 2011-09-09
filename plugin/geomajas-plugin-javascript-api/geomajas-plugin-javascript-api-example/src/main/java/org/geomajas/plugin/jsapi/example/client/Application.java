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

package org.geomajas.plugin.jsapi.example.client;

import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.jsapi.map.Map;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.MapImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private Legend legend;

	// private ApplicationMessages messages = GWT.create(ApplicationMessages.class);

	public Application() {
	}

	public void onModuleLoad() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setBackgroundColor("#A0A0A0");

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(2);
//		layout.setMargin(10);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget mapWidget = new MapWidget("mapMain", "app");
		// Expose to javascript API!
		new MapImpl(mapWidget);
		
		final Toolbar toolbar = new Toolbar(mapWidget);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_SMALL);
		toolbar.setBackgroundColor("#647386");
		toolbar.setBackgroundImage("");
		toolbar.setBorder("0px");

		VLayout mapLayout = new VLayout();
		mapLayout.addMember(toolbar);
		mapLayout.addMember(mapWidget);
		mapLayout.setHeight("65%");

		VLayout leftLayout = new VLayout();
		leftLayout.addMember(mapLayout);

		layout.addMember(leftLayout);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("200px", "100%");

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
		legend.setBackgroundColor("#FFFFFF");
		section3.addItem(legend);
		sectionStack.addSection(section3);

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		// ---------------------------------------------------------------------
		// Finally add to html and draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);

		mainLayout.setHtmlElement(DOM.getElementById("map"));
		mainLayout.setWidth(DOM.getElementById("map").getStyle().getWidth());
		mainLayout.setHeight(DOM.getElementById("map").getStyle().getHeight());
		mainLayout.draw();

		// Install a loading screen.
		// This only works if the application initially shows a map with at least 1 vector layer:
		// LoadingScreen loadScreen = new LoadingScreen(map, "Geomajas GWT application");
		// loadScreen.draw();

		// Then initialize:
		initialize();
	}

	private void initialize() {
		legend.setHeight(200);
	}
}
