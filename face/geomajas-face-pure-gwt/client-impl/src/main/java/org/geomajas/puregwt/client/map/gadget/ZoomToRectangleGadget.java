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

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.path.ClosePath;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

/**
 * Map gadget that displays a button for zooming in to a rectangle on the map. The user is supposed to drag the
 * rectangle after hitting this button.
 * 
 * @author Pieter De Graef
 */
public class ZoomToRectangleGadget implements MapGadget {

	private int top;

	private int left;

	private VectorContainer container;

	private ZoomToRectGroup zoomToRectangleGroup;

	private HandlerRegistration escapeHandler;

	private String zoomToRectangleImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoom_rectangle.png";

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public ZoomToRectangleGadget(int top, int left) {
		this.top = top;
		this.left = left;
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(final ViewPort viewPort, final VectorContainer container) {
		this.container = container;

		Image zoomToRectangle = new Image(left, top, 20, 20, zoomToRectangleImage);
		zoomToRectangle.setTitle("Zoom to rectangle by dragging the mouse on the map.");
		DOM.setStyleAttribute(zoomToRectangle.getElement(), "cursor", "pointer");
		zoomToRectangle.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				zoomToRectangleGroup = new ZoomToRectGroup(viewPort);
				escapeHandler = Event.addNativePreviewHandler(new NativePreviewHandler() {

					public void onPreviewNativeEvent(NativePreviewEvent event) {
						if (event.getTypeInt() == Event.ONKEYDOWN || event.getTypeInt() == Event.ONKEYPRESS) {
							if (KeyCodes.KEY_ESCAPE == event.getNativeEvent().getKeyCode()) {
								onScale();
							}
						}
					}
				});

				container.add(zoomToRectangleGroup);
				event.stopPropagation();
			}
		});

		StopPropagationHandler handler = new StopPropagationHandler();
		zoomToRectangle.addMouseDownHandler(handler);
		zoomToRectangle.addClickHandler(handler);
		zoomToRectangle.addDoubleClickHandler(handler);

		container.add(zoomToRectangle);

	}

	public void onTranslate() {
		// Do nothing.
	}

	public void onScale() {
		if (zoomToRectangleGroup != null) {
			container.remove(zoomToRectangleGroup);
			zoomToRectangleGroup = null;
		}
		if (escapeHandler != null) {
			escapeHandler.removeHandler();
			escapeHandler = null;
		}
	}

	public void onResize() {
		// Do nothing.
	}

	public void onDestroy() {
		if (container != null) {
			container.clear();
		}
		if (escapeHandler != null) {
			escapeHandler.removeHandler();
			escapeHandler = null;
		}
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map.
	 * 
	 * @author Pieter De Graef
	 */
	private class StopPropagationHandler implements MouseDownHandler, ClickHandler, DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
		}
	}

	/**
	 * Vector group that lets the user zoom to a rectangle.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectGroup extends Group {

		private int offset;

		private Rectangle eventCatcher;

		private Path zoomInRect;

		private boolean dragging;

		private int x;

		private int y;

		private Bbox screenBounds;

		public ZoomToRectGroup(final ViewPort viewPort) {
			eventCatcher = new Rectangle(0, 0, viewPort.getMapWidth(), viewPort.getMapHeight());
			eventCatcher.setFillOpacity(0);
			eventCatcher.setStrokeOpacity(0);

			zoomInRect = new Path(0, 0);
			zoomInRect.setFillColor("#000000");
			zoomInRect.setFillOpacity(0.2);
			zoomInRect.setStrokeColor("#000000");
			zoomInRect.setStrokeWidth(1);
			zoomInRect.setStrokeOpacity(1);
			DOM.setElementAttribute(zoomInRect.getElement(), "fill-rule", "evenodd");
			zoomInRect.lineTo(viewPort.getMapWidth(), 0);
			zoomInRect.lineTo(viewPort.getMapWidth(), viewPort.getMapHeight());
			zoomInRect.lineTo(0, viewPort.getMapHeight());
			zoomInRect.close();
			zoomInRect.moveTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.close();

			add(zoomInRect);
			add(eventCatcher);

			eventCatcher.addMouseDownHandler(new MouseDownHandler() {

				public void onMouseDown(MouseDownEvent event) {
					if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
						dragging = true;
						x = event.getX() - offset;
						y = event.getY() - offset;
						updateRectangle(event);
					}
					event.stopPropagation();
					event.preventDefault();
				}
			});
			eventCatcher.addMouseUpHandler(new MouseUpHandler() {

				public void onMouseUp(MouseUpEvent event) {
					if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT && dragging) {
						dragging = false;
						if (screenBounds != null) {
							Bbox worldBounds = viewPort.transform(screenBounds, RenderSpace.SCREEN, RenderSpace.WORLD);
							viewPort.applyBounds(worldBounds);
						}
					}
					event.stopPropagation();
				}
			});
			eventCatcher.addMouseMoveHandler(new MouseMoveHandler() {

				public void onMouseMove(MouseMoveEvent event) {
					if (dragging) {
						updateRectangle(event);
					}
					event.stopPropagation();
				}
			});
			eventCatcher.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					event.stopPropagation();
				}
			});
			eventCatcher.addDoubleClickHandler(new DoubleClickHandler() {

				public void onDoubleClick(DoubleClickEvent event) {
					event.stopPropagation();
				}
			});
		}

		private void updateRectangle(MouseEvent<?> event) {
			int beginX = x;
			int beginY = y;
			int endX = event.getX() - offset;
			int endY = event.getY() - offset;

			// Check if begin and end need to be reversed:
			if (beginX > endX) {
				int temp = endX;
				endX = beginX;
				beginX = temp;
			}
			if (beginY > endY) {
				int temp = endY;
				endY = beginY;
				beginY = temp;
			}

			int width = endX - beginX;
			int height = endY - beginY;
			if (height != 0 && width != 0) {
				zoomInRect.setStep(5, new MoveTo(false, beginX, beginY));
				zoomInRect.setStep(6, new LineTo(false, endX, beginY));
				zoomInRect.setStep(7, new LineTo(false, endX, endY));
				zoomInRect.setStep(8, new LineTo(false, beginX, endY));
				zoomInRect.setStep(9, new ClosePath());
				screenBounds = new Bbox(beginX, beginY, width, height);
			}
		}
	}
}