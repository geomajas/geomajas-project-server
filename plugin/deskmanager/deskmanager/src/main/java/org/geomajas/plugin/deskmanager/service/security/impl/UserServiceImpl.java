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
package org.geomajas.plugin.deskmanager.service.security.impl;

import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.Base64;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.List;

/**
 * Default implementation of {@link org.geomajas.plugin.deskmanager.service.security.UserService}.
 * 
 * @author Jan De Moerloose
 * 
 */
@Repository
@Transactional(rollbackFor = { Exception.class })
public class UserServiceImpl implements UserService {

	private static final String PREFIX = "Geomajas is a wonderful framework";

	private static final String PADDING = "==";

	@Autowired
	private SessionFactory factory;

	//@Autowired
	//private SecurityContext securityContext;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private ProfileService profileService;

	public static final String EMAIL_VALIDATION_REGEX =
			"^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public User createUser(String name, String surname, String email, String password)
			throws GeomajasSecurityException {
		User user = new User();
		user.setActive(true);
		user.setName(name);
		user.setSurname(surname);
		user.setEmail(email);
		// TODO: check password strength + security
		user.setPassword(encodePassword(email, password));
		factory.getCurrentSession().save(user);
		return user;
	}

	@Override
	public boolean deleteUser(long userId) throws GeomajasSecurityException {
		Object user = factory.getCurrentSession().get(User.class, userId);
		if (user != null) {
			factory.getCurrentSession().delete(user);
			return true;
		}
		return false;
	}

	@Override
	public User findById(long userId) throws GeomajasSecurityException {
		return (User) factory.getCurrentSession().get(User.class, userId);
	}

	@Override
	public User findByAddress(String email) throws GeomajasSecurityException {
		return (User) factory.getCurrentSession().createCriteria(User.class).add(Restrictions.eq("email", email))
				.uniqueResult();
	}

	@Override
	public List<User> findAll(boolean includeProfiles) {
		Criteria crit = factory.getCurrentSession().createCriteria(User.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		List<User> users = crit.list();
		if (includeProfiles) {
			for (User user : users) {
				Hibernate.initialize(user.getGroups());
			}
		}
		return users;
	}

	@Override
	public List<GroupMember> findGroupsOfUser(long userId) {
		User user = (User) factory.getCurrentSession().get(User.class, userId);
		List<GroupMember> groups = user.getGroups();
		Hibernate.initialize(groups);
		return groups;
	}

	@Override
	public boolean deleteByAddress(String email) throws GeomajasSecurityException {
		User user = findByAddress(email);
		if (user != null) {
			factory.getCurrentSession().delete(user);
			return true;
		}
		return false;
	}

	@Override
	public void updateUser(User user) throws GeomajasSecurityException {
		// need to get the user from db before updating: password is also in this object.
		User userDb = findById(user.getId());
		/*userDb.setEmail(user.getEmail());*/ //can't update email
		userDb.setName(user.getName());
		userDb.setSurname(user.getSurname());
		userDb.setActive(user.isActive());
		factory.getCurrentSession().saveOrUpdate(userDb);
	}

	@Override
	public void setUserActive(long userId, boolean active) throws GeomajasSecurityException {
		User user = (User) factory.getCurrentSession().load(User.class, userId);
		if (user != null) {
			user.setActive(active);
		}
	}

	@Override
	public void changePassword(long userId, String newPassword) throws GeomajasSecurityException {
		User user = (User) factory.getCurrentSession().load(User.class, userId);
		if (user != null) {
			user.setPassword(encodePassword(user.getEmail(), newPassword));
			factory.getCurrentSession().saveOrUpdate(user);
		}
	}



	//TODO: this code should be available in some service from geomejas security plugin?

	/**
	 * Encode a password string.
	 *
	 * @param mail
	 * @param password
	 * @return
	 */
	public static String encodePassword(String mail, String password) {
		String plaintext = PREFIX + mail + password;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plaintext.getBytes("UTF-8"));
			// TODO change Base64 to {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.Base64}
			// problem: having to add SecurityServiceInfo object.
			String result = Base64.encodeBytes(md.digest());
			if (result.endsWith(PADDING)) {
				result = result.substring(0, result.length() - 2);
			}
			return result;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}
