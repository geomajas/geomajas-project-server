/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.geocoder.gwt.example.client;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.i18n.client.NumberFormat;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.plugin.geocoder.gwt.example.client.i18n.Translation;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
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
		// Used for i18n in configuration files:
		I18nProvider.setLookUp(GWT.<ConstantsWithLookup>create(Translation.class));

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

		Label title = new Label("Geomajas, geocoder GWT widget example");
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
		MapWidget map = new MapWidget("osmMap", "app");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

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
		LoadingScreen loadScreen = new LoadingScreen(map, "Geomajas, geocoder GWT widget example");
		loadScreen.draw();

		toolbar.addMember(new GeocoderWidget(map, "description", "Geocoder"));
		toolbar.addMember(new Label("Try searching bla, london or booischot."));
	}
}
