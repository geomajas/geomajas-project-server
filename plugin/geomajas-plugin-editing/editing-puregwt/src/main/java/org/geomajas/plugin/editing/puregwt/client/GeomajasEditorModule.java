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
package org.geomajas.plugin.editing.puregwt.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * Gin module for this plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeomajasEditorModule extends AbstractGinModule {

	protected void configure() {
		install(new GinFactoryModuleBuilder().implement(GeometryEditor.class, GeometryEditorImpl.class).build(
				GeometryEditorFactory.class));
	}

}
