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
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.example.client.event.GeometryEditSuspensionHandler;
import org.geomajas.plugin.editing.gwt.example.client.widget.AddRingBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.CancelEditingBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.DeleteRingBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.RedoBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.SnappingOptionWindow;
import org.geomajas.plugin.editing.gwt.example.client.widget.UndoBtn;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 * The editing toolbar used within this sample application.
 * 
 * @author Pieter De Graef
 */
public class MenuBar extends ToolStrip {

	private GeometryEditor editor;

	private MapWidget map;

	private EventBus eventBus;

	private SnappingOptionWindow wnd;

	public MenuBar(GeometryEditor editor) {
		this.editor = editor;
		eventBus = new SimpleEventBus();
		map = editor.getMapWidget();
		setWidth100();
		setHeight(36);

		// Add creation options:
		addMenuButton(getNewGeometryButton());
		addMenuButton(getEditGeometryButton());
		addSeparator();

		// Add buttons to help the editing process:
		addButton(new CancelEditingBtn(editor.getService()));

		UndoBtn undoBtn = new UndoBtn(editor.getService());
		addGeometryEditSuspensionHandler(undoBtn);
		addButton(undoBtn);

		RedoBtn redoBtn = new RedoBtn(editor.getService());
		addGeometryEditSuspensionHandler(redoBtn);
		addButton(redoBtn);

		addSeparator();

		AddRingBtn addRingBtn = new AddRingBtn(editor.getService());
		addGeometryEditSuspensionHandler(addRingBtn);
		addButton(addRingBtn);
		addButton(new DeleteRingBtn(this, editor.getService(), editor.getRenderer()));

		addSeparator();
		ToolStripButton snappingBtn = new ToolStripButton("Snapping options");
		snappingBtn.setHeight(32);
		snappingBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			public void onClick(ClickEvent event) {
				if (wnd == null) {
					wnd = new SnappingOptionWindow(MenuBar.this.editor);
				}
				wnd.show();
			}
		});
		addButton(snappingBtn);
	}

	public HandlerRegistration addGeometryEditSuspensionHandler(GeometryEditSuspensionHandler handler) {
		return eventBus.addHandler(GeometryEditSuspensionHandler.TYPE, handler);
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	protected ToolStripMenuButton getNewGeometryButton() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItem pointItem = new MenuItem("Draw Point", "[ISOMORPHIC]/geomajas/osgeo/point-create.png");
		pointItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry point = new Geometry(Geometry.POINT, 0, 0);
				GeometryIndex index = editor.getService().getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);

				editor.getService().start(point);
				editor.getService().setInsertIndex(index);
				editor.getService().setEditingState(GeometryEditingState.INSERTING);
			}
		});
		MenuItem lineItem = new MenuItem("Draw Line", "[ISOMORPHIC]/geomajas/osgeo/line-create.png");
		lineItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry line = new Geometry(Geometry.LINE_STRING, 0, 0);
				GeometryIndex index = editor.getService().getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);

				editor.getService().start(line);
				editor.getService().setInsertIndex(index);
				editor.getService().setEditingState(GeometryEditingState.INSERTING);
			}
		});
		MenuItem polyItem = new MenuItem("Draw Polygon", "[ISOMORPHIC]/geomajas/osgeo/polygon-create.png");
		polyItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

				editor.getService().start(polygon);
				try {
					GeometryIndex index = editor.getService().addEmptyChild();
					editor.getService().setInsertIndex(
							editor.getService().getIndexService().addChildren(index, GeometryIndexType.TYPE_VERTEX, 0));
					editor.getService().setEditingState(GeometryEditingState.INSERTING);
				} catch (GeometryOperationFailedException e) {
					editor.getService().stop();
					Window.alert("Exception during editing: " + e.getMessage());
				}
			}
		});

		menu.setItems(pointItem, lineItem, polyItem);

		ToolStripMenuButton menuButton = new ToolStripMenuButton("Free drawing", menu);
		menuButton.setWidth(100);
		menuButton.setHeight(32);
		return menuButton;
	}

	protected ToolStripMenuButton getEditGeometryButton() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItem pointItem = new MenuItem("Point", "[ISOMORPHIC]/geomajas/osgeo/point.png");
		pointItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry point = new Geometry(Geometry.POINT, 0, 0);
				point.setCoordinates(new Coordinate[] { map.getMapModel().getMapView().getBounds().getCenterPoint() });
				editor.getService().start(point);
			}
		});
		MenuItem lineItem = new MenuItem("LineString", "[ISOMORPHIC]/geomajas/osgeo/line.png");
		lineItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
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
		MenuItem rectItem = new MenuItem("Rectangle (polygon)", "[ISOMORPHIC]/geomajas/silk/square.png");
		rectItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
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

				polgon.setGeometries(new Geometry[] { shell });
				editor.getService().start(polgon);
			}
		});
		MenuItem donutItem = new MenuItem("Donut (polygon)", "[ISOMORPHIC]/geomajas/osgeo/ring.png");
		donutItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
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

		menu.setItems(pointItem, lineItem, rectItem, donutItem);

		ToolStripMenuButton menuButton = new ToolStripMenuButton("New shape...", menu);
		menuButton.setWidth(100);
		menuButton.setHeight(32);
		return menuButton;
	}
}