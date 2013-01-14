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

package org.geomajas.security;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.layer.feature.InternalFeature;

/**
 * Possible authorizations at individual attribute level.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
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
