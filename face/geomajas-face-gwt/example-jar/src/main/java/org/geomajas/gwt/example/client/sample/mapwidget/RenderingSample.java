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

package org.geomajas.gwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows some direct rendering. It is mainly a test to see if everything works.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RenderingSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "RenderingSample";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new RenderingSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		// Map with ID wmsMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapOsm", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Layout with a huge list of buttons to test the rendering:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setPadding(10);
		buttonLayout.setMembersMargin(10);
		VLayout column1 = new VLayout();
		column1.setMembersMargin(10);
		VLayout column2 = new VLayout();
		column2.setMembersMargin(10);
		VLayout column3 = new VLayout();
		column3.setMembersMargin(10);

		final Composite group1 = new Composite("some-group");
		final Composite group2 = new Composite("another-group");

		// Button1: Draw circle
		IButton button1 = new IButton(MESSAGES.renderingDrawCircle());
		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group1);
				ShapeStyle style = new ShapeStyle("#66CC22", 0.5f, "#66AA22", 1, 2);
				map.getVectorContext().drawCircle(group1, "circle", new Coordinate(200, 100), 30, style);
			}
		});
		button1.setWidth100();
		column1.addMember(button1);

		// Button2: Draw LineString
		IButton button2 = new IButton(MESSAGES.renderingDrawLineString());
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group2);
				LineString geometry = map.getMapModel().getGeometryFactory().createLineString(
						new Coordinate[] { new Coordinate(60, 20), new Coordinate(120, 80), new Coordinate(80, 100) });
				ShapeStyle style = new ShapeStyle("#994488", 0.0f, "#993388", 1, 2);
				map.getVectorContext().drawLine(group2, "LineString", geometry, style);
			}
		});
		button2.setWidth100();
		column1.addMember(button2);

		// Button3: Draw Polygon
		IButton button3 = new IButton(MESSAGES.renderingDrawPolygon());
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group2);
				GeometryFactory factory = map.getMapModel().getGeometryFactory();
				LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(110, 10),
						new Coordinate(210, 10), new Coordinate(210, 110), new Coordinate(110, 110),
						new Coordinate(110, 10) });
				LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(140, 40),
						new Coordinate(170, 40), new Coordinate(170, 70), new Coordinate(140, 70),
						new Coordinate(140, 40) });
				Polygon polygon = factory.createPolygon(shell, new LinearRing[] { hole });
				ShapeStyle style = new ShapeStyle("#9933EE", 0.5f, "#9900FF", 1, 2);
				map.getVectorContext().drawPolygon(group2, "Polygon", polygon, style);
			}
		});
		button3.setWidth100();
		column1.addMember(button3);

		// Button4: Draw Text
		IButton button4 = new IButton(MESSAGES.renderingDrawText());
		button4.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group1);
				FontStyle style = new FontStyle("#FF0000", 12, "Verdana", "normal", "normal");
				map.getVectorContext()
						.drawText(group1, "text", "This is some text...", new Coordinate(100, 120), style);
			}
		});
		button4.setWidth100();
		column2.addMember(button4);

		// Button5: Draw Rectangle
		IButton button5 = new IButton(MESSAGES.renderingDrawRectangle());
		button5.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group2);
				ShapeStyle style = new ShapeStyle("#337788", 0.5f, "#337766", 1, 2);
				map.getVectorContext().drawRectangle(group2, "rectangle", new Bbox(50, 200, 200, 50), style);
			}
		});
		button5.setWidth100();
		column2.addMember(button5);

		// Button6: Draw Image
		IButton button6 = new IButton(MESSAGES.renderingDrawImage());
		button6.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group1);
				Image image = new Image("image");
				image.setHref(Geomajas.getIsomorphicDir() + "geomajas/example/image/smile.png");
				image.setBounds(new Bbox(250, 70, 48, 48));
				image.setStyle(new PictureStyle(0.5));
				// map.render(image, "all");
				map.getVectorContext().drawImage(group1, image.getId(), image.getHref(), image.getBounds(),
						(PictureStyle) image.getStyle());
			}
		});
		button6.setWidth100();
		column2.addMember(button6);

		// Button7: Set cursor
		IButton button7 = new IButton(MESSAGES.renderingNewCursor());
		button7.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().setCursor(group2, "rectangle", "wait");
			}
		});
		button7.setWidth100();
		column3.addMember(button7);

		// Button8: Transform elements
		IButton button8 = new IButton(MESSAGES.renderingTransform());
		button8.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group1,
						new Matrix(0, 0, 0, 0, 150, 00));
				map.getVectorContext().drawGroup(map.getGroup(RenderGroup.SCREEN), group2,
						new Matrix(0, 0, 0, 0, 0, 100));
			}
		});
		button8.setWidth100();
		column3.addMember(button8);

		// Button9: Delete everything
		IButton button9 = new IButton(MESSAGES.renderingDelete());
		button9.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getVectorContext().deleteGroup(group1);
				map.getVectorContext().deleteGroup(group2);
			}
		});
		button9.setWidth100();
		column3.addMember(button9);

		buttonLayout.addMember(column1);
		buttonLayout.addMember(column2);
		buttonLayout.addMember(column3);

		layout.addMember(mapLayout);
		layout.addMember(buttonLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.renderingDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
