/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.example.client.sample.mapwidget;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.gfx.paintable.Circle;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample that shows the difference in rendering in screen space versus world space.
 * 
 * @author Pieter De Graef
 */
public class WorldScreenSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "ScreenVersusWorld";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new WorldScreenSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create map with OSM layer, and add a PanController to it:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		final MapWidget map = new MapWidget("mapOsm", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Create a button layout:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setHeight(25);
		buttonLayout.setMembersMargin(10);
		IButton button1 = new IButton(MESSAGES.screenWorldBTNScreen());
		button1.setWidth("50%");

		final Image screenImage = new Image("imageInScreenSpace");
		screenImage.setHref(Geomajas.getIsomorphicDir() + "geomajas/example/image/smile.png");
		screenImage.setBounds(new Bbox(60, 60, 48, 48)); // Pixel coordinates
		screenImage.setStyle(new PictureStyle(0.6));

		button1.addClickHandler(new ClickHandler() {

			// Draw an image in screen space:
			public void onClick(ClickEvent event) {
				map.render(screenImage, RenderGroup.SCREEN, RenderStatus.ALL);
			}
		});
		buttonLayout.addMember(button1);

		IButton button2 = new IButton(MESSAGES.screenWorldBTNWorld());
		button2.setWidth("50%");

		// An image
		final Image worldImage = new Image("imageInWorldSpace");
		worldImage.setHref(Geomajas.getIsomorphicDir() + "geomajas/example/image/smile.png");
		worldImage.setBounds(new Bbox(-2000000, -2000000, 4000000, 4000000)); // Mercator coordinates
		worldImage.setStyle(new PictureStyle(0.8));

		// A rectangle
		final Rectangle worldRectangle = new Rectangle("RectangleInWorldSpace");
		worldRectangle.setBounds(new Bbox(2000000, 2000000, 500000, 800000));
		worldRectangle.setStyle(new ShapeStyle("#FFFF00", 0.5f, "#FFFF00", 1.0f, 2));
		
		// A circle
		final Circle worldCircle = new Circle("CircleInWorldSpace");
		worldCircle.setPosition(new Coordinate(2000000, 2000000));
		worldCircle.setRadius(500000);
		worldCircle.setStyle(new ShapeStyle("#FF00FF", 0.5f, "#FF00FF", 1.0f, 2));

		// And some geometries
		final GfxGeometry worldGeometry = new GfxGeometry("MultiPolygonInWorldSpace");
		final GeometryFactory gf = new GeometryFactory(map.getMapModel().getSrid(), map.getMapModel().getPrecision());
		Polygon p1 = gf.createPolygon(gf.createLinearRing(new Bbox(10000000d, 1000d, 1000000d, 1000000d)), null);
		Polygon p2 = gf.createPolygon(gf.createLinearRing(new Bbox(12000000d, 1000d, 500000d, 500000d)), null);
		MultiPolygon mp = gf.createMultiPolygon(new Polygon[] { p1, p2 });
		worldGeometry.setStyle(new ShapeStyle("#FF0000", 0.5f, "#FF0000", 1.0f, 2));
		worldGeometry.setGeometry(mp);

		final GfxGeometry worldGeometry2 = new GfxGeometry("MultiPointInWorldSpace");
		SymbolInfo si = new SymbolInfo();
		CircleInfo ci = new CircleInfo();
		ci.setR(8.0f);
		si.setCircle(ci);
		Point pt1 = gf.createPoint(new Coordinate(6000000, -5000000));
		Point pt2 = gf.createPoint(new Coordinate(8000000, -5000000));
		Point pt3 = gf.createPoint(new Coordinate(7000000, -7000000));
		worldGeometry2.setStyle(new ShapeStyle("#0000FF", 0.3f, "#0000FF", 1.0f, 2));
		worldGeometry2.setGeometry(gf.createMultiPoint(new Point[] { pt1, pt2, pt3 }));
		worldGeometry2.setSymbolInfo(si);

		// Some text
		FontStyle fontStyle = new FontStyle("#FF0000", 20, "Verdana", "normal", "normal");
		final Text worldText = new Text("TextInWorldSpace", "Sample scaling text",
				new Coordinate(2500000, 2500000), fontStyle);

		button2.addClickHandler(new ClickHandler() {

			// Draw an image and some geometries in world space:
			public void onClick(ClickEvent event) {
				map.registerWorldPaintable(worldImage);
				map.registerWorldPaintable(worldRectangle);
				map.registerWorldPaintable(worldCircle);
				map.registerWorldPaintable(worldGeometry);
				map.registerWorldPaintable(worldGeometry2);
				map.registerWorldPaintable(worldText);
			}
		});
		buttonLayout.addMember(button2);

		// Create a second button layout (delete buttons):
		HLayout buttonLayout2 = new HLayout();
		buttonLayout2.setMembersMargin(10);
		IButton button3 = new IButton(MESSAGES.screenWorldBTNScreenDelete());
		button3.setWidth("50%");
		button3.addClickHandler(new ClickHandler() {

			// Delete the image in screen space:
			public void onClick(ClickEvent event) {
				map.render(screenImage, RenderGroup.SCREEN, RenderStatus.DELETE);
			}
		});
		buttonLayout2.addMember(button3);

		IButton button4 = new IButton(MESSAGES.screenWorldBTNWorldDelete());
		button4.setWidth("50%");
		button4.addClickHandler(new ClickHandler() {

			// Delete the image and geometries in world space:
			public void onClick(ClickEvent event) {
				map.unregisterWorldPaintable(worldImage);
				map.unregisterWorldPaintable(worldRectangle);
				map.unregisterWorldPaintable(worldCircle);
				map.unregisterWorldPaintable(worldGeometry);
				map.unregisterWorldPaintable(worldGeometry2);
				map.unregisterWorldPaintable(worldText);
			}
		});
		buttonLayout2.addMember(button4);

		// Place both in the layout:
		layout.addMember(mapLayout);
		layout.addMember(buttonLayout);
		layout.addMember(buttonLayout2);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.screenWorldDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
