/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.geomajas.graphics.client.controller.MetaController;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.operation.GraphicsOperation;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Default implementation of {@link GraphicsService}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsServiceImpl implements GraphicsService, GraphicsOperationEvent.Handler {

	private Stack<GraphicsOperation> undoStack = new Stack<GraphicsOperation>();

	private Stack<GraphicsOperation> redoStack = new Stack<GraphicsOperation>();

	private GraphicsObjectContainer objectContainer;

	private List<GraphicsControllerFactory> controllerFactories = new ArrayList<GraphicsControllerFactory>();

	private MetaControllerFactory metaControllerFactory = new MetaControllerFactory() {
		
		@Override
		public GraphicsController createController(GraphicsService graphicsService) {
			return new MetaController(graphicsService);
		}
	};
	
	private GraphicsController metaController;
	
	private EventBus eventBus;
	
	private HandlerRegistration standardGraphicsOperationEventRegistration;
	
	private boolean showOriginalObjectWhileDragging;

	public GraphicsServiceImpl(final EventBus eventBus, boolean undoKeys) {
		this(eventBus, undoKeys, true);
	}
	
	public GraphicsServiceImpl(final EventBus eventBus, boolean undoKeys, boolean showOriginalObjectWhileDragging) {
		this.eventBus = eventBus;
		this.showOriginalObjectWhileDragging = showOriginalObjectWhileDragging;
		standardGraphicsOperationEventRegistration = eventBus.addHandler(GraphicsOperationEvent.getType(), this);
		if (undoKeys) {
			Event.addNativePreviewHandler(new NativePreviewHandler() {

				@Override
				public void onPreviewNativeEvent(NativePreviewEvent event) {
					if (event.getTypeInt() == Event.ONKEYDOWN) {
						NativeEvent ne = event.getNativeEvent();

						if (ne.getCtrlKey() && ne.getKeyCode() == 'Z') {
							event.cancel();
							undo();
						} else if (ne.getCtrlKey() && ne.getKeyCode() == 'Y') {
							event.cancel();
							redo();
						}
					}
				}
			});
		}
	}
	
	@Override
	public void start() {
		if (metaController == null) {
			metaController = metaControllerFactory.createController(this);
		}
		metaController.setActive(true);
	}

	@Override
	public void stop() {
		if (metaController != null) {
			metaController.setActive(false);
		}
	}

	@Override
	public void execute(GraphicsOperation operation) {
			undoStack.push(operation);
			redoStack.clear();
			operation.execute();
			eventBus.fireEvent(new GraphicsOperationEvent(operation));
	}

	@Override
	public void undo() {
		if (!undoStack.isEmpty()) {
			GraphicsOperation operation = undoStack.pop();
			operation.undo();
			redoStack.add(operation);
			eventBus.fireEvent(new GraphicsOperationEvent(operation));
		}
	}

	@Override
	public void redo() {
		if (!redoStack.isEmpty()) {
			GraphicsOperation operation = redoStack.pop();
			operation.execute();
			undoStack.push(operation);
			eventBus.fireEvent(new GraphicsOperationEvent(operation));
		}
	}

	public HandlerRegistration addGraphicsOperationHandler(GraphicsOperationEvent.Handler handler) {
		standardGraphicsOperationEventRegistration.removeHandler();
		return eventBus.addHandler(GraphicsOperationEvent.getType(), handler);
	}
	
	public void setMetaControllerFactory(MetaControllerFactory metaControllerFactory) {
		this.metaControllerFactory = metaControllerFactory;
	}

	@Override
	public GraphicsController getMetaController() {
		return metaController;
	}

	public void registerControllerFactory(GraphicsControllerFactory controllerFactory) {
		controllerFactories.add(controllerFactory);
	}

	public void setObjectContainer(GraphicsObjectContainer objectContainer) {
		this.objectContainer = objectContainer;
	}

	protected VectorObjectContainer createContainer() {
		return objectContainer.createContainer();
	}

	protected void removeContainer(VectorObjectContainer container) {
		objectContainer.removeContainer(container);
	}

	@Override
	public GraphicsObjectContainer getObjectContainer() {
		return objectContainer;
	}

	@Override
	public List<GraphicsControllerFactory> getControllerFactories() {
		return controllerFactories;
	}

	@Override
	public void update(GraphicsObject object) {
		objectContainer.update(object);
	}
	
	@Override
	public void onOperation(GraphicsOperationEvent event) {
		switch (event.getOperation().getType()) {
			case ADD:
				getObjectContainer().add(event.getOperation().getObject());
				break;
			case REMOVE:
				getObjectContainer().remove(event.getOperation().getObject());
				break;
			case UPDATE:
				getObjectContainer().update(event.getOperation().getObject());
				break;
			default:
				break;
		}
	}

	@Override
	public boolean isShowOriginalObjectWhileDragging() {
		return showOriginalObjectWhileDragging;
	}

	@Override
	public void setShowOriginalObjectWhileDragging(boolean showOriginalObjectWhileDragging) {
		this.showOriginalObjectWhileDragging = showOriginalObjectWhileDragging;
	}

}
