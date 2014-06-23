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
package org.geomajas.plugin.deskmanager.domain.security.dto;

import org.geomajas.plugin.deskmanager.service.security.UserServiceImpl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO version of user.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * 
 */
public class UserDto implements Serializable {

	private long id;

	@NotNull
	@Pattern(regexp = UserServiceImpl.EMAIL_VALIDATION_REGEX, message = "Not a valid email.")
	private String email;

	private String name;

	private String surname;

	private boolean active;

	private List<ProfileDto> profiles = new ArrayList<ProfileDto>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<ProfileDto> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<ProfileDto> profiles) {
		this.profiles = profiles;
	}

}
