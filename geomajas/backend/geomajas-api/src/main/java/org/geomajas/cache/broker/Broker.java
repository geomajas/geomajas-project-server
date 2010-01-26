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

package org.geomajas.cache.broker;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.global.ExpectAlternatives;

/**
 * <p>
 * A Broker is an object that can fill a certain <code>RenderContent</code> object with content. Different
 * implementations of this interface will get their content from different places. What's important here is that the
 * created content should be the same - no matter where it came from.
 * </p>
 *
 * @author Pieter De Graef
 */
@ExpectAlternatives
public interface Broker {

	/**
	 * Try and get the content for a specific empty <code>RenderContent</code> object.
	 *
	 * @param renderContent
	 *            An empty renderContent. This function should try and fill it with the right content.
	 * @return Returns true if the content for the <code>RenderContent</code> object was successfully set. If not, false
	 *         is returned.
	 * @throws org.geomajas.cache.CacheException
	 */
	boolean read(RenderContent renderContent) throws CacheException;

	/**
	 * Utility function that can update the broker, if needed.
	 *
	 * @param renderContent
	 *            The <code>RenderContent</code> object passed here should already be filled with the right content. Use
	 *            it well.
	 * @throws org.geomajas.cache.CacheException
	 */
	void update(RenderContent renderContent) throws CacheException;
}
