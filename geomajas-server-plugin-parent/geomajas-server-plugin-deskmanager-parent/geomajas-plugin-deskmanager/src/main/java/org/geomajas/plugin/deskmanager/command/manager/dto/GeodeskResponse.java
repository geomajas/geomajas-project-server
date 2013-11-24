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
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

/**
 * Response object for commands that return geodesks. Such as:
 * {@link org.geomajas.plugin.deskmanager.command.manager.CreateGeodeskCommand},
 * {@link org.geomajas.plugin.deskmanager.command.manager.GetGeodeskCommand} and
 * {@link org.geomajas.plugin.deskmanager.command.manager.SaveGeodeskCommand}.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class GeodeskResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private GeodeskDto loket;

	public GeodeskDto getGeodesk() {
		return loket;
	}

	public void setGeodesk(GeodeskDto loket) {
		this.loket = loket;
	}
}
