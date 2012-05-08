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
package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Interface for handling layer events.
 * 
 * @author Frank Wynants
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerChangedHandler extends EventHandler {

	/** Event type. */
	Type<LayerChangedHandler> TYPE = new Type<LayerChangedHandler>();

	/**
	 * Called when the layer is made visible or invisible.
	 * 
	 * @param event event
	 */
	void onVisibleChange(LayerShownEvent event);

	/**
	 * Called when labels are enabled or disabled on the layer.
	 * 
	 * @param event event
	 */
	void onLabelChange(LayerLabeledEvent event);
	
}
