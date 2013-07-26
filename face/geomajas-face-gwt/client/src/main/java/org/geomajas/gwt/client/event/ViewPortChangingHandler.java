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

package org.geomajas.gwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

/**
 * Interface for event handlers that catch intermediate changes in the <code>ViewPort</code>. These changes are caused
 * by actions like dragging or zoom-pinching and are of intermediate nature. They will be followed by their peer events
 * in {@link ViewPortChangedHandler} when the dragging or zoom-pinching action comes to an end).
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ViewPortChangingHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<ViewPortChangingHandler> TYPE = new Type<ViewPortChangingHandler>();

	/**
	 * Catches events where the <code>ViewPort</code> is both scaling and translating.
	 * 
	 * @param event The actual {@link org.geomajas.gwt.client.event.ViewPortChangingEvent}.
	 */
	void onViewPortChanging(ViewPortChangingEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> is scaling only.
	 * 
	 * @param event The actual {@link org.geomajas.gwt.client.event.ViewPortScalingEvent}.
	 */
	void onViewPortScaling(ViewPortScalingEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> is translating only.
	 * 
	 * @param event The actual {@link org.geomajas.gwt.client.event.ViewPortTranslatingEvent}.
	 */
	void onViewPortTranslating(ViewPortTranslatingEvent event);
}