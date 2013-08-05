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
package org.geomajas.smartgwt.client.widget.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface provides registration for {@link GraphicsReadyHandler} instances.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface HasGraphicsReadyHandlers extends HasHandlers {

	/**
	 * Adds a {@link GraphicsReadyHandler} handler.
	 * 
	 * @param handler
	 *            the graphics ready handler
	 * @return {@link HandlerRegistration} used to remove this handler
	 */
	HandlerRegistration addGraphicsReadyHandler(GraphicsReadyHandler handler);

}