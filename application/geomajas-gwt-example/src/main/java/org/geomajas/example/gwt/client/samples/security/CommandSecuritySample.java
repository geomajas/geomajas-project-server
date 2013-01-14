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

package org.geomajas.example.gwt.client.samples.security;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.example.gwt.client.samples.base.GetResourcesRequest;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.staticsecurity.client.Authentication;

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
				GwtCommand commandRequest = new GwtCommand(GetMapConfigurationRequest.COMMAND);
				commandRequest.setCommandRequest(new GetMapConfigurationRequest("mapOsm", "gwt-samples"));
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
						new String[] { "WEB-INF/security.xml" });
				GwtCommand command = new GwtCommand("example.gwt.server.samples.GetSourceCommand");
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

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/security/CommandSecuritySample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/security.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
