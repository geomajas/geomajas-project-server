/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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