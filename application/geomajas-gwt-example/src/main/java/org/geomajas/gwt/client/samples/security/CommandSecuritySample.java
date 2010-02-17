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

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.samples.base.GetResourcesRequest;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.plugin.springsecurity.client.Authentication;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that tests security on command level.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CommandSecuritySample extends SamplePanel {

	public static final String TITLE = "CommandSecurity";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CommandSecuritySample();
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

		// Create a button that logs in user "mark":
		IButton loginButtonMarino = new IButton(I18nProvider.getSampleMessages().securityLogInWith("mark"));
		loginButtonMarino.setWidth(150);
		loginButtonMarino.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Authentication.getInstance().login("mark", "mark", null);
			}
		});
		buttonLayout.addMember(loginButtonMarino);

		// Create a button that logs in user "luc":
		IButton loginButtonLuc = new IButton(I18nProvider.getSampleMessages().securityLogInWith("luc"));
		loginButtonLuc.setWidth(150);
		loginButtonLuc.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Authentication.getInstance().login("luc", "luc", null);
			}
		});
		buttonLayout.addMember(loginButtonLuc);

		// Create horizontal layout for login buttons:
		HLayout commandLayout = new HLayout();
		commandLayout.setMembersMargin(10);
		commandLayout.setHeight(20);

		// Create a button that calls the GetMapConfigurationCommand:
		IButton getMapButton = new IButton("command.GetMap");
		getMapButton.setWidth(150);
		getMapButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				GwtCommand commandRequest = new GwtCommand("command.configuration.GetMap");
				commandRequest.setCommandRequest(new GetMapConfigurationRequest("osmMap", "gwt-samples"));
				GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

					public void execute(CommandResponse response) {
						SC.say("Command executed successfully");
					}
				});
			}
		});
		commandLayout.addMember(getMapButton);

		// Create a button that calls the GetMapConfigurationCommand:
		IButton getResourcesButton = new IButton("command.GetResources");
		getResourcesButton.setWidth(150);
		getResourcesButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				GetResourcesRequest request = new GetResourcesRequest(
						new String[] { "/org/geomajas/gwt/samples/security/security.xml" });
				GwtCommand command = new GwtCommand("gwt.server.samples.GetSourceCommand");
				command.setCommandRequest(request);
				GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

					public void execute(CommandResponse response) {
						// User mark should never get here...
						SC.say("Command executed successfully");
					}
				});
			}
		});
		commandLayout.addMember(getResourcesButton);

		layout.addMember(buttonLayout);
		layout.addMember(commandLayout);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().commandSecurityDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "/org/geomajas/gwt/samples/security/security.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
