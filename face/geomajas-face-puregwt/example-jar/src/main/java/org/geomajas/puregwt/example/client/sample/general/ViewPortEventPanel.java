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

package org.geomajas.puregwt.example.client.sample.general;

import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates view port events.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class ViewPortEventPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, ViewPortEventPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel eventLayout;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		// Create the layout for this sample:
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addViewPortChangedHandler(new MyViewPortChangedHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapOsm");
		return layout;
	}

	// ------------------------------------------------------------------------
	// Click handlers for the buttons:
	// ------------------------------------------------------------------------

	@UiHandler("enlargeMapBtn")
	protected void onEnlargeMapBtnClicked(ClickEvent event) {
		int width = mapPresenter.getViewPort().getMapWidth() + 20;
		int height = mapPresenter.getViewPort().getMapHeight() + 15;
		mapPresenter.setSize(width, height);
	}

	@UiHandler("shrinkMapBtn")
	protected void onShrinkMapBtnClicked(ClickEvent event) {
		int width = mapPresenter.getViewPort().getMapWidth() - 20;
		int height = mapPresenter.getViewPort().getMapHeight() - 15;
		mapPresenter.setSize(width, height);
	}

	@UiHandler("clearEventsBtn")
	protected void onClearEventBtnClicked(ClickEvent event) {
		eventLayout.clear();
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Handler that catches view port events and prints them on the layout.
	 * 
	 * @author Jan De Moerloose
	 */
	private class MyViewPortChangedHandler implements ViewPortChangedHandler {

		@Override
		public void onViewPortChanged(ViewPortChangedEvent event) {
			eventLayout.add(new Label("ViewPortChangedEvent"));
		}

		@Override
		public void onViewPortScaled(ViewPortScaledEvent event) {
			eventLayout.add(new Label("ViewPortScaledEvent"));
		}

		@Override
		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			eventLayout.add(new Label("ViewPortTranslatedEvent"));
		}
	}
}