/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.ScreenContainer;
import org.geomajas.puregwt.client.map.ScreenGroup;
import org.geomajas.puregwt.client.map.WorldGroup;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.VectorObject;

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
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class MapWidgetImpl extends AbsolutePanel implements MapWidget {

	private HtmlContainer htmlContainer;

	private DrawingArea drawingArea;

	private List<WorldGroup> worldContainers = new ArrayList<WorldGroup>();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	// @Inject
	public MapWidgetImpl() {
		super();

		// Attach an HtmlContainer inside the clipping area (used for rendering layers):
		htmlContainer = new HtmlContainer();
		add(htmlContainer, 0, 0);

		// Attach a DrawingArea inside the clipping area (used for vector rendering):
		drawingArea = new DrawingArea(0, 0);
		add(drawingArea, 0, 0);

		// Firefox and Chrome allow for DnD of images. This default behavior is not wanted.
		addMouseDownHandler(new MouseDownHandler() {

			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
			}
		});
		addMouseMoveHandler(new MouseMoveHandler() {

			public void onMouseMove(MouseMoveEvent event) {
				event.preventDefault();
			}
		});
	}

	public Widget asWidget() {
		return this;
	}

	public HtmlContainer getHtmlContainer() {
		return htmlContainer;
	}

	public ScreenContainer getScreenContainer(String id) {
		// First we try to find the container:
		for (int i = 0; i < drawingArea.getVectorObjectCount(); i++) {
			VectorObject container = drawingArea.getVectorObject(i);
			if (container instanceof ScreenContainer) {
				if (id.equals(((ScreenContainer) container).getId())) {
					return (ScreenContainer) container;
				}
			}
		}

		// Else we create a new container:
		ScreenGroup container = new ScreenGroup(id);
		drawingArea.add(container);
		return container;
	}

	public void removeScreenContainer(ScreenContainer container) {
		drawingArea.remove((VectorObject) container);
	}

	public WorldGroup getWorldContainer(String id) {
		// First we try to find the container:
		for (int i = 0; i < drawingArea.getVectorObjectCount(); i++) {
			VectorObject container = drawingArea.getVectorObject(i);
			if (container instanceof WorldGroup) {
				if (id.equals(((WorldGroup) container).getId())) {
					return (WorldGroup) container;
				}
			}
		}

		// Else we create a new container:
		WorldGroup container = new WorldGroup(id);
		drawingArea.add(container);
		worldContainers.add(container);
		return container;
	}

	public void removeWorldContainer(WorldGroup container) {
		drawingArea.remove((VectorObject) container);
		worldContainers.remove(container);
	}

	public List<WorldGroup> getWorldContainers() {
		return worldContainers;
	}

	// ------------------------------------------------------------------------
	// Overriding resize methods:
	// ------------------------------------------------------------------------

	public void setPixelSize(int width, int height) {
		htmlContainer.setPixelSize(width, height);
		drawingArea.setPixelSize(width, height);
		super.setPixelSize(width, height);
	}

	public void setSize(String width, String height) {
		htmlContainer.setSize(width, height);
		drawingArea.setSize(width, height);
		super.setSize(width, height);
	}

	public void setWidth(String width) {
		htmlContainer.setWidth(width);
		drawingArea.setWidth(width);
		super.setWidth(width);
	}

	public void setHeight(String height) {
		htmlContainer.setHeight(height);
		drawingArea.setHeight(height);
		super.setHeight(height);
	}

	// ------------------------------------------------------------------------
	// Add mouse event catch methods:
	// ------------------------------------------------------------------------

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}
}