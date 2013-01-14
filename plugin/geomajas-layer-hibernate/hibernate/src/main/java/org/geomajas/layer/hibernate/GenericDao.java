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
package org.geomajas.layer.hibernate;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Generic dao for hibernate features.
 *
 * @param <T> object type
 * @param <ID> id type
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public interface GenericDao<T, ID extends Serializable> {

	T findById(ID id, boolean lock) throws IOException;

	List<T> findAll() throws IOException;

	List<T> findByExample(T exampleInstance, String... excludeProperty) throws IOException;

	T findUniqueByExample(T exampleInstance) throws IOException;

	T makePersistent(T entity) throws IOException;

	void makeTransient(T entity) throws IOException;

	void merge(T entity) throws IOException;

}