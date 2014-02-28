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

/**
 * Response object for {@link org.geomajas.plugin.deskmanager.command.security.AuthenticateUserCommand}.
 * 
 * @author Jan Venstermans
 * 
 */
public class AuthenticateUserResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private String securityToken;

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
}
