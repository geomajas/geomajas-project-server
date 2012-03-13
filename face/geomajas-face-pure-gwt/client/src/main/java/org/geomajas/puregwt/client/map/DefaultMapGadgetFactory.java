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
package org.geomajas.puregwt.client.map;

import org.geomajas.configuration.client.ClientMapInfo;

/**
 * Factory for default map gadgets.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface DefaultMapGadgetFactory {

	/**
	 * The map gadget types.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum Type {
		/**
		 * Panning gadget.
		 */
		PANNING,
		/**
		 * Scalebar.
		 */
		SCALEBAR,
		/**
		 * Simple zoom.
		 */
		SIMPLE_ZOOM,
		/**
		 * Watermark.
		 */
		WATERMARK,
		/**
		 * Stepped zoom.
		 */
		ZOOM_STEP,
		/**
		 * Zoom to rectangle.
		 */
		ZOOM_TO_RECTANGLE
	}

	/**
	 * Create a new gadget of the specified type and at the specified location.
	 * 
	 * @param type the gadget type
	 * @param top the CSS top location
	 * @param left the CSS left location
	 * @param clientMapInfo the client map information
	 * @return the new gadget
	 */
	MapGadget createGadget(Type type, int top, int left, ClientMapInfo clientMapInfo);
}
