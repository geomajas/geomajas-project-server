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

package org.geomajas.example.gwt.client.samples.toolbar;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how a toolbar can be added to the map. The toolbar contains a button that shows info about
 * features.
 * </p>
 * 
 * @author Frank Wynants
 */
public class ToolbarFeatureInfoSample extends SamplePanel {

	public static final String TITLE = "ToolbarFeatureInfo";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ToolbarFeatureInfoSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID featureInfoMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("mapFeatureInfo", "gwt-samples");

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		layout.addMember(toolbar);
		layout.addMember(map);

		// wait for the map to be loaded and select the 1st layer
		// the map only has one layer so selecting the 1st one is correct
		// We need to select a layer cause the FeatureInfo works on selected layers only
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				map.getMapModel().selectLayer(map.getMapModel().getLayers().get(0));
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().toolbarFeatureInfoDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/toolbar/ToolbarFeatureInfoSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerCountries110m.xml",
				"WEB-INF/layerWmsBluemarble.xml",
				"WEB-INF/mapFeatureInfo.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
