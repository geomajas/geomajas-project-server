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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event;

import org.geomajas.plugin.deskmanager.client.gwt.geodesk.UserApplication;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Oliver May
 *
 */
public class UserApplicationEvent extends GwtEvent<UserApplicationHandler> {

	public static final Type<UserApplicationHandler> TYPE = new Type<UserApplicationHandler>();

	private UserApplication userApplication;

	public UserApplicationEvent(UserApplication userApplication) {
		this.setUserApplication(userApplication);
	}

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	public UserApplication getUserApplication() {
		return userApplication;
	}

	@Override
	public Type<UserApplicationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserApplicationHandler handler) {
		handler.onUserApplicationLoad(this);
	}
}
