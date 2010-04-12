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
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.gfx.context.DefaultImageContext;
import org.geomajas.gwt.client.gfx.context.SvgGraphicsContext;
import org.geomajas.gwt.client.gfx.context.VmlGraphicsContext;
import org.geomajas.gwt.client.util.GwtEventUtil;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusWidget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

/**
 * <p>
 * GWT widget implementation meant for actual drawing. To this end it implements the <code>GraphicsContext</code>
 * interface. Actually it delegates the real work to either a {@link org.geomajas.gwt.client.gfx.context.VmlGraphicsContext}
 * or a {@link org.geomajas.gwt.client.gfx.context.SvgGraphicsContext} object.
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
public class GraphicsWidget extends FocusWidget implements MapContext, HasDoubleClickHandlers {

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

	/** A list of handler registrations that are needed to correctly clean up after a controller is deactivated. */
	private List<HandlerRegistration> handlers;

	/** Every time a right mouse button has been clicked, this widget will store the event's coordinates. */
	private Coordinate rightButtonCoordinate;

	/** Every time a right mouse button has been clicked, this widget will store the event's target DOM element. */
	private String rightButtonTarget;

	/** Parent canvas, needed for resizing */
	private Canvas parent;

	/** Base canvas to insert this widget in */
	private Canvas base;

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
	public GraphicsWidget(Canvas parent, String graphicsId) {
		super(Document.get().createDivElement());
		this.parent = parent;
		this.graphicsId = graphicsId;
		// append a raster context
		rasterContext = new DefaultImageContext(this);
		// append a vector context
		if (SC.isIE()) {
			vectorContext = new VmlGraphicsContext(this);
		} else {
			vectorContext = new SvgGraphicsContext(this);
		}
		menuContext = new MapMenuContext();
		handlers = new ArrayList<HandlerRegistration>();

		addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				if (event.getNativeButton() == Event.BUTTON_RIGHT) {
					rightButtonCoordinate = GwtEventUtil.getPosition(event);
					rightButtonTarget = GwtEventUtil.getTargetId(event);
				}
			}
		});
		// workaround for an unsupported mix of SmartGWT and pure DOM
		base = new Canvas();
		base.setWidth100();
		base.setHeight100();
		// raster at the back
		base.addChild(this);
		parent.addChild(base);
		parent.addResizedHandler(new GWTResizedHandler());
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
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
		if (graphicsController != null) {
			handlers.add(addMouseDownHandler(graphicsController));
			handlers.add(addMouseMoveHandler(graphicsController));
			handlers.add(addMouseOutHandler(graphicsController));
			handlers.add(addMouseOverHandler(graphicsController));
			handlers.add(addMouseUpHandler(graphicsController));
			handlers.add(addMouseWheelHandler(graphicsController));
			handlers.add(addDoubleClickHandler(graphicsController));
			controller = graphicsController;
			controller.onActivate();
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public String getGraphicsId() {
		return graphicsId;
	}

	public int getHeight() {
		return vectorContext.getHeight();
	}

	public int getWidth() {
		return vectorContext.getWidth();
	}

	public void setBackgroundColor(String color) {
		base.setBackgroundColor(color);
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

	/** Fixes resize problem by manually re-adding this component */
	private class GWTResizedHandler implements ResizedHandler {

		public void onResized(ResizedEvent event) {
			final int width = parent.getWidth();
			final int height = parent.getHeight();
			setHeight(base.getHeight() + "px");
			setWidth(base.getWidth() + "px");
			vectorContext.setSize(width, height);
			parent.removeChild(base);
			parent.addChild(base);
			parent.redraw();
		}
	}

}
