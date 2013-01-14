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

package org.geomajas.plugin.editing.client;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

/**
 * Function to be executed on a single geometry. Often used as call-back.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public interface GeometryFunction {

	void execute(Geometry geometry);
}