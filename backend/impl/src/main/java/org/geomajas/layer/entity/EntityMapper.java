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
package org.geomajas.layer.entity;

import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;

/**
 * An {@link EntityMapper} maps layer-specific objects (features, association values) to a common {@link Entity}
 * interface. The {@link Entity} interface allows to navigate/modify the object graph in a generic way.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface EntityMapper {

	/**
	 * Merges the specified attribute map to this layer object. This means that the complete attribute graph is merged
	 * into the object's entity graph, overriding existing attributes where applicable and creating new ones as well.
	 * The resulting objects should be ready to pass to the layer for persistence.
	 * 
	 * @param object the layer-specific object
	 * @param attributes attribute map
	 * @throws LayerException oops
	 */
	void mergeEntity(Object object, Map<String, Attribute<?>> attributes) throws LayerException;

	/**
	 * Converts the specified object to an entity.
	 * 
	 * @param object
	 * @return entity
	 * @throws LayerException
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
