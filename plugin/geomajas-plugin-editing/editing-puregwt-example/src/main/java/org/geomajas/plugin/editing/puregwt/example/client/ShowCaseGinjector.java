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
package org.geomajas.plugin.editing.puregwt.example.client;

import org.geomajas.plugin.editing.puregwt.client.GeomajasEditorModule;
import org.geomajas.plugin.editing.puregwt.client.GeometryEditorFactory;
import org.geomajas.puregwt.client.GeomajasGinjector;

import com.google.gwt.inject.client.GinModules;

/**
 * Ginjector for the showcase.
 * 
 * @author Jan De Moerloose
 * 
 */
@GinModules(GeomajasEditorModule.class)
public interface ShowCaseGinjector extends GeomajasGinjector {

	GeometryEditorFactory getGeometryEditorFactory();
}