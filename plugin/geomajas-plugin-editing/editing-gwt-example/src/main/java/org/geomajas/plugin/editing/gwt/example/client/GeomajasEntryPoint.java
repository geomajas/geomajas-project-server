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

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.GeometryEditor;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

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
		final MapWidget map = new MapWidget("mapEditing", "app");
		final GeometryEditor editor = new GeometryEditor(map);

		VLayout mapLayout = new VLayout();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();

		ToolStripButton stopBtn = new ToolStripButton("stop");
		stopBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				editor.getService().stop();
			}
		});
		toolStrip.addButton(stopBtn);

		ToolStripButton btn1 = new ToolStripButton("new point");
		btn1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry point = new Geometry(Geometry.POINT, 0, 0);
				point.setCoordinates(new Coordinate[] { map.getMapModel().getMapView().getBounds().getCenterPoint() });
				editor.getService().start(point);
			}
		});
		toolStrip.addButton(btn1);

		ToolStripButton btn2 = new ToolStripButton("new line");
		btn2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry line = new Geometry(Geometry.LINE_STRING, 0, 0);

				Coordinate origin = map.getMapModel().getMapView().getBounds().getOrigin();
				Coordinate center = map.getMapModel().getMapView().getBounds().getCenterPoint();
				double deltaX = center.getX() - origin.getX();
				double deltaY = center.getY() - origin.getY();
				Coordinate c1 = new Coordinate(center.getX() - deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c2 = new Coordinate(center.getX(), center.getY() - deltaY / 2);
				Coordinate c3 = new Coordinate(center.getX() + deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c4 = new Coordinate(center.getX() + deltaX / 2, center.getY());
				Coordinate c5 = new Coordinate(center.getX() + deltaX / 2, center.getY() + deltaY / 2);

				line.setCoordinates(new Coordinate[] { c1, c2, c3, c4, c5 });
				editor.getService().start(line);
			}
		});
		toolStrip.addButton(btn2);

		mapLayout.addMember(toolStrip);
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
