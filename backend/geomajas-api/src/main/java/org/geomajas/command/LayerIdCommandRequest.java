/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.command;

import org.geomajas.global.Api;

/**
 * Command request object which contains a layer id. Should be extended when you need a layer id as request
 * parameter.
 * <p/>
 * It is intended that this may be used for transaction support to assure transactions are started for relevant layers
 * only.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerIdCommandRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;
	private String layerId;

	/**
	 * Get the layer id.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set the layer id.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

}
