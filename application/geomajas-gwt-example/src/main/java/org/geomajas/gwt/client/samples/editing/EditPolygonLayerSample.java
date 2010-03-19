/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.samples.editing;

import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how editing a polygon layer can be done.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class EditPolygonLayerSample extends SamplePanel {

	public static final String TITLE = "EditPolygonLayer";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditPolygonLayerSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setShowResizeBar(true);

		// Map with ID editPolygonLayerMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("editPolygonLayerMap", "gwt-samples");
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			// When the map is initialized: select the cities layer - so that new features are created in this layer:
			public void onMapModelChange(MapModelEvent event) {
				map.getMapModel().selectLayer(map.getMapModel().getLayer("structuresLayer"));
			}
		});
		map.setController(new ParentEditController(map));
		mapLayout.addMember(map);

		// Add an explanation to the page that explains how editing is done:
		HLayout infoLayout = new HLayout();
		infoLayout.setHeight("35%");
		infoLayout.setShowEdges(true);
		infoLayout.addMember(new Label("Explanation....todo"));

		layout.addMember(mapLayout);
		layout.addMember(infoLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().editPolygonLayerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/editing/EditPolygonLayerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/editing/mapEditLineLayer.xml",
				"classpath:org/geomajas/gwt/samples/shapeinmem/layerStructures.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
