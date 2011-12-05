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

package org.geomajas.plugin.editing.gwt.example.client;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 * 
 * @author Pieter De Graef
 */
public class GeomajasEntryPoint implements EntryPoint {

	public void onModuleLoad() {

		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// ---------------------------------------------------------------------
		// Top bar:
		// ---------------------------------------------------------------------

		HLayout topBar = new HLayout();
		topBar.setSize("100%", "52px");
		topBar.setStyleName("titleBar");
		Img logo = new Img("[ISOMORPHIC]/images/geomajas_logo.png", 350, 52);
		topBar.addMember(logo);

		Label title = new Label("GWT Editing Example");
		title.setWidth100();
		title.setWrap(false);
		title.setStyleName("title");
		topBar.addMember(title);
		mainLayout.addMember(topBar);

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapEditing", "app");
		final GeometryEditor editor = new GeometryEditor(map);

		VLayout mapLayout = new VLayout();
		MenuBar editingToolStrip = new MenuBar(editor);
		mapLayout.addMember(editingToolStrip);
		mapLayout.addMember(map);
		mapLayout.setHeight("100%");

		layout.addMember(mapLayout);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();
	}
}