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
package org.geomajas.widget.searchandfilter.client.util;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.widget.searchandfilter.command.dto.DeleteSearchFavouriteRequest;
import org.geomajas.widget.searchandfilter.command.dto.GetSearchFavouritesRequest;
import org.geomajas.widget.searchandfilter.command.dto.GetSearchFavouritesResponse;
import org.geomajas.widget.searchandfilter.command.dto.SaveSearchFavouriteRequest;
import org.geomajas.widget.searchandfilter.command.dto.SaveSearchFavouriteResponse;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

/**
 * Convenience class with helper methods for commands.
 * 
 * @author Kristof Heirwegh
 */
public final class FavouritesCommService {

	/**
	 * Utility class
	 */
	private FavouritesCommService() {
	}

	public static void getSearchFavourites(final DataCallback<List<SearchFavourite>> onFinished) {
		GwtCommand command = new GwtCommand(GetSearchFavouritesRequest.COMMAND);
		command.setCommandRequest(new GetSearchFavouritesRequest());
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetSearchFavouritesResponse>() {
			public void execute(GetSearchFavouritesResponse response) {
				if (onFinished != null) {
					int size = response.getPrivateSearchFavourites().size() +
							response.getSharedSearchFavourites().size();
					List<SearchFavourite> result = new ArrayList<SearchFavourite>(size);
					result.addAll(response.getPrivateSearchFavourites());
					result.addAll(response.getSharedSearchFavourites());
					onFinished.execute(result);
				}
			}
		});
	}

	/**
	 * Returns the persisted instance (this has extra properties + id set).
	 * @param sf search favourite
	 * @param onFinished callback when finished
	 */
	public static void saveSearchFavourite(SearchFavourite sf, final DataCallback<SearchFavourite> onFinished) {
		SaveSearchFavouriteRequest ssfr = new SaveSearchFavouriteRequest();
		ssfr.setSearchFavourite(sf);
		GwtCommand command = new GwtCommand(SaveSearchFavouriteRequest.COMMAND);
		command.setCommandRequest(ssfr);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SaveSearchFavouriteResponse>() {
			public void execute(SaveSearchFavouriteResponse response) {
				if (onFinished != null) {
					onFinished.execute(response.getSearchFavourite());
				}
			}
		});
	}

	public static void deleteSearchFavourite(SearchFavourite sf, final DataCallback<Boolean> onFinished) {
		DeleteSearchFavouriteRequest dsfr = new DeleteSearchFavouriteRequest();
		dsfr.setSearchFavouriteId(sf.getId());
		GwtCommand command = new GwtCommand(DeleteSearchFavouriteRequest.COMMAND);
		command.setCommandRequest(dsfr);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SuccessCommandResponse>() {
			public void execute(SuccessCommandResponse response) {
				if (onFinished != null) {
					onFinished.execute(response.isSuccess());
				}
			}
		});
	}
}
