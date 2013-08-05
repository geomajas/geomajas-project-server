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
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.controller.listener.AbstractListener;
import org.geomajas.smartgwt.client.controller.listener.ListenerEvent;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that demonstrates the use of a listener on the map. It follows the mouse position and prints it out.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MouseMoveListenerSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "MouseMoveListener";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new MouseMoveListenerSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Create the map, using the wmsMap configuration (mapOsm.xml):
		final MapWidget map = new MapWidget("mapOsm", "gwtExample");

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");
		mapLayout.addMember(map);

		VLayout labelLayout = new VLayout();
		final Label label = new Label();
		labelLayout.addMember(label);

		/**
		 * Listener implementation that prints out the mouse position.
		 * 
		 * @author Pieter De Graef
		 */
		class MouseMoveListener extends AbstractListener {

			public void onMouseMove(ListenerEvent event) {
				Coordinate screenPosition = event.getScreenPosition();
				Coordinate worldPosition = event.getWorldPosition();
				String x = NumberFormat.getFormat("0.000").format(worldPosition.getX());
				String y = NumberFormat.getFormat("0.000").format(worldPosition.getY());
				label.setContents(MESSAGES.customControllerScreenCoordinates() + " = "
						+ screenPosition + "<br/>"
						+ MESSAGES.customControllerWorldCoordinates() + " = (" + x + ", " + y
						+ ")");
			}
		}
		map.addListener(new MouseMoveListener());

		layout.addMember(mapLayout);
		layout.addMember(labelLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.mouseMoveListenerDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/smartgwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
