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

import org.geomajas.layer.LayerException;

/**
 * An {@link Entity} is a representation of a layer object (feature, association attribute) as a tree of objects.
 * Navigation and modification of the tree is possible in a generic way by the provided methods.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Entity {

	/**
	 * Returns the unique identifier value of this entity.
	 * 
	 * @param name name of the identifier attribute
	 * @return id
	 * @throws LayerException
	 */
	Object getId(String name) throws LayerException;

	/**
	 * Returns the child entity of this entity with the specified name (attribute name). A single child entity
	 * represents a many-to-one association.
	 * 
	 * @param name attribute name
	 * @return the child entity
	 * @throws LayerException
	 */
	Entity getChild(String name) throws LayerException;

	/**
	 * Sets the child entity with the specified name (attribute name). A single child entity represents a many-to-one
	 * association.
	 * 
	 * @param name attribute name
	 * @param entity the new entity
	 * @throws LayerException oops
	 */
	void setChild(String name, Entity entity) throws LayerException;

	/**
	 * Gets the collection of entities with the specified name. An empty collection is returned if the corresponding
	 * object value is null.
	 * 
	 * @param attribute name
	 * @return a modifiable collection of entities
	 * @throws LayerException oops
	 */
	EntityCollection getChildCollection(String name) throws LayerException;

	/**
	 * Sets the primitive attribute value with the specified name.
	 * 
	 * @param name attribute name
	 * @param value primitive value
	 * @throws LayerException oops
	 */
	void setPrimitiveAttribute(String name, Object value) throws LayerException;

}
