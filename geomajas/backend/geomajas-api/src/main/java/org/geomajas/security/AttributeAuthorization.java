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

package org.geomajas.security;

import org.geomajas.layer.feature.InternalFeature;

/**
 * Possible authorizations at individual attribute level.
 *
 * @author Joachim Van der Auwera
 */
public interface AttributeAuthorization extends BaseAuthorization {

	/**
	 * Check whether a specific attribute of a feature is visible.
	 * <p/>
	 * When no feature id is specified, the general status of the attribute (across all features) is checked.
	 * <p/>
	 * If the feature in not visible, the attributes should also be not visible.
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature to test (or null)
	 * @param attributeName name of the attribute to test
	 * @return true when the feature is visible
	 */
	boolean isAttributeReadable(String layerId, InternalFeature feature, String attributeName);

	/**
	 * Check whether a specific attribute of a feature is writable.
	 * <p/>
	 * No distinction is made between create, update and delete. It merely indicates whether the user is allowed to
	 * change the value. When this is an association, it implies that both create, update and deleted of the linked
	 * object are allowed.
	 * <p/>
	 * When no feature id is specified, the general status of the attribute (across all features) is checked.
	 * <p/>
	 * When isAttributeWritable() is true, then isAttributeReadable() should also be true.
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature to test (or null)
	 * @param attributeName name of the attribute to test
	 * @return true when the feature is visible
	 */
	boolean isAttributeWritable(String layerId, InternalFeature feature, String attributeName);
}
