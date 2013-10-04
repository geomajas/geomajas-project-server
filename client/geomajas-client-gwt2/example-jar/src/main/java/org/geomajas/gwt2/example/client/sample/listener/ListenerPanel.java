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

package org.geomajas.gwt2.example.client.sample.listener;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

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
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected SpanElement worldX;

	@UiField
	protected SpanElement worldY;

	@UiField
	protected SpanElement screenX;

	@UiField
	protected SpanElement screenY;

	private Widget layout;

	public ListenerPanel() {
		layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.initialize("gwt-app", "mapLayerVisibility");

		// Add a passive listener that print out mouse coordinates:
		mapPresenter.addMapListener(new MapMouseMoveListener());

		// Add the map to the GUI:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);
	}

	public Widget asWidget() {
		return layout;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Mouse move listener. Map listener class should extend AbstractMapController.
	 * 
	 * @author Dosi Bingov
	 */
	private class MapMouseMoveListener extends AbstractMapController {

		public MapMouseMoveListener() {
			super(false); // set dragging to false. We're not dragging right now.
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			Coordinate screenPosition = getLocation(event, RenderSpace.SCREEN);
			Coordinate worldPosition = getLocation(event, RenderSpace.WORLD);
			logCoordinates(screenPosition, worldPosition);
		}

		private void logCoordinates(Coordinate screenPosition, Coordinate worldPosition) {
			worldX.setInnerText(format(worldPosition.getX()));
			worldY.setInnerText(format(worldPosition.getY()));
			screenX.setInnerText(format(screenPosition.getX()));
			screenY.setInnerText(format(screenPosition.getY()));
		}

		private String format(double d) {
			return NumberFormat.getFormat("#.00").format(d);
		}
	}
}