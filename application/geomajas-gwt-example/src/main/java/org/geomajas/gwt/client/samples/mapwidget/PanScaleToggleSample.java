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
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how to switch the scalebar and the toggle buttons on and off.
 * </p>
 * 
 * @author Frank Wynants
 */
public class PanScaleToggleSample extends SamplePanel {

	public static final String TITLE = "panscaletoggle";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new PanScaleToggleSample();
		}
	};

	public Canvas getViewPanel() {

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);

		// Map with ID osmMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("osmMap", "gwt-samples");

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);

		// Create a button to toggle the navigation addons on and off
		final IButton butTogglePanButton = new IButton(I18nProvider.getSampleMessages().togglePanButtons());
		butTogglePanButton.setWidth100();
		buttonLayout.addMember(butTogglePanButton);

		butTogglePanButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.setNavigationAddonEnabled(!map.isNavigationAddonEnabled());
			}
		});

		// Create a button to toggle the scalebar on and off
		final IButton butToggleScaleBar = new IButton(I18nProvider.getSampleMessages().toggleScaleBar());
		butToggleScaleBar.setWidth100();
		buttonLayout.addMember(butToggleScaleBar);

		butToggleScaleBar.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.setScalebarEnabled(!map.isScaleBarEnabled());
			}
		});

		layout.addMember(mapLayout);
		layout.addMember(buttonLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().panScaleToggleDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/mapwidget/PanScaleToggleSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
