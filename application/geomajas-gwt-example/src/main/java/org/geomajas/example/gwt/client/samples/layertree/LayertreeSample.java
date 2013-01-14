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

package org.geomajas.example.gwt.client.samples.layertree;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows the usage of the LayerTree.
 * </p>
 * 
 * @author Frank Wynants
 */
public class LayertreeSample extends SamplePanel {

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
		final MapWidget map = new MapWidget("mapLegend", "gwt-samples");
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
		return I18nProvider.getSampleMessages().layertreeDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/layertree/LayertreeSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapLegend.xml", "WEB-INF/layerLakes110m.xml",
				"WEB-INF/layerRivers50m.xml", "WEB-INF/layerPopulatedPlaces110m.xml",
				"WEB-INF/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
