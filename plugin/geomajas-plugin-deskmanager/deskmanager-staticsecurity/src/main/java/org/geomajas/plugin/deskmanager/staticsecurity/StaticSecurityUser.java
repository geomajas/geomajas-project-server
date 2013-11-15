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
package org.geomajas.plugin.deskmanager.staticsecurity;

import org.geomajas.plugin.deskmanager.domain.security.Profile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Representation of a user login, defined by username, password and a list of profiles.
 *
 * @author Oliver May
 */
public class StaticSecurityUser {

	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private List<StaticSecurityProfile> profiles;

	private String name;
	private String surname;


	/**
	 * Get the username of this user.
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username of this user.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the password of this user.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password of this user.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the profiles attached to this user.
	 *
	 * @return the profiles
	 */
	public List<StaticSecurityProfile> getProfiles() {
		return profiles;
	}

	/**
	 * Set the profiles attached to this user.
	 *
	 * @param profiles the profiles
	 */
	public void setProfiles(List<StaticSecurityProfile> profiles) {
		this.profiles = profiles;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
