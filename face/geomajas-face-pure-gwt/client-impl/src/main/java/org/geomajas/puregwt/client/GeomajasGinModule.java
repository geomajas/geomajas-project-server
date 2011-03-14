/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.command.CommandService;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.GeometryFactoryImpl;
import org.geomajas.puregwt.client.spatial.MathService;
import org.geomajas.puregwt.client.spatial.MathServiceImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Gin binding module. All bindings defined in here are used by the GeomajasGinjector.<br/>
 * TODO check to see if we can create multiple GeometryFactories with different SRIDs.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class GeomajasGinModule extends AbstractGinModule {

	protected void configure() {
		// Communication:
		bind(CommandService.class).in(Singleton.class);

		// Spatial services:
		bind(MathService.class).to(MathServiceImpl.class).in(Singleton.class);
		bind(GeometryFactory.class).to(GeometryFactoryImpl.class).in(Singleton.class);
	}
}