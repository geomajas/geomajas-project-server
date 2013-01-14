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

import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that displays zoom in, zoom out and zoom to maximum extent buttons.
 * 
 * @author Pieter De Graef
 */
public class SimpleZoomGadget extends AbstractMapGadget {

	/**
	 * UI binder definition for the {@link SimpleZoomGadget} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface SimpleZoomGadgetUiBinder extends UiBinder<Widget, SimpleZoomGadget> {
	}

	private static final SimpleZoomGadgetUiBinder UI_BINDER = GWT.create(SimpleZoomGadgetUiBinder.class);

	private Widget layout;

	@UiField
	protected SimplePanel zoomInElement;

	@UiField
	protected SimplePanel zoomOutElement;

	@UiField
	protected SimplePanel extentElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public SimpleZoomGadget(int top, int left) {
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

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		layout = UI_BINDER.createAndBindUi(this);
		layout.getElement().getStyle().setPosition(Position.ABSOLUTE);
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		layout.addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

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

		// Zoom to maximum extent button:
		extentElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				viewPort.applyBounds(viewPort.getMaximumBounds());
				event.stopPropagation();
			}
		}, ClickEvent.getType());
	}
}