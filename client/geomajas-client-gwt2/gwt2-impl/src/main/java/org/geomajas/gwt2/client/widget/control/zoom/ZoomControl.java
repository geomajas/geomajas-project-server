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

package org.geomajas.gwt2.client.widget.control.zoom;

import org.geomajas.gwt.client.util.Browser;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.widget.AbstractMapWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map widget that displays zoom in, zoom out and zoom to maximum extent buttons. This widget is meant to be added to
 * the map's widget pane (see {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 */
public class ZoomControl extends AbstractMapWidget {

	/**
	 * UI binder definition for the {@link ZoomControl} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ZoomControlUiBinder extends UiBinder<Widget, ZoomControl> {
	}

	private static final ZoomControlUiBinder UI_BINDER = GWT.create(ZoomControlUiBinder.class);

	private final ZoomControlResource resource;

	@UiField
	protected SimplePanel zoomInElement;

	@UiField
	protected SimplePanel zoomOutElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a widget with zoom in, zoom out and zoom to maximum extent button.
	 * 
	 * @param mapPresenter
	 *            The map to show this widget on.
	 */
	public ZoomControl(MapPresenter mapPresenter) {
		this(mapPresenter, (ZoomControlResource) GWT.create(ZoomControlResource.class));
	}

	/**
	 * Create a widget with zoom in, zoom out and zoom to maximum extent button.
	 * 
	 * @param mapPresenter
	 *            The map to show this widget on.
	 * @param resource
	 *            Custom resource bundle in case you want to provide your own style for this widget.
	 */
	public ZoomControl(MapPresenter mapPresenter, ZoomControlResource resource) {
		super(mapPresenter);
		this.resource = resource;
		this.resource.css().ensureInjected();
		buildGui();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		initWidget(UI_BINDER.createAndBindUi(this));
		zoomInElement.getElement().setInnerText("+");
		zoomOutElement.getElement().setInnerText("-");

		if (Browser.isMobile()) {
			initializeForMobile();
		} else {
			initializeForDesktop();
		}
	}

	/** Initialize handlers for desktop browser. */
	private void initializeForDesktop() {
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		final ViewPort viewPort = mapPresenter.getViewPort();

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
	}

	/** Initialize handlers for mobile devices. */
	private void initializeForMobile() {
		// Add touch handlers to the zoom in button:
		zoomInElement.addDomHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				zoomInElement.addStyleName(resource.css().zoomControlZoomInTouch());
				ViewPort viewPort = mapPresenter.getViewPort();
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		}, TouchStartEvent.getType());
		zoomInElement.addDomHandler(new TouchEndHandler() {

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				zoomInElement.removeStyleName(resource.css().zoomControlZoomInTouch());
			}
		}, TouchEndEvent.getType());

		// Add touch handlers to the zoom out button:
		zoomOutElement.addDomHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				zoomOutElement.addStyleName(resource.css().zoomControlZoomOutTouch());
				ViewPort viewPort = mapPresenter.getViewPort();
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		}, TouchStartEvent.getType());
		zoomOutElement.addDomHandler(new TouchEndHandler() {

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				zoomOutElement.removeStyleName(resource.css().zoomControlZoomOutTouch());
			}
		}, TouchEndEvent.getType());
	}
}