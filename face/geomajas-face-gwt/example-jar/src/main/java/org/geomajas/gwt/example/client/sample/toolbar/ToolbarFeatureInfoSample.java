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

package org.geomajas.gwt.example.client.sample.toolbar;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how a toolbar can be added to the map. The toolbar contains a button that shows info about
 * features.
 * </p>
 * 
 * @author Frank Wynants
 */
public class ToolbarFeatureInfoSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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
		final MapWidget map = new MapWidget("mapFeatureInfo", "gwtExample");

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

		layout.addMember(toolbar);
		layout.addMember(map);

		// wait for the map to be loaded and select the 1st layer
		// the map only has one layer so selecting the 1st one is correct
		// We need to select a layer cause the FeatureInfo works on selected layers only
		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				map.getMapModel().selectLayer(map.getMapModel().getLayers().get(0));
			}
		});

		return layout;
	}

	public String getDescription() {
		return MESSAGES.toolbarFeatureInfoDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/base/layerCountries110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml",
				"classpath:org/geomajas/gwt/example/context/mapFeatureInfo.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
