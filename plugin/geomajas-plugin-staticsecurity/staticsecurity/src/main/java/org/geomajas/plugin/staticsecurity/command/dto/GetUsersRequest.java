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

package org.geomajas.plugin.staticsecurity.command.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geomajas.annotation.Api;
import org.geomajas.command.EmptyCommandRequest;

/**
 * Request object for {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.GetUsersCommand}.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public class GetUsersRequest extends EmptyCommandRequest {

	private static final long serialVersionUID = 190L;

	/**
	 * Command name for this request.
	 */
	public static final String COMMAND = "command.staticsecurity.GetUsers";

	private Map<String, String> parameters;

	private Set<String> roles = new HashSet<String>();

	/**
	 * Get the query parameters.
	 * 
	 * @return the map of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Set the additional query parameters (key/value pairs). These parameters will be passed to the
	 * {@link org.geomajas.plugin.staticsecurity.security.UserDirectoryService} implementors.
	 * 
	 * @param parameters custom map of parameters
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Get the roles to filter on.
	 * 
	 * @return the roles
	 */
	public Set<String> getRoles() {
		return roles;
	}

	/**
	 * Set the roles to filter on.
	 * 
	 * @param roles the roles
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	/**
	 * Add a role to filter on.
	 * 
	 * @param role a role
	 */
	public void addRole(String role) {
		roles.add(role);
	}

}
