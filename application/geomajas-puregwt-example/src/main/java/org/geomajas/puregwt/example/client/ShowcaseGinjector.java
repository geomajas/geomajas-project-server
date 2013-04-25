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

package org.geomajas.puregwt.example.client;

import org.geomajas.puregwt.client.GeomajasGinModule;
import org.geomajas.puregwt.client.GeomajasGinjector;

import com.google.gwt.inject.client.GinModules;

/**
 * Ginjector specific for this showcase application.
 * 
 * @author Pieter De Graef
 */
@GinModules({ GeomajasGinModule.class })
public interface ShowcaseGinjector extends GeomajasGinjector {
}