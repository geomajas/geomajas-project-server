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

package org.geomajas.plugin.geocoder.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for {@link org.geomajas.plugin.geocoder.client.event.SelectAlternativeEvent}.
 *
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface SelectAlternativeHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<SelectAlternativeHandler> TYPE = new GwtEvent.Type<SelectAlternativeHandler>();

	/**
	 * Called when the geocoder returned multiple alternatives, purpose is to allow the user to select one.
	 *
	 * @param event event, contains the list of alternatives
	 */
	void onSelectAlternative(SelectAlternativeEvent event);
}