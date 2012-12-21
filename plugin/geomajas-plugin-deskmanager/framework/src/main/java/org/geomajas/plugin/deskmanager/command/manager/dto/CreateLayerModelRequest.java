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

import org.geomajas.command.CommandRequest;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

/**
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.CreateLayerModelCommand}.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class CreateLayerModelRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.manager.CreateLayerModel";

	private DynamicLayerConfiguration configuration;

	public DynamicLayerConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(DynamicLayerConfiguration configuration) {
		this.configuration = configuration;
	}
}
