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

package org.geomajas.puregwt.example.client.sample.rendering;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.client.Showcase;
import org.geomajas.puregwt.example.client.sample.SamplePanel;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space.
 * 
 * @author Pieter De Graef
 */
public class WorldSpaceRenderingPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, WorldSpaceRenderingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private VectorContainer container;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = Showcase.GEOMAJASINJECTOR.getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapOsm");
		return layout;
	}

	@UiHandler("circleBtn")
	public void onCircleBtnClicked(ClickEvent event) {
		Circle circle = new Circle(0, 0, 3000000);
		circle.setFillColor("#66CC66");
		circle.setFillOpacity(0.4);
		container.add(circle);
	}

	@UiHandler("rectangleBtn")
	public void onRectangleBtnClicked(ClickEvent event) {
		Rectangle rectangle = new Rectangle(1000000, 1000000, 2000000, 1000000);
		rectangle.setFillColor("#CC9900");
		rectangle.setFillOpacity(0.4);
		container.add(rectangle);
	}

	@UiHandler("deleteBtn")
	public void onDeleteAllBtnClicked(ClickEvent event) {
		container.clear();
	}

	/**
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			container = mapPresenter.addWorldContainer();
		}
	}
}