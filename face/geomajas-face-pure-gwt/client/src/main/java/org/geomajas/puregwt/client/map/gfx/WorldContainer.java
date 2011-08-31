/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.global.FutureApi;

/**
 * A {@link VectorContainer} that also supports {@link WorldObject} objects. This is the recommended way of quickly
 * drawing vector objects in world space. Objects that should be fully scaled to each zoom level can be added as normal
 * vector objects using the {@link VectorContainer} methods. Objects that require custom or no scaling should implement
 * the {@link WorldObject} interface and can be added/removed using the specific methods of this interface.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@FutureApi
public interface WorldContainer extends VectorContainer {

	/**
	 * Add the given WorldObject to this WorldContainer.
	 * 
	 * @param worldObject
	 * @return
	 */
	WorldObject add(WorldObject worldObject);

	/**
	 * Insert the given WorldObject before the specified index.
	 * 
	 * If the WorldContainer already contains the WorldObject, it will be removed from the WorldContainer before
	 * insertion.
	 * 
	 * @param vo WorldObject to be inserted
	 * @param beforeIndex the index before which the WorldObject will be inserted.
	 * @return inserted WorldObject
	 * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
	 */
	WorldObject insert(WorldObject worldObject, int beforeIndex);

	/**
	 * Remove the given WorldObject from this WorldContainer.
	 * 
	 * @param vo WorldObject to be removed
	 * 
	 * @return removed WorldObject or null if the container doesn't contained the given WorldObject
	 */
	WorldObject remove(WorldObject worldObject);

	/**
	 * 
	 * Brings the given WorldObject to front in this WorldContainer.
	 * 
	 * @param vo WorldObject to be brought to front
	 * @return the popped WorldObject
	 */
	WorldObject bringToFront(WorldObject worldObject);

	/**
	 * Returns the WorldObject element at the specified position.
	 * 
	 * @param index index of element to return.
	 * @return the WorldObject element at the specified position.
	 */
	WorldObject getWorldObject(int index);

	/**
	 * Returns the number of WorldObjects in this WorldContainer.
	 * 
	 * @return the number of WorldObjects in this WorldContainer.
	 */
	int getWorldObjectCount();
}