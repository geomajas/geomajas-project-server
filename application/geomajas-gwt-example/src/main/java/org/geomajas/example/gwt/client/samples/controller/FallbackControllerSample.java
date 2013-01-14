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

package org.geomajas.example.gwt.client.samples.controller;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.MeasureDistanceController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how to use the fall-back controller on a map. It changes the default fall-back controller to the
 * selection controller.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FallbackControllerSample extends SamplePanel {

	public static final String TITLE = "FallbackController";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new FallbackControllerSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		// Map with ID wmsMap is defined in the XML configuration. (mapWms.xml)
		final MapWidget map = new MapWidget("mapToolbarSecurity", "gwt-samples");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		// Set a different fall-back controller:
		map.setFallbackController(new MeasureDistanceController(map));

		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);

		VLayout labelLayout = new VLayout();
		Label help = new Label();
		help.setContents(I18nProvider.getSampleMessages().fallbackControllerExplanation());
		labelLayout.addMember(help);

		layout.addMember(mapLayout);
		layout.addMember(labelLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().fallbackControllerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/controller/FallbackControllerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapToolbarSecurity.xml", "WEB-INF/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
