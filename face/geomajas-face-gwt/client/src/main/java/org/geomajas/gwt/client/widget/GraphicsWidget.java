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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.listener.Listener;
import org.geomajas.gwt.client.controller.listener.ListenerController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.gfx.context.DefaultImageContext;
import org.geomajas.gwt.client.gfx.context.SvgGraphicsContext;
import org.geomajas.gwt.client.gfx.context.VmlGraphicsContext;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.util.GwtEventUtil;
import org.geomajas.gwt.client.widget.event.GraphicsReadyEvent;
import org.geomajas.gwt.client.widget.event.GraphicsReadyHandler;
import org.geomajas.gwt.client.widget.event.HasGraphicsReadyHandlers;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DoubleClickHandler;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusWidget;
import com.smartgwt.client.widgets.WidgetCanvas;
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
public class GraphicsWidget extends VLayout implements MapContext, HasGraphicsReadyHandlers {

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

	/**
	 * A list of handler registrations that are needed to correctly clean up after a controller is deactivated.
	 */
	private List<HandlerRegistration> handlers;

	/**
	 * A list of handler registrations that are needed to correctly clean up after a listener is deactivated.
	 */
	private Map<ListenerController, List<HandlerRegistration>> listeners;

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

	private int previousWidth, previousHeight;
	
	private HandlerRegistration resizedHandlerRegistration;


	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create and initialise a graphics widget. It will instantiate the correct delegate {@link GraphicsContext}
	 * and build the initial DOM elements.
	 * 
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
		if (Dom.isSvg()) {
			vectorContext = new SvgGraphicsContext(eventWidget.getWidget());
		} else {
			vectorContext = new VmlGraphicsContext(eventWidget.getWidget());
		}
		menuContext = new MapMenuContext();
		handlers = new ArrayList<HandlerRegistration>();
		listeners = new HashMap<ListenerController, List<HandlerRegistration>>();

		// capture right mouse info (target id and coordinate)
		RightMouseHandler rmh = new RightMouseHandler();
		// we connect to both mouse events just to be sure (ubuntu/ff3.6 does
		// not fire mouse up)
		eventWidget.addMouseDownHandler(rmh);
		eventWidget.addMouseUpHandler(rmh);
		// add a resize handler to connect the event widget and set the sizes
		resizedHandlerRegistration = addResizedHandler(new GwtResizedHandler());
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

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
			handlers.add(eventWidget.addDoubleClickHandler(graphicsController));
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
	 * Get the full set of listener controllers currently active on this widget.
	 * 
	 * @return The list of listener controllers.
	 * @since 1.8.0
	 */
	public Set<ListenerController> getListeners() {
		return listeners.keySet();
	}

	/**
	 * Add a new listener controller on this widget. These listeners passively listen to mouse events on the map. They
	 * do not interfere with these events.
	 * 
	 * @param listenerController
	 *            The new listener controller to add.
	 * @return Returns true if addition was successful, false otherwise.
	 * @since 1.8.0
	 */
	public boolean addListener(ListenerController listenerController) {
		if (listenerController != null && !listeners.containsKey(listenerController)) {
			List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
			registrations.add(eventWidget.addMouseDownHandler(listenerController));
			registrations.add(eventWidget.addMouseMoveHandler(listenerController));
			registrations.add(eventWidget.addMouseOutHandler(listenerController));
			registrations.add(eventWidget.addMouseOverHandler(listenerController));
			registrations.add(eventWidget.addMouseUpHandler(listenerController));
			registrations.add(eventWidget.addMouseWheelHandler(listenerController));
			listeners.put(listenerController, registrations);
			return true;
		}
		return false;
	}

	/**
	 * Remove an existing listener controller from this widget. These listeners passively listen to mouse events on the
	 * map. They do not interfere with these events.
	 * 
	 * @param listenerController
	 *            The existing listener controller to remove.
	 * @return Returns true if removal was successful, false otherwise (i.e. if it could not be found).
	 * @since 1.8.0
	 */
	public boolean removeListener(ListenerController listenerController) {
		if (listenerController != null && listeners.containsKey(listenerController)) {
			List<HandlerRegistration> registrations = listeners.get(listenerController);
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
			listeners.remove(listenerController);
			return true;
		}
		return false;
	}

