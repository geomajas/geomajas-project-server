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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GetGroupsResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<TerritoryDto> groups = new ArrayList<TerritoryDto>();

	public List<TerritoryDto> getGroups() {
		return groups;
	}

	public void setGroups(List<TerritoryDto> groups) {
		this.groups = groups;
	}

}
