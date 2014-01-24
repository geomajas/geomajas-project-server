/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.plugin.deskmanager.command.manager.CheckGeodeskIdExistsCommand}.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 */
public class CheckGeodeskIdExistsResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private Boolean exists;

	public Boolean getExists() {
		return exists;
	}

	public void setExists(Boolean exists) {
		this.exists = exists;
	}

}
