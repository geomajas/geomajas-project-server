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

package org.geomajas.puregwt.widget.client.featureselectbox.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

/**
 * Interface for event handlers that catch {@link FeatureClickedEvent}s.
 *
 * @author Dosi Bingov
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureClickedHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<FeatureClickedHandler> TYPE = new Type<FeatureClickedHandler>();

	/**
	 * Called when feature is selected.
	 *
	 * @param event {@link FeatureClickedEvent}
	 */
	void onFeatureClicked(FeatureClickedEvent event);
}