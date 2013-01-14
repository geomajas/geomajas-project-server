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

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
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
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that displays a butten for each zoom step on the map. Also displays a zoom in and zoom out button.
 * 
 * @author Pieter De Graef
 */
public class ZoomStepGadget extends AbstractMapGadget {

	/**
	 * UI binder definition for the {@link ZoomStepGadget} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ZoomStepGadgetUiBinder extends UiBinder<Widget, ZoomStepGadget> {
	}

	private static final ZoomStepGadgetUiBinder UI_BINDER = GWT.create(ZoomStepGadgetUiBinder.class);

	private static final int ZOOMSTEP_HEIGHT = 10;

	private static final int ZOOMBUTTON_HEIGHT = 20;

	private int gadgetTop;

	private int gadgetLeft;

	private ViewPort viewPort;

	private Widget layout;

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

	public ZoomStepGadget(int gadgetTop, int gadgetLeft) {
		this.gadgetTop = gadgetTop;
		this.gadgetLeft = gadgetLeft;
		setHorizontalMargin(gadgetLeft);
		setVerticalMargin(gadgetTop);
		setHorizontalAlignment(Alignment.BEGIN);
		setVerticalAlignment(Alignment.BEGIN);
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

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		viewPort = mapPresenter.getViewPort();
		layout = UI_BINDER.createAndBindUi(this);
		layout.getElement().getStyle().setPosition(Position.ABSOLUTE);
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();

		// Calculate height:
		int y = 0;
		for (int i = 0; i < viewPort.getZoomStrategy().getZoomStepCount(); i++) {
			final int count = i;

			SimplePanel zoomStep = new SimplePanel();
			zoomStep.setSize(ZOOMBUTTON_HEIGHT + "px", (ZOOMSTEP_HEIGHT + 1) + "px");
			zoomStep.setStyleName("gm-ZoomStepGadget-step");
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
		zoomStepsPanel.setSize("24px", (y + 1) + "px");
		layout.setSize("24px", (y + 41) + "px");

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
		zoomStepHandler.setMinY(gadgetTop + ZOOMBUTTON_HEIGHT);
		zoomStepHandler.setMaxY(gadgetTop + ZOOMBUTTON_HEIGHT + (viewPort.getZoomStrategy().getZoomStepCount() - 1)
				* ZOOMSTEP_HEIGHT);
		zoomHandle.addDomHandler(zoomStepHandler, MouseDownEvent.getType());
		layout.addDomHandler(zoomStepHandler, MouseUpEvent.getType());
		layout.addDomHandler(zoomStepHandler, MouseMoveEvent.getType());
		layout.addDomHandler(zoomStepHandler, MouseOutEvent.getType());

		// Apply correct positions for all widgets:
		applyPositions();
	}

	private void stretchLayout() {
		setHorizontalMargin(0);
		setVerticalMargin(0);
		layout.getElement().getStyle().setTop(0, Unit.PX);
		layout.getElement().getStyle().setLeft(0, Unit.PX);
		layout.setSize(viewPort.getMapWidth() + "px", viewPort.getMapHeight() + "px");
		stretched = true;
		setHorizontalAlignment(Alignment.STRETCH);
		setVerticalAlignment(Alignment.STRETCH);
		applyPositions();
	}

	private void shrinkLayout() {
		setHorizontalMargin(gadgetLeft);
		setVerticalMargin(gadgetTop);
		setHorizontalAlignment(Alignment.BEGIN);
		setVerticalAlignment(Alignment.BEGIN);
		stretched = false;
		layout.getElement().getStyle().setTop(gadgetTop, Unit.PX);
		layout.getElement().getStyle().setLeft(gadgetLeft, Unit.PX);
		int y = viewPort.getZoomStrategy().getZoomStepCount() * ZOOMSTEP_HEIGHT;
		layout.setSize("24px", (y + 1 + (ZOOMBUTTON_HEIGHT * 2)) + "px");
		applyPositions();
	}

	private void positionZoomHandle() {
		int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
		int handleY = getBaseTop() + 21 + (index * 10);
		int handleX = getBaseLeft();
		zoomHandle.getElement().getStyle().setLeft(handleX, Unit.PX);
		zoomHandle.getElement().getStyle().setTop(handleY, Unit.PX);
	}

	private void applyPositions() {
		int top = getBaseTop();
		int left = getBaseLeft();
		zoomInElement.getElement().getStyle().setTop(top, Unit.PX);
		zoomInElement.getElement().getStyle().setLeft(left + 2, Unit.PX);
		zoomStepsPanel.getElement().getStyle().setTop(top + ZOOMBUTTON_HEIGHT, Unit.PX);
		zoomStepsPanel.getElement().getStyle().setLeft(left + 2, Unit.PX);

		int y = viewPort.getZoomStrategy().getZoomStepCount() * ZOOMSTEP_HEIGHT;
		zoomOutElement.getElement().getStyle().setTop(top + ZOOMBUTTON_HEIGHT + y + 1, Unit.PX);
		zoomOutElement.getElement().getStyle().setLeft(left + 2, Unit.PX);
		positionZoomHandle();
	}

	private int getBaseTop() {
		return stretched ? gadgetTop : 0;
	}

	private int getBaseLeft() {
		return stretched ? gadgetLeft : 0;
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
				int handleY = getBaseTop() + ZOOMBUTTON_HEIGHT + 1 + (index * ZOOMSTEP_HEIGHT);
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