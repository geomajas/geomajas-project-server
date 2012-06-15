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

package org.geomajas.layer.wms.gwt.example.client;

import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.layer.wms.gwt.example.client.i18n.WmsMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample to demonstrate use of the WMS layer.
 *
 * @author Jan De Moerloose
 */
public class WmsPanel extends SamplePanel {

	public static final String TITLE = "WmsLayer";

	public static final WmsMessages MESSAGES = GWT.create(WmsMessages.class);

	public static final String WMS_TITLE = "WMS";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new WmsPanel();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("mapWms", "appWms");

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		layout.addMember(map);

		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.wmsDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { 
				"classpath:org/geomajas/layer/wms/gwt/example/context/appWms.xml",
				"classpath:org/geomajas/layer/wms/gwt/example/context/mapWms.xml",
				"classpath:org/geomajas/layer/wms/gwt/example/context/layerWmsBluemarble.xml"};
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
