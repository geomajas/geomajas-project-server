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
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.path.ClosePath;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that displays a button for zooming in to a rectangle on the map. The user is supposed to drag the
 * rectangle after hitting this button.
 * 
 * @author Pieter De Graef
 */
public class ZoomToRectangleGadget extends AbstractMapGadget {

	private static final int DEFAULT_LEFT = 60;

	private static final int DEFAULT_TOP = 5;

	private VectorContainer container;

	private ZoomToRectGroup zoomToRectangleGroup;

	private HandlerRegistration escapeHandler;

	private SimplePanel layout;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public ZoomToRectangleGadget() {
		setHorizontalMargin(DEFAULT_LEFT);
		setVerticalMargin(DEFAULT_TOP);
	}

	public ZoomToRectangleGadget(int top, int left) {
		setHorizontalMargin(left);
		setVerticalMargin(top);
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public Widget asWidget() {
		if (layout == null) {
			buildGui();
		}
		return layout;
	}

	public void beforeDraw(MapPresenter mapPresenter) {
		super.beforeDraw(mapPresenter);
		mapPresenter.getEventBus().addHandler(ViewPortChangedHandler.TYPE, new ViewPortChangedHandler() {

			public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			}

			public void onViewPortScaled(ViewPortScaledEvent event) {
				cleanup();
			}

			public void onViewPortChanged(ViewPortChangedEvent event) {
				cleanup();
			}
		});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void cleanup() {
		if (zoomToRectangleGroup != null) {
			MapGadget mapGadget = zoomToRectangleGroup.getEventCatcher();
			mapPresenter.removeMapGadget(mapGadget);
			container.remove(zoomToRectangleGroup);
			zoomToRectangleGroup = null;
		}
		if (escapeHandler != null) {
			escapeHandler.removeHandler();
			escapeHandler = null;
		}
		mapPresenter.removeVectorContainer(container);
	}

	private void buildGui() {
		layout = new SimplePanel();
		layout.setStyleName("gm-ZoomToRectangleGadget");
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		// Create TOP button:
		layout.addDomHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				cleanup();
				container = mapPresenter.addScreenContainer();
				zoomToRectangleGroup = new ZoomToRectGroup(mapPresenter.getViewPort());
				escapeHandler = Event.addNativePreviewHandler(new NativePreviewHandler() {

					public void onPreviewNativeEvent(NativePreviewEvent event) {
						if (event.getTypeInt() == Event.ONKEYDOWN || event.getTypeInt() == Event.ONKEYPRESS) {
							if (KeyCodes.KEY_ESCAPE == event.getNativeEvent().getKeyCode()) {
								cleanup();
							}
						}
					}
				});

				container.add(zoomToRectangleGroup);
			}
		}, MouseUpEvent.getType());

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
			event.preventDefault();
		}
	}

	/**
	 * Vector group that lets the user zoom to a rectangle.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectGroup extends Group {

		private AbstractMapGadget eventCatcher;

		private Path zoomInRect;

		private boolean dragging;

		private int x;

		private int y;

		private Bbox screenBounds;

		public ZoomToRectGroup(final ViewPort viewPort) {
			eventCatcher = new AbstractMapGadget() {

				private SimplePanel rectangle;

				public Widget asWidget() {
					if (rectangle == null) {
						rectangle = new SimplePanel();
						rectangle.setSize(mapPresenter.getViewPort().getMapWidth() + "px", mapPresenter.getViewPort()
								.getMapHeight() + "px");
					}
					return rectangle;
				}
			};

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
			mapPresenter.addMapGadget(eventCatcher);

			eventCatcher.asWidget().addDomHandler(new MouseDownHandler() {

				public void onMouseDown(MouseDownEvent event) {
					if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
						dragging = true;
						x = event.getRelativeX(mapPresenter.asWidget().getElement());
						y = event.getRelativeY(mapPresenter.asWidget().getElement());
						updateRectangle(event);
					}
					event.stopPropagation();
					event.preventDefault();
				}
			}, MouseDownEvent.getType());

			eventCatcher.asWidget().addDomHandler(new MouseUpHandler() {

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
			}, MouseUpEvent.getType());

			eventCatcher.asWidget().addDomHandler(new MouseMoveHandler() {

				public void onMouseMove(MouseMoveEvent event) {
					if (dragging) {
						updateRectangle(event);
					}
					event.stopPropagation();
				}
			}, MouseMoveEvent.getType());

			eventCatcher.asWidget().addDomHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					event.stopPropagation();
				}
			}, ClickEvent.getType());

			eventCatcher.asWidget().addDomHandler(new DoubleClickHandler() {

				public void onDoubleClick(DoubleClickEvent event) {
					event.stopPropagation();
				}
			}, DoubleClickEvent.getType());
		}

		public MapGadget getEventCatcher() {
			return eventCatcher;
		}

		private void updateRectangle(MouseEvent<?> event) {
			int beginX = x;
			int beginY = y;
			int endX = event.getRelativeX(mapPresenter.asWidget().getElement());
			int endY = event.getRelativeY(mapPresenter.asWidget().getElement());

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