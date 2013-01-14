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
package org.geomajas.layer.entity;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.layer.LayerException;

/**
 * An {@link EntityMapper} maps layer-specific objects (features, association values) to a common {@link Entity}
 * interface. The {@link Entity} interface allows to navigate/modify the object graph in a generic way.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 * 
 */
@Api(allMethods = true)
@UserImplemented
public interface EntityMapper {

	/**
	 * Converts the specified object to an entity.
	 * 
	 * @param object the object
	 * @return entity
	 * @throws LayerException oops
	 */
	Entity asEntity(Object object) throws LayerException;

	/**
	 * Find the existing entity with the specified id or create a new one.
	 * 
	 * @param dataSourceName this name uniquely identifies the entity class
	 * @param id the identifier value to look for
	 * @return an existing or new entity
	 * @throws LayerException oops
	 */
	Entity findOrCreateEntity(String dataSourceName, Object id) throws LayerException;

}
