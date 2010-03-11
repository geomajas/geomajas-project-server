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

package org.geomajas.gwt.client.samples.controller;

import org.geomajas.gwt.client.controller.RectangleController;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how a RectangleController can be added to the map and acted upon. 
 * In this sample the RectangleController is used as a tool to measure an area.
 * </p>
 * 
 * @author Frank Wynants
 */
public class RectangleControllerSample extends SamplePanel {

	private static final double KM_DEGREE = 112.12; // the number of km in 1 degree

	public static final String TITLE = "RectangleController";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new RectangleControllerSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		/**
		 * <p>
		 * A RectangleController that calculates the size of the selected area and outputs it.
		 * </p>
		 * @author Frank Wynants
		 *
		 */
		class AreaController extends RectangleController { // A RectangleController that prints out the size of the
															// selected area
			/**
			 * Constructor
			 * 
			 * @param mapWidget
			 */
			public AreaController(MapWidget mapWidget) {
				super(mapWidget);
			}

			/**
			 * When the user selects a rectangle the size of the rectangle in km is printed out and the total size of
			 * the area is computed and printed out
			 * 
			 * @param worldBounds
			 */
			protected void selectRectangle(Bbox worldBounds) {

				double cWidth = worldBounds.getWidth();
				double cHeight = worldBounds.getHeight();

				// transfer degrees to km
				double kmWidth = cWidth * KM_DEGREE;
				double kmHeight = cHeight * KM_DEGREE;

				// compute total area
				double area = kmWidth * kmHeight;

				// round the numbers two 2 digits for easier human reading
				double roundedKmWidth = Math.round(kmWidth * 100) / 100.0;
				double roundedKmHeight = Math.round(kmHeight * 100) / 100.0;
				double roundedArea = Math.round(area * 100) / 100.0;

				SC.say(I18nProvider.getSampleMessages().rectangeControllerOutput(roundedKmWidth, roundedKmHeight,
						roundedArea));
			}
		}

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("wmsMap", "gwt-samples");
		map.addDrawHandler(new DrawHandler() {

			public void onDraw(DrawEvent event) {
				AreaController areaController = new AreaController(map);
				map.setController(areaController);
			}
		});

		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().rectangleControllerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/controller/RectangleControllerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerWmsBlueMarble.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/mapWms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
