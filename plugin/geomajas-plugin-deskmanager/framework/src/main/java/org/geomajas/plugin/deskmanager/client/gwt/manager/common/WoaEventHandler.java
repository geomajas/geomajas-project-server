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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout.ChangedHandler;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Event handler for the ConfigurationLayout panels in the deskmanager application.
 * 
 *  @author Oliver May
 */
public interface WoaEventHandler {

	/**
	 * Trigger edit event, the handler should prepare to be edited (enable forms etc).
	 * 
	 * @return state of event. (eg. when event is save, return true when successfully saved, false if for instance
	 *         some fields were invalid) a return value of false means the buttons will not change state and no
	 *         editingSessionevents will be fired.
	 */
	boolean onEditClick(ClickEvent event);

	/**
	 * Trigger reset event, the handler should reset it's state to default values, reset all overridden values.
	 * 
	 * @param event
	 * @return
	 */
	boolean onResetClick(ClickEvent event);

	/**
	 * Check if the current configuration is default, not overridden.
	 * 
	 * @return true if the configuration is the default configuration.
	 */
	boolean isDefault();

	/**
	 * Save the configuration.
	 * 
	 * @param event
	 * @return
	 */
	boolean onSaveClick(ClickEvent event);

	/**
	 * Cancel the current edit session, reverting changes in the current edit session.
	 * 
	 * @param event
	 * @return
	 */
	boolean onCancelClick(ClickEvent event);

	/**
	 * Register a change handler. Called when the waoeventhandler has changed.
	 */
	void registerChangedHandler(ChangedHandler handler);

}