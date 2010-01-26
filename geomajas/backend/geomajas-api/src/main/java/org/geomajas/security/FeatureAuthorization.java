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

import org.geomajas.layer.feature.Feature;

/**
 * Authorizations at the individual feature level.
 *
 * @author Joachim Van der Auwera
 */
public interface FeatureAuthorization extends BaseAuthorization {

	/**
	 * Check whether a feature with given id is visible.
	 * <p/>
	 * It is assumed the layer can convert the feature id to a String and that the security component and/or the person
	 * who writes the policies "know" how the layer does this (to allow the policies to be set).
	 * <p/>
	 * If the layer in not visible, the features should also be not visible.
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature which needs to be tested
	 * @return true when the feature is visible
	 */
	boolean isFeatureVisible(String layerId, Feature feature);

	/**
	 * Check whether the feature is allowed to be updated in some way.
	 * <p/>
	 * This method can be used to know whether is makes sense to allow the logged in user to change anything about the
	 * feature.
	 * <p/>
	 * It is assumed the layer can convert the feature id to a String and that the security component and/or the person
	 * who writes the policies "know" how the layer does this (to allow the policies to be set).
	 * <p/>
	 * When isFeatureUpdateAuthorized() is true, then isFeatureVisible() should also be true.
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature which needs to tested
	 * @return true when the feature is visible
	 */
	boolean isFeatureUpdateAuthorized(String layerId, Feature feature);

	/**
	 * Check whether a update of the feature to the new state is authorized.
	 * <p/>
	 * This method can be used to check whether the actual update will result in a security exception.
	 * <p/>
	 * It is assumed the layer can convert the feature id to a String and that the security component and/or the person
	 * who writes the policies "know" how the layer does this (to allow the policies to be set).
	 *
	 * @param layerId layer which contains the feature to test
	 * @param orgFeature feature original state, note that just receiving the original state for the client is
	 *                   insufficient for this check as the client could have tampered with the value.
	 * @param newFeature feature new state
	 * @return true when the feature is visible
	 */
	boolean isFeatureUpdateAuthorized(String layerId, Feature orgFeature, Feature newFeature);

	/**
	 * Check whether deletion of the feature is allowed.
	 * <p/>
	 * When no feature is specified, it checks whether it may be possible to delete a feature.
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature which needs to be deleted
	 * @return true when deleting the feature is allowed
	 */
	boolean isFeatureDeleteAuthorized(String layerId, Feature feature);

	/**
	 * Check whether creation of the feature is allowed.
	 * <p/>
	 * When no feature is specified, it checks whether it may be possible to create a feature, same as
	 * isLayerCreateAuthorized().
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature which needs to be created
	 * @return true when deleting the feature is allowed
	 */
	boolean isFeatureCreateAuthorized(String layerId, Feature feature);
}
