/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.security;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Base authorization, determines authorization of tools and commands and entire layers.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface BaseAuthorization {

	/**
	 * Id for this authorization object if any.
	 * <p/>
	 * Lack of id can hamper caching of data.
	 * <p/>
	 * When two {@link org.geomajas.security.Authorization} objects from the same
	 * {@link org.geomajas.security.SecurityService} have the same id, they should behave exactly the same.
	 * <p/>
	 * The id is not allowed to contain the horizontal bar ("|") or at ("@") characters (except on the
	 * {@link org.geomajas.security.SecurityContext} where these characters are used to make the combination of
	 * contained authentications unique). 
	 *
	 * @return authentication id
	 */
	String getId();

	/**
	 * Check whether the tool with given id is allowed to be used.
	 *
	 * @param toolId tool id, as specified in {@link org.geomajas.configuration.client.ClientToolInfo}.
	 * @return true when access is allowed
	 */
	boolean isToolAuthorized(String toolId);

	/**
	 * Check whether the command with given name is allowed to be used.
	 *
	 * @param commandName name of command as passed to {@link org.geomajas.command.CommandDispatcher}
	 * @return true when access is allowed
	 */
	boolean isCommandAuthorized(String commandName);

	/**
	 * Check whether there are features in the layer which are visible.
	 *
	 * @param layerId layer id for which the visibility should be tested
	 * @return true when the logged in user is allowed to see part of the layer
	 */
	boolean isLayerVisible(String layerId);

	/**
	 * Check whether there are features in the layer which can be updated.
	 * <p/>
	 * When isLayerUpdateAuthorized() is true, then isLayerVisible() should also be true.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return true when the logged in user is allowed to edit the visible part of the layer
	 */
	boolean isLayerUpdateAuthorized(String layerId);

	/**
	 * Check whether creating new features may be allowed.
	 * <p/>
	 * When isLayerCreateAuthorized() is true, then isLayerVisible() should also be true.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return true when the logged in user is allowed to edit the visible part of the layer
	 */
	boolean isLayerCreateAuthorized(String layerId);

	/**
	 * Check whether there are features in the layer which can be deleted.
	 * <p/>
	 * When isLayerDeleteAuthorized() is true, then isLayerVisible() should also be true.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return true when the logged in user is allowed to edit the visible part of the layer
	 */
	boolean isLayerDeleteAuthorized(String layerId);	
}
