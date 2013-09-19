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
package org.geomajas.graphics.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.action.Action;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.shape.AnchoredImage;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

/**
 * Controller that shows a popup menu at the upper left corner of the {@link GraphicsObject}. The menu is created only
 * once when the controller is initalized.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PopupMenuController extends AbstractGraphicsController implements GraphicsObjectContainerEvent.Handler,
	GraphicsOperationEvent.Handler {

	public static final int IMG_DIST = 10;

	/**
	 * Is controller active (listening to mouse events) ?
	 */
	private boolean active;

	private PopupMenu menu;
	
	// value that determines the offset of the cog icon from the left hand side of theresizable it is linked to.
	private double offsetX;
	
	private double offsetY;

	private String iconUrl;

	/**
	 * Our own container.
	 */
	private VectorObjectContainer container;

	private PropertyHandler handler;

	private List<Action> actions;
	
	private PopupMenuFactory popupMenuFactory;

	public PopupMenuController(List<Action> actions, GraphicsObject object, GraphicsService service, 
			double offsetX, double offsetY, String iconUrl, PopupMenuFactory popupMenuFactory) {
		super(service, object);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.iconUrl = iconUrl;
		this.popupMenuFactory = popupMenuFactory;
		
		//only register actions that are compatible with the object
		this.actions = new ArrayList<Action>();
		for (Action action : actions) {
			if (action.supports(object)) {
				this.actions.add(action);
			}
		}

		container = createContainer();
		// listen to changes to our object
		service.getObjectContainer().addGraphicsObjectContainerHandler(this);
		service.getObjectContainer().addGraphicsOperationEventHandler(this);
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
				if (menu == null) {
					menu = popupMenuFactory.createPopupMenu(getObject());
					for (Action action : actions) {
						menu.addAction(action.getLabel(), action);
					}
				}
			} else {
				// just remove the handler
				if (handler != null) {
					handler.remove(container);
				}
				if (menu != null) {
					menu.hide();
				}
			}
		}
	}

	private void init() {
		// create the handler and attach it
		handler = new PropertyHandler();
		handler.update();
		// add the handler
		handler.add(container);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (event.getObject() == getObject()) {
			if (event.getActionType() == ActionType.UPDATE) {
				// must re-initialize as this object has changed (mask)
				container.clear();
				if (isActive()) {
					init();
				}
			} else {
				// handled by meta controller
			}
		}
	}
	
	public AnchoredImage getPropertyImage() {
		return handler.getPropertyImage();
	}
	
	public void setPropertyImage(AnchoredImage propertyImage) {
		handler.setPropertyImage(propertyImage);
	}

	/**
	 * 
	 */
	class PropertyHandler implements MouseDownHandler {

		private AnchoredImage propertyImage;

		public PropertyHandler() {
			propertyImage = new AnchoredImage(0, 0, 16, 16, iconUrl != null ? iconUrl : GWT.getModuleBaseURL()
					+ "image/cogContrast.png", offsetX, offsetY);
			propertyImage.setFixedSize(true);
			propertyImage.addMouseDownHandler(this);
		}

		public void update() {
			BboxPosition bboxPos = transform(BboxPosition.CORNER_UL, Space.SCREEN, Space.USER);
			Coordinate pos = transform(new Coordinate(IMG_DIST, IMG_DIST), Space.SCREEN, Space.USER);
			if (getObject().hasRole(Resizable.TYPE)) {
				pos = GraphicsUtil.getPosition(getObject().getRole(Resizable.TYPE).getUserBounds(), bboxPos);
			} else if (getObject().hasRole(Draggable.TYPE)) {
				pos = GraphicsUtil.getPosition(getObject().getRole(Draggable.TYPE).getUserBounds(), bboxPos);
			}
			propertyImage.setUserX(pos.getX());
			propertyImage.setUserY(pos.getY());
		}

		public void remove(VectorObjectContainer container) {
			container.remove(propertyImage);
		}

		public void add(VectorObjectContainer container) {
			container.add(propertyImage);
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			menu.show(event.getClientX(), event.getClientY());
		}

		public void onClick(ClickEvent event) {
		}
		
		
		public AnchoredImage getPropertyImage() {
			return propertyImage;
		}

		
		public void setPropertyImage(AnchoredImage propertyImage) {
			this.propertyImage = propertyImage;
		}
		
	}

	@Override
	public void setVisible(boolean visible) {
		if (handler == null) {
			// create and (implicitly) activate the handler group
			init();
		}
		handler.getPropertyImage().setVisible(visible);
	}

	@Override
	public void onOperation(GraphicsOperationEvent event) {
		if (event.getOperation().getObject() == getObject() && handler != null) {
			handler.update();
		}
	}

}
