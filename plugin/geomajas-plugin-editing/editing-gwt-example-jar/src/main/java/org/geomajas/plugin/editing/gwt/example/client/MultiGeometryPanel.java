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

package org.geomajas.plugin.editing.gwt.example.client;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.plugin.editing.gwt.example.client.i18n.EditingMessages;
import org.geomajas.plugin.editing.gwt.example.client.widget.CancelEditingBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.RedoBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.UndoBtn;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 * Showcase for editing multi geometries.
 * 
 * @author Pieter De Graef
 */
public class MultiGeometryPanel extends SamplePanel {

	public static final String TITLE = "gepMultiGeometry";

	public static final EditingMessages MESSAGES = GWT.create(EditingMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new MultiGeometryPanel();
		}
	};

	protected ToolStripMenuButton getEditGeometryButton(final MapWidget map, final GeometryEditor editor) {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItem multipointItem = new MenuItem("Multi Point", "[ISOMORPHIC]/geomajas/osgeo/point.png");
		multipointItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry mp = new Geometry(Geometry.MULTI_POINT, 0, 0);

				Bbox mapBounds = map.getMapModel().getMapView().getBounds();
				Coordinate origin = mapBounds.getOrigin();
				Coordinate center = mapBounds.getCenterPoint();
				double deltaX = center.getX() - origin.getX();
				double deltaY = center.getY() - origin.getY();

				Coordinate c1 = new Coordinate(center.getX() - deltaX / 2, center.getY() - deltaY / 2);
				Geometry point1 = new Geometry(Geometry.POINT, 0, 0);
				point1.setCoordinates(new Coordinate[] { c1 });

				Coordinate c2 = new Coordinate(center.getX() - deltaX / 2, center.getY() + deltaY / 2);
				Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
				point2.setCoordinates(new Coordinate[] { c2 });

				Coordinate c3 = new Coordinate(center.getX() + deltaX / 2, center.getY() - deltaY / 2);
				Geometry point3 = new Geometry(Geometry.POINT, 0, 0);
				point3.setCoordinates(new Coordinate[] { c3 });

				Coordinate c4 = new Coordinate(center.getX() + deltaX / 2, center.getY() + deltaY / 2);
				Geometry point4 = new Geometry(Geometry.POINT, 0, 0);
				point4.setCoordinates(new Coordinate[] { c4 });

				mp.setGeometries(new Geometry[] { point1, point2, point3, point4 });
				editor.getEditService().start(mp);
			}
		});

		MenuItem multiLineItem = new MenuItem("Multi Line", "[ISOMORPHIC]/geomajas/osgeo/line.png");
		multiLineItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry mls = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);

				Coordinate origin = map.getMapModel().getMapView().getBounds().getOrigin();
				Coordinate center = map.getMapModel().getMapView().getBounds().getCenterPoint();
				double deltaX = center.getX() - origin.getX();
				double deltaY = center.getY() - origin.getY();

				Coordinate c1 = new Coordinate(center.getX() - deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c2 = new Coordinate(center.getX() - deltaX / 2, center.getY() + deltaY / 2);
				Geometry line1 = new Geometry(Geometry.LINE_STRING, 0, 0);
				line1.setCoordinates(new Coordinate[] { c1, c2 });

				Coordinate c3 = new Coordinate(center.getX() + deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c4 = new Coordinate(center.getX() + deltaX / 2, center.getY() + deltaY / 2);
				Geometry line2 = new Geometry(Geometry.LINE_STRING, 0, 0);
				line2.setCoordinates(new Coordinate[] { c3, c4 });

				mls.setGeometries(new Geometry[] { line1, line2 });
				editor.getEditService().start(mls);
			}
		});

		MenuItem mpItem = new MenuItem("MultiPolygon", "[ISOMORPHIC]/geomajas/osgeo/island.png");
		mpItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry mp = new Geometry(Geometry.MULTI_POLYGON, 0, 0);

				Coordinate origin = map.getMapModel().getMapView().getBounds().getOrigin();
				Coordinate center = map.getMapModel().getMapView().getBounds().getCenterPoint();
				double deltaX = center.getX() - origin.getX();
				double deltaY = center.getY() - origin.getY();

				Coordinate c1 = new Coordinate(center.getX() - deltaX / 2, center.getY() + deltaY / 2);
				Coordinate c2 = new Coordinate(center.getX() - deltaX / 4, center.getY() + deltaY / 2);
				Coordinate c3 = new Coordinate(center.getX() - deltaX / 4, center.getY() - deltaY / 2);
				Coordinate c4 = new Coordinate(center.getX() - deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c5 = new Coordinate(center.getX() - deltaX / 2, center.getY() + deltaY / 2);
				Geometry ring1 = new Geometry(Geometry.LINEAR_RING, 0, 0);
				ring1.setCoordinates(new Coordinate[] { c1, c2, c3, c4, c5 });
				Geometry polygon1 = new Geometry(Geometry.POLYGON, 0, 0);
				polygon1.setGeometries(new Geometry[] { ring1 });

				Coordinate c11 = new Coordinate(center.getX() + deltaX / 4, center.getY() + deltaY / 2);
				Coordinate c12 = new Coordinate(center.getX() + deltaX / 2, center.getY() + deltaY / 2);
				Coordinate c13 = new Coordinate(center.getX() + deltaX / 2, center.getY() - deltaY / 2);
				Coordinate c14 = new Coordinate(center.getX() + deltaX / 4, center.getY() - deltaY / 2);
				Coordinate c15 = new Coordinate(center.getX() + deltaX / 4, center.getY() + deltaY / 2);
				Geometry ring2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
				ring2.setCoordinates(new Coordinate[] { c11, c12, c13, c14, c15 });
				Geometry polygon2 = new Geometry(Geometry.POLYGON, 0, 0);
				polygon2.setGeometries(new Geometry[] { ring2 });

				mp.setGeometries(new Geometry[] { polygon1, polygon2 });
				editor.getEditService().start(mp);
			}
		});

		menu.setItems(multipointItem, multiLineItem, mpItem);

		ToolStripMenuButton menuButton = new ToolStripMenuButton("New shape...", menu);
		menuButton.setWidth(100);
		menuButton.setHeight(32);
		return menuButton;
	}

	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		MapWidget map = new MapWidget("mapGepEditing2", "appEditing");
		GeometryEditor editor = new GeometryEditorImpl(map);

		VLayout layout = new VLayout();
		layout.setSize("100%", "100%");
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setMembersMargin(2);
		toolStrip.setWidth("100%");

		toolStrip.addMenuButton(getEditGeometryButton(map, editor));
		toolStrip.addSeparator();

		// Add buttons to help the editing process:
		toolStrip.addButton(new CancelEditingBtn(editor.getEditService()));

		UndoBtn undoBtn = new UndoBtn(editor.getEditService());
		toolStrip.addButton(undoBtn);

		RedoBtn redoBtn = new RedoBtn(editor.getEditService());
		toolStrip.addButton(redoBtn);

		layout.addMember(toolStrip);
		layout.addMember(map);
		return layout;
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return MESSAGES.multiGeometryDescription();
	}

	/** {@inheritDoc} */
	public String[] getConfigurationFiles() {
		return new String[]{"classpath:org/geomajas/plugin/editing/gwt/example/context/appEditing.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerCountries.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerOsm.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/mapEditing2.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries.xml"};
	}

	/** {@inheritDoc} */
	public String ensureUserLoggedIn() {
		return "luc";
	}

}