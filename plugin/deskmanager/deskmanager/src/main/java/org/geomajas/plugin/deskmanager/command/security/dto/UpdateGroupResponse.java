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
package org.geomajas.plugin.deskmanager.command.security.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * Response for {@link org.geomajas.plugin.deskmanager.command.security.UpdateGroupCommand}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class UpdateGroupResponse extends CommandResponse {

	private static final long serialVersionUID = 115L;

	private TerritoryDto group;

	public TerritoryDto getGroup() {
		return group;
	}

	public void setGroup(TerritoryDto group) {
		this.group = group;
	}

}
