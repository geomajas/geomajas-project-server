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
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.listener.AbstractListener;
import org.geomajas.gwt.client.controller.listener.ListenerEvent;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that demonstrates the use of a listener on the map. It follows the mouse position and prints it out.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MouseMoveListenerSample extends SamplePanel {

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
		final MapWidget map = new MapWidget("osmMap", "gwt-samples");

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
				label.setContents(I18nProvider.getSampleMessages().customControllerScreenCoordinates() + " = "
						+ screenPosition + "<br/>"
						+ I18nProvider.getSampleMessages().customControllerWorldCoordinates() + " = (" + x + ", " + y
						+ ")");
			}
		}
		map.setListener(new MouseMoveListener());

		layout.addMember(mapLayout);
		layout.addMember(labelLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().mouseMoveListenerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/MouseMoveListenerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/example/gwt/servercfg/raster/layerWmsBluemarble.xml",
				"classpath:org/geomajas/example/gwt/clientcfg/layer/mapWms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
