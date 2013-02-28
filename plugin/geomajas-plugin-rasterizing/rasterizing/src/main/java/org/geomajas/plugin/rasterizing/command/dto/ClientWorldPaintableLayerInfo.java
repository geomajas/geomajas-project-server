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
package org.geomajas.plugin.rasterizing.command.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;

/**
 * Metadata DTO class that represents a client layer with arbitrary world paintables in world space. This layer has no
 * server-side equivalent.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ClientWorldPaintableLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 100L;

	private List<WorldPaintableInfo> paintables = new ArrayList<WorldPaintableInfo>();

	// default showing
	private boolean showing = true;
	
	public List<WorldPaintableInfo> getPaintables() {
		return paintables;
	}

	
	public void setPaintables(List<WorldPaintableInfo> paintables) {
		this.paintables = paintables;
	}

	/**
	 * Get the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @return The showing status of this layer
	 */
	public boolean isShowing() {
		return showing;
	}

	/**
	 * Sets the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @param showing
	 *            showing status of this layer
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	
}
