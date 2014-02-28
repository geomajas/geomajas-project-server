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

import java.util.List;

import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.security.GeomajasSecurityException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Service for {@link org.geomajas.plugin.deskmanager.domain.security.Territory}
 * related database calls, where {@link org.geomajas.plugin.deskmanager.domain.security.Territory}
 * is a user group .
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface GroupService {

	Territory createGroup(String name, String key, String crs, Geometry geometry, String categoryId)
			throws GeomajasSecurityException;

	boolean deleteGroup(long groupId) throws GeomajasSecurityException;
	
	Territory updateGroup(long groupId, String name, String key) throws GeomajasSecurityException;	
	
	Territory updateGroupGeometry(long groupId, String crs, Geometry geometry) throws GeomajasSecurityException;	

	Territory findById(long groupId) throws GeomajasSecurityException;

	Territory findByCode(String key) throws GeomajasSecurityException;

	List<Territory> findAll() throws GeomajasSecurityException;

	void addUserToGroup(long userId, long groupId, Role role) throws GeomajasSecurityException;

	void removeUserFromGroupInRole(long userId, long groupId, Role role) throws GeomajasSecurityException;

	TerritoryCategory findCategoryById(String territoryCategoryId) throws GeomajasSecurityException;

	Geometry getGeometryOfShpFile(String shpFileToken, String toCrs) throws GeomajasSecurityException;
}
