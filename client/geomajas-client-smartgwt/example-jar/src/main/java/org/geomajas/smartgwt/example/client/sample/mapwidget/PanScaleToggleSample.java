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

package org.geomajas.smartgwt.example.client.sample.mapwidget;

import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
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

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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
		mapLayout.setHeight("60%");

		// Map with ID mapOsm is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapOsm", "gwtExample");

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);

		// Create a button to toggle the navigation addons on and off
		final IButton butTogglePanButton = new IButton(MESSAGES.togglePanButtons());
		butTogglePanButton.setWidth100();
		buttonLayout.addMember(butTogglePanButton);

		butTogglePanButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.setNavigationAddonEnabled(!map.isNavigationAddonEnabled());
			}
		});

		// Create a button to toggle the scalebar on and off
		final IButton butToggleScaleBar = new IButton(MESSAGES.toggleScaleBar());
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
		return MESSAGES.panScaleToggleDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/smartgwt/example/context/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
