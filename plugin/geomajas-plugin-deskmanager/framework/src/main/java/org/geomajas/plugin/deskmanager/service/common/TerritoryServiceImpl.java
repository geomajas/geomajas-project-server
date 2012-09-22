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
package org.geomajas.plugin.deskmanager.service.common;

import java.util.List;

import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Service("TerritoryService")
@Repository
@Transactional(rollbackFor = { Exception.class })
public class TerritoryServiceImpl implements TerritoryService {

	@Autowired
	private SessionFactory factory;

	@SuppressWarnings("unchecked")
	public List<Territory> getTerritories() {
		Criteria critter = factory.getCurrentSession().createCriteria(Territory.class);
		critter.createAlias("category", "cat");
		critter.addOrder(Order.asc("cat.description"));
		critter.addOrder(Order.asc("name"));
		critter.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);

		return critter.list();
	}

	public Territory getById(long id) {
		return (Territory) factory.getCurrentSession().get(Territory.class, id);
	}

	public Territory getByCode(String code) {
		return (Territory) factory.getCurrentSession().createCriteria(Territory.class)
				.add(Restrictions.eq("code", code)).uniqueResult();
	}

	public void saveOrUpdateTerritory(Territory territory) {
		factory.getCurrentSession().saveOrUpdate(territory);
	}

	public void saveOrUpdateCategory(TerritoryCategory category) {
		factory.getCurrentSession().saveOrUpdate(category);
	}
}
