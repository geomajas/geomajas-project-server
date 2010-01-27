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

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusWidget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.MenuGraphicsContext;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.gfx.svg.SvgGraphicsContext;
import org.geomajas.gwt.client.gfx.vml.VmlGraphicsContext;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DOM;
import org.geomajas.gwt.client.util.GwtEventUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> GWT widget implementation meant for actual drawing. To this end it implements the <code>GraphicsContext</code>
 * interface. Actually it delegates the real work to either a {@link org.geomajas.gwt.client.gfx.vml.VmlGraphicsContext}
 * or a {@link org.geomajas.gwt.client.gfx.svg.SvgGraphicsContext} object. </p> <p> By default this widget will draw in
 * screen space. It will check the given ID's and add the screen space group in front of it if the ID is not compatible
 * (meaning if an ID does not start with the graphicsId...). </p> <p> It is also responsible for handling {@link
 * org.geomajas.gwt.client.controller.GraphicsController}s (only one at a time). The reason to place the controller
 * handling here, is because we needed a default GWT widget to handle the events, not a SmartGWT widget. The SmartGWT
 * events do not contain the actual DOM elements for MouseEvents, while the default GWT events do. </p> <p> One extra
 * function this widget has, is to store the latest right mouse click. Usually the right mouse button is used for
 * drawing context menus. But sometimes it is necessary to have the DOM element onto which the context menu was clicked,
 * to influence this menu. That is why this widget always stores this latest event (or at least it's DOM element ID, and
 * screen location). </p>
 *
 * @author Pieter De Graef
 */
public class GraphicsWidget extends FocusWidget implements MenuGraphicsContext, HasDoubleClickHandlers {

	/** The ID from which to start building the rendering DOM tree. */
	private String graphicsId;

