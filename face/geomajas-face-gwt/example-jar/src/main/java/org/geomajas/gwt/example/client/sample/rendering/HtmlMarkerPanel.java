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

import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.gfx.AbstractTransformableWidget;
import org.geomajas.gwt.client.gfx.TransformableWidget;
import org.geomajas.gwt.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in HTML world space.
 * 
 * @author Jan De Moerloose
 */
public class HtmlMarkerPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, HtmlMarkerPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private TransformableWidgetContainer container;

	private static MarkerImageResources resources = GWT.create(MarkerImageResources.class);

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

	@UiHandler("markersBtn")
	public void onMarkersBtnClicked(ClickEvent event) {
		for (int i = 0; i < 20; i++) {
			double userX = Random.nextDouble() * 40000000 - 20000000;
			double userY = Random.nextDouble() * 40000000 - 20000000;
			Marker1 marker1 = new Marker1(resources.marker1(), userX, userY, 16, 32);
			container.add(marker1);
		}
	}

	@UiHandler("deleteBtn")
	public void onDeleteAllBtnClicked(ClickEvent event) {
		container.clear();
	}

	/**
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			container = mapPresenter.addWorldWidgetContainer();
		}
	}

	/**
	 * A simple image-based marker.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class Marker1 extends AbstractTransformableWidget implements TransformableWidget {

		private int anchorX;

		private int anchorY;

		public Marker1(ImageResource url, double x, double y, int anchorX, int anchorY) {
			super(new Image(url), x, y);
			this.anchorX = anchorX;
			this.anchorY = anchorY;
			asWidget().getElement().getStyle().setPosition(Position.ABSOLUTE);
		}

		@Override
		protected void setScreenPosition(double left, double top) {
			asWidget().getElement().getStyle().setLeft(left - anchorX, Unit.PX);
			asWidget().getElement().getStyle().setTop(top - anchorY, Unit.PX);
		}

	}

}