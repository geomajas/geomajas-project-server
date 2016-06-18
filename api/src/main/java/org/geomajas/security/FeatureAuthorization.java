/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.security;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.layer.feature.InternalFeature;

/**
 * Authorizations at the individual feature level.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
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
	boolean isFeatureVisible(String layerId, InternalFeature feature);

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
	boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature);

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
	boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature, InternalFeature newFeature);

	/**
	 * Check whether deletion of the feature is allowed.
	 * <p/>
	 * When no feature is specified, it checks whether it may be possible to delete a feature.
	 *
	 * @param layerId layer which contains the feature to test
	 * @param feature feature which needs to be deleted
	 * @return true when deleting the feature is allowed
	 */
	boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature);

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
	boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature);
}
