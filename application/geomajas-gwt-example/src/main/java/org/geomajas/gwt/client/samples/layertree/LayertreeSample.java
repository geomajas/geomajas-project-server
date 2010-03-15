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

package org.geomajas.gwt.client.samples.layertree;

import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
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
		// configure some layouts
		HLayout mainLayout = new HLayout();
		VLayout layertreeAndLegendLayout = new VLayout();
		layertreeAndLegendLayout.setWidth("25%");
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// build the map
		final MapWidget map = new MapWidget("layerTreeMap", "gwt-samples");
		map.setWidth("75%");
		mainLayout.addMember(map);
		// Set a panning controller on the map:
		map.setController(new PanController(map));

		// build the layertree
		final LayerTree layerTree = new LayerTree(map);
		layerTree.setHeight("50%");
		layertreeAndLegendLayout.addMember(layerTree);

		// build the legend
		final Legend legend = new Legend(map.getMapModel());
		legend.setHeight("50%");
		layertreeAndLegendLayout.addMember(legend);

		// add layertree and legend to layout
		mainLayout.addMember(layertreeAndLegendLayout);

		return mainLayout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().layertreeDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/layertree/LayertreeSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/layertree/mapLayerTree.xml",
				"classpath:org/geomajas/gwt/samples/layertree/layerStructures.xml",
				"classpath:org/geomajas/gwt/samples/layertree/layerRoads.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
