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
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.operation.ResizeOperation;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.UpdateHandlerGraphicsController;
import org.geomajas.graphics.client.shape.AnchoredRectangle;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.FlipState;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.dom.client.Style.Cursor;
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
public class ResizeController extends UpdateHandlerGraphicsController implements GraphicsObjectContainerEvent.Handler,
		MouseDownHandler, GraphicsOperationEvent.Handler {

	private static final int HANDLER_SIZE = 8;

	/**
	 * Object under control.
	 */
	private Resizable object;

	/**
	 * List of resize handlers (small corner and mid-size squares to stretch the object)
	 */
	private List<ResizeHandler> handlers = new ArrayList<ResizeHandler>();

	/**
	 * Handler to drag our object.
	 */
	private GraphicsObjectDragHandler dragHandler;

	private String captureCursor;

	private boolean dragOnActivate;
	
	public ResizeController(GraphicsObject object, GraphicsService service, boolean dragOnActivate) {
		super(service, object);
		this.object = object.getRole(Resizable.TYPE);
		setContainer(createContainer());
		this.dragOnActivate = dragOnActivate;
		// listen to changes to our object
		service.getObjectContainer().addGraphicsObjectContainerHandler(this);
		service.getObjectContainer().addGraphicsOperationEventHandler(this);
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (event.getObject() == getObject()) {
			if (event.getActionType() == ActionType.UPDATE) {
				// must re-initialize as this object has changed (mask)
				getContainer().clear();
				setHandlerGroup(null);
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
					if (getService().getObjectContainer().getObjects().contains(el)) {
						getService().getObjectContainer().remove(el);
					}
				}
			} else {
				// handled by meta controller
			}
		}
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

	protected Coordinate getAnchorPointCoordinate(BboxPosition type, int size) {
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
		Coordinate anchor = getAnchorPointCoordinate(type, HANDLER_SIZE);
		AnchoredRectangle handler = new AnchoredRectangle(0, 0, HANDLER_SIZE, HANDLER_SIZE, (int) anchor.getX(),
				(int) anchor.getY());
		handler.setFillColor("#99FFFF");
		handler.setStrokeColor("#000000");
		handler.setStrokeWidth(1);
		return handler;
	}

	protected Shape createClickableArea(BboxPosition type) {
		Coordinate anchor = getAnchorPointCoordinate(type, HANDLER_SIZE);
		AnchoredRectangle clickableArea = new AnchoredRectangle(0, 0, 2 * HANDLER_SIZE, 2 * HANDLER_SIZE,
				(int) anchor.getX(), (int) anchor.getY());
		clickableArea.setFillColor("#000000");
		clickableArea.setStrokeColor("#000000");
		clickableArea.setFillOpacity(0.0);
		clickableArea.setStrokeOpacity(0);
		clickableArea.setFixedSize(true);
		return clickableArea;
	}

	@Override
	protected void init() {
		setHandlerGroup(new Group());
		// create the drag handler and attach it
		dragHandler = new GraphicsObjectDragHandler(getObject(), getService(), this);
		getHandlerGroup().add(dragHandler.getInvisbleMaskGraphicsObject().asObject());
		// create all resize handlers and attach them
		if (object.isAutoHeight()) {
			BboxPosition[] positions = new BboxPosition[] { BboxPosition.CORNER_UL, BboxPosition.CORNER_UR };
			for (BboxPosition position : positions) {
				ResizeHandler handler = new ResizeHandler(position);
				handler.render();
				handler.addToGroup(getHandlerGroup());
				handlers.add(handler);
			}		
		} else {
			for (BboxPosition type : BboxPosition.values()) {
				ResizeHandler handler = new ResizeHandler(type);
				handler.render();
				handler.addToGroup(getHandlerGroup());
				handlers.add(handler);
			}
		}
		// update positions
		updateHandlers();
		// add the group
		getContainer().add(getHandlerGroup());
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
	
	/**
	 * Handles resizing.
	 * There are (4 corner handlers + 4 side handlers).
	 * For every position, there is this DragHandler that will result in resizing of the graphics object.
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
		
		/**
		 * Are we dragging ?
		 */
		private boolean dragging;

		public ResizeHandler(BboxPosition type) {
			this(type, 0, 0);
		}

		public ResizeHandler(BboxPosition type, double userX, double userY) {
			this.type = type;
			if (getHandlerGroup() != null) {
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
			if (getHandlerGroup() != null) {
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
			if (getHandlerGroup() != null) {
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
				setDragging(true);
				onDragStart(event.getClientX(), event.getClientY());
				if (mask != null) { // may happen in unusual scenario where mouse-up is not called
					getHandlerGroup().remove(mask.asObject());
				}
				mask = (GraphicsObject) getObject().cloneObject();
				mask.setOpacity(0.5);
				mask.getRole(Resizable.TYPE).setUserBounds(beginBounds);
				getHandlerGroup().add(mask.asObject());
			}
		}

		/** {@inheritDoc} */
		public void onMouseUp(MouseUpEvent event) {
			if (dragging) {
				setDragging(false);
				getHandlerGroup().remove(mask.asObject());
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

		void setVisible(boolean visible) {
			rectangle.setVisible(visible);
		}
		
		private void setDragging(boolean draggingNewValue) {
			dragging = draggingNewValue;
			if (!getService().isShowOriginalObjectWhileDragging()) {
				getObject().asObject().setVisible(!dragging);
				((MetaController) getService().getMetaController()).
				setControllersOfObjectVisible(getObject(), !dragging);
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


	}

	@Override
	public void setVisible(boolean visible) {
		for (ResizeHandler handler : handlers) {
			handler.setVisible(!dragHandler.isDragging());
		}
	}
	
	@Override
	public void onOperation(GraphicsOperationEvent event) {
		if (event.getOperation().getObject() == getObject() && handlers != null) {
			updateHandlers();
		}
	}

}
