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

import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.security.GeomajasSecurityException;

import java.util.List;

/**
 * Service for User related database calls.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public interface UserService {

	User createUser(String name, String surname, String email, String password) throws GeomajasSecurityException;

	boolean deleteUser(long userId) throws GeomajasSecurityException;

	User findById(long userId) throws GeomajasSecurityException;

	User findByAddress(String email) throws GeomajasSecurityException;

	/**
	 * Argument to indicate whether or not to initialize the lazy field 'profile list'.
	 * @param includeProfiles to initialize lazy profile list
	 * @return
	 */
	List<User> findAll(boolean includeProfiles);

	List<GroupMember> findGroupsOfUser(long userId);

	boolean deleteByAddress(String email) throws GeomajasSecurityException;

	void updateUser(User user) throws GeomajasSecurityException;

	void setUserActive(long userId, boolean active) throws GeomajasSecurityException;

	void changePassword(long userId, String newPassword) throws GeomajasSecurityException;

}
