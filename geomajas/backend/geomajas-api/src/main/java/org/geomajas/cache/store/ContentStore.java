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

package org.geomajas.cache.store;

import org.geomajas.cache.CacheException;

/**
 * <p>
 * Interface for a basic store of rendering contents.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface ContentStore {

	/**
	 * Create a new entry in the content store.
	 *
	 * @param renderContent
	 *            The content that is to be added to the store.
	 * @throws CacheException oops
	 */
	void create(RenderContent renderContent) throws CacheException;

	/**
	 * Try and read the byte array contents of a certain <code>RenderContent</code> object.
	 *
	 * @param renderContent
	 *            The <code>RenderContent</code> object to search in the store.
	 * @return Returns the byte array content of the <code>RenderContent</code> object, or NULL if no content could be
	 *         found in this store.
	 * @throws CacheException oops
	 */
	byte[] read(RenderContent renderContent) throws CacheException;

	/**
	 * Delete a certain entry from this store.
	 *
	 * @param renderContent
	 *            The <code>RenderContent</code> object to search in the store.
	 * @return Returns true if the deletion was successful, false otherwise.
	 * @throws CacheException oops
	 */
	boolean delete(RenderContent renderContent) throws CacheException;
}
