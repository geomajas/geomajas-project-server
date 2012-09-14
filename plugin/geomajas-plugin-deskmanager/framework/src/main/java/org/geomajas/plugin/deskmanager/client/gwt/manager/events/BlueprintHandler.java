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
 * Handler that will trigger on create, update or delete of a blueprint.
 * 
 * @author Jan De Moerloose
 *
 */
public interface BlueprintHandler {

	/**
	 * Called when a blueprint is created, updated or deleted.
	 * 
	 * @param bpe the blueprintevent.
	 */
	void onBlueprintChange(BlueprintEvent bpe);

}

