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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk;

import java.util.Collections;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.gwt.client.GwtCommandCallback;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.event.UserApplicationEvent;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.event.UserApplicationHandler;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.impl.DeskmanagerGwtCommandCallback;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract deskmanager user application. It implements the handlers, provides empty supported widget configuration
 * lists and handles user friendly error messages.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public abstract class AbstractUserApplication implements UserApplication {

	private HandlerManager handlerManager;

	private ClientApplicationInfo clientApplicationInfo;

	private String applicationId;

	/**
	 * Construct an abstract application and initialize handlers.
	 */
	public AbstractUserApplication() {
		handlerManager = new HandlerManager(this);
		GwtCommandCallback cb = new DeskmanagerGwtCommandCallback();
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(cb);
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(cb);
	}

	/**
	 * Get the client application info.
	 * 
	 * @return the client application info
	 */
	protected ClientApplicationInfo getClientApplicationInfo() {
		return clientApplicationInfo;
	}

	/**
	 * Get the application id.
	 * 
	 * @return the applicationId
	 */
	protected String getApplicationId() {
		return applicationId;
	}

	@Override
	public HandlerRegistration addUserApplicationLoadedHandler(final UserApplicationHandler userApplicationHandler) {
		return handlerManager.addHandler(UserApplicationEvent.TYPE, userApplicationHandler);
	}

	@Override
	public void fireUserApplicationEvent() {
		handlerManager.fireEvent(new UserApplicationEvent(this));
	}

	@Override
	public void setClientApplicationInfo(ClientApplicationInfo clientAppInfo) {
		clientApplicationInfo = clientAppInfo;
	}

	@Override
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public List<String> getSupportedApplicationWidgetKeys() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getSupportedMainMapWidgetKeys() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getSupportedOverviewMapWidgetKeys() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getSupportedLayerWidgetKeys() {
		return Collections.emptyList();
	}

}
