/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;

/**
 * Map gadget that displays four panning arrows at the top-left of the map.
 * 
 * @author Pieter De Graef
 */
public class ZoomStepGadget implements MapGadget {

	private final int top, left;

	private int y;

	private VectorContainer container;

	private ViewPort viewPort;

	private Image zoomStepHandleImg;

	private Rectangle zoomHandlerBg;

	private ZoomStephandler zoomHandler;

	// Zooming:
	private String zoomIn = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomstep/zoom-in.png";

	private String zoomOut = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomstep/zoom-out.png";

	private String zoomStep = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomstep/zoom-step.png";

	private String zoomStepHandle = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomstep/zoom-step-handle.png";

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public ZoomStepGadget(int top, int left) {
		this.top = top;
		this.left = left;
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(final ViewPort viewPort, final VectorContainer container) {
		this.viewPort = viewPort;
		this.container = container;
		render();
	}

	/** Does nothing. */
	public void onTranslate() {
		// Do nothing.
	}

	/** Scaling: sets the zoom step handle at the right position. */
	public void onScale() {
		if (container != null) {
			renderHandler(false);
		}
	}

	/** Resizing the map might change the total number of zoom levels. */
	public void onResize() {
		if (container != null) {
			container.clear();
			render();
		}
	}

	/** Clear everything. */
	public void onDestroy() {
		if (container != null) {
			container.clear();
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void renderHandler(boolean add) {
		if (container != null && viewPort != null) {
			int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
			int handleY = top + 21 + (index * 10);
			if (zoomStepHandleImg == null) {
				StopPropagationHandler handler = new StopPropagationHandler();

				zoomHandler = new ZoomStephandler();
				zoomHandler.setMinY(top + 21);
				zoomHandler.setMaxY(top + 21 + (viewPort.getZoomStrategy().getZoomStepCount() - 1) * 10);

				zoomStepHandleImg = new Image(left - 2, handleY, 24, 9, zoomStepHandle);
				DOM.setStyleAttribute(zoomStepHandleImg.getElement(), "cursor", "pointer");
				zoomStepHandleImg.addMouseDownHandler(zoomHandler);
				zoomStepHandleImg.addClickHandler(handler);
				zoomStepHandleImg.addDoubleClickHandler(handler);

				container.add(zoomStepHandleImg);

				zoomHandlerBg = new Rectangle(0, 0, 0, 0);
				zoomHandlerBg.setFillOpacity(0);
				zoomHandlerBg.setStrokeOpacity(0);

				zoomHandlerBg.addMouseMoveHandler(zoomHandler);
				zoomHandlerBg.addMouseUpHandler(zoomHandler);
				zoomHandlerBg.addMouseOutHandler(zoomHandler);

				container.add(zoomHandlerBg);
			} else {
				zoomHandler.setMinY(top + 21);
				zoomHandler.setMaxY(top + 21 + (viewPort.getZoomStrategy().getZoomStepCount() - 1) * 10);
				zoomStepHandleImg.setY(handleY);
				if (add) {
					container.add(zoomStepHandleImg);
					container.add(zoomHandlerBg);
				}
			}
		}
	}

	private void render() {
		y = top;
		Image zoomInImg = new Image(left, y, 20, 20, zoomIn);
		y += 20;
		DOM.setStyleAttribute(zoomInImg.getElement(), "cursor", "pointer");
		zoomInImg.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		});

		StopPropagationHandler handler = new StopPropagationHandler();
		zoomInImg.addMouseDownHandler(handler);
		zoomInImg.addClickHandler(handler);
		zoomInImg.addDoubleClickHandler(handler);
		container.add(zoomInImg);

		// Zoom steps:
		for (int i = 0; i < viewPort.getZoomStrategy().getZoomStepCount(); i++) {
			final int count = i;
			Image zoomStepImg = new Image(left, y, 20, 11, zoomStep);
			DOM.setStyleAttribute(zoomStepImg.getElement(), "cursor", "pointer");
			zoomStepImg.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					double scale = viewPort.getZoomStrategy().getZoomStepScale(count);
					viewPort.applyScale(scale);
					event.stopPropagation();
				}
			});
			zoomStepImg.addMouseDownHandler(handler);
			zoomStepImg.addDoubleClickHandler(handler);
			zoomStepImg.addMouseUpHandler(handler);
			y += 10;
			container.add(zoomStepImg);
		}

		// Zoom out:
		Image zoomOutImg = new Image(left, y + 1, 20, 20, zoomOut);
		DOM.setStyleAttribute(zoomOutImg.getElement(), "cursor", "pointer");
		zoomOutImg.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		});
		zoomOutImg.addMouseDownHandler(handler);
		zoomOutImg.addClickHandler(handler);
		zoomOutImg.addDoubleClickHandler(handler);
		container.add(zoomOutImg);

		// Zoom step handler:
		renderHandler(true);
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Handler for dragging the zoom step handle. The mouse down goes onto the handle, the rest onto a large rectangle.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomStephandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler {

		private int minY, maxY;

		private int currentY;

		private boolean dragging;

		public void onMouseUp(MouseUpEvent event) {
			if (dragging) {
				dragging = false;
				zoomHandlerBg.setWidth(0);
				zoomHandlerBg.setHeight(0);
				int index = Math.round((currentY - minY) / 10);
				double scale = viewPort.getZoomStrategy().getZoomStepScale(index);
				viewPort.applyScale(scale);
			}
		}

		public void onMouseDown(MouseDownEvent event) {
			dragging = true;
			zoomHandlerBg.setWidth(viewPort.getMapWidth());
			zoomHandlerBg.setHeight(viewPort.getMapHeight());
			event.stopPropagation();
			event.preventDefault();
		}

		public void onMouseMove(MouseMoveEvent event) {
			if (dragging) {
				int y = event.getY();
				if (y < minY) {
					y = minY;
				}
				if (y > maxY) {
					y = maxY;
				}
				zoomStepHandleImg.setY(y);
				currentY = y;
				event.stopPropagation();
			}
		}

		public void onMouseOut(MouseOutEvent event) {
			if (dragging) {
				dragging = false;
				zoomHandlerBg.setWidth(0);
				zoomHandlerBg.setHeight(0);
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				int handleY = top + 21 + (index * 10);
				zoomStepHandleImg.setY(handleY);
			}
		}

		public void setMinY(int minY) {
			this.minY = minY;
		}

		public void setMaxY(int maxY) {
			this.maxY = maxY;
		}
	}

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map.
	 * 
	 * @author Pieter De Graef
	 */
	private class StopPropagationHandler implements MouseDownHandler, ClickHandler, DoubleClickHandler, MouseUpHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}

		public void onMouseUp(MouseUpEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}
	}
}