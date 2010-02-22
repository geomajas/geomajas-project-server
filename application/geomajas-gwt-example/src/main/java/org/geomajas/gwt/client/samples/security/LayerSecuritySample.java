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

package org.geomajas.gwt.client.samples.security;

import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.springsecurity.client.Authentication;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that tests security on layer level.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LayerSecuritySample extends SamplePanel {

	public static final String LAYER_SECUTIRY_TITLE = "LayerSecurity";

	private MapWidget map;

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayerSecuritySample();
		}
	};

	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Create horizontal layout for login buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);
		buttonLayout.setHeight(20);

		// Map with ID duisburgMap is defined in the XML configuration. (mapDuisburg.xml)
		map = new MapWidget("duisburgMap", "gwt-samples");
		map.addDrawHandler(new DrawHandler() {

			public void onDraw(DrawEvent event) {
				map.initialize();
			}
		});

		// Create login handler that re-initializes the map on a successful login:
		final BooleanCallback initMapCallback = new BooleanCallback() {

			public void execute(Boolean value) {
				if (value) {
					map.destroy();
					map = new MapWidget("duisburgMap", "gwt-samples");
					layout.addMember(map);
					map.initialize();
					map.setController(new PanController(map));
				}
			}
		};

		// Create a button that logs in user "marino":
		IButton loginButtonMarino = new IButton(I18nProvider.getSampleMessages().securityLogInWith("marino"));
		loginButtonMarino.setWidth(150);
		loginButtonMarino.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Authentication.getInstance().login("marino", "marino", initMapCallback);
			}
		});
		buttonLayout.addMember(loginButtonMarino);

		// Create a button that logs in user "luc":
		IButton loginButtonLuc = new IButton(I18nProvider.getSampleMessages().securityLogInWith("luc"));
		loginButtonLuc.setWidth(150);
		loginButtonLuc.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Authentication.getInstance().login("luc", "luc", initMapCallback);
			}
		});
		buttonLayout.addMember(loginButtonLuc);

		// Set a panning controller on the map:
		map.setController(new PanController(map));

		layout.addMember(buttonLayout);
		layout.addMember(map);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().layerSecurityDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/security/LayerSecuritySample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/security/security.xml",
				"classpath:org/geomajas/gwt/samples/shapeinmem/layerRoads.xml",
				"classpath:org/geomajas/gwt/samples/shapeinmem/layerStructures.xml",
				"classpath:org/geomajas/gwt/samples/shapeinmem/mapDuisburg.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
