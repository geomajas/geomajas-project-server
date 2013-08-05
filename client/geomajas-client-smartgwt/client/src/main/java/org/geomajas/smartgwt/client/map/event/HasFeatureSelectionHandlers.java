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

import com.google.gwt.event.shared.HandlerRegistration;
import org.geomajas.annotation.UserImplemented;

/**
 * Classes triggering the {@link FeatureSelectedEvent} should implement this interface, thereby allowing handlers to
 * be registered to catch these events.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface HasFeatureSelectionHandlers {

	/**
	 * Add a new handler for {@link FeatureSelectionHandler} events.
	 *
	 * @param handler The handler to be registered.
	 * @return Returns the handlers registration object.
	 */
	HandlerRegistration addFeatureSelectionHandler(FeatureSelectionHandler handler);
}
