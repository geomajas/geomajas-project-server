/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.client.ClientMapInfo;

/**
 * DTO object for RasterizedLayersComponent.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 *
 */
public class RasterizedLayersComponentInfo extends BaseLayerComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private ClientMapInfo mapInfo;
	
	public ClientMapInfo getMapInfo() {
		return mapInfo;
	}
	
	public void setMapInfo(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}
	
}
