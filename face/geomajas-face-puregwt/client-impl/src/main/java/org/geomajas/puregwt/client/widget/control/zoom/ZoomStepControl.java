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

package org.geomajas.puregwt.client.widget.control.zoom;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.widget.AbstractMapWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map widget that displays a button for each zoom step on the map. Also displays a zoom in and zoom out button.This
 * widget is meant to be added to the map's widget pane (see {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ZoomStepControl extends AbstractMapWidget {

	/**
	 * UI binder definition for the {@link ZoomStepControl} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ZoomStepGadgetUiBinder extends UiBinder<Widget, ZoomStepControl> {
	}

	private static final ZoomStepGadgetUiBinder UI_BINDER = GWT.create(ZoomStepGadgetUiBinder.class);

	private static final int ZOOMSTEP_HEIGHT = 10;

	private static final int ZOOMBUTTON_SIZE = 22;
	
	private final ZoomStepControlResource resource;

	private int top;

	private int left;

	private ViewPort viewPort;

	private boolean stretched;

	@UiField
	protected SimplePanel zoomInElement;

	@UiField
	protected SimplePanel zoomOutElement;

	@UiField
	protected AbsolutePanel zoomStepsPanel;

	@UiField
	protected SimplePanel zoomHandle;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter
	 *            The map presenter.
	 */
	public ZoomStepControl(MapPresenter mapPresenter) {
		this(mapPresenter, (ZoomStepControlResource) GWT.create(ZoomStepControlResource.class));
	}

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter
	 *            The map presenter.
	 * @param resource
	 *            Custom resource bundle in case you want to provide your own style for this widget.
	 */
	public ZoomStepControl(MapPresenter mapPresenter, ZoomStepControlResource resource) {
		super(mapPresenter);
		this.resource = resource;
		this.resource.css().ensureInjected();
		viewPort = mapPresenter.getViewPort();
		initWidget(UI_BINDER.createAndBindUi(this));
		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			}

			public void onViewPortScaled(ViewPortScaledEvent event) {
				positionZoomHandle();
			}

			public void onViewPortChanged(ViewPortChangedEvent event) {
				positionZoomHandle();
			}
		});
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		buildGui();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		left = toInteger(getElement().getStyle().getLeft());
		top = toInteger(getElement().getStyle().getTop());
		zoomInElement.getElement().setInnerText("+");
		zoomOutElement.getElement().setInnerText("-");
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();

		// Calculate height:
		int y = 0;
		for (int i = 0; i < viewPort.getZoomStrategy().getZoomStepCount(); i++) {
			final int count = i;

			SimplePanel zoomStep = new SimplePanel();
			zoomStep.setSize(ZOOMBUTTON_SIZE + "px", (ZOOMSTEP_HEIGHT + 1) + "px");
			zoomStep.setStyleName(resource.css().zoomStepControlStep());
			zoomStep.addDomHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					double scale = viewPort.getZoomStrategy().getZoomStepScale(count);
					viewPort.applyScale(scale);
					event.stopPropagation();
				}
			}, ClickEvent.getType());
			zoomStep.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
			zoomStep.addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
			zoomStep.addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
			zoomStep.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());
			zoomStepsPanel.add(zoomStep, 0, y);
			y += ZOOMSTEP_HEIGHT;
		}
		zoomStepsPanel.setSize(ZOOMBUTTON_SIZE + "px", (y + 1) + "px");
		setSize(ZOOMBUTTON_SIZE + "px", (y + (ZOOMBUTTON_SIZE * 2) + 1) + "px");

		// Zoom in button:
		zoomInElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());
		zoomInElement.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		zoomInElement.addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		zoomInElement.addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		zoomInElement.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		// Zoom out button:
		zoomOutElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());
		zoomOutElement.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		zoomOutElement.addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		zoomOutElement.addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		zoomOutElement.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		// Add the zoom handle:
		ZoomStephandler zoomStepHandler = new ZoomStephandler();
		zoomStepHandler.setMinY(top + ZOOMBUTTON_SIZE);
		zoomStepHandler.setMaxY(top + ZOOMBUTTON_SIZE + (viewPort.getZoomStrategy().getZoomStepCount() - 1)
				* ZOOMSTEP_HEIGHT);
		zoomHandle.addDomHandler(zoomStepHandler, MouseDownEvent.getType());
		addDomHandler(zoomStepHandler, MouseUpEvent.getType());
		addDomHandler(zoomStepHandler, MouseMoveEvent.getType());
		addDomHandler(zoomStepHandler, MouseOutEvent.getType());

		// Apply correct positions for all widgets:
		applyPositions();
	}

	private void stretchLayout() {
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setLeft(0, Unit.PX);
		setSize(viewPort.getMapWidth() + "px", viewPort.getMapHeight() + "px");
		stretched = true;
		applyPositions();
	}

	private void shrinkLayout() {
		stretched = false;
		getElement().getStyle().setTop(top, Unit.PX);
		getElement().getStyle().setLeft(left, Unit.PX);
		int y = viewPort.getZoomStrategy().getZoomStepCount() * ZOOMSTEP_HEIGHT;
		setSize(ZOOMBUTTON_SIZE + "px", (y + 1 + (ZOOMBUTTON_SIZE * 2)) + "px");
		applyPositions();
	}

	private void positionZoomHandle() {
		int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
		int handleY = getBaseTop() + ZOOMBUTTON_SIZE + 1 + (index * 10);
		int handleX = getBaseLeft();
		zoomHandle.getElement().getStyle().setLeft(handleX, Unit.PX);
		zoomHandle.getElement().getStyle().setTop(handleY, Unit.PX);
	}

	private void applyPositions() {
		int top = getBaseTop();
		int left = getBaseLeft();
		zoomInElement.getElement().getStyle().setTop(top, Unit.PX);
		zoomInElement.getElement().getStyle().setLeft(left/* + 2 */, Unit.PX);
		zoomStepsPanel.getElement().getStyle().setTop(top + ZOOMBUTTON_SIZE, Unit.PX);
		zoomStepsPanel.getElement().getStyle().setLeft(left/* + 2 */, Unit.PX);

		int y = viewPort.getZoomStrategy().getZoomStepCount() * ZOOMSTEP_HEIGHT;
		zoomOutElement.getElement().getStyle().setTop(top + ZOOMBUTTON_SIZE + y + 1, Unit.PX);
		zoomOutElement.getElement().getStyle().setLeft(left/* + 2 */, Unit.PX);
		positionZoomHandle();
	}

	private int getBaseTop() {
		return stretched ? top : 0;
	}

	private int getBaseLeft() {
		return stretched ? left : 0;
	}

	private int toInteger(String str) {
		int index = str.indexOf("px");
		try {
			return Integer.parseInt(str.substring(0, index));
		} catch (Exception e) {
			return 0;
		}
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
				shrinkLayout();
				int index = Math.round((currentY - minY) / 10);
				double scale = viewPort.getZoomStrategy().getZoomStepScale(index);
				viewPort.applyScale(scale);
			}
		}

		public void onMouseDown(MouseDownEvent event) {
			dragging = true;
			stretchLayout();
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
				zoomHandle.getElement().getStyle().setTop(y, Unit.PX);
				currentY = y;
				event.stopPropagation();
			}
		}

		public void onMouseOut(MouseOutEvent event) {
			if (dragging) {
				dragging = false;
				shrinkLayout();
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				int handleY = getBaseTop() + ZOOMBUTTON_SIZE + 1 + (index * ZOOMSTEP_HEIGHT);
				zoomHandle.getElement().getStyle().setTop(handleY, Unit.PX);
			}
		}

		public void setMinY(int minY) {
			this.minY = minY;
		}

		public void setMaxY(int maxY) {
			this.maxY = maxY;
		}
	}
}