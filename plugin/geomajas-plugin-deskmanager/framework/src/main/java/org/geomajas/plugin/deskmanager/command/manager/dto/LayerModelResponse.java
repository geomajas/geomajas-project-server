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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

/**
 * Response object for commands that return layermodels. Such as:
 * {@link org.geomajas.plugin.deskmanager.command.manager.GetLayerModelCommand},
 * {@link org.geomajas.plugin.deskmanager.command.manager.CreateLayerModelCommand} and 
 * {@link org.geomajas.plugin.deskmanager.command.manager.SaveLayerModelCommand}
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class LayerModelResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private LayerModelDto layerModel;

	public LayerModelDto getLayerModel() {
		return layerModel;
	}

	public void setLayerModel(LayerModelDto layerModel) {
		this.layerModel = layerModel;
	}

}
