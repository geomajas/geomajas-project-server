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

package org.geomajas.plugin.editing.puregwt.example.client;

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.puregwt.client.GeometryEditor;
import org.geomajas.plugin.editing.puregwt.example.client.button.BufferAllButton;
import org.geomajas.plugin.editing.puregwt.example.client.button.CancelButton;
import org.geomajas.plugin.editing.puregwt.example.client.button.RedoButton;
import org.geomajas.plugin.editing.puregwt.example.client.button.UndoButton;
import org.geomajas.puregwt.client.event.MapResizedEvent;
import org.geomajas.puregwt.client.event.MapResizedHandler;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point and main class for PureGWT example application.
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint, MapResizedHandler {


	private static final ShowCaseGinjector INJECTOR = GWT.create(ShowCaseGinjector.class);

	private MapPresenter mapPresenter;

	private CancelButton cancelButton;
	private UndoButton undoButton;

	private RedoButton redoButton;

	private GeometryEditService editService;

	private GeometryToShapeConverter geometryToShapeConverter;

	public void onModuleLoad() {
		mapPresenter = INJECTOR.getMapPresenter();
		GeometryEditor editor = INJECTOR.getGeometryEditorFactory().create(mapPresenter);
		editService = editor.getEditService();
		geometryToShapeConverter = new GeometryToShapeConverter(editService, mapPresenter);

		DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
		layout.setSize("100%", "100%");

		// North: buttons
		HorizontalPanel buttonPanel = new HorizontalPanel();
		layout.addNorth(buttonPanel, 40);

		// Center: map
		final ResizeLayoutPanel mapLayout = new ResizeLayoutPanel();
		mapLayout.setSize("100%", "100%");
		mapLayout.add(mapPresenter.asWidget());

		// Add an automatic resize handler to set the correct size when the window resizes:
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				mapPresenter.setSize(mapLayout.getOffsetWidth(), mapLayout.getOffsetHeight());
			}
		});

		// Calculate the correct size on load:
		mapLayout.addAttachHandler(new AttachEvent.Handler() {

			public void onAttachOrDetach(AttachEvent event) {
				Timer timer = new Timer() {

					@Override
					public void run() {
						int width = mapLayout.getOffsetWidth();
						int height = mapLayout.getOffsetHeight();
						if (width > 100 && height > 100) {
							mapPresenter.setSize(width, height);
						} else {
							schedule(50);
						}
					}
				};
				timer.run();
			}
		});
		layout.add(mapLayout);

		// Add buttons to the button panel:
		buttonPanel.add(getBtnFreePoint());
		buttonPanel.add(getBtnFreeLine());
		buttonPanel.add(getBtnFreePolygon());
		buttonPanel.add(new Label());
		cancelButton = new CancelButton();
		buttonPanel.add(cancelButton);
		cancelButton.setEditService(editService);
		undoButton = new UndoButton();
		buttonPanel.add(undoButton);
		undoButton.setEditService(editService);
		redoButton = new RedoButton();
		buttonPanel.add(redoButton);
		redoButton.setEditService(editService);
		TextBox bufferDistance = new TextBox();
		bufferDistance.setValue("50000");
		buttonPanel.add(bufferDistance);
		buttonPanel.add(new BufferAllButton(geometryToShapeConverter, bufferDistance));

		// Initialize the map:
		mapPresenter.initialize("showcase", "mapOsm");
		RootPanel.get().add(layout);
	}

	private Widget getBtnFreePoint() {
		Button btn = new Button("Draw point");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();
				
				Geometry point = new Geometry(Geometry.POINT, 0, -1);
				editService.start(point);
				
				GeometryIndex index = editService.getIndexService()
						.create(GeometryIndexType.TYPE_VERTEX, 0);
				editService.setEditingState(GeometryEditState.INSERTING);
				editService.setInsertIndex(index);
			}
		});
		return btn;
	}

	private Widget getBtnFreeLine() {
		Button btn = new Button("Draw LineString");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();
				
				Geometry line = new Geometry(Geometry.LINE_STRING, 0, -1);
				editService.start(line);
				
				GeometryIndex index = editService.getIndexService()
						.create(GeometryIndexType.TYPE_VERTEX, 0);
				editService.setEditingState(GeometryEditState.INSERTING);
				editService.setInsertIndex(index);
			}
		});
		return btn;
	}

	private Widget getBtnFreePolygon() {
		Button btn = new Button("Draw Polygon");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				geometryToShapeConverter.processCurrentGeometry();
				Geometry polygon = new Geometry(Geometry.POLYGON, 0, -1);
				editService.start(polygon);
				
				try {
					GeometryIndex index = editService.addEmptyChild();
					index = editService.getIndexService()
							.addChildren(index, GeometryIndexType.TYPE_VERTEX, 0);
					editService.setEditingState(GeometryEditState.INSERTING);
					editService.setInsertIndex(index);
				} catch (GeometryOperationFailedException e) {
					e.printStackTrace();
				}
			}
		});
		return btn;
	}

	public void onMapResized(MapResizedEvent event) {
		// TODO Auto-generated method stub
		
	}
}