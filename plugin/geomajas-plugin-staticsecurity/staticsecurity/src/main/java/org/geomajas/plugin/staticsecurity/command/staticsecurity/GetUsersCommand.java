/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command.staticsecurity;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.plugin.staticsecurity.command.dto.GetUsersRequest;
import org.geomajas.plugin.staticsecurity.command.dto.GetUsersResponse;
import org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Command which allows you to know which users exist. This can be useful to show a select box in a login screen, to be
 * able to assign things to users etc.
 * <p/>
 * Note that this command can be considered unsafe as the results make it a lot easier to guess credentials. You
 * probably should not allow access to everybody!
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api
@Component
public class GetUsersCommand implements Command<GetUsersRequest, GetUsersResponse> {

	@Autowired
	private SecurityServiceInfo securityServiceInfo;

	/** {@inheritDoc} */
	public GetUsersResponse getEmptyCommandResponse() {
		return new GetUsersResponse();
	}

	/** {@inheritDoc} */
	public void execute(GetUsersRequest request, GetUsersResponse response) throws Exception {
		HashSet<String> users = new HashSet<String>();
		for (UserInfo userInfo : securityServiceInfo.getUsers()) {
			users.add(userInfo.getUserId());
		}
		response.setUsers(users);
	}
}
