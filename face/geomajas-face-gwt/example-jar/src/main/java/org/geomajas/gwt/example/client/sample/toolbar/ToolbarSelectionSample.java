/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Sample that shows how a toolbar can be added to the map. The toolbar contains some buttons the user can use to do
 * select actions on the map (select items in a vector layer, zoom to selection)
 * </p>
 * 
 * @author Frank Wynants
 */
public class ToolbarSelectionSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "ToolbarSelection";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ToolbarSelectionSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID selectionMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("mapSelection", "gwtExample");

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

		layout.addMember(toolbar);
		layout.addMember(map);

		// wait for the map to be loaded and select the 1st layer
		// the map only has one layer so selecting the 1st one is correct
		// We need to select a layer cause the SelectionMode works on selected layers only
		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				//map.getMapModel().selectLayer(map.getMapModel().getLayer("clientLayerCountries110mGT"));
			}
		});

		return layout;
	}

	public String getDescription() {
		return MESSAGES.toolbarSelectionDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapSelection.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
