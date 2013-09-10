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
import org.geomajas.gwt.client.gfx.TransformableWidget;
import org.geomajas.gwt.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
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

	static MarkerImageResources resources = GWT.create(MarkerImageResources.class);

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
			Marker1 marker1 = new Marker1(userX, userY);
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

	private class Marker1 extends Image implements TransformableWidget {

		private double userX;

		private double userY;

		private double deltaX;

		private double deltaY;

		private double scaleX;

		private double scaleY;

		public Marker1(double userX, double userY) {
			super(resources.marker1());
			getElement().getStyle().setPosition(Position.ABSOLUTE);
			this.userX = userX;
			this.userY = userY;
		}

		@Override
		public void setTranslation(double deltaX, double deltaY) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
			render();
		}

		@Override
		public void setScale(double scaleX, double scaleY) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			render();
		}

		@Override
		public void setFixedSize(boolean fixedSize) {
		}

		@Override
		public boolean isFixedSize() {
			return true;
		}

		private void render() {
			double left = userX * scaleX + deltaX;
			double top = userY * scaleY + deltaY;
			getElement().getStyle().setLeft(left - 16, Unit.PX);
			getElement().getStyle().setTop(top - 32, Unit.PX);
		}

	}

}