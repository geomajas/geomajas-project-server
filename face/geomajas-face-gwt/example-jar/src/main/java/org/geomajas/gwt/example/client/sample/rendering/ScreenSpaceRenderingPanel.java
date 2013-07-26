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

package org.geomajas.gwt.example.client.sample.rendering;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.gfx.VectorContainer;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.client.ExampleJar;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Circle;
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
 * ContentPanel that demonstrates rendering abilities in screen space.
 * 
 * @author Pieter De Graef
 */
public class ScreenSpaceRenderingPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, ScreenSpaceRenderingPanel> {
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
		mapPresenter.initialize("gwt-app", "mapOsm");
		return layout;
	}

	@UiHandler("circleBtn")
	public void onCircleBtnClicked(ClickEvent event) {
		Circle circle = new Circle(300, 140, 30);
		circle.setFillColor("#0099CC");
		circle.setFillOpacity(0.4);
		container.add(circle);
	}

	@UiHandler("rectangleBtn")
	public void onRectangleBtnClicked(ClickEvent event) {
		Rectangle rectangle = new Rectangle(60, 40, 200, 80);
		rectangle.setFillColor("#CC9900");
		rectangle.setFillOpacity(0.4);
		container.add(rectangle);
	}

	@UiHandler("textBtn")
	public void onTextBtnClicked(ClickEvent event) {
		Text text = new Text(70, 60, "Hello World");
		text.setFontFamily("Arial");
		text.setFontSize(16);
		text.setStrokeOpacity(0);
		text.setFillColor("#000000");
		container.add(text);
	}

	@UiHandler("pathBtn")
	public void onPathBtnClicked(ClickEvent event) {
		Geometry geometry = new Geometry(Geometry.POLYGON, 0, 0);
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(120, 160), new Coordinate(220, 160),
				new Coordinate(220, 260), new Coordinate(120, 260), new Coordinate(120, 160) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(140, 180), new Coordinate(190, 180),
				new Coordinate(190, 230), new Coordinate(140, 230), new Coordinate(140, 180) });
		geometry.setGeometries(new Geometry[] { shell, hole });

		Shape shape = (Shape) ExampleJar.getInjector().getGfxUtil().toShape(geometry);
		shape.setFillColor("#0066AA");
		shape.setFillOpacity(0.4);
		shape.setStrokeColor("#004499");

		container.add(shape);
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
			container = mapPresenter.addScreenContainer();
		}
	}
}