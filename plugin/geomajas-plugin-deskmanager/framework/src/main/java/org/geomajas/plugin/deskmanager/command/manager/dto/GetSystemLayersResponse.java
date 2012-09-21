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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.util.HashSet;
import java.util.Set;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;


/**
 * Response for {@link GetSystemLayersCommand}. Contains an (ordered) list of ClientLayerInfo's and layerModels.
 * 
 * @author Oliver May
 *
 */
public class GetSystemLayersResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;
	
	private Set<LayerDto> layers = new HashSet<LayerDto>();
	
	/**
	 * @param layers the layers to set
	 */
	public void setLayers(Set<LayerDto> layers) {
		this.layers = layers;
	}

	/**
	 * @return the layers
	 */
	public Set<LayerDto> getLayers() {
		return layers;
	}
}
