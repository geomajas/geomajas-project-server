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

package org.geomajas.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Marker interface objects which can be added in layer configuration, using
 * {@link org.geomajas.configuration.LayerInfo#setExtraInfo(java.util.Map)}.
 *
 * @author Joachim Van der Auwera
 * @see org.geomajas.configuration.client.ClientApplicationInfo
 * @see org.geomajas.configuration.client.ClientMapInfo
 * @since 1.9.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerExtraInfo extends IsInfo {

}
