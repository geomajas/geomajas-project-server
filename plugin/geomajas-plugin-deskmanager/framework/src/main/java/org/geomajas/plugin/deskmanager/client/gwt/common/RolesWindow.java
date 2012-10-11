/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * RolesWindow that asks for a specific role from a list.
 * 
 * @author Oliver May
 * 
 */
public class RolesWindow implements ProfileSelectionWindow {

	private boolean onlyAdminRoles;

	/**
	 * Constructor for the roleswindow.
	 * 
	 * @param onlyAdminRoles
	 *            is true the window will only ask for admin roles. This is introduced for the 'beheersmodule'. TODO:
	 *            make sure the getAvailableRolesCommand only returns the correct roles so that this constructor can be
	 *            removed.
	 */
	public RolesWindow(boolean onlyAdminRoles) {
		this.onlyAdminRoles = onlyAdminRoles;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.client.gwt.common.ProfileSelectionWindow#askRole(java.lang.String, org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow.AskRoleCallback)
	 */
	public void askRole(String geodeskId, final AskRoleCallback callback) {
		RetrieveRolesRequest request = new RetrieveRolesRequest();
		request.setGeodeskId(geodeskId);
		
		GwtCommand command = new GwtCommand(RetrieveRolesResponse.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<RetrieveRolesResponse>() {

			public void execute(RetrieveRolesResponse response) {
				// If only one role, use default
				if (response.getRoles().size() == 1) {
					for (Entry<String, ProfileDto> role : response.getRoles().entrySet()) {
						callback.execute(role.getKey(), role.getValue());
					}
				} else if (response.getRoles().size() > 0) {
					askRoleWindow(response.getRoles(), callback);
				} else {
					showUnauthorizedWindow();
				}
			}
		});
	}

	// TODO: i18n
	private void showUnauthorizedWindow() {
		final Window winModal = new Window();
		winModal.setWidth(500);
		winModal.setHeight(300);
		winModal.setTitle("Geen rechten.");
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();
		winModal.setShowCloseButton(false);
		winModal.setZIndex(CommonLayout.roleSelectZindex);

		HTMLPane pane = new HTMLPane();
		pane.setContents("<br/><br/><center>U heeft onvoldoende privileges om dit loket te openen.</center>");

		winModal.addItem(pane);
		winModal.show();
	}

	// TODO: i18n
	private void askRoleWindow(Map<String, ProfileDto> roles, final AskRoleCallback callback) {
		final Window winModal = new Window();
		winModal.setWidth(500);
		winModal.setHeight(300);
		winModal.setTitle("Rolkeuze");
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();
		winModal.setShowCloseButton(false);
		winModal.setZIndex(2000);

		VLayout layout = new VLayout();
		layout.setLayoutMargin(25);
		layout.setMembersMargin(10);

		boolean first = true;

		for (final Entry<String, ProfileDto> role : roles.entrySet()) {
			if (!onlyAdminRoles || Role.DESK_MANAGER.equals(role.getValue().getRole())
					|| Role.ADMINISTRATOR.equals(role.getValue().getRole())) {
				if (first) {
					first = false;
					Label label = new Label("Welkom " + role.getValue().getName() + " " + role.getValue().getSurname()
							+ ", gelieve uw rol te selecteren waarmee u dit loket wil openen.");
					label.setAutoHeight();
					layout.addMember(label);
				}

				final Button button = new Button();
				button.setTitle(role.getValue().getRoleDescription());
				button.setWidth100();
				button.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
						callback.execute(role.getKey(), role.getValue());
						winModal.destroy();
					}
				});

				layout.addMember(button);
			}
		}

		if (!first) {
			winModal.addItem(layout);
			winModal.show();
		} else {
			SC.warn("U heeft onvoldoende rechten voor dit loket!", new BooleanCallback() {

				public void execute(Boolean value) {
					SC.warn("U heeft nog steeds niet voldoende rechten voor dit loket!", this);
				}
			});
		}
	}

	/**
	 * Callback class for the roles window.
	 * 
	 * @author Oliver May
	 * 
	 */
	public interface AskRoleCallback {

		/**
		 * Callback when a role is selected.
		 * 
		 * @param token
		 *            the selected token.
		 */
		void execute(String token, ProfileDto profile);
	}

}
