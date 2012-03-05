/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;

/**
 * Ginjector specific for the Geomajas pure GWT client.
 * 
 * @author Pieter De Graef
 */
@GinModules(GeomajasGinModule.class)
public interface GeomajasGinjector extends Ginjector {

	Provider<MapPresenter> getMapPresenter();

	GfxUtil getGfxUtil();
}