/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.example.client.samples.editing;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.samples.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how editing a MultiLineString layer can be done.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class EditMultiLineLayerSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "EditMultiLineLayer";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditMultiLineLayerSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setShowResizeBar(true);

		// Map with ID editLineLayerMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("mapEditMultiLine", "gwt-samples");
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			// When the map is initialized: select the cities layer - so that new features are created in this layer:
			public void onMapModelChange(MapModelEvent event) {
				map.getMapModel().selectLayer(map.getMapModel().getLayer("editableRoadtrl020LayerML"));
			}
		});

		// Create a tool-bar for this map:
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);

		// Add an explanation to the page that explains how editing is done:
		HLayout infoLayout = new HLayout();
		infoLayout.setHeight("35%");
		infoLayout.setShowEdges(true);
		infoLayout.addMember(new EditingManual());

		layout.addMember(mapLayout);
		layout.addMember(infoLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.editMultiLineLayerDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapEditMultiLineLayer.xml", "WEB-INF/layerGoogleSat.xml",
				"WEB-INF/layerRoadsTrl020-ML.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}