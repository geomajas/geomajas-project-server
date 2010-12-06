/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.listener.ListenerController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.gfx.context.DefaultImageContext;
import org.geomajas.gwt.client.gfx.context.SvgGraphicsContext;
import org.geomajas.gwt.client.gfx.context.VmlGraphicsContext;
import org.geomajas.gwt.client.util.GwtEventUtil;
import org.geomajas.gwt.client.widget.event.GraphicsReadyEvent;
import org.geomajas.gwt.client.widget.event.GraphicsReadyHandler;
import org.geomajas.gwt.client.widget.event.HasGraphicsReadyHandlers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusWidget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * GWT widget implementation meant for actual drawing. To this end it implements the <code>GraphicsContext</code>
 * interface. Actually it delegates the real work to either a
 * {@link org.geomajas.gwt.client.gfx.context.VmlGraphicsContext} or a
 * {@link org.geomajas.gwt.client.gfx.context.SvgGraphicsContext} object.
 * </p>
 * <p>
 * By default this widget will draw in screen space. It will check the given ID's and add the screen space group in
 * front of it if the ID is not compatible (meaning if an ID does not start with the graphicsId...).
 * </p>
 * <p>
 * It is also responsible for handling {@link org.geomajas.gwt.client.controller.GraphicsController}s (only one at a
 * time). The reason to place the controller handling here, is because we needed a default GWT widget to handle the
 * events, not a SmartGWT widget. The SmartGWT events do not contain the actual DOM elements for MouseEvents, while the
 * default GWT events do.
 * </p>
 * <p>
 * One extra function this widget has, is to store the latest right mouse click. Usually the right mouse button is used
 * for drawing context menus. But sometimes it is necessary to have the DOM element onto which the context menu was
 * clicked, to influence this menu. That is why this widget always stores this latest event (or at least it's DOM
 * element ID, and screen location).
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GraphicsWidget extends VLayout implements MapContext, HasDoubleClickHandlers, HasGraphicsReadyHandlers {

	/** The ID from which to start building the rendering DOM tree. */
	private String graphicsId;

	/**
	 * The actual GraphicsContext implementation that does the drawing. Depending on the browser the user uses, this
	 * will be the {@link org.geomajas.gwt.client.gfx.context.VmlGraphicsContext} or the
	 * {@link org.geomajas.gwt.client.gfx.context.SvgGraphicsContext}.
	 */
	private GraphicsContext vectorContext;

	/**
	 * The context for drawing raster images.
	 */
	private DefaultImageContext rasterContext;

	/**
	 * The menu context.
	 */
	private MenuContext menuContext;

	/** The current controller on the map. Can be only one at a time! */
	private GraphicsController controller;

	/**
	 * An optional fallbackController to return to, when no controller is explicitly set, or when null is set.
	 */
	private GraphicsController fallbackController;

	/** An optional passive listener that listens to mouse events on a map without interfering. */
	private ListenerController listenerController;

	/**
	 * A list of handler registrations that are needed to correctly clean up after a controller is deactivated.
	 */
	private List<HandlerRegistration> handlers;

	/**
	 * A list of handler registrations that are needed to correctly clean up after a listener is deactivated.
	 */
	private List<HandlerRegistration> listenerHandlers;

	/**
	 * Every time a right mouse button has been clicked, this widget will store the event's coordinates.
	 */
	private Coordinate rightButtonCoordinate;

	/**
	 * Every time a right mouse button has been clicked, this widget will store the event's target DOM element.
	 */
	private String rightButtonTarget;

	/** Focus widget with the real graphics, needed for native GWT events */
	private EventWidget eventWidget;

	private Timer resizeTimer;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create and initialise a graphics widget. It will instantiate the correct delegate <code>GraphicsContext</code>
	 * and build the initial DOM elements.
	 * 
	 * @param parent
	 *            The canvas to which this widget will be added as a child.
	 * @param graphicsId
	 *            The ID from which to start building the rendering DOM tree.
	 */
	public GraphicsWidget(String graphicsId) {
		eventWidget = new EventWidget(graphicsId);
		setWidth100();
		setHeight100();
		setID(graphicsId);
		this.graphicsId = graphicsId;
		// append a raster context
		rasterContext = new DefaultImageContext(eventWidget.getWidget());
		// append a vector context
		if (SC.isIE()) {
			vectorContext = new VmlGraphicsContext(eventWidget.getWidget());
		} else {
			vectorContext = new SvgGraphicsContext(eventWidget.getWidget());
		}
		menuContext = new MapMenuContext();
		handlers = new ArrayList<HandlerRegistration>();
		listenerHandlers = new ArrayList<HandlerRegistration>();

		// capture right mouse info (target id and coordinate)
		RightMouseHandler rmh = new RightMouseHandler();
		// we connect to both mouse events just to be sure (ubuntu/ff3.6 does
		// not fire mouse up)
		eventWidget.addMouseDownHandler(rmh);
		eventWidget.addMouseUpHandler(rmh);
		// raster at the back
		// WARNING ! adding the child here was causing several problems:
		// - GWT-153: embedding in plain html or other frameworks
		// - GWT-145: autoscroll is no longer working (even after fixing height)
		// base.addChild(this); // moved to GwtResizedHandler
		// parent.addChild(base);
		GwtResizedHandler h = new GwtResizedHandler();
		super.addResizedHandler(h);
		addDrawHandler(h);
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	public HandlerRegistration addGraphicsReadyHandler(GraphicsReadyHandler handler) {
		return doAddHandler(handler, GraphicsReadyEvent.TYPE);
	}

	/**
	 * Apply a new <code>GraphicsController</code> on the graphics. When an old controller is to be removed for this new
	 * controller, its <code>onDeactivate</code> method will be called. For the new controller, its
	 * <code>onActivate</code> method will be called.
	 * 
	 * @param graphicsController
	 *            The new <code>GraphicsController</code> to be applied on the graphics.
	 */
	public void setController(GraphicsController graphicsController) {
		for (HandlerRegistration registration : handlers) {
			registration.removeHandler();
		}
		if (controller != null) {
			controller.onDeactivate();
			controller = null;
		}
		handlers = new ArrayList<HandlerRegistration>();
		if (null == graphicsController) {
			graphicsController = fallbackController;
		}
		if (graphicsController != null) {
			handlers.add(eventWidget.addMouseDownHandler(graphicsController));
			handlers.add(eventWidget.addMouseMoveHandler(graphicsController));
			handlers.add(eventWidget.addMouseOutHandler(graphicsController));
			handlers.add(eventWidget.addMouseOverHandler(graphicsController));
			handlers.add(eventWidget.addMouseUpHandler(graphicsController));
			handlers.add(eventWidget.addMouseWheelHandler(graphicsController));
			handlers.add(addDoubleClickHandler(graphicsController));
			controller = graphicsController;
			controller.onActivate();
		}
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return eventWidget.addMouseWheelHandler(handler);
	}

	/**
	 * Get the currently active GraphicsController.
	 * 
	 * @return current GraphicsController
	 */
	public GraphicsController getController() {
		return controller;
	}

	/**
	 * An optional fallbackController to return to, when no controller is explicitly set, or when null is set. If no
	 * current controller is active when this setter is called, it is applied immediately.
	 * 
	 * @param fallbackController
	 *            The new fall-back controller.
	 */
	public void setFallbackController(GraphicsController fallbackController) {
		boolean fallbackActive = (controller == this.fallbackController);
		this.fallbackController = fallbackController;
		if (controller == null || fallbackActive) {
			setController(fallbackController);
		}
	}

	/**
	 * Return the controller of a listener that passively listens to mouse events on the map. These listeners do not
	 * interfere with the mouse events.
	 * 
	 * @return Return the ListenerController or null if there is none active.
	 */
	public ListenerController getListener() {
		return listenerController;
	}

	/**
	 * Apply a controller for a listener that passively listens to mouse events on the map. These listeners do not
	 * interfere with the mouse events.
	 * 
	 * @param listener
	 *            The actual listener object or null to deactive the current listener.
	 */
	public void setListener(ListenerController listener) {
		for (HandlerRegistration registration : listenerHandlers) {
			registration.removeHandler();
		}
		if (listenerController != null) {
			listenerController.onDeactivate();
			listenerController = null;
		}
		listenerHandlers = new ArrayList<HandlerRegistration>();
		if (listener != null) {
			listenerController = listener;
			listenerHandlers.add(eventWidget.addMouseDownHandler(listenerController));
			listenerHandlers.add(eventWidget.addMouseMoveHandler(listenerController));
			listenerHandlers.add(eventWidget.addMouseOutHandler(listenerController));
			listenerHandlers.add(eventWidget.addMouseOverHandler(listenerController));
			listenerHandlers.add(eventWidget.addMouseUpHandler(listenerController));
			listenerController.onActivate();
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public String getGraphicsId() {
		return graphicsId;
	}

	public MenuContext getMenuContext() {
		return menuContext;
	}

	public ImageContext getRasterContext() {
		return rasterContext;
	}

	public GraphicsContext getVectorContext() {
		return vectorContext;
	}

	/**
	 * Menu context that captures raster and vector context events.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class MapMenuContext implements MenuContext {

		public Coordinate getRightButtonCoordinate() {
			return rightButtonCoordinate;
		}

		public String getRightButtonName() {
			String name = vectorContext.getNameById(rightButtonTarget);
			if (name != null) {
				return name;
			} else {
				return rasterContext.getNameById(rightButtonTarget);
			}
		}

		public Object getRightButtonObject() {
			Object object = vectorContext.getGroupById(rightButtonTarget);
			if (object != null) {
				return object;
			} else {
				return rasterContext.getGroupById(rightButtonTarget);
			}
		}
	}

	public void markForResize() {
		// remove child while browser is resizing + schedule redraw in 0.1 sec
		if (contains(eventWidget)) {
			removeChild(eventWidget);
			eventWidget.setVisible(false);
		}
		if (resizeTimer == null) {
			resizeTimer = new Timer() {

				@Override
				public void run() {
					resize();
				}

			};
		}
		resizeTimer.schedule(100);
	}

	public void resize() {
		// child was removed in markForResize(), add it again
		if (!contains(eventWidget)) {
			addChild(eventWidget);
			eventWidget.setVisible(true);
		}
		// set the size and notify parents so they can redraw
		vectorContext.setSize(getWidth(), getHeight());
		if (isReady()) {
			fireEvent(new GraphicsReadyEvent());
		}
	}

	/** Fixes resize problem by manually re-adding this component */
	private class GwtResizedHandler implements ResizedHandler, DrawHandler {

		public void onResized(ResizedEvent event) {
			// resize later
			markForResize();
		}

		public void onDraw(DrawEvent event) {
			// called on first draw
			resize();
		}

	}

	/** sets the right mouse coordinate and target */
	private class RightMouseHandler implements MouseUpHandler, MouseDownHandler {

		public void onMouseDown(MouseDownEvent event) {
			process(event);
		}

		public void onMouseUp(MouseUpEvent event) {
			process(event);
		}

		private void process(MouseEvent<?> event) {
			if (event.getNativeButton() == Event.BUTTON_RIGHT) {
				rightButtonCoordinate = GwtEventUtil.getPosition(event);
				rightButtonTarget = GwtEventUtil.getTargetId(event);
			}
		}
	}

	/**
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private static class EventWidget extends WidgetCanvas {

		private FocusWidget widget;

		private EventWidget(FocusWidget widget) {
			super(widget);
			this.widget = widget;
			setWidth100();
			setHeight100();
		}

		public EventWidget(String id) {
			this(new FocusWidget(Document.get().createDivElement()) {
			});
		}

		public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
			return widget.addMouseDownHandler(handler);
		}

		public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
			return widget.addMouseMoveHandler(handler);
		}

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return widget.addMouseOutHandler(handler);
		}

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return widget.addMouseOverHandler(handler);
		}

		public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
			return widget.addMouseUpHandler(handler);
		}

		public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
			return widget.addMouseWheelHandler(handler);
		}

		public FocusWidget getWidget() {
			return widget;
		}

	}

	public boolean isReady() {
		return contains(eventWidget) && eventWidget.getWidget().isAttached();
	}
}
