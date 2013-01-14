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
package org.geomajas.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.IsInfo;

/**
 * Marker interface for widget information which is contained in the map (or application)
 * and needs to be sent to the client.
 * 
 * @see ClientApplicationInfo
 * @see ClientMapInfo
 *
 * @author Kristof Heirwegh
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ClientWidgetInfo extends IsInfo {

}
