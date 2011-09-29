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
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.GeometryEditor;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

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

		ToolStripButton btn1 = new ToolStripButton("edit point");
		btn1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry point = new Geometry(Geometry.POINT, 0, 0);
				point.setCoordinates(new Coordinate[] { map.getMapModel().getMapView().getBounds().getCenterPoint() });
				editor.getService().start(point);
			}
		});
		toolStrip.addButton(btn1);

		ToolStripButton btn2 = new ToolStripButton("edit line");
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

		ToolStripButton btn3 = new ToolStripButton("edit polygon");
		btn3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry polgon = new Geometry(Geometry.POLYGON, 0, 0);

				Coordinate origin = map.getMapModel().getMapView().getBounds().getOrigin();
				Coordinate center = map.getMapModel().getMapView().getBounds().getCenterPoint();
				double deltaX = center.getX() - origin.getX();
				double deltaY = center.getY() - origin.getY();

				Coordinate c1 = new Coordinate(center.getX() - deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c2 = new Coordinate(center.getX() + deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c3 = new Coordinate(center.getX() + deltaX / 2, center.getY() + deltaY / 2);
				Coordinate c4 = new Coordinate(center.getX() - deltaX / 2, center.getY() + deltaY / 2);
				Coordinate c5 = new Coordinate(center.getX() - deltaX / 2, center.getY() - deltaY / 2);
				Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
				shell.setCoordinates(new Coordinate[] { c1, c2, c3, c4, c5 });

				Coordinate c11 = new Coordinate(center.getX() - deltaX / 4, center.getY() - deltaY / 4);
				Coordinate c12 = new Coordinate(center.getX() + deltaX / 4, center.getY() - deltaY / 4);
				Coordinate c13 = new Coordinate(center.getX() + deltaX / 4, center.getY() + deltaY / 4);
				Coordinate c14 = new Coordinate(center.getX() - deltaX / 4, center.getY() + deltaY / 4);
				Coordinate c15 = new Coordinate(center.getX() - deltaX / 4, center.getY() - deltaY / 4);
				Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
				hole.setCoordinates(new Coordinate[] { c11, c12, c13, c14, c15 });

				polgon.setGeometries(new Geometry[] { shell, hole });
				editor.getService().start(polgon);
			}
		});
		toolStrip.addButton(btn3);

		ToolStripButton btn4 = new ToolStripButton("new point");
		btn4.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry point = new Geometry(Geometry.POINT, 0, 0);
				GeometryIndex index = editor.getService().getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);

				editor.getService().start(point);
				editor.getService().setInsertIndex(index);
				editor.getService().setEditingState(GeometryEditingState.INSERTING);
			}
		});
		toolStrip.addButton(btn4);

		ToolStripButton btn5 = new ToolStripButton("new line");
		btn5.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry line = new Geometry(Geometry.LINE_STRING, 0, 0);
				GeometryIndex index = editor.getService().getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);

				editor.getService().start(line);
				editor.getService().setInsertIndex(index);
				editor.getService().setEditingState(GeometryEditingState.INSERTING);
			}
		});
		toolStrip.addButton(btn5);

		ToolStripButton btn6 = new ToolStripButton("new polygon");
		btn6.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry line = new Geometry(Geometry.POLYGON, 0, 0);
				GeometryIndex index = editor.getService().getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0, 0);

				editor.getService().start(line);
				editor.getService().setInsertIndex(index);
				editor.getService().setEditingState(GeometryEditingState.INSERTING);
			}
		});
		toolStrip.addButton(btn6);

		mapLayout.addMember(toolStrip);
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