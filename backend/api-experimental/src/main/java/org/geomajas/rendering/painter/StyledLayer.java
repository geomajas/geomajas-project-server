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
package org.geomajas.rendering.painter;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;

/**
 * A layer with a specific style, ready for rendering.
 * 
 * @author Jan De Moerloose
 */
public class StyledLayer {

	private final VectorLayerInfo layerInfo;

	private final NamedStyleInfo styleInfo;
	
	private final String id;

	/**
	 * Constructs a styled layer.
	 * 
	 * @param layer layer
	 * @param styleInfo the style metadata
	 */
	public StyledLayer(VectorLayer layer, NamedStyleInfo styleInfo) {
		this.layerInfo = layer.getLayerInfo();
		this.styleInfo = styleInfo;
		this.id = layer.getId();
	}
	
	public String getId() {
		return id;
	}

	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public NamedStyleInfo getStyleInfo() {
		return styleInfo;
	}

}
