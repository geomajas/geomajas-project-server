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

package org.geomajas.puregwt.client.widget;

import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Touch widget to zoom in and zoom out on mobile devices.
 * 
 * @author Dosi Bingov
 * @since 1.0.0
 */
public class TouchZoomWidget extends AbstractMapWidget {

	private static TouchZoomWidgetUiBinder uiBinder = GWT.create(TouchZoomWidgetUiBinder.class);

	/**
	 * ...
	 * @author Dosi Bingov
	 *
	 */
	interface TouchZoomWidgetUiBinder extends UiBinder<Widget, TouchZoomWidget> {
	}

	/**
	 * touched and normal css style of touch widget.
	 * @author Dosi Bingov
	 *
	 */
	interface TouchZoomStyle extends CssResource {
		String normal();
		String touched();
	}

	@UiField
	protected TouchZoomStyle style;

	private int topPos = 15; // css top position

	private int leftPos = 15; // css left position

	@UiField
	protected Button zoomIn;

	@UiField
	protected Button zoomOut;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a widget with zoom in, zoom out buttons.
	 * 
	 * @param mapPresenter
	 *            The map to show this widget on.
	 */
	public TouchZoomWidget(MapPresenter mapPresenter) {
		super(mapPresenter);
		init();
	}

	/**
	 * Create a widget with zoom in, zoom out buttons.
	 * 
	 * @param mapPresenter
	 *            The map to show this widget on.
	 * 
	 * @param topPos
	 *            top location of the widget on the map in pixels.
	 * @param top
	 *            top left location of the widget on the map in pixels.
	 */
	public TouchZoomWidget(MapPresenter mapPresenter, int topPos, int leftPos) {
		super(mapPresenter);
		this.topPos = topPos;
		this.leftPos = leftPos;
		init();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void init() {
		initWidget(uiBinder.createAndBindUi(this));
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setTop(topPos, Unit.PX);
		getElement().getStyle().setLeft(leftPos, Unit.PX);

		zoomIn.addTouchStartHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				zoomIn.addStyleName(style.touched());

				int index = TouchZoomWidget.this.mapPresenter.getViewPort().getZoomStrategy()
						.getZoomStepIndex(TouchZoomWidget.this.mapPresenter.getViewPort().getScale());
				try {
					TouchZoomWidget.this.mapPresenter.getViewPort().applyScale(
							TouchZoomWidget.this.mapPresenter.getViewPort().getZoomStrategy()
									.getZoomStepScale(index - 1));
				} catch (IllegalArgumentException e) {
				}

				event.stopPropagation();
			}
		});

		zoomIn.addTouchEndHandler(new TouchEndHandler() {

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				zoomIn.removeStyleName(style.touched());
			}
		});

		zoomOut.addTouchStartHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				zoomOut.addStyleName(style.touched());

				int index = TouchZoomWidget.this.mapPresenter.getViewPort().getZoomStrategy()
						.getZoomStepIndex(TouchZoomWidget.this.mapPresenter.getViewPort().getScale());
				try {
					TouchZoomWidget.this.mapPresenter.getViewPort().applyScale(
							TouchZoomWidget.this.mapPresenter.getViewPort().getZoomStrategy()
									.getZoomStepScale(index + 1));
				} catch (IllegalArgumentException e) {
				}

				event.stopPropagation();
			}
		});

		zoomOut.addTouchEndHandler(new TouchEndHandler() {

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				zoomOut.addStyleName(style.touched());
			}
		});

		zoomOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				zoomOut.removeStyleName(style.touched());
			}
		});
	}
}
