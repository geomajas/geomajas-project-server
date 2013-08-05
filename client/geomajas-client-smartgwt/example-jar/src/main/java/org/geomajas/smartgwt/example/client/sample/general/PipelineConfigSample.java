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

package org.geomajas.smartgwt.example.client.sample.general;

import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.widget.LayerTree;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Example of a layer with customized.
 *
 * @author Joachim Van der Auwera
 */
public class PipelineConfigSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "PipelineConfig";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new PipelineConfigSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		final MapWidget map = new MapWidget("mapPipeline", "gwtExample");
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
		return MESSAGES.pipelineConfigDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/mapPipeline.xml",
				"classpath:org/geomajas/smartgwt/example/context/layerPipeline.xml",
				"classpath:org/geomajas/smartgwt/example/server/samples/ProcessFeaturesPipelineStep.java",
				"classpath:org/geomajas/smartgwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
