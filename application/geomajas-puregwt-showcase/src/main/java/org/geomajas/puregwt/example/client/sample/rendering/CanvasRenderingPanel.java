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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.gfx.CanvasContainer;
import org.geomajas.puregwt.client.gfx.CanvasPath;
import org.geomajas.puregwt.client.gfx.CanvasRect;
import org.geomajas.puregwt.client.gfx.CanvasShape;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.client.Showcase;
import org.geomajas.puregwt.example.client.sample.SamplePanel;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates canvas rendering abilities.
 * 
 * @author Jan De Moerloose
 */
public class CanvasRenderingPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, CanvasRenderingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private CanvasContainer container;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected Label label;

	@UiField
	protected ListBox countBox;

	private static final double TOTAL_SIZE = 40000000;

	private static final double SHAPE_SIZE = 40000000;

	private int count = 100;

	private int[] options = { 1, 10, 100, 1000, 10000, 20000, 50000, 100000 };

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
		label.setVisible(false);
		for (int option : options) {
			countBox.addItem(option + "");
		}
		countBox.setSelectedIndex(2);
		return layout;
	}

	@UiHandler("countBox")
	public void changeCount(ChangeEvent event) {
		count = options[countBox.getSelectedIndex()];
	}

	@UiHandler("polyBtn")
	public void onPolyBtnClicked(ClickEvent event) {
		if (container != null) {
			container.clear();
			List<CanvasShape> shapes = new ArrayList<CanvasShape>();
			double factor = Math.pow(count, -0.5);
			for (int i = 0; i < count; i++) {
				double x1 = (Random.nextDouble() - 0.5) * (TOTAL_SIZE - SHAPE_SIZE * factor);
				double y1 = (Random.nextDouble() - 0.5) * (TOTAL_SIZE - SHAPE_SIZE * factor);
				double x2 = x1 + (Random.nextDouble() - 0.5) * SHAPE_SIZE * factor;
				double y2 = y1 + (Random.nextDouble() - 0.5) * SHAPE_SIZE * factor;
				double x3 = x1 + (Random.nextDouble() - 0.5) * SHAPE_SIZE * factor;
				double y3 = y1 + (Random.nextDouble() - 0.5) * SHAPE_SIZE * factor;
				Coordinate[] coords = new Coordinate[] { new Coordinate(x1, y1), new Coordinate(x2, y2),
						new Coordinate(x3, y3), new Coordinate(x1, y1) };
				Geometry ring = new Geometry(Geometry.LINEAR_RING, 0, 5);
				ring.setCoordinates(coords);
				Geometry poly = new Geometry(Geometry.POLYGON, 0, 5);
				poly.setGeometries(new Geometry[] { ring });
				CanvasPath path = new CanvasPath(poly);
				path.setFillStyle(getRandomRGB(0.5));
				path.setStrokeStyle(getRandomRGB(1));
				shapes.add(path);
			}
			container.addAll(shapes);
			container.repaint();
		}
	}

	@UiHandler("rectBtn")
	public void onRectangleBtnClicked(ClickEvent event) {
		if (container != null) {
			container.clear();
			List<CanvasShape> shapes = new ArrayList<CanvasShape>();
			double factor = Math.pow(count, -0.5);
			for (int i = 0; i < count; i++) {
				double x = (Random.nextDouble() - 0.5) * (TOTAL_SIZE - SHAPE_SIZE * factor);
				double y = (Random.nextDouble() - 0.5) * (TOTAL_SIZE - SHAPE_SIZE * factor);
				double width = Random.nextDouble() * SHAPE_SIZE * factor;
				double height = Random.nextDouble() * SHAPE_SIZE * factor;
				CanvasRect rect = new CanvasRect(new Bbox(x - width / 2, y - height / 2, width, height));
				rect.setFillStyle(getRandomRGB(0.5));
				rect.setStrokeStyle(getRandomRGB(1));
				shapes.add(rect);
			}
			container.addAll(shapes);
			container.repaint();
		}
	}

	private String getRandomRGB(double d) {
		int r = Random.nextInt(256);
		int g = Random.nextInt(256);
		int b = Random.nextInt(256);
		return "rgba(" + r + "," + g + "," + b + "," + d + ")";
	}

	/**
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			if (Canvas.isSupported()) {
				container = mapPresenter.addWorldCanvas();
			} else {
				label.setText(Showcase.MSG_SAMPLE.renderingMissingCanvas());
				label.setVisible(true);
			}
		}
	}
}