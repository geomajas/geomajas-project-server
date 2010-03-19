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

package org.geomajas.gwt.client.samples.toolbar;

import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how a toolbar can be added to the map. The toolbar contains a buttons the user can use to do
 * measurements actions on the map.
 * </p>
 * 
 * @author Frank Wynants
 */
public class ToolbarMeasureSample extends SamplePanel {

	public static final String TITLE = "ToolbarMeasure";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ToolbarMeasureSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID osmMeasureMap is defined in the XML configuration. (mapOsmMeasure.xml)
		final MapWidget map = new MapWidget("osmMeasureMap", "gwt-samples");

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		layout.addMember(toolbar);
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().toolbarMeasureDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/toolbar/ToolbarMeasureSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/toolbar/mapOsmMeasure.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
