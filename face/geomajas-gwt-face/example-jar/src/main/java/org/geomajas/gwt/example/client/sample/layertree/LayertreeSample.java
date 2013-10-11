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

package org.geomajas.gwt.example.client.sample.layertree;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows the usage of the LayerTree.
 * </p>
 * 
 * @author Frank Wynants
 */
public class LayertreeSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "LAYERTREE";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayertreeSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		final MapWidget map = new MapWidget("mapLegend", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Build the LayerTree:
		final LayerTree layerTree = new LayerTree(map);
		layerTree.setHeight(180);
		layerTree.setWidth100();
		layerTree.setShowEdges(true);
		layerTree.setShowResizeBar(true);
		layerTree.setMinHeight(100);

		// Add both to the main layout:
		mainLayout.addMember(layerTree);
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	public String getDescription() {
		return MESSAGES.layertreeDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapLegend.xml",
				"classpath:org/geomajas/gwt/example/base/layerLakes110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerRivers50m.xml",
				"classpath:org/geomajas/gwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
