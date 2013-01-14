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

import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.snap.SnapService;

/**
 * Base interface for geometry editors in all faces. TODO: check for redundant methods.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface BaseGeometryEditor {

	/**
	 * Get the geometry editor service.
	 * 
	 * @return the service
	 */
	GeometryEditService getEditService();

	/**
	 * Is the editing session busy ?
	 * 
	 * @return true if busy
	 */
	boolean isBusyEditing();

	/**
	 * Get the snapping service for this editor.
	 * 
	 * @return the service
	 */
	SnapService getSnappingService();

	/**
	 * Get the renderer for the editor.
	 * 
	 * @return the renderer
	 */
	GeometryRenderer getRenderer();

	/**
	 * Is snapping on for dragging ?
	 * 
	 * @return true if on
	 */
	boolean isSnapOnDrag();

	/**
	 * Set whether to snap while in drag mode.
	 * 
	 * @param true if snapping is on
	 */
	void setSnapOnDrag(boolean b);
	

	/**
	 * Set whether to snap while in insert mode.
	 * 
	 * @param true if snapping is on
	 */
	void setSnapOnInsert(boolean b);

}