	/**
	 * The actual GraphicsContext implementation that does the drawing. Depending on the browser the user uses,
	 * this will be the {@link org.geomajas.gwt.client.gfx.vml.VmlGraphicsContext} or the {@link
	 * org.geomajas.gwt.client.gfx.svg.SvgGraphicsContext}.
	 */
	private GraphicsContext delegate;

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
	 * @param parent The canvas to which this widget will be added as a child.
	 * @param graphicsId The ID from which to start building the rendering DOM tree.
	 */
	public GraphicsWidget(Canvas parent, String graphicsId) {
		super(Document.get().createDivElement());
		this.parent = parent;
		this.graphicsId = graphicsId;
		if (SC.isIE()) {
			delegate = new VmlGraphicsContext(graphicsId);
		} else {
			delegate = new SvgGraphicsContext(graphicsId);
		}

		initialize(getElement());
		handlers = new ArrayList<HandlerRegistration>();

		addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				if (event.getNativeButton() == Event.BUTTON_RIGHT) {
					rightButtonCoordinate = GwtEventUtil.getPosition(event);
					rightButtonTarget = GwtEventUtil.getTargetId(event);
				}
			}
		});
		base = new Canvas();
		base.setWidth100();
		base.setHeight100();
		base.addChild(this);
		parent.addChild(base);
		parent.addResizedHandler(new GWTResizedHandler());
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addHandler(handler, DoubleClickEvent.getType());
	}

	/**
	 * Apply a new <code>GraphicsController</code> on the graphics. When an old controller is to be removed for this new
	 * controller, its <code>onDeactivate</code> method will be called. For the new controller, its
	 * <code>onActivate</code> method will be called.
	 *
	 * @param graphicsController The new <code>GraphicsController</code> to be applied on the graphics.
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

	/**
	 * Set the controller of an element of this <code>GraphicsWidget</code> so it can react to events.
	 *
	 * @param id The id of the element of which the controller should be set.
	 * @param controller The new <code>GraphicsController</code>
	 */
	public void setController(String id, GraphicsController controller) {
		delegate.setController(checkId(id), controller);
	}

	/**
	 * Set the controller of an element of this <code>GraphicsWidget</code> so it can react to events.
	 *
	 * @param id The id of the element of which the controller should be set.
	 * @param controller The new <code>GraphicsController</code>
	 * @param eventMask a bitmask to specify exactly which events you wish to listen for @see {@link Event}
	 */
	public void setController(String id, GraphicsController controller, int eventMask) {
		delegate.setController(checkId(id), controller, eventMask);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public String getGraphicsId() {
		return graphicsId;
	}

	/**
	 * Retrieve the coordinate of the last right mouse event.
	 *
	 * @return Returns the event's position.
	 */
	public Coordinate getRightButtonCoordinate() {
		return rightButtonCoordinate;
	}

	/**
	 * Retrieve the DOM element ID of the last right mouse event.
	 *
	 * @return Returns the DOM element's ID.
	 */
	public String getRightButtonTarget() {
		return rightButtonTarget;
	}

	// -------------------------------------------------------------------------
	// GraphicsContext implementation:
	// -------------------------------------------------------------------------

	public void setSize(int width, int height) {
		if (isReady()) {
			delegate.setSize(width, height);
		}
	}

	public void initialize(Element parent) {
		delegate.initialize(parent);
	}

	public void deleteShape(String id, boolean childrenOnly) {
		if (isReady()) {
			delegate.deleteShape(checkId(id), childrenOnly);
		}
	}

	public void drawCircle(String id, Coordinate position, double radius, ShapeStyle style) {
		if (isReady()) {
			delegate.drawCircle(checkId(id), position, radius, style);
		}
	}

	public void drawData(String id, String data) {
		if (isReady()) {
			delegate.drawData(id, data);
		}
	}

	public void drawGroup(String id, String namespace) {
		if (isReady()) {
			delegate.drawGroup(checkId(id), namespace);
		}
	}

	public void drawGroup(String id, String namespace, int width, int height, Matrix transformation) {
		if (isReady()) {
			delegate.drawGroup(checkId(id), namespace, width, height, transformation);
		}
	}

	public void drawGroup(String id, String namespace, Matrix transformation) {
		if (isReady()) {
			delegate.drawGroup(checkId(id), namespace, transformation);
		}
	}

	public void drawGroup(String id, String namespace, Style style) {
		if (isReady()) {
			delegate.drawGroup(checkId(id), namespace, style);
		}
	}

	public void drawGroup(String id, String namespace, Matrix transformation, Style style) {
		if (isReady()) {
			delegate.drawGroup(checkId(id), namespace, transformation, style);
		}
	}

	public void drawImage(String id, String href, Bbox bounds, PictureStyle style, boolean asDiv) {
		if (isReady()) {
			delegate.drawImage(checkId(id), href, bounds, style, asDiv);
		}
	}

	public void drawLine(String id, LineString line, ShapeStyle style) {
		if (isReady()) {
			delegate.drawLine(checkId(id), line, style);
		}
	}

	public void drawPolygon(String id, Polygon polygon, ShapeStyle style) {
		if (isReady()) {
			delegate.drawPolygon(checkId(id), polygon, style);
		}
	}

	public void drawRectangle(String id, Bbox rectangle, ShapeStyle style) {
		if (isReady()) {
			delegate.drawRectangle(checkId(id), rectangle, style);
		}
	}

	public void drawShapeType(String id, SymbolInfo symbol, ShapeStyle style, Matrix transformation) {
		if (isReady()) {
			delegate.drawShapeType(checkId(id), symbol, style, transformation);
		}
	}

	public void drawSymbol(String id, Point symbol, ShapeStyle style, String shapeTypeId) {
		if (isReady()) {
			delegate.drawSymbol(checkId(id), symbol, style, checkId(shapeTypeId));
		}
	}

	public void drawText(String id, String text, Coordinate position, FontStyle style) {
		if (isReady()) {
			delegate.drawText(checkId(id), text, position, style);
		}
	}

	public int getHeight() {
		return delegate.getHeight();
	}

	public int getWidth() {
		return delegate.getWidth();
	}

	public void hide(String id) {
		delegate.hide(checkId(id));
	}

	public void setBackgroundColor(String color) {
		base.setBackgroundColor(color);
		delegate.setBackgroundColor(color);
	}

	public void setCursor(String id, String cursor) {
		delegate.setCursor(checkId(id), cursor);
	}

	public void unhide(String id) {
		delegate.unhide(checkId(id));
	}

	/**
	 * Workaround for a bug that is probably caused by the GWTResizedHandler, checks if getElementById() can be used.
	 * @return
	 */
	private boolean isReady() {
		return DOM.getElementById(graphicsId + "_screen") != null;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private String checkId(String elementId) {
		if (elementId == null) {
			return null;
		} else if (elementId.indexOf("screen") == 0) {
			return graphicsId + "_" + elementId;
		} else if (elementId.indexOf("world") == 0) {
			return graphicsId + "_" + elementId;
		}
		return elementId;
	}

	/** Fixes resize problem by manually re-adding this component */
	private class GWTResizedHandler implements ResizedHandler {

		public void onResized(ResizedEvent event) {
			final int width = parent.getWidth();
			final int height = parent.getHeight();
			setHeight(base.getHeight() + "px");
			setWidth(base.getWidth() + "px");
			setSize(width, height);
			parent.removeChild(base);
			parent.addChild(base);
			parent.redraw();
		}
	}

}
