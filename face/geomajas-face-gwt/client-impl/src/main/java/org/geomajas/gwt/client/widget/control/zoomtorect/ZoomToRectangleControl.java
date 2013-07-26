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

package org.geomajas.gwt.client.widget.control.zoomtorect;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.event.ViewPortChangedEvent;
import org.geomajas.gwt.client.event.ViewPortChangedHandler;
import org.geomajas.gwt.client.event.ViewPortScaledEvent;
import org.geomajas.gwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.gwt.client.gfx.VectorContainer;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.map.ViewPort;
import org.geomajas.gwt.client.widget.AbstractMapWidget;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.path.ClosePath;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

import com.google.gwt.core.shared.GWT;
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

/**
 * Map widget that displays a button for zooming in to a rectangle on the map. The user is supposed to drag the
 * rectangle after hitting this button. This widget is meant to be added to the map's widget pane (see
 * {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ZoomToRectangleControl extends AbstractMapWidget {

	private final ZoomToRectangleControlResource resource;

	private VectorContainer container;

	private ZoomToRectGroup zoomToRectangleGroup;

	private HandlerRegistration escapeHandler;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter
	 *            The map presenter.
	 */
	public ZoomToRectangleControl(MapPresenter mapPresenter) {
		this(mapPresenter, (ZoomToRectangleControlResource) GWT.create(ZoomToRectangleControlResource.class));
	}

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter
	 *            The map presenter.
	 */
	public ZoomToRectangleControl(MapPresenter mapPresenter, ZoomToRectangleControlResource resource) {
		super(mapPresenter);
		this.resource = resource;
		this.resource.css().ensureInjected();
		buildGui();
		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortTranslated(ViewPortTranslatedEvent event) {
				cleanup();
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
		initWidget(new SimplePanel());
		setStyleName(resource.css().zoomToRectangle());
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		// Create TOP button:
		addDomHandler(new MouseUpHandler() {

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
	 * Vector group that lets the user zoom to a rectangle.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectGroup extends Group {

		private Rectangle eventCatcher;

		private Path zoomInRect;

		private boolean dragging;

		private int x;

		private int y;

		private Bbox screenBounds;

		public ZoomToRectGroup(final ViewPort viewPort) {
			eventCatcher = new Rectangle(0, 0, mapPresenter.getViewPort().getMapWidth(), mapPresenter.getViewPort()
					.getMapHeight());
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
						x = event.getRelativeX(mapPresenter.asWidget().getElement());
						y = event.getRelativeY(mapPresenter.asWidget().getElement());
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