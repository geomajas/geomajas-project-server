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
package org.geomajas.widget.searchandfilter.command.searchandfilter;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.widget.searchandfilter.command.dto.GetSearchFavouritesRequest;
import org.geomajas.widget.searchandfilter.command.dto.GetSearchFavouritesResponse;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesService;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command to retrieve SearchFavourites.
 * 
 * @author Kristof Heirwegh
 */
@Component
public class GetSearchFavouritesCommand implements Command<GetSearchFavouritesRequest, GetSearchFavouritesResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(GetSearchFavouritesCommand.class);

	@Autowired
	private SearchFavouritesService searchFavouritesService;

	@Autowired(required = false)
	private SearchFavouritesSettings settings;

	@Autowired
	private SecurityContext securityContext;

	public void execute(final GetSearchFavouritesRequest request, final GetSearchFavouritesResponse response)
			throws Exception {
		String user = securityContext.getUserName();
		if (user == null || "".equals(user)) {
			if (settings != null && settings.isAllowAnonymous() && settings.isAnonymousCanEdit()) {
				user = "anonymous";
			} else {
				throw new GeomajasSecurityException(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID, "Need username.");
			}
		}

		response.setPrivateSearchFavourites(searchFavouritesService.getPrivateSearchFavourites(user));
		response.setSharedSearchFavourites(searchFavouritesService.getSharedSearchFavourites());
	}

	public GetSearchFavouritesResponse getEmptyCommandResponse() {
		return new GetSearchFavouritesResponse();
	}
}
