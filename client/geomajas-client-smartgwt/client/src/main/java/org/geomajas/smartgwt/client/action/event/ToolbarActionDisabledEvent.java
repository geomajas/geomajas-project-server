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

package org.geomajas.smartgwt.client.action.event;

import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.action.ToolbarBaseAction;

import com.google.gwt.event.shared.GwtEvent;

/**
 * <p>
 * Event that signals the disabling of a single {@link org.geomajas.smartgwt.client.action.ToolbarBaseAction}.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ToolbarActionDisabledEvent extends GwtEvent<ToolbarActionHandler> {

	@Override
	public Type<ToolbarActionHandler> getAssociatedType() {
		return ToolbarActionHandler.TYPE;
	}

	@Override
	protected void dispatch(ToolbarActionHandler toolbarActionHandler) {
		toolbarActionHandler.onToolbarActionDisabled(this);
	}

	/**
	 * Get the {@link org.geomajas.smartgwt.client.action.ToolbarBaseAction} that is disabled.
	 *
	 * @return action which is disabled
	 */
	public ToolbarBaseAction getAction() {
		return (ToolbarBaseAction) getSource();
	}
}
