/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.entity;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.layer.LayerException;

/**
 * Represents a collection of entities. {@link EntityCollection}s represent one-to-many relationships in the entity
 * graph.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 * 
 */
@Api(allMethods = true)
@UserImplemented
public interface EntityCollection extends Iterable<Entity> {

	/**
	 * Adds an entity to this collection.
	 * 
	 * @param entity the entity to add
	 * @throws LayerException oops
	 */
	void addEntity(Entity entity) throws LayerException;

	/**
	 * Removes an entity from this colection.
	 * 
	 * @param entity the entity to remove
	 * @throws LayerException oops
	 */
	void removeEntity(Entity entity) throws LayerException;
}
