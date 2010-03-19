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

package org.geomajas.gwt.client.pages;

import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
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
import org.geomajas.gwt.client.widget.OverviewMap;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * ???
 * 
 * @author check subversion
 */
public class ButtonPage extends AbstractTestPage {

	// private final OverviewMap overviewMap;

	private VLayout layout1;

	private VLayout layout2;

	private VLayout layout3;

	public ButtonPage(MapWidget map, final OverviewMap overviewMap) {
		super("Buttons", map);
		// this.overviewMap = overviewMap;
		mainLayout.setMembersMargin(5);

		HLayout horizontal = new HLayout();
		layout1 = new VLayout();
		layout1.setMembersMargin(5);
		layout1.setWidth(210);
		layout2 = new VLayout();
		layout2.setMembersMargin(5);
		layout2.setWidth(210);
		layout3 = new VLayout();
		layout3.setMembersMargin(5);
		layout3.setWidth(210);
		Label overviewLabel = new Label("OverviewMap buttons");
		overviewLabel.setHeight(30);
		layout3.addMember(overviewLabel);

		horizontal.addMember(layout1);
		horizontal.addMember(layout2);
		horizontal.addMember(layout3);

		VLayout layout10 = new VLayout();
		layout10.setWidth100();
		horizontal.addMember(layout10);
		mainLayout.addMember(horizontal);
		
		final Composite group1 = new Composite("group1");
		final Composite group2 = new Composite("group2");
		final Composite group3 = new Composite("group3");
		

		// ---------------------------------------------------------------------
		// Part1: adding buttons:
		// ---------------------------------------------------------------------

		// Button 1: Log inner HTML
		addButton("log html", new ClickHandler() {

			public void onClick(ClickEvent event) {
				GWT.log(getMap().getDOM().getInnerHTML(), null);
			}
		});

		// Button2: Draw circle
		addButton("Draw circle", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().drawGroup(null, group1);
				ShapeStyle style = new ShapeStyle("#00FF00", 0.5f, "#009900", 1, 2);
				getMap().getGraphics().drawCircle(group1, "circle", new Coordinate(200, 100), 30, style);
			}
		});

		// Button3: Draw LineString
		addButton("Draw LineString", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().drawGroup(null, group2);
				GeometryFactory factory = new GeometryFactory(4326, -1);
				LineString geometry = factory.createLineString(new Coordinate[] { new Coordinate(10, 10),
						new Coordinate(100, 50), new Coordinate(50, 100) });
				ShapeStyle style = new ShapeStyle("#00FF00", 0.5f, "#0000FF", 1, 2);
				getMap().getGraphics().drawLine(group2, "LineString", geometry, style);
			}
		});

		// Button4: Draw Polygon
		addButton("Draw Polygon", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().drawGroup(null, group3);
				GeometryFactory factory = new GeometryFactory(4326, -1);
				LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(110, 10),
						new Coordinate(210, 10), new Coordinate(210, 110), new Coordinate(110, 110),
						new Coordinate(110, 10) });
				LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(140, 40),
						new Coordinate(170, 40), new Coordinate(170, 70), new Coordinate(140, 70),
						new Coordinate(140, 40) });
				Polygon polygon = factory.createPolygon(shell, new LinearRing[] { hole });
				ShapeStyle style = new ShapeStyle("#A0A0A0", 0.5f, "#000000", 1, 2);
				getMap().getGraphics().drawPolygon(group2, "Polygon", polygon, style);
			}
		});

		// Button5: Draw Text
		addButton("Draw text", new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					getMap().getGraphics().drawGroup(null, group1);
					FontStyle style = new FontStyle("#00FF00", 12, "Verdana", "normal", "normal");
					getMap().getGraphics().drawText(group1, "text", "This is some text...", new Coordinate(200, 100),
							style);
				} catch (Throwable t) {
					GWT.log("Draw text failed", t);
				}
			}
		});

		// Button6: Draw Rectangle
		addButton("Draw Rectangle", new ClickHandler() {
			public void onClick(ClickEvent event) {
				getMap().getGraphics().drawGroup(null, group2);
				ShapeStyle style = new ShapeStyle("#00FF00", 0.5f, "#009900", 1, 2);
				getMap().getGraphics().drawRectangle(group2, "rectangle", new Bbox(50, 200, 200, 50), style);
			}
		});

		// Button7: Draw Image
		addButton("Draw Image", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().drawGroup(null, group1);
				Image image = new Image("image");
				image.setHref("http://www.geomajas.org/sites/all/themes/geomajas/images/header_logo.gif");
				image.setBounds(new Bbox(1, 1, 760, 120));
				image.setStyle(new PictureStyle(0.5));
				getMap().getGraphics().drawImage(group1, "image",
						"http://www.geomajas.org/sites/all/themes/geomajas/images/header_logo.gif",
						new Bbox(1, 1, 760, 120), new PictureStyle(0.5));
			}
		});

		// Button8: Transform elements
		addButton("Transform elements", new ClickHandler() {

			public void onClick(ClickEvent event) {
				Matrix matrix1 = new Matrix(0, 0, 0, 0, 40, 10);
				Matrix matrix2 = new Matrix(0, 0, 0, 0, 10, 40);
				Matrix matrix3 = new Matrix(0, 0, 0, 0, -10, -100);
				getMap().getGraphics().drawGroup(null, group1, matrix1);
				getMap().getGraphics().drawGroup(null, group2, matrix2);
				getMap().getGraphics().drawGroup(null, group3, matrix3);
			}
		});

		// Button9: Set cursor
		addButton("New cursor on circle", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().setCursor(group1, "circle", "wait");
			}
		});

		// Button10: Set ShapeType
		addButton2("Draw ShapeType", new ClickHandler() {

			public void onClick(ClickEvent event) {
				SymbolInfo symbol = new SymbolInfo();
				CircleInfo circle = new CircleInfo();
				circle.setR(5);
				symbol.setCircle(circle);

				getMap().getGraphics().drawSymbolDefinition(null, "screen.circleShapeType", symbol,
						new ShapeStyle("#00FF88", 0.5f, "#009966", 1, 2), null);
				getMap().getGraphics().drawSymbol(null, "screen.test1.symbol", new Coordinate(30, 30),
						new ShapeStyle("#00FF88", 0.5f, "#009966", 1, 2), "screen.circleShapeType");
			}
		});

		// Button11: Select a layer
		addButton2("Select a layer", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getMapModel().selectLayer(getMap().getMapModel().getLayer("structuresLayer"));
			}
		});

		// Button12: Hide test1 group
		addButton2("Hide + busy", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().hide(group1);
			}
		});

		// Button13: Unhide test1 group
		addButton2("Unhide - busy", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().unhide(group1);
			}
		});

		// Button14: Delete test1 group
		addButton2("Delete", new ClickHandler() {

			public void onClick(ClickEvent event) {
				getMap().getGraphics().deleteGroup(group1);
				getMap().getGraphics().deleteGroup(group2);
			}
		});

		// Button15: OverviewMap - new rectangle style
		addButton3("change rect style", new ClickHandler() {

			public void onClick(ClickEvent event) {
				overviewMap.setRectangleStyle(new ShapeStyle("#000000", 0.6f, "#000000", 1, 1));
			}
		});

		// Button16: OverviewMap - new extent style
		addButton3("change extent style", new ClickHandler() {

			public void onClick(ClickEvent event) {
				overviewMap.setTargetMaxExtentRectangleStyle(new ShapeStyle("#FF0000", 0, "#FF0000", 1, 3));
			}
		});

		// Button17: OverviewMap - toggle draw extent
		addButton3("toggle draw extent", new ClickHandler() {

			public void onClick(ClickEvent event) {
				overviewMap.setDrawTargetMaxExtent(!overviewMap.isDrawTargetMaxExtent());
			}
		});
	}

	/**
	 * To add buttons to your test page, use this method.
	 * 
	 * @param text
	 * @param handler
	 */
	protected void addButton(String text, ClickHandler handler) {
		IButton button = new IButton(text);
		button.addClickHandler(handler);
		button.setWidth(180);
		layout1.addMember(button);
	}

	protected void addButton2(String text, ClickHandler handler) {
		IButton button = new IButton(text);
		button.addClickHandler(handler);
		button.setWidth(180);
		layout2.addMember(button);
	}

	protected void addButton3(String text, ClickHandler handler) {
		IButton button = new IButton(text);
		button.addClickHandler(handler);
		button.setWidth(180);
		layout3.addMember(button);
	}

	public void initialize() {
	}
}
