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

import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how editing a point layer can be done.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class EditPointLayerSample extends SamplePanel {

	public static final String TITLE = "EditPointLayer";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditPointLayerSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setShowResizeBar(true);

		// Map with ID editPointLayerMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("editPointLayerMap", "gwt-samples");
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			// When the map is initialized: select the cities layer - so that new features are created in this layer:
			public void onMapModelChange(MapModelEvent event) {
				map.getMapModel().selectLayer(map.getMapModel().getLayer("citiesLayer"));
			}
		});

		// Create a toolbar for this map:
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		mapLayout.addMember(toolbar);
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
		return I18nProvider.getSampleMessages().editPointLayerDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/editing/EditPointLayerSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/editing/mapEditPointLayer.xml",
				"classpath:org/geomajas/gwt/samples/editing/layerCities.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