	/**
	 * Get the controller that belongs to the given listener. Protected method, used by the MapWidget.
	 * 
	 * @param listener
	 *            The listeners to search for.
	 * @return Return the controller, or null if it could not be found.
	 */
	protected ListenerController getController(Listener listener) {
		for (ListenerController controller : listeners.keySet()) {
			if (controller.getListener().equals(listener)) {
				return controller;
			}
		}
		return null;
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

	private void resizeIfReady() {
		// check whether the new size has been established
		if (hasStableSize()) {
			// attach now
			if (!hasMember(eventWidget)) {
				addMember(eventWidget);
			}
			// set the stable sizes on widget and context
			eventWidget.setWidth(getWidth());
			eventWidget.setHeight(getHeight());
			vectorContext.setSize(getWidth(), getHeight());				
			fireEvent(new GraphicsReadyEvent());
		} else {
			// schedule a new call to ourselves to make sure we are called after the event loop with stable size !
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				public void execute() {
					resizeIfReady();
				}
			});
		}
	}

	/**
	 * Is the size stable (browser resize causes many resize events) ?
	 * @return true if the size has been established (2 times same size in succession)
	 */
	private boolean hasStableSize() {
		try {
			Integer.parseInt(getWidthAsString());
			int width = getWidth();
			int height = getHeight();
			// compare with previous size and return true if same
			if (previousWidth != width || previousHeight != height) {
				// force small size (workaround for smartgwt problem where size can otherwise not shrink below the
				// previous size)
				vectorContext.setSize(EventWidget.INITIAL_SIZE, EventWidget.INITIAL_SIZE);
				eventWidget.setInitialSize();
				previousWidth = width;
				previousHeight = height;
				return false;
			} else {
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/** Fixes resize problem by manually re-adding this component */
	private class GwtResizedHandler implements ResizedHandler {

		public void onResized(ResizedEvent event) {
			// try resize
			resizeIfReady();
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
	 * Extension of WidgetCanvas that wraps a plain GWT FocusWidget. It allows for native GWT event registration and
	 * explicitly setting the size of the FocusWidget.
	 * 
	 * @author Jan De Moerloose
	 */
	private static class EventWidget extends WidgetCanvas {

		private FocusWidget widget;

		public static final int INITIAL_SIZE = 10;

		
		public EventWidget(FocusWidget widget) {
			super(widget);
			this.widget = widget;
			// this makes sure the auto-resizing works, see Overflow.VISIBLE javadoc
			setWidth(INITIAL_SIZE);
			setHeight(INITIAL_SIZE);
		}

		public void setInitialSize() {
			setWidth(INITIAL_SIZE);
			setHeight(INITIAL_SIZE);
		}
		
		protected void onDraw() {
			super.onDraw();
			// must force size as in some cases smartgwt is not setting the size on our element !
			String width = DOM.getStyleAttribute(getDOM(), "width");
			// only set if not already defined, to avoid invisible images in IE (see GWT-432)
			if (width == null || width.isEmpty()) {
				DOM.setStyleAttribute(getDOM(), "width", "100%");
				DOM.setStyleAttribute(getDOM(), "height", "100%");
			}
		}

		public EventWidget(String id) {
			this(new StyledFocusWidget(Document.get().createDivElement()));
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

		public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
			return widget.addDoubleClickHandler(handler);
		}

		public FocusWidget getWidget() {
			return widget;
		}

		/**
		 * Xhtml does not like divs without size.
		 * 
		 * @author Jan De Moerloose
		 */
		private static final class StyledFocusWidget extends FocusWidget {

			private StyledFocusWidget(Element elem) {
				super(elem);
				setSize("100%", "100%");
			}
		}
	}

	public boolean isReady() {
		return contains(eventWidget) && eventWidget.getWidget().isAttached();
	}

	@Override
	protected void onDestroy() {
		resizedHandlerRegistration.removeHandler();
		super.onDestroy();
	}	
	
}
