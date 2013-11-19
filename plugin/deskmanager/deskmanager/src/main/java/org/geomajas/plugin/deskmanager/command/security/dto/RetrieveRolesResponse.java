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
package org.geomajas.plugin.deskmanager.command.security.dto;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

/**
 * Response object for {@link org.geomajas.plugin.deskmanager.command.security.RetrieveRolesCommand}.
 * 
 * @author Oliver May
 * 
 */
public class RetrieveRolesResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private Map<String, ProfileDto> profiles = new HashMap<String, ProfileDto>();

	public void setRoles(Map<String, ProfileDto> profiles) {
		this.profiles = profiles;
	}

	public Map<String, ProfileDto> getRoles() {
		return profiles;
	}

}
