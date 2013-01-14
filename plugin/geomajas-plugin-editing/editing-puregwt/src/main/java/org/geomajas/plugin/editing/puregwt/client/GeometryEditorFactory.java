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

import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * Gin factory for {@link GeometryEditor} instances.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryEditorFactory {

	/**
	 * Create an editor for this map.
	 * 
	 * @param mapPresenter the map presenter
	 * @return the editor
	 */
	GeometryEditor create(MapPresenter mapPresenter);
}
