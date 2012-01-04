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

import java.util.Date;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.widget.searchandfilter.command.dto.SaveSearchFavouriteRequest;
import org.geomajas.widget.searchandfilter.command.dto.SaveSearchFavouriteResponse;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesService;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command to save SearchFavourite.
 * 
 * @author Kristof Heirwegh
 */
@Component
public class SaveSearchFavouriteCommand implements Command<SaveSearchFavouriteRequest, SaveSearchFavouriteResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(SaveSearchFavouriteCommand.class);

	@Autowired
	private SearchFavouritesService searchFavouritesService;

	@Autowired(required = false)
	private SearchFavouritesSettings settings;

	@Autowired
	private SecurityContext securityContext;

	public void execute(final SaveSearchFavouriteRequest request, final SaveSearchFavouriteResponse response)
			throws Exception {
		SearchFavourite sf = request.getSearchFavourite();
		if (null == sf) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "SearchFavourite");
		}
		if (null == sf.getCriterion()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "Criterion missing.");
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
			// -- new --
			if (sf.getId() == null) {
				sf.setCreator(user);
				sf.setLastChange(new Date());
				sf.setLastChangeBy(user);

				// -- existing
			} else {
				SearchFavourite persisted = searchFavouritesService.getSearchFavourite(sf.getId());
				if (persisted == null) {
					response.getErrorMessages().add("Id is set but not found??");
					return;
				} else {
					if (anonymous && !user.equals(sf.getCreator())) {
						response.getErrorMessages().add("User Anonymous can only delete his own favourites.");
					} else {
						// not trusting client one.
						sf.setCreator(persisted.getCreator());
						sf.setLastChange(new Date());
						sf.setLastChangeBy(user);
					}
				}
			}
			searchFavouritesService.saveOrUpdateSearchFavourite(sf);
			response.setSearchFavourite(sf);

		} catch (Exception e) {
			response.getErrorMessages().add("Failed saving SearchFavourite! " + e.getMessage());
		}
	}

	public SaveSearchFavouriteResponse getEmptyCommandResponse() {
		return new SaveSearchFavouriteResponse();
	}
}
