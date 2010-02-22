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

package org.geomajas.gwt.client.samples.mapwidget;

import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a map one raster and one vector layer, both using the same CRS.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CrsSample extends SamplePanel {

	public static final String TITLE = "Crs";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CrsSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID osmMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("crsMap", "gwt-samples");
		map.addDrawHandler(new DrawHandler() {

			public void onDraw(DrawEvent event) {
				map.initialize();
			}
		});

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().crsDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "/org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"/org/geomajas/gwt/samples/mapwidget/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
