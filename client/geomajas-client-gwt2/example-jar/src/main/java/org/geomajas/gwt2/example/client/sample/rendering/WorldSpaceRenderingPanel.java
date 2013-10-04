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

package org.geomajas.gwt2.example.client.sample.rendering;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.client.ExampleJar;
import org.vaadin.gwtgraphics.client.VectorObject;
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

	@UiHandler("geometryBtn")
	public void onGeometryBtnClicked(ClickEvent event) {
		try {
			Geometry polygon = WktService
					.toGeometry("POLYGON ((0 0, 0 0.7, 0.7 0.7, 0 0),(0.1 0.2, 0.1 0.4, 0.3 0.4, 0.1 0.2))");
			Geometry line = WktService.toGeometry("LINESTRING (-2 0, -2 0.7, -1.3 0, -1.3 0.7)");
			Geometry point = WktService.toGeometry("POINT (-3.5 0.5)");
			Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 5);
			Matrix m1 = new Matrix(1, 0, 0, 1, -0.5, -2);
			Matrix m2 = new Matrix(1, 0, 0, 1, 0.5, -2);
			Matrix m3 = new Matrix(1, 0, 0, 1, 0, -1);
			multiPolygon.setGeometries(new Geometry[] { transform(polygon, m1), transform(polygon, m2),
					transform(polygon, m3), });
			Geometry multiLinestring = new Geometry(Geometry.MULTI_LINE_STRING, 0, 5);
			multiLinestring.setGeometries(new Geometry[] { transform(line, m1), transform(line, m2),
					transform(line, m3), });
			Geometry mp = new Geometry(Geometry.MULTI_POINT, 0, 5);
			mp.setGeometries(new Geometry[] { transform(point, m1), transform(point, m2), transform(point, m3), });
			container.add(scaleAndStyle(polygon));
			container.add(scaleAndStyle(line));
			container.add(scaleAndStyle(point));
			container.add(scaleAndStyle(multiPolygon));
			container.add(scaleAndStyle(multiLinestring));
			container.add(scaleAndStyle(mp));
		} catch (WktException e) {
			// not possible
		}
	}

	private VectorObject scaleAndStyle(Geometry geom) {
		Matrix scale = new Matrix(1000000, 0, 0, 1000000, 0, 0);
		GfxUtil util = ExampleJar.getInjector().getGfxUtil();
		VectorObject shape = util.toShape(transform(geom, scale));
		util.applyStroke(shape, "#CC9900", 0.8, 1, "2 5");
		util.applyFill(shape, "#CC9900", geom.getGeometryType().endsWith("String") ? 0f : 0.5f);
		return shape;
	}

	public Geometry transform(Geometry geometry, Matrix matrix) {
		Geometry copy = GeometryService.clone(geometry);
		transformInplace(copy, matrix);
		return copy;
	}

	private void transformInplace(Geometry geometry, Matrix matrix) {
		if (geometry.getGeometries() != null) {
			for (Geometry g : geometry.getGeometries()) {
				transformInplace(g, matrix);
			}
		} else if (geometry.getCoordinates() != null) {
			for (Coordinate c : geometry.getCoordinates()) {
				double x = c.getX() * matrix.getXx() + c.getY() * matrix.getXy() + matrix.getDx();
				double y = c.getX() * matrix.getYx() + c.getY() * matrix.getYy() + matrix.getDy();
				c.setX(x);
				c.setY(y);
			}
		}
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