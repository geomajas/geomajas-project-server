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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.event.AddAnchoredLinesEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.DraggableDecoratedPopupPanel;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableGraphicsObject;
import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.anchor.Anchorable;
import org.geomajas.graphics.client.object.anchor.MaskHasLinesToStaticPositions;
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.operation.DragOperation;
import org.geomajas.graphics.client.operation.ResizeOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.shape.AnchoredRectangle;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.FlipState;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * {@link AbstractGraphicsController} that handles resizing (through anchor points) and dragging.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizeControllerHtml extends AbstractGraphicsController implements GraphicsObjectContainerEvent.Handler,
		MouseDownHandler, AddAnchoredLinesEvent.Handler {

	private static final int HANDLER_SIZE = 8;

	/**
	 * Object under control.
	 */
	private Resizable object;

	/**
	 * Our own container.
	 */
	private VectorObjectContainer container;

	/**
	 * Group with all handler objects.
	 */
	private Group handlerGroup;

	/**
	 * List of resize handlers (small corner and mid-size squares to stretch the object)
	 */
	private List<ResizeHandler> handlers = new ArrayList<ResizeHandler>();

	/**
	 * Handler to drag our object.
	 */
	private DragHandler dragHandler;

	/**
	 * Are we dragging ?
	 */
	private boolean dragging;

	/**
	 * Is controller active (listening to mouse events) ?
	 */
	private boolean active;

	private String captureCursor;

	private boolean dragOnActivate;
	
	private GraphicsService service;

	public ResizeControllerHtml(GraphicsObject object, GraphicsService service, boolean dragOnActivate) {
		super(service, object);
		this.object = object.getRole(Resizable.TYPE);
		container = createContainer();
		this.dragOnActivate = dragOnActivate;
		this.service = service;
		// listen to changes to our object
		service.getObjectContainer().addGraphicsObjectContainerHandler(this);
		service.getObjectContainer().addAddAnchoredLinesHandler(this);
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (event.getObject() == getObject()) {
			if (event.getActionType() == ActionType.UPDATE) {
				// must re-initialize as this object has changed (mask)
				container.clear();
				handlerGroup = null;
				if (isActive()) {
					init();
				}
				if (getObject().hasRole(Labeled.TYPE)
						&& getObject().getRole(Labeled.TYPE) instanceof ExternalizableLabeled) {
					ExternalLabel el = ((ExternalizableLabeled) (getObject().getRole(ExternalizableLabeled.TYPE)))
							.getExternalLabel();
					el.update();
				}
			} else if (event.getActionType() == ActionType.REMOVE) {
				if (getObject().hasRole(Labeled.TYPE)
						&& getObject().getRole(Labeled.TYPE) instanceof ExternalizableLabeled) {
					ExternalLabel el = ((ExternalizableLabeled) (getObject().getRole(ExternalizableLabeled.TYPE)))
							.getExternalLabel();
					if (service.getObjectContainer().getObjects().contains(el)) {
						service.getObjectContainer().remove(el);
					}
				}
			} else {
				// handled by meta controller
			}
		}
	}

	@Override
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			if (active) {
				if (handlerGroup == null || handlerGroup.getVectorObjectCount() < 1) {
					// create and (implicitly) activate the handler group
					init();
				} else {
					// the group may be detached, update and reattach !
					updateHandlers();
					container.add(handlerGroup);
				}
			} else {
				// just remove the handler group
				if (handlerGroup != null) {
					container.remove(handlerGroup);
				}
			}
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
		container.clear();
		removeContainer(container);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (isActive()) {
			if (dragOnActivate) {
				if (BboxService.contains(object.getUserBounds(), getUserCoordinate(event))) {
					dragHandler.onMouseDown(event);
					event.stopPropagation();
				}
			}
		}
	}

	protected Coordinate getAnchorPoint(BboxPosition type, int size) {
		switch (type) {
			case CORNER_LL:
				return new Coordinate(size, 0);
			case CORNER_LR:
				return new Coordinate(0, 0);
			case CORNER_UL:
				return new Coordinate(size, size);
			case CORNER_UR:
				return new Coordinate(0, size);
			case MIDDLE_LEFT:
				return new Coordinate(size, size / 2);
			case MIDDLE_LOW:
				return new Coordinate(size / 2, 0);
			case MIDDLE_RIGHT:
				return new Coordinate(0, size / 2);
			case MIDDLE_UP:
			default:
				return new Coordinate(size / 2, size);
		}
	}

	protected Shape createHandlerArea(BboxPosition type) {
		Coordinate anchor = getAnchorPoint(type, HANDLER_SIZE);
		AnchoredRectangle handler = new AnchoredRectangle(0, 0, HANDLER_SIZE, HANDLER_SIZE, (int) anchor.getX(),
				(int) anchor.getY());
		handler.setFillColor("#99FFFF");
		handler.setStrokeColor("#000000");
		handler.setStrokeWidth(1);
		return handler;
	}

	protected Shape createClickableArea(BboxPosition type) {
		Coordinate anchor = getAnchorPoint(type, HANDLER_SIZE);
		AnchoredRectangle clickableArea = new AnchoredRectangle(0, 0, 2 * HANDLER_SIZE, 2 * HANDLER_SIZE,
				(int) anchor.getX(), (int) anchor.getY());
		clickableArea.setFillColor("#000000");
		clickableArea.setStrokeColor("#000000");
		clickableArea.setFillOpacity(0.0);
		clickableArea.setStrokeOpacity(0);
		clickableArea.setFixedSize(true);
		return clickableArea;
	}

	private void init() {
		handlerGroup = new Group();
		// create the drag handler and attach it
		dragHandler = new DragHandler();
		dragHandler.addToGroup(handlerGroup);
		// create all resize handlers and attach them
		for (BboxPosition type : BboxPosition.values()) {
			ResizeHandler handler = new ResizeHandler(type);
			handler.render();
			handler.addToGroup(handlerGroup);
			handlers.add(handler);
		}
		// update positions
		updateHandlers();
		// add the group
		container.add(handlerGroup);
	}

	public void updateHandlers() {
		// update positions
		for (ResizeHandler handler : handlers) {
			handler.update();
		}
		if (dragHandler != null) {
			dragHandler.update();
		}
	}

	protected void capture(Element element, Cursor cursor) {
		DOM.setCapture(element);
		captureCursor = RootPanel.getBodyElement().getStyle().getCursor();
		RootPanel.getBodyElement().getStyle().setCursor(cursor);
	}

	protected void release(Element element) {
		DOM.releaseCapture(element);
		RootPanel.getBodyElement().getStyle().setProperty("cursor", captureCursor);
	}
	
	@Override
	public void onAction(AddAnchoredLinesEvent event) {
		if (isActive()) {
			if (dragOnActivate) {
				dragHandler.onAction(event);
			}
		}
	}

	/**
	 * Handles resizing (4 corner handlers + 4 side handlers).
	 */
	class ResizeHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOverHandler,
			MouseOutHandler {

		private BboxPosition type;

		private Shape clickableArea;

		private Shape rectangle;

		private Coordinate userBegin;

		private Bbox beginBounds;

		private FlipState flipstate;

		private GraphicsObject mask;

		public ResizeHandler(BboxPosition type) {
			this(type, 0, 0);
		}

		public ResizeHandler(BboxPosition type, double userX, double userY) {
			this.type = type;
			if (handlerGroup != null) {
				render();
			}
		}

		public void update() {
			Bbox userBounds = object.getUserBounds();
			Bbox screenBounds = transform(userBounds, Space.USER, Space.SCREEN);
			Coordinate screenCenter = BboxService.getCenterPoint(screenBounds);
			// minimal screen width/height + increase with half handler size so handlers don't overlap
			double minSize = 3 * HANDLER_SIZE;
			double width = Math.max(screenBounds.getWidth(), minSize);
			double height = Math.max(screenBounds.getHeight(), minSize);
			screenBounds = new Bbox(screenCenter.getX() - width / 2, screenCenter.getY() - height / 2, width, height);
			userBounds = transform(screenBounds, Space.SCREEN, Space.USER);
			Coordinate location = GraphicsUtil.getPosition(userBounds,
					transform(getBboxPosition(), Space.SCREEN, Space.USER));
			setLocation(location);
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
		}

		private void render() {
			if (handlerGroup != null) {
				clickableArea = createClickableArea(type);
				rectangle = createHandlerArea(type);
				setCursor(clickableArea);
				setCursor(rectangle);
			}
		}

		private Cursor getCursor() {
			switch (type) {
				case CORNER_LL:
					return Cursor.SW_RESIZE;
				case CORNER_UR:
					return Cursor.NE_RESIZE;
				case CORNER_LR:
					return Cursor.SE_RESIZE;
				case CORNER_UL:
					return Cursor.NW_RESIZE;
				case MIDDLE_LEFT:
					return Cursor.W_RESIZE;
				case MIDDLE_RIGHT:
					return Cursor.E_RESIZE;
				case MIDDLE_LOW:
					return Cursor.S_RESIZE;
				case MIDDLE_UP:
				default:
					return Cursor.N_RESIZE;
			}
		}

		private void setCursor(VectorObject rectangle) {
			rectangle.getElement().getStyle().setCursor(getCursor());
		}

		public void setLocation(Coordinate location) {
			if (handlerGroup != null) {
				rectangle.setUserX(location.getX());
				rectangle.setUserY(location.getY());
				clickableArea.setUserX(location.getX());
				clickableArea.setUserY(location.getY());
			}
		}

		public BboxPosition getBboxPosition() {
			return type;
		}

		public void addToGroup(Group group) {
			group.add(clickableArea);
			group.add(rectangle);
			clickableArea.addMouseDownHandler(this);
			rectangle.addMouseDownHandler(this);
			rectangle.addMouseUpHandler(this);
			rectangle.addMouseMoveHandler(this);
			rectangle.addMouseOverHandler(this);
			rectangle.addMouseOutHandler(this);
		}

		public void onMouseDown(MouseDownEvent event) {
			if (!dragging) {
				capture(rectangle.getElement(), getCursor());
				dragging = true;
				onDragStart(event.getClientX(), event.getClientY());
				if (mask != null) { // may happen in unusual scenario where mouse-up is not called
					handlerGroup.remove(mask.asObject());
				}
				mask = (GraphicsObject) getObject().cloneObject();
				mask.setOpacity(0.5);
				mask.getRole(Resizable.TYPE).setUserBounds(beginBounds);
				handlerGroup.add(mask.asObject());
			}
		}

		/** {@inheritDoc} */
		public void onMouseUp(MouseUpEvent event) {
			if (dragging) {
				dragging = false;
				handlerGroup.remove(mask.asObject());
				mask = null;
				boolean preserveRatio = ((Resizable) object).isPreserveRatio() || event.isShiftKeyDown();
				onDragStop(event.getClientX(), event.getClientY(), preserveRatio);
				release(rectangle.getElement());
			}
		}

		/** {@inheritDoc} */
		public void onMouseMove(MouseMoveEvent event) {
			if (dragging) {
				boolean preserveRatio = ((Resizable) object).isPreserveRatio() || event.isShiftKeyDown();
				mask.getRole(Resizable.TYPE).setUserBounds(
						getNewBounds(event.getClientX(), event.getClientY(), preserveRatio));
				onDragContinue();
			}
		}

		protected void performOperation(Bbox before, Bbox after, FlipState flipState) {
			ResizeOperation resizeOperation = new ResizeOperation(getObject(), before, after, flipState);
			execute(resizeOperation);
		}

		protected void onDragStart(int x, int y) {
			userBegin = transform(new Coordinate(x, y), Space.SCREEN, Space.USER);
			beginBounds = GraphicsUtil.clone(object.getUserBounds());
		}

		protected void onDragContinue() {
			updateHandlers();
		}

		protected void onDragStop(int x, int y, boolean preserveRatio) {
			onDragContinue();
			performOperation(beginBounds, getNewBounds(x, y, preserveRatio), flipstate);
		}

		private Bbox getNewBounds(int x, int y, boolean preserveRatio) {
			Coordinate userEnd = transform(new Coordinate(x, y), Space.SCREEN, Space.USER);
			double dx = userEnd.getX() - userBegin.getX();
			double dy = userEnd.getY() - userBegin.getY();
			BboxPosition userPosition = transform(getBboxPosition(), Space.SCREEN, Space.USER);
			Bbox newBounds = GraphicsUtil.translatePosition(beginBounds, userPosition, dx, dy);
			flipstate = GraphicsUtil.getFlipState(beginBounds, userPosition, dx, dy);
			if (preserveRatio) {
				double ratio = beginBounds.getWidth() / beginBounds.getHeight();
				newBounds = GraphicsUtil.stretchToRatio(newBounds, ratio, userPosition);
			}
			return newBounds;
		}

	}

	/**
	 * Uses the mask for dragging.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class DragHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, ClickHandler, 
		DoubleClickHandler, AddAnchoredLinesEvent.Handler {

		private GraphicsObject mask;

		private GraphicsObject invisibleMask;

		private Coordinate beginPosition;

		private Coordinate userBegin;

		public DragHandler() {
			render();
		}

		private void render() {
			if (handlerGroup != null) {
				invisibleMask = (GraphicsObject) getObject().cloneObject();
				invisibleMask.setOpacity(0);
				invisibleMask.asObject().addDoubleClickHandler(this);
				invisibleMask.asObject().addClickHandler(this);
				invisibleMask.asObject().addMouseDownHandler(this);
				invisibleMask.asObject().addMouseUpHandler(this);
				invisibleMask.asObject().addMouseMoveHandler(this);
				invisibleMask.asObject().getElement().getStyle().setCursor(Cursor.MOVE);
			}
		}

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
		}

		@Override
		public void onClick(ClickEvent event) {
		}

		public void update() {
			invisibleMask.getRole(Resizable.TYPE).setUserBounds(object.getUserBounds());
		}

		public void onMouseDown(MouseDownEvent event) {
			if (!dragging && BboxService.contains(object.getUserBounds(), getUserCoordinate(event))) {
				capture(invisibleMask.asObject().getElement(), Cursor.MOVE);
				dragging = true;
				onDragStart(event.getClientX(), event.getClientY());
				if (mask != null) { // may happen in unusual scenario where mouse-up is not called
					handlerGroup.remove(mask.asObject());
				}
				mask = (GraphicsObject) getObject().cloneObject();
				mask.setOpacity(0.5);
				mask.getRole(Draggable.TYPE).setPosition(beginPosition);
				if (mask.hasRole(Anchorable.TYPE)) {
					service.getObjectContainer().findObjectsAnchoredTo(getObject());
				} else {
					handlerGroup.add(mask.asObject());
				}
				
			}
		}

		/** {@inheritDoc} */
		public void onMouseUp(MouseUpEvent event) {
			if (dragging) {
				dragging = false;
				handlerGroup.remove(mask.asObject());
				mask = null;
				onDragStop(event.getClientX(), event.getClientY());
				release(invisibleMask.asObject().getElement());
			}
		}

		/** {@inheritDoc} */
		public void onMouseMove(MouseMoveEvent event) {
			if (dragging) {
				mask.getRole(Draggable.TYPE).setPosition(getNewPosition(event.getClientX(), event.getClientY()));
				onDragContinue();
			}
		}

		protected void onDragStart(int x, int y) {
			userBegin = transform(new Coordinate(x, y), Space.SCREEN, Space.USER);
			beginPosition = (Coordinate) object.getPosition().clone();
		}

		protected void onDragContinue() {
			updateHandlers();
		}

		protected void onDragStop(int x, int y) {
			onDragContinue();
			performOperation(beginPosition, getNewPosition(x, y));
		}

		protected void performOperation(Coordinate before, Coordinate after) {
			DragOperation op = new DragOperation(getObject(), before, after);
			execute(op);
		}

		private Coordinate getNewPosition(int x, int y) {
			Coordinate userEnd = transform(new Coordinate(x, y), Space.SCREEN, Space.USER);
			double dx = userEnd.getX() - userBegin.getX();
			double dy = userEnd.getY() - userBegin.getY();
			return new Coordinate(beginPosition.getX() + dx, beginPosition.getY() + dy);
		}

		public void addToGroup(Group group) {
			group.add(invisibleMask.asObject());
		}

		@Override
		public void onAction(AddAnchoredLinesEvent event) {
			List<CoordinatePath> paths = event.getCoordinatePaths();
			if (paths != null && paths.size() > 0) {
				((ResizableGraphicsObject) mask).addRole(new MaskHasLinesToStaticPositions(paths));
			}
			handlerGroup.add(mask.asObject());
		}
	}
	
	/**
	 * Uses the mask for dragging.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class HandlerHtmlObject extends DraggableDecoratedPopupPanel implements MouseDownHandler, MouseUpHandler,
			ClickHandler, DoubleClickHandler {

		public HandlerHtmlObject(DraggableDecoratedPopupPanel original) {
			super(original.getLabel());
			setPopupPosition(original.getPopupLeft(), original.getPopupTop());
			setWidth(getOffsetWidth() + "");
			setHeight(getOffsetHeight() + "");
			show();
			addDomHandler(this, MouseDownEvent.getType());
			addDomHandler(this, MouseUpEvent.getType());
			addDomHandler(this, ClickEvent.getType());
			addDomHandler(this, DoubleClickEvent.getType());
		}
		
		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

}
