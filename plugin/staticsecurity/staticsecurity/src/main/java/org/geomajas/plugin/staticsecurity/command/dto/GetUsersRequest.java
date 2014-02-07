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

package org.geomajas.plugin.staticsecurity.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.plugin.staticsecurity.security.dto.AllUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;

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

	private UserFilter filter = new AllUserFilter();

	/**
	 * Get the user filter.
	 * 
	 * @return the filter
	 */
	public UserFilter getFilter() {
		return filter;
	}

	/**
	 * Set the user filter.
	 * 
	 * @param filter
	 */
	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}	
	


}
