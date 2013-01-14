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

package org.geomajas.gwt.example.client.sample.controller;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.AbstractRectangleController;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how a RectangleController can be added to the map and acted upon. 
 * In this sample the RectangleController is used as a tool to measure an area.
 * </p>
 * 
 * @author Frank Wynants
 */
public class RectangleControllerSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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
		 */
		class AreaController extends AbstractRectangleController {
			// A RectangleController that prints the size of the selected area
			
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

				SC.say(MESSAGES.rectangeControllerOutput(roundedKmWidth, roundedKmHeight, roundedArea));
			}
		}

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("mapWms", "gwtExample");
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
		return MESSAGES.rectangleControllerDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml",
				"classpath:org/geomajas/gwt/example/context/mapWms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
