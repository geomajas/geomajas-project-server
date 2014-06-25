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

import org.geomajas.command.CommandRequest;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request for {@link org.geomajas.plugin.deskmanager.command.security.GroupAssignmentCommand}.
 * 
 * @author Jan Venstermans
 *
 */
public class GroupAssignmentRequest implements CommandRequest {

	private static final long serialVersionUID = 115L;

	public static final String COMMAND = "GroupAssignment";

	private TerritoryDto territoryDto;

	private Map<Long, List<Role>> addedProfiles = new HashMap<Long, List<Role>>();

	private Map<Long, List<Role>> removedProfiles = new HashMap<Long, List<Role>>();

	public TerritoryDto getTerritoryDto() {
		return territoryDto;
	}

	public void setTerritoryDto(TerritoryDto territoryDto) {
		this.territoryDto = territoryDto;
	}

	public Map<Long, List<Role>> getAddedProfiles() {
		return addedProfiles;
	}

	public void setAddedProfiles(Map<Long, List<Role>> addedProfiles) {
		this.addedProfiles = addedProfiles;
	}

	public Map<Long, List<Role>> getRemovedProfiles() {
		return removedProfiles;
	}

	public void setRemovedProfiles(Map<Long, List<Role>> removedProfiles) {
		this.removedProfiles = removedProfiles;
	}
}
