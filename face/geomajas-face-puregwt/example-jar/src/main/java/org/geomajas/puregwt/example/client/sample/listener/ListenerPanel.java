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

package org.geomajas.puregwt.example.client.sample.listener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.controller.AbstractMapController;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.base.client.ExampleBase;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;

/**
 * ContentPanel that demonstrates layer visibility.
 *
 * @author Pieter De Graef
 */
public class ListenerPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, ListenerPanel> {

	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel layerEventLayout;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected Label worldPos;

	@UiField
	protected Label screenPos;

	private MapMouseMoveListener mapMouseMoveListener = new MapMouseMoveListener();

	private MapMouseDownListener mapMouseDownListener = new MapMouseDownListener();

	private Widget layout;

	/**
	 *  Mouse move listener. Map listener class should extend AbstractMapController.
	 *
	 *  @author Dosi Bingov
	 *
	 */
	protected class MapMouseMoveListener extends AbstractMapController {

		public MapMouseMoveListener() {
			super(false); //set dragging to false
		}

		// ------------------------------------------------------------------------
		// MapMouseMoveListener implementation:
		// ------------------------------------------------------------------------

		@Override
		public void onActivate(MapPresenter mapPresenter) {
			super.onActivate(mapPresenter);
		}

		@Override
		public void onDeactivate(MapPresenter presenter) {
			//do clean your controls
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			logEvent(event.getAssociatedType().getName());
			Coordinate screenPosition = getLocation(event, RenderSpace.SCREEN);
			Coordinate worldPosition = getLocation(event, RenderSpace.WORLD);
			logCoordinates(screenPosition, worldPosition);
			super.onMouseMove(event);  //not really needed if not dragging
		}
	}

	/**
	 *  Mouse down listener. Map listener class should extend AbstractMapController.
	 *
	 *  @author Dosi Bingov
	 *
	 */
	protected class MapMouseDownListener extends AbstractMapController {

		public MapMouseDownListener() {
			super(false); //set dragging to false
		}

		// ------------------------------------------------------------------------
		// MapMouseDownListener implementation:
		// ------------------------------------------------------------------------

		@Override
		public void onActivate(MapPresenter mapPresenter) {
			super.onActivate(mapPresenter);
		}

		@Override
		public void onDeactivate(MapPresenter presenter) {
			//do clean your controls
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			logEvent(event.getAssociatedType().getName());
			Coordinate screenPosition = getLocation(event, RenderSpace.SCREEN);
			Coordinate worldPosition = getLocation(event, RenderSpace.WORLD);
			logCoordinates(screenPosition, worldPosition);
		}
	}

	public ListenerPanel() {
		layout = UI_BINDER.createAndBindUi(this);
		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleBase.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);

		mapPresenter.initialize("puregwt-app", "mapLayerVisibility");

		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());

		initListeners();
		mapPanel.add(mapDecorator);
	}

	void initListeners() {
		mapPresenter.addMapListener(mapMouseDownListener);
		//mapPresenter.addMapListener(mapMouseMoveListener);
	}

	private void logEvent(String eventType) {
		layerEventLayout.add(new Label("Event type => " + eventType));
	}

	private void logCoordinates(Coordinate screenPosition, Coordinate worldPosition)  {
		screenPos.setText("(" + screenPosition.getX() + ", " +
				screenPosition.getY() + ")");

		worldPos.setText("(" + worldPosition.getX() + ", " +
				worldPosition.getY() + ")");
	}

	public Widget asWidget() {
		return layout;
	}
}