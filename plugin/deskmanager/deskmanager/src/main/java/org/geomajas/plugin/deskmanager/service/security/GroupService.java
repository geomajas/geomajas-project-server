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
 * is a user group.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface GroupService {

	/**
	 * Create a new {@link org.geomajas.plugin.deskmanager.domain.security.Territory} object based on
	 * provided information and save the new territory to the database.
	 *
	 * @param name
	 * @param key
	 * @param crs
	 * @param geometry
	 * @param categoryId
	 * @return the group from the database
	 * @throws GeomajasSecurityException
	 */
	Territory createGroup(String name, String key, String crs, Geometry geometry, String categoryId)
			throws GeomajasSecurityException;

	/**
	 * Deletes the {@link Territory} instance with the provided id from the database.
	 * @param groupId groupId or territoryId
	 * @return successful or not
	 * @throws GeomajasSecurityException
	 */
	boolean deleteGroup(long groupId) throws GeomajasSecurityException;

	/**
	 * Updates the name and key fields of the {@link Territory} instance with the provided id in the database.
	 *
	 * @param groupId
	 * @param name
	 * @param key
	 * @return the updated group from the database
	 * @throws GeomajasSecurityException
	 */
	Territory updateGroup(long groupId, String name, String key) throws GeomajasSecurityException;

	/**
	 * Updates the crs and geometry fields of the {@link Territory} instance with the provided id in the database.
	 *
	 * @param groupId
	 * @param crs
	 * @param geometry
	 * @return the updated group from the database
	 * @throws GeomajasSecurityException
	 */
	Territory updateGroupGeometry(long groupId, String crs, Geometry geometry) throws GeomajasSecurityException;

	/**
	 * Returns the {@link Territory} instance with the provided id from the database.
	 *
	 * @param groupId
	 * @return the group from the database
	 * @throws GeomajasSecurityException
	 */
	Territory findById(long groupId) throws GeomajasSecurityException;

	/**
	 *  Returns the {@link Territory} instance with the provided key from the database.
	 * @param key
	 * @return the group from the database
	 * @throws GeomajasSecurityException
	 */
	Territory findByCode(String key) throws GeomajasSecurityException;

	/**
	 * Returns all the {@link Territory} instances in the database.
	 *
	 * @return all the groups in the database
	 * @throws GeomajasSecurityException
	 */
	List<Territory> findAll() throws GeomajasSecurityException;

	/**
	 * Will save the link of a {@link org.geomajas.plugin.deskmanager.domain.security.User}, identified by provided
	 * userId, a {@link org.geomajas.plugin.deskmanager.domain.security.Territory}, identified by provided groupId,
	 * and the provided {@link Role} in the database.
	 *
	 * @param userId
	 * @param groupId
	 * @param role
	 * @throws GeomajasSecurityException
	 */
	void addUserToGroup(long userId, long groupId, Role role) throws GeomajasSecurityException;

	/**
	 * Removes the combination user/group/role from the database.
	 * @param userId
	 * @param groupId
	 * @param role
	 * @throws GeomajasSecurityException
	 */
	void removeUserFromGroupInRole(long userId, long groupId, Role role) throws GeomajasSecurityException;

	/**
	 *  Returns the {@link TerritoryCategory} as identified by provided territoryCategoryId.
	 * @param territoryCategoryId category identifier
	 * @return requested territory category
	 * @throws GeomajasSecurityException
	 */
	TerritoryCategory findCategoryById(String territoryCategoryId) throws GeomajasSecurityException;

	/**
	 * Will convert a provided shapefile into a {@link Geometry} object. The shapefile needs
	 * to be uploaded beforehand; the shpFileToken refers to the shapefile's memory location.
	 *
	 * @param shpFileToken shapefile token for memory location
	 * @param toCrs crs of the returned geometry
	 * @return
	 * @throws GeomajasSecurityException
	 */
	Geometry getGeometryOfShpFile(String shpFileToken, String toCrs) throws GeomajasSecurityException;
}
