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
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.example.client.ExampleJar;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Ellipse;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates fixed size rendering abilities in world space.
 * 
 * @author Pieter De Graef
 */
public class FixedSizeWorldSpaceRenderingPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, FixedSizeWorldSpaceRenderingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private VectorContainer container;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
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
		Circle circle = new Circle(200000, 5000000, 10);
		circle.setFillColor("#FF0000");
		circle.setFillOpacity(1);
		circle.setStrokeOpacity(0);
		container.add(circle);
	}

	@UiHandler("ellipseBtn")
	public void onEllipseBtnClicked(ClickEvent event) {
		Ellipse ellipse = new Ellipse(300000, 2000000, 20, 30);
		ellipse.setFillColor("#0000FF");
		ellipse.setFillOpacity(1);
		ellipse.setStrokeOpacity(0);
		container.add(ellipse);
	}

	@UiHandler("rectangleBtn")
	public void onRectangleBtnClicked(ClickEvent event) {
		Rectangle rectangle = new Rectangle(-5000000, -2000000, 40, 80);
		rectangle.setFillColor("#FFFF00");
		rectangle.setFillOpacity(1);
		rectangle.setStrokeOpacity(0);
		container.add(rectangle);
	}

	@UiHandler("imageBtn")
	public void onImageBtnClicked(ClickEvent event) {
		Image image = new Image(6000000, -3000000, 24, 24, GWT.getModuleBaseURL() + "image/layer/city1.png");
		container.add(image);
	}

	@UiHandler("textBtn")
	public void onTextBtnClicked(ClickEvent event) {
		Text text = new Text(-6000000, 1000000, "Hello World");
		text.setFillColor("#0066AA");
		text.setFillOpacity(1);
		text.setStrokeOpacity(0);
		container.add(text);
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
			container.setFixedSize(true);
		}
	}
}