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
package org.geomajas.plugin.deskmanager.service.geodesk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;
import org.geomajas.widget.searchandfilter.service.SearchFavouritesService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * DataStore service for SearchFavourites using a database (Hibernate) to persist the Favourites.
 * <p>
 * 
 * @author Kristof Heirwegh
 */
@Transactional(rollbackFor = { Exception.class })
public class SearchFavouritesServiceHibernateImpl implements SearchFavouritesService {

	@Autowired
	private SessionFactory factory;

	@Autowired
	private SearchFavouritesConverterService conversion;

	@SuppressWarnings("unchecked")
	public Collection<SearchFavourite> getPrivateSearchFavourites(String user) throws IOException {
		List<SearchFavourite> result = new ArrayList<SearchFavourite>();
		if (user == null || "".equals(user)) {
			return result;
		}
		Query q = factory.getCurrentSession().createQuery(
				"FROM SearchFavourite s WHERE s.creator like ? and s.shared=false");
		q.setString(0, user);
		List<org.geomajas.plugin.deskmanager.domain.SearchFavourite> interal = q.list();
		for (org.geomajas.plugin.deskmanager.domain.SearchFavourite fav : interal) {
			result.add(conversion.toDto(fav));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<SearchFavourite> getSharedSearchFavourites() throws IOException {
		List<SearchFavourite> result = new ArrayList<SearchFavourite>();
		Query q = factory.getCurrentSession().createQuery("FROM SearchFavourite s where s.shared=true");
		List<org.geomajas.plugin.deskmanager.domain.SearchFavourite> interal = q.list();
		for (org.geomajas.plugin.deskmanager.domain.SearchFavourite fav : interal) {
			result.add(conversion.toDto(fav));
		}
		return result;
	}

	public void deleteSearchFavourite(SearchFavourite sf) throws IOException {
		if (sf.getId() == null) {
			return;
		}

		Query q = factory.getCurrentSession().createQuery("DELETE FROM SearchFavourite s where s.id=?");
		q.setLong(0, sf.getId());

		int res = q.executeUpdate();
		if (res != 1) {
			throw new IOException("Failed deleting record with id: " + sf.getId());
		}
	}

	public void saveOrUpdateSearchFavourite(SearchFavourite sf) throws IOException {
		if (sf == null) {
			throw new IllegalArgumentException("Need a favourite");
		}
		org.geomajas.plugin.deskmanager.domain.SearchFavourite internal = conversion.toInternal(sf);

		factory.getCurrentSession().saveOrUpdate(internal);
		if (sf.getId() == null) {
			sf.setId(internal.getId());
		}
	}

	public SearchFavourite getSearchFavourite(Long id) throws IOException {
		org.geomajas.plugin.deskmanager.domain.SearchFavourite internal = 
			(org.geomajas.plugin.deskmanager.domain.SearchFavourite) factory
				.getCurrentSession().get(org.geomajas.plugin.deskmanager.domain.SearchFavourite.class, id);
		if (internal == null) {
			return null;
		} else {
			return conversion.toDto(internal);
		}
	}
}
