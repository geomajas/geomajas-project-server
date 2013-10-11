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
package org.geomajas.gwt.client.widget.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Interface for graphics ready events.
 * 
 * @author Jan De Moerloose
 */
public interface GraphicsReadyHandler extends EventHandler {

	/**
	 * Called when graphics is ready to draw.
	 * 
	 * @param event
	 *            The search event
	 */
	void onReady(GraphicsReadyEvent event);
}
