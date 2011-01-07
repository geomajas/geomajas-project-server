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

package org.geomajas.example.gwt.client.samples.controller;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how to add a customer controller to the map that can be dragged around by the user.
 * </p>
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class ControllerOnElementSample extends SamplePanel {

	public static final String TITLE = "ControllerOnElement";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ControllerOnElementSample();
		}
	};

	public Canvas getViewPanel() {

		// Drawing information:
		final Bbox rectangle = new Bbox(100, 100, 300, 100);
		final ShapeStyle style = new ShapeStyle("#66AA00", 0.7f, "#667700", 0.8f, 3);
		final ShapeStyle hoverStyle = new ShapeStyle("#66AA00", 0.5f, "#667700", 0.7f, 3);

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("osmMap", "gwt-samples");

		// Create the custom controller:
		final GraphicsController customController = new AbstractGraphicsController(map) {

			public void onMouseOver(MouseOverEvent event) {
				// When the mouse hovers over the image, make it transparent:
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle,
						hoverStyle);
			}

			public void onMouseOut(MouseOutEvent event) {
				// When the mouse moves away from the image, make it visible again:
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle, style);
			}

			public void onMouseDown(MouseDownEvent event) {
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle,
						new ShapeStyle("#AA0000", 0.8f, "#990000", 1.0f, 3));
			}

			public void onMouseUp(MouseUpEvent event) {
				map.getVectorContext().drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle,
						hoverStyle);
			}
		};

		// After map initialization we draw an image on the map:
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				GraphicsContext graphics = map.getVectorContext();
				graphics.drawRectangle(map.getGroup(RenderGroup.SCREEN), "rectangle", rectangle, style);
				map.getVectorContext().setController(map.getGroup(RenderGroup.SCREEN), "rectangle", customController);
			}
		});

		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().controllerOnElementDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/ControllerOnElementSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml", "WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
