/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.samples.mapwidget;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows some direct rendering. It is mainly a test to see if everything works.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RenderingSample extends SamplePanel {

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

		// Map with ID wmsMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("osmMap", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Layout with a huge list of buttons to test the rendering:
		VLayout buttonLayout = new VLayout();
		buttonLayout.setPadding(10);
		buttonLayout.setHeight(150);
		buttonLayout.setShowEdges(true);
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
		IButton button1 = new IButton("Draw circle");
		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group1);
				ShapeStyle style = new ShapeStyle("#66CC22", 0.5f, "#66AA22", 1, 2);
				map.getGraphics().drawCircle(group1, "circle", new Coordinate(200, 100), 30, style);
			}
		});
		button1.setWidth100();
		column1.addMember(button1);

		// Button2: Draw LineString
		IButton button2 = new IButton("Draw LineString");
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group2);
				LineString geometry = map.getMapModel().getGeometryFactory().createLineString(
						new Coordinate[] { new Coordinate(60, 20), new Coordinate(120, 80), new Coordinate(80, 100) });
				ShapeStyle style = new ShapeStyle("#994488", 0.0f, "#993388", 1, 2);
				map.getGraphics().drawLine(group2, "LineString", geometry, style);
			}
		});
		button2.setWidth100();
		column1.addMember(button2);

		// Button3: Draw Polygon
		IButton button3 = new IButton("Draw Polygon");
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group1);
				GeometryFactory factory = map.getMapModel().getGeometryFactory();
				LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(110, 10),
						new Coordinate(210, 10), new Coordinate(210, 110), new Coordinate(110, 110),
						new Coordinate(110, 10) });
				LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(140, 40),
						new Coordinate(170, 40), new Coordinate(170, 70), new Coordinate(140, 70),
						new Coordinate(140, 40) });
				Polygon polygon = factory.createPolygon(shell, new LinearRing[] { hole });
				ShapeStyle style = new ShapeStyle("#9933EE", 0.5f, "#9900FF", 1, 2);
				map.getGraphics().drawPolygon(group2, "Polygon", polygon, style);
			}
		});
		button3.setWidth100();
		column1.addMember(button3);

		// Button4: Draw Text
		IButton button4 = new IButton("Draw text");
		button4.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group1);
				FontStyle style = new FontStyle("#009900", 12, "Verdana", "normal", "normal");
				map.getGraphics().drawText(group1, "text", "This is some text...", new Coordinate(100, 120), style);
			}
		});
		button4.setWidth100();
		column2.addMember(button4);

		// Button5: Draw Rectangle
		IButton button5 = new IButton("Draw Rectangle");
		button5.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group2);
				ShapeStyle style = new ShapeStyle("#337788", 0.5f, "#337766", 1, 2);
				map.getGraphics().drawRectangle(group2, "rectangle", new Bbox(50, 200, 200, 50), style);
			}
		});
		button5.setWidth100();
		column2.addMember(button5);

		// Button6: Draw Image
		IButton button6 = new IButton("Draw Image");
		button6.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group1);
				Image image = new Image("image");
				image.setHref(Geomajas.getIsomorphicDir() + "geomajas/example/images/smile.png");
				image.setBounds(new Bbox(30, 70, 48, 48));
				image.setStyle(new PictureStyle(0.5));
				// map.render(image, "all");
				map.getGraphics().drawImage(group1, image.getId(), image.getHref(), image.getBounds(),
						(PictureStyle) image.getStyle());
			}
		});
		button6.setWidth100();
		column2.addMember(button6);

		// Button7: Set cursor
		IButton button7 = new IButton("New cursor on circle");
		button7.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().setCursor(group1, "circle", "wait");
			}
		});
		button7.setWidth100();
		column3.addMember(button7);

		// Button8: Transform elements
		IButton button8 = new IButton("Transform elements");
		button8.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group1, new Matrix(0, 0, 0, 0, 50, 00));
				map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), group2, new Matrix(0, 0, 0, 0, 0, 50));
			}
		});
		button8.setWidth100();
		column3.addMember(button8);

		// Button9: Delete everything
		IButton button9 = new IButton("Delete everything");
		button9.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getGraphics().deleteGroup(group1);
				map.getGraphics().deleteGroup(group2);
			}
		});
		button9.setWidth100();
		column3.addMember(button9);

		buttonLayout.addMember(column1);
		buttonLayout.addMember(column2);
		buttonLayout.addMember(column3);

		layout.addMember(buttonLayout);
		layout.addMember(mapLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().renderingDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/mapwidget/RenderingSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
