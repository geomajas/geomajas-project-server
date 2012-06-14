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

package org.geomajas.application.gwt.showcase.client.layer;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;

/**
 * <p>
 * Sample that shows a map with an OpenStreetMap layer using OpenCycleMap tiles.
 * </p>
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class TmsSample extends SamplePanel {

	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

	public static final String TITLE = "TMS";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new TmsSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID mapOsm is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapTms", "gwtShowcase");

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.tmsDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"WEB-INF/mapTms.xml",
				"WEB-INF/layerTms.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
