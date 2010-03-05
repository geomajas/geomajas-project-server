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

import org.geomajas.configuration.client.UnitType;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how to switch between european and english units.
 * </p>
 * 
 * @author Frank Wynants
 */
public class UnitTypesSample extends SamplePanel {

	public static final String TITLE = "UnitTypes";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new UnitTypesSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID osmMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("osmMap", "gwt-samples");
		map.addDrawHandler(new DrawHandler() {

			public void onDraw(DrawEvent event) {
				map.initialize();
			}
		});

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		layout.addMember(map);

		Button butSwitch = new Button(I18nProvider.getSampleMessages().switchUnitTypes());
		layout.addMember(butSwitch);

		butSwitch.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				
				if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.METRIC) {
					SC.say("Current unit: " + map.getMapModel().getMapInfo().getDisplayUnitType()
							+ "<BR>Switching to ENGLISH");
					map.getMapModel().getMapInfo().setDisplayUnitType(UnitType.ENGLISH); // set English
				} else {
					SC.say("Current unit: " + map.getMapModel().getMapInfo().getDisplayUnitType()
							+ "<BR>Switching to METRIC");
					map.getMapModel().getMapInfo().setDisplayUnitType(UnitType.METRIC); // set English
				}
				map.redraw("unittype switched");

				// FIXME The scalebar remains in METRIC mode...
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().unitTypesDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/mapwidget/UnitTypesSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
