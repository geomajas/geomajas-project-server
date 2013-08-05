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

package org.geomajas.smartgwt.example.client.sample.controller;

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.client.controller.AbstractCircleController;
import org.geomajas.smartgwt.client.util.DistanceFormat;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how a CircleController can be added to the map and acted upon. In this sample the CircleController
 * is used as a tool to measure an area.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CircleControllerSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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
		final MapWidget map = new MapWidget("mapWms", "gwtExample");

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
				SC.say(MESSAGES.circleControllerOutput(area));
			}
		}

		// Apply the CircleAreaController onto the map:
		map.setController(new CircleAreaController(map));

		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.circleControllerDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/base/layerWmsBluemarble.xml",
				"classpath:org/geomajas/smartgwt/example/context/mapWms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
