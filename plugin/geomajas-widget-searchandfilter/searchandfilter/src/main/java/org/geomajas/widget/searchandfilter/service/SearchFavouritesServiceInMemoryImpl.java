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
package org.geomajas.widget.searchandfilter.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * DataStore service for SearchFavourites using some simple HashMaps. This is
 * not a persistent implementation.
 * <p>
 * Good for small sets and testing.
 * <p>
 * This is the default datastore. If you want to use hibernate you will need to
 * configure your ApplicationContext to use the
 * SearchFavouritesServiceHibernateImpl version instead.
 * 
 * @author Kristof Heirwegh
 * 
 */
@Service("SearchFavouritesService")
@Repository
public class SearchFavouritesServiceInMemoryImpl implements SearchFavouritesService {

	private final Logger log = LoggerFactory.getLogger(SearchFavouritesServiceInMemoryImpl.class);

	private final Map<String, Map<Long, SearchFavourite>> privateFavourites =
		new HashMap<String, Map<Long, SearchFavourite>>();
	private final Map<Long, SearchFavourite> sharedFavourites = new HashMap<Long, SearchFavourite>();
	private final Map<Long, SearchFavourite> allFavourites = new HashMap<Long, SearchFavourite>();

	private final Object lock = new Object();
	private long idCounter;

	public SearchFavourite getSearchFavourite(Long id) throws IOException {
		synchronized (lock) {
			return allFavourites.get(id);
		}
	}

	public Collection<SearchFavourite> getPrivateSearchFavourites(String user) throws IOException {
		synchronized (lock) {
			Map<Long, SearchFavourite> tmp = privateFavourites.get(user);
			if (tmp == null) {
				return new ArrayList<SearchFavourite>(0);
			} else {
				return new ArrayList<SearchFavourite>(tmp.values());
			}
		}
	}

	public Collection<SearchFavourite> getSharedSearchFavourites() throws IOException {
		synchronized (lock) {
			// not returning real list as it needs to be used in a
			// concurrent-safe fashion.
			return new ArrayList<SearchFavourite>(sharedFavourites.values());
		}
	}

	/**
	 * If creator is not set, only shared favourites will be checked (if
	 * shared)!
	 */
	public void deleteSearchFavourite(SearchFavourite sf) throws IOException {
		synchronized (lock) {
			if (sf == null || sf.getId() == null) {
				throw new IllegalArgumentException("null, or id not set!");
			} else {
				allFavourites.remove(sf.getId());
				if (sf.isShared()) {
					sharedFavourites.remove(sf.getId());
				} else {
					if (sf.getCreator() != null) {
						Map<Long, SearchFavourite> favs = privateFavourites.get(sf.getCreator());
						if (favs != null) {
							favs.remove(sf.getId());
						}
					} else {
						log.warn("Creator is not set! I'm not checking all users so I'm giving up.");
					}
				}
			}
		}
	}

	public void saveOrUpdateSearchFavourite(SearchFavourite sf) throws IOException {
		synchronized (lock) {
			if (sf.getCreator() == null && !sf.isShared()) {
				throw new IllegalArgumentException("Creator is not set!");
			}
			if (sf.getId() == null) {
				sf.setId(idCounter);
				idCounter++;
			} else {
				// do some cleanup if needed
				SearchFavourite persisted = getSearchFavourite(sf.getId());
				if (persisted != null && sf.isShared() != persisted.isShared()) {
					deleteSearchFavourite(persisted);
				}
			}

			if (sf.isShared()) {
				// this will overwrite the existing value if already existed (making it an update)
				sharedFavourites.put(sf.getId(), sf);
			} else {
				Map<Long, SearchFavourite> favs = privateFavourites.get(sf.getCreator());
				if (favs == null) {
					favs = new HashMap<Long, SearchFavourite>();
				}
				// this will overwrite the existing value if already existed (making it an update)
				favs.put(sf.getId(), sf);
				privateFavourites.put(sf.getCreator(), favs);
			}
			allFavourites.put(sf.getId(), sf);
		}
	}
}
