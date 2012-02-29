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
package org.geomajas.widget.searchandfilter.command.searchandfilter;

import org.geomajas.command.Command;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.widget.searchandfilter.command.dto.DeleteSearchFavouriteRequest;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesService;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command to delete SearchFavourite.
 * 
 * @author Kristof Heirwegh
 */
@Component
public class DeleteSearchFavouriteCommand implements Command<DeleteSearchFavouriteRequest, SuccessCommandResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(DeleteSearchFavouriteCommand.class);

	@Autowired
	private SearchFavouritesService searchFavouritesService;

	@Autowired(required = false)
	private SearchFavouritesSettings settings;

	@Autowired
	private SecurityContext securityContext;

	public void execute(final DeleteSearchFavouriteRequest request, final SuccessCommandResponse response)
			throws Exception {
		if (null == request.getSearchFavouriteId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "SearchFavouriteId");
		}

		String user = securityContext.getUserName();
		boolean anonymous = false;
		if (user == null || "".equals(user)) {
			if (settings != null && settings.isAllowAnonymous() && settings.isAnonymousCanEdit()) {
				user = "anonymous";
				anonymous = true;
			} else {
				throw new GeomajasSecurityException(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID, "Need username.");
			}
		}

		try {
			SearchFavourite sf = searchFavouritesService.getSearchFavourite(request.getSearchFavouriteId());
			if (sf == null) {
				response.setSuccess(false);
				response.getErrorMessages().add("No SearchFavourite found with id: " + request.getSearchFavouriteId());
			} else {
				if (anonymous && !user.equals(sf.getCreator())) {
					response.setSuccess(false);
					response.getErrorMessages().add("User Anonymous can only delete his own favourites.");
				} else {
					searchFavouritesService.deleteSearchFavourite(sf);
					response.setSuccess(true);
				}
			}
		} catch (Exception e) { // NOSONAR
			response.setSuccess(false);
			response.getErrorMessages().add(
					"Failed deleting SearchFavourite with id: " + request.getSearchFavouriteId());
		}
	}

	public SuccessCommandResponse getEmptyCommandResponse() {
		return new SuccessCommandResponse();
	}
}
