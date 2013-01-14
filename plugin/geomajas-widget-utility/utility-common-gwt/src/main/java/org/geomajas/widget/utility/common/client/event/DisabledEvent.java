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

package org.geomajas.widget.utility.common.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * <p>
 * Event that is triggered when the source's state switches to disabled.
 * </p>
 * 
 * @author Jan De Moerloose
 */
public class DisabledEvent extends GwtEvent<EnabledHandler> {

	public DisabledEvent(Object source) {
		setSource(source);
	}

	public Type<EnabledHandler> getAssociatedType() {
		return EnabledHandler.TYPE;
	}

	protected void dispatch(EnabledHandler enabledHandler) {
		enabledHandler.onDisabled(this);
	}

}