/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Definition of a role.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public class RoleInfo {

	private List<AuthorizationInfo> authorizations = new ArrayList<AuthorizationInfo>();

	/**
	 * Get authorization info for role.
	 *
	 * @return authorization info
	 */
	public List<AuthorizationInfo> getAuthorizations() {
		return authorizations;
	}

	/**
	 * Set authorization info.
	 *
	 * @param authorizations authorization info
	 */
	public void setAuthorizations(List<AuthorizationInfo> authorizations) {
		this.authorizations = authorizations;
	}

}
