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
package org.geomajas.plugin.graphicsediting.client.controller;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.geomajas.plugin.graphicsediting.client.object.GeometryEditable;
import org.geomajas.plugin.graphicsediting.client.operation.GeometryEditOperation;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorFactory;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

/**
 * Controller for {@link GeometryEditable} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryEditController extends AbstractGraphicsController implements GeometryEditChangeStateHandler,
		GeometryEditStopHandler, GraphicsObjectContainerEvent.Handler {

	public static final int IMG_DIST = 16;

	/**
	 * Is controller active (listening to mouse events) ?
	 */
	private boolean active;

	private GeometryEditable object;

	private GeometryEditor editor;

	private GeometryEditService service;

	private GeometryEditorFactory editorFactory;

	private MapPresenter mapPresenter;

	/**
	 * Our own container.
	 */
	private VectorObjectContainer container;

	private EditHandler handler;

	public GeometryEditController(GraphicsObject object, GraphicsService graphicsService,
			GeometryEditorFactory editorFactory, MapPresenter mapPresenter) {
		super(graphicsService, object);
		this.mapPresenter = mapPresenter;
		this.editorFactory = editorFactory;
		this.object = object.getRole(GeometryEditable.TYPE);
		container = createContainer();
		getObjectContainer().addGraphicsObjectContainerHandler(this);
	}

	@Override
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			if (active) {
				if (handler == null) {
					// create and (implicitly) activate the handler group
					init();
				} else {
					// the group may be detached, update and reattach !
					handler.update();
					handler.add(container);
				}
			} else {
				// just remove the handler
				if (handler != null) {
					handler.remove(container);
				}
			}
		}
	}

	private void init() {
		// create the handler and attach it
		handler = new EditHandler();
		handler.update();
		// add the handler
		handler.add(container);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (handler != null) {
			handler.update();
		}
	}

	private void startEditing() {
		if (service == null) {
			service = createEditService();
			service.addGeometryEditChangeStateHandler(this);
			service.addGeometryEditStopHandler(this);
		}
		getService().getMetaController().setActive(false);
		service.start(object.getGeometry());
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		Geometry start = object.getGeometry();
		getService().execute(new GeometryEditOperation(getObject(), start, event.getGeometry()));
	}

	@Override
	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		// TODO Auto-generated method stub

	}

	public GeometryEditService createEditService() {
		editor = editorFactory.create(mapPresenter);
		editor.getBaseController().setClickToStop(true);
		return editor.getEditService();
	}

	@Override
	public void destroy() {
	}

	/**
	 * 
	 */
	class EditHandler implements MouseUpHandler {

		private Image propertyImage;

		public EditHandler() {
			propertyImage = new Image(0, 0, 16, 16, GWT.getModuleBaseURL() + "image/pencil16.png");
			propertyImage.setFixedSize(true);
			propertyImage.addMouseUpHandler(this);
		}

		public void update() {
			double posX = IMG_DIST;
			double posY = IMG_DIST;
			if (getObject().hasRole(Resizable.TYPE)) {
				Bbox userBounds = getObject().getRole(Resizable.TYPE).getUserBounds();
				Bbox screenBounds = transform(userBounds, Space.USER, Space.SCREEN);
				screenBounds = BboxService.buffer(screenBounds, IMG_DIST);
				userBounds = transform(screenBounds, Space.SCREEN, Space.USER);
				Coordinate location = GraphicsUtil.getPosition(userBounds,
						transform(BboxPosition.CORNER_UL, Space.SCREEN, Space.USER));
				posX = location.getX();
				posY = location.getY();
			} else if (getObject().hasRole(Draggable.TYPE)) {
				Coordinate position = getObject().getRole(Draggable.TYPE).getPosition();
				posX = position.getX();
				posY = position.getY();
			}
			propertyImage.setUserX(posX);
			propertyImage.setUserY(posY);
		}

		public void remove(VectorObjectContainer container) {
			container.remove(propertyImage);
		}

		public void add(VectorObjectContainer container) {
			container.add(propertyImage);
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			if (isActive()) {
				startEditing();
			}
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
		}

	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

}
