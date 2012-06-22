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

package org.geomajas.plugin.printing.gwt.example.client;

import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Pieter De Graef
 */
public class GeomajasEntryPoint implements EntryPoint {

	public GeomajasEntryPoint() {
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

		Label title = new Label("Geomajas, Simple printing GWT example");
		title.setStyleName("sgwtTitle");
		title.setWidth(400);
		topBar.addMember(title);

		mainLayout.addMember(topBar);

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);
		layout.setMargin(5);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("printingMap", "app");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);
		map.getMapModel().runWhenInitialized(new Runnable() {
			
			public void run() {
				map.getMapModel().getVectorLayer("clientLayerCountriesPrinting").setLabeled(true);
			}
		});

		VLayout mapLayout = new VLayout();
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("100%");

		layout.addMember(mapLayout);
		
		

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();

		// Install a loading screen
		// This only works if the application initially shows a map with at least 1 vector layer:
		LoadingScreen loadScreen = new LoadingScreen(map, "Geomajas, printing GWT widget example");
		loadScreen.draw();
	}
}
