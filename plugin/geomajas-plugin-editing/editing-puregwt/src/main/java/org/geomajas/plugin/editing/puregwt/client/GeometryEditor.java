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

import org.geomajas.plugin.editing.client.BaseGeometryEditor;
import org.geomajas.plugin.editing.puregwt.client.controller.EditGeometryBaseController;
import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * Extends {@link BaseGeometryEditor} to provide access to the {@link MapPresenter}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryEditor extends BaseGeometryEditor {

	/**
	 * Get the map on which this editor is running.
	 * 
	 * @return the map presenter
	 */
	MapPresenter getMapPresenter();

	/**
	 * Get the base controller that handles the editing interaction on the map.
	 * 
	 * @return base controller
	 */
	EditGeometryBaseController getBaseController();

	/**
	 * Set the base controller that handles the editing interaction on the map.
	 * 
	 * @param baseController
	 *            controller
	 */
	void setBaseController(EditGeometryBaseController baseController);
}
