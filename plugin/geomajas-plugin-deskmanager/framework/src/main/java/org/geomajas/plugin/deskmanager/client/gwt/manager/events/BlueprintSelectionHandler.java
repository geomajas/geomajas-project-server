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
package org.geomajas.plugin.deskmanager.client.gwt.manager.events;


/**
 * Handler that listens to the selection of blueprints in the grid. 
 * 
 * @author Oliver May
 *
 */
public interface BlueprintSelectionHandler {
	
	/**
	 * Called when the selection of the blueprint changes.
	 * 
	 * @param blueprintevent
	 */
	void onBlueprintSelectionChange(BlueprintEvent blueprintEvent);

}
