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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.event;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;

import com.google.gwt.event.shared.GwtEvent;

/**
 * User application event thrown by the {@link UserApplicationHandler}.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public class UserApplicationEvent extends GwtEvent<UserApplicationHandler> {

	public static final Type<UserApplicationHandler> TYPE = new Type<UserApplicationHandler>();

	private UserApplication userApplication;

	/**
	 * Construct a new user applicaiton event.
	 * 
	 * @param userApplication the application
	 */
	public UserApplicationEvent(UserApplication userApplication) {
		this.setUserApplication(userApplication);
	}

	/**
	 * Set the user application.
	 * 
	 * @param userApplication the user application.
	 */
	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

	/**
	 * Get the user application.
	 * 
	 * @return the user application/
	 */
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
