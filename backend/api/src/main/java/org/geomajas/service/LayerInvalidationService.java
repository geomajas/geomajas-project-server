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

package org.geomajas.service;

import org.geomajas.global.Api;
import org.geomajas.global.ExpectAlternatives;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.UserImplemented;
import org.geomajas.layer.Layer;

/**
 * Service for invalidating layers. This should be called whenever significant changes to the layer configuration have
 * occurred. These include deleting the layer, changes in the style, data structure, security, authorizations,...
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
@UserImplemented
@ExpectAlternatives
public interface LayerInvalidationService {

	/**
	 * Invalidate the layer. This should be called whenever significant changes to the layer have occurred. These
	 * include deleting the layer, changes in the style, data structure, security, authorizations,...
	 *
	 * @param layer layer which needs to be invalidated
	 * @throws GeomajasException oops (should not happen, steps should just log and not (re)throw exceptions)
	 */
	void invalidateLayer(Layer layer) throws GeomajasException;

}
