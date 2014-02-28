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
package org.geomajas.plugin.deskmanager.service.security;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.service.manager.GenericFileService;
import org.geomajas.plugin.deskmanager.service.manager.ShapeFileService;
import org.geomajas.plugin.deskmanager.utility.FileUtils;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link GroupService}.
 * 
 * @author Jan De Moerloose
 * 
 */
@Repository
@Transactional(rollbackFor = { Exception.class })
public class GroupServiceImpl implements GroupService {

	@Autowired
	private SessionFactory factory;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private GenericFileService genericFileService;

	@Autowired
	private ShapeFileService shapeFileService;

	private static final String DEFAULT_CATEGORY_ID = "ALL";

	@Override
	public Territory createGroup(String name, String key, String crs, Geometry geometry, String categoryId)
				throws GeomajasSecurityException {
		TerritoryCategory category = findCategoryById(categoryId != null ? categoryId : DEFAULT_CATEGORY_ID);
		Territory group = new Territory();
		group.setName(name);
		group.setCode(key);
		group.setCrs(crs);
		group.setGeometry(geometry);
		group.setCategory(category);
		factory.getCurrentSession().save(group);
		return group;
	}

	@Override
	public boolean deleteGroup(long groupId) throws GeomajasSecurityException {
		Object group = factory.getCurrentSession().get(Territory.class, groupId);
		if (group != null) {
			factory.getCurrentSession().delete(group);
			return true;
		}
		return false;
	}

	@Override
	public Territory findById(long groupId) throws GeomajasSecurityException {
		return (Territory) factory.getCurrentSession().load(Territory.class, groupId);
	}

	@Override
	public Territory updateGroup(long groupId, String name, String key) {
		Territory group = (Territory) factory.getCurrentSession().load(Territory.class, groupId);
		if (group != null) {
			group.setName(name);
			group.setCode(key);
		}
		return group;
	}

	@Override
	public Territory updateGroupGeometry(long groupId, String crs, Geometry geometry) {
		Territory group = (Territory) factory.getCurrentSession().load(Territory.class, groupId);
		if (group != null) {
			group.setCrs(crs);
			group.setGeometry(geometry);
		}
		return group;
	}

	@Override
	public void addUserToGroup(long userId, long groupId, Role role) throws GeomajasSecurityException {
		User user = (User) factory.getCurrentSession().load(User.class, userId);
		Territory group = (Territory) factory.getCurrentSession().load(Territory.class, groupId);
		if (user != null && group != null) {
			user.addToGroupInRole(group, role);
		}
	}

	@Override
	public void removeUserFromGroupInRole(long userId, long groupId, Role role) throws GeomajasSecurityException {
		User user = (User) factory.getCurrentSession().load(User.class, userId);
		Territory group = (Territory) factory.getCurrentSession().load(Territory.class, groupId);
		if (user != null && group != null) {
			user.removeFromGroupInRole(group, role);
		}
	}

	@Override
	public List<Territory> findAll() {
		Criteria crit = factory.getCurrentSession().createCriteria(Territory.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		return crit.list();
	}

	@Override
	public Territory findByCode(String code) throws GeomajasSecurityException {
		return (Territory) factory.getCurrentSession().createCriteria(Territory.class)
				.add(Restrictions.eq("code", code)).uniqueResult();
	}

	@Override
	public TerritoryCategory findCategoryById(String territoryCategoryId) throws GeomajasSecurityException {
		return (TerritoryCategory) factory.getCurrentSession().get(TerritoryCategory.class, territoryCategoryId);
	}

	@Override
	public Geometry getGeometryOfShpFile(String shpFileToken, String toCrs) throws GeomajasSecurityException {
		File file = genericFileService.getFile(shpFileToken);
		Geometry geometry = null;

		try {
			// unzip the temporary file
			if (shapeFileService.unzip(file.getAbsolutePath(), file.getParentFile().getAbsolutePath())) {
				List<Geometry> geometries = shapeFileService.
						extractGeometriesFromShapeFile(file.getParentFile().getAbsolutePath(), toCrs);
				// search for one polygon
				while (geometries != null && geometries.size() > 0) {
					if (geometries.size() == 1) {
					   geometry = geometries.get(0);
						if ("Polygon".equals(geometry.getGeometryType())) {
							geometries = null;
						} else {
							geometries = new ArrayList<Geometry>();
							for (int i = 0 ; i < geometry.getNumGeometries() ; i++) {
								geometries.add(geometry.getGeometryN(i));
							}
						}
					} else {
						geometry = null;
						geometries = null;
					}
				}
			}
		} catch (Exception e) {
//			log.warn("Exception while processing shapefile", e);
			int i = 5;
		} finally {
			FileUtils.deleteFolder(file.getParentFile()); // always clean up temporary files
		}
		return geometry;
	}

}
