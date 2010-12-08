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
import org.geomajas.gwt.client.controller.AbstractCircleController;
import org.geomajas.gwt.client.util.DistanceFormat;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how a CircleController can be added to the map and acted upon. In this sample the CircleController
 * is used as a tool to measure an area.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CircleControllerSample extends SamplePanel {

	public static final String TITLE = "CircleController";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CircleControllerSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Create the map, using the wmsMap configuration (mapWms.xml):
		final MapWidget map = new MapWidget("wmsMap", "gwt-samples");

		/**
		 * Define a AbstractCircleController that calculates the size of the selected area and outputs it.
		 * 
		 * @author Pieter De Graef
		 */
		class CircleAreaController extends AbstractCircleController {

			public CircleAreaController(MapWidget mapWidget) {
				super(mapWidget);
			}

			/** onMouseUp: calculate circle area, and print it. */
			protected void onCircleReady() {
				// Get the circle radius in map units, and calculate the area:
				double radius = getWorldRadius();
				double circleArea = Math.PI * radius * radius;

				// Transform the area from map units, to a readable format (km, mile, ...)
				String area = DistanceFormat.asMapLength(map, circleArea);
				SC.say(I18nProvider.getSampleMessages().circleControllerOutput(area));
			}
		}

		// Apply the CircleAreaController onto the map:
		map.setController(new CircleAreaController(map));

		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().circleControllerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/CircleControllerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerWmsBluemarble.xml", "WEB-INF/mapWms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
