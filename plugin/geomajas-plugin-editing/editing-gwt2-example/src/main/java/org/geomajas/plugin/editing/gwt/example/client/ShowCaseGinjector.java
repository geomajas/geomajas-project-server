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
package org.geomajas.plugin.editing.gwt.example.client;

import org.geomajas.gwt2.client.GeomajasGinjector;
import org.geomajas.plugin.editing.gwt.client.GeomajasEditorModule;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorFactory;

import com.google.gwt.inject.client.GinModules;

/**
 * Ginjector for the showcase.
 * 
 * @author Jan De Moerloose
 */
@GinModules(GeomajasEditorModule.class)
public interface ShowCaseGinjector extends GeomajasGinjector {

	/** Geometry editor factory. */
	GeometryEditorFactory getGeometryEditorFactory();
}