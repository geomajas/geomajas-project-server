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

package org.geomajas.example.gwt.client.samples.controller;

import com.google.gwt.i18n.client.NumberFormat;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a map one raster and one vector layer, both using the same CRS.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CustomControllerSample extends SamplePanel {

	public static final String TITLE = "CustomController";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CustomControllerSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("mapWms", "gwt-samples");
		mapLayout.addMember(map);

		VLayout labelLayout = new VLayout();
		final Label label = new Label();
		labelLayout.addMember(label);

		// Create the custom controller:
		GraphicsController customController = new AbstractGraphicsController(map) {
			
			public void onMouseMove(MouseMoveEvent event) {
				Coordinate screenPosition = getScreenPosition(event);
				Coordinate worldPosition = getWorldPosition(event);
				String x = NumberFormat.getFormat("0.000").format(worldPosition.getX());
				String y = NumberFormat.getFormat("0.000").format(worldPosition.getY());
				label.setContents(I18nProvider.getSampleMessages().customControllerScreenCoordinates() + " = "
						+ screenPosition + "<br/>"
						+ I18nProvider.getSampleMessages().customControllerWorldCoordinates()
						+ " = (" + x + ", " + y + ")");
			}

		};

		// Set the controller on the map:
		map.setController(customController);

		layout.addMember(mapLayout);
		layout.addMember(labelLayout);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().customControllerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/CustomControllerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerWmsBluemarble.xml", "WEB-INF/mapWms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
