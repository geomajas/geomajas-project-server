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

package org.geomajas.application.gwt.showcase.client.security;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.base.command.dto.GetResourceRequest;

/**
 * <p>
 * Sample that tests security on command level.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CommandSecuritySample extends SamplePanel {

	public static final String TITLE = "CommandSecurity";
	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CommandSecuritySample();
		}
	};

	@Override
	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Create horizontal layout for login buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);
		buttonLayout.setHeight(20);

		buttonLayout.addMember(new UserLoginButton("mark"));
		buttonLayout.addMember(new UserLoginButton("luc"));

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
				commandRequest.setCommandRequest(new GetMapConfigurationRequest("mapOsm", "gwtExample"));
				GwtCommandDispatcher.getInstance().execute(commandRequest, new AbstractCommandCallback() {

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
				GetResourceRequest request = new GetResourceRequest(
						new String[] { "WEB-INF/security.xml" });
				GwtCommand command = new GwtCommand(GetResourceRequest.COMMAND);
				command.setCommandRequest(request);
				GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback() {

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

	@Override
	public String getDescription() {
		return MESSAGES.commandSecurityDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/security.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
