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

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.puregwt.client.GeometryEditor;
import org.geomajas.plugin.editing.puregwt.example.client.button.CancelButton;
import org.geomajas.plugin.editing.puregwt.example.client.button.RedoButton;
import org.geomajas.plugin.editing.puregwt.example.client.button.UndoButton;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
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
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point and main class for pure GWT example application.
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint {


	private static final ShowCaseGinjector INJECTOR = GWT.create(ShowCaseGinjector.class);

	private MapPresenter mapPresenter;

	private CancelButton cancelButton;
	private UndoButton undoButton;

	private RedoButton redoButton;

	private GeometryEditService editService;

	private GfxUtil gfxUtil;

	private VectorContainer polygonContainer;

	private VectorContainer pointContainer;

	public void onModuleLoad() {
		mapPresenter = INJECTOR.getMapPresenter();
		GeometryEditor editor = INJECTOR.getGeometryEditorFactory().create(mapPresenter);
		gfxUtil = INJECTOR.getGfxUtil();
		editService = editor.getEditService();
		polygonContainer = mapPresenter.addWorldContainer();
		pointContainer = mapPresenter.addWorldContainer();
		pointContainer.setFixedSize(true);

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

		// Initialize the map:
		mapPresenter.initialize("showcase", "mapOsm");
		RootPanel.get().add(layout);
	}

	private Widget getBtnFreePoint() {
		Button btn = new Button("Draw point");
		btn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				processCurrentGeometry();
				
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
				processCurrentGeometry();
				
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
				processCurrentGeometry();
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
	
	/**
	 * Process the edited geometry into a {@link Shape} and place it into a {@link VectorContainer}.
	 */
	private void processCurrentGeometry() {
		Geometry geometry = null;
		if (editService.isStarted()) {
			geometry = editService.stop();
		}
		if (null != geometry) {
			Shape shape = null;
			if (Geometry.POINT.equals(geometry.getGeometryType())) {
				Coordinate[] coordinates = geometry.getCoordinates();
				shape = new Circle(coordinates[0].getX(), coordinates[0].getY(), 5);
			} else {
				shape = gfxUtil.toPath(geometry);
			}
			if (null != shape) {
				shape.setStrokeWidth(3);
				shape.setFillOpacity(0);
				shape.addClickHandler(new SelectShapeHandler(geometry, shape));
				shape.addMouseOverHandler(new SelectShapeHandler(geometry, shape));
				shape.addMouseOutHandler(new SelectShapeHandler(geometry, shape));
				if (shape instanceof Circle) {
					shape.setFixedSize(true);
					pointContainer.add(shape);
				} else {
					polygonContainer.add(shape);
				}
			}
		}
	}
	
	/**
	 * Handler for converting a {@link Shape} back to a editable {@link Geometry}. 
	 * Also provides the {@link MouseOverHandler} and {@link MouseOutHandler} styles. 
	 * 
	 * @author Emiel Ackermann
	 *
	 */
	class SelectShapeHandler implements ClickHandler, MouseOverHandler, MouseOutHandler {

		private final Geometry geometry;
		private final Shape shape;

		public SelectShapeHandler(Geometry geometry, Shape shape) {
			this.geometry = geometry;
			this.shape = shape;
		}
		
		public void onClick(ClickEvent event) {
			processCurrentGeometry();
			if (shape instanceof Circle) {
				pointContainer.remove(shape);
			} else {
				polygonContainer.remove(shape);
			}
			editService.start(geometry);			
		}

		public void onMouseOver(MouseOverEvent event) {
			shape.setStrokeWidth(4);
			shape.setStrokeColor("#F00");
			if (Geometry.POLYGON.equals(geometry.getGeometryType()) || 
				Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
				shape.setFillColor("#F00");
				shape.setFillOpacity(0.3);
			}
		}
		
		public void onMouseOut(MouseOutEvent event) {
			shape.setStrokeWidth(3);
			shape.setStrokeColor("#000");
			shape.setFillOpacity(0);
		}
	}
}