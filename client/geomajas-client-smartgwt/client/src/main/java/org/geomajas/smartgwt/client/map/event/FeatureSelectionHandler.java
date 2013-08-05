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

package org.geomajas.smartgwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for event handlers that catch {@link FeatureSelectedEvent}s.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureSelectionHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<FeatureSelectionHandler> TYPE = new GwtEvent.Type<FeatureSelectionHandler>();

	/**
	 * Called when feature is selected.
	 *
	 * @param event {@link FeatureSelectedEvent}
	 */
	void onFeatureSelected(FeatureSelectedEvent event);

	/**
	 * Called when feature is selected.
	 *
	 * @param event {@link FeatureDeselectedEvent}
	 */
	void onFeatureDeselected(FeatureDeselectedEvent event);
}
