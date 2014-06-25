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

	/**
	 * Creates a new {@link User} object based on the provided information and saves this
	 * new user to the database.
	 *
	 * @param name
	 * @param surname
	 * @param email
	 * @param password
	 * @return user as in the database
	 * @throws GeomajasSecurityException
	 */
	User createUser(String name, String surname, String email, String password) throws GeomajasSecurityException;

	/**
	 * Removes a {@link User}, identified by userId, from the database.
	 * @param userId
	 * @return successful or not.
	 * @throws GeomajasSecurityException
	 */
	boolean deleteUser(long userId) throws GeomajasSecurityException;

	/**
	 * Finds a {@link User} based on the user's id.
	 * @param userId
	 * @return user as in database
	 * @throws GeomajasSecurityException
	 */
	User findById(long userId) throws GeomajasSecurityException;

	/**
	 * Finds a {@link User} based on the user's email.
	 * @param email
	 * @return user as in database
	 * @throws GeomajasSecurityException
	 */
	User findByAddress(String email) throws GeomajasSecurityException;

	/**
	 * Returns a list of all {@link User} in database.
	 * Boolean argument includeProfiles to indicate whether or not to initialize the lazy field 'profile list'.
	 * @param includeProfiles to initialize lazy profile list
	 * @return all users in database
	 */
	List<User> findAll(boolean includeProfiles);

	/**
	 * Returns all the {@link GroupMember} objects of a specific {@link User}.
	 * A {@link GroupMember} is a combination of user/group/role.
	 *
	 * @param userId
	 * @return
	 */
	List<GroupMember> findGroupsOfUser(long userId);

	/**
	 * Delete a {@link User}, identified by his/her email, from the database.
	 * @param email
	 * @return successful or not
	 * @throws GeomajasSecurityException
	 */
	boolean deleteByAddress(String email) throws GeomajasSecurityException;

	/**
	 *  Updates information of a {@link User} in the database.
	 *  The provided user's id is used to identify the {@link User} to update in the database.
	 *
	 * @param user
	 * @throws GeomajasSecurityException
	 */
	void updateUser(User user) throws GeomajasSecurityException;

	/**
	 * Update the activity status us a user.
	 * @param userId
	 * @param active
	 * @throws GeomajasSecurityException
	 */
	void setUserActive(long userId, boolean active) throws GeomajasSecurityException;

	/**
	 * Changes the password of a {@link User}, identified by userId. The provided new password
	 * must be encrypted; encrypting is done in the method.
	 *
	 * @param userId
	 * @param newPassword unencrypted new password
	 * @throws GeomajasSecurityException
	 */
	void changePassword(long userId, String newPassword) throws GeomajasSecurityException;

}
