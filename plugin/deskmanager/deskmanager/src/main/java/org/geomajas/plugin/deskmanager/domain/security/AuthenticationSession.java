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
package org.geomajas.plugin.deskmanager.domain.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Session information.
 *
 * @author Jan Venstermans
 */
@Entity
@Table(name = "gdm_session_authentication")
public class AuthenticationSession {

	@Id
	private String authenticationSessionToken;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "expiration_time", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date expirationTime;

	/**
	 * Default constructor.
	 */
	protected AuthenticationSession() {
	}

	/**
	 * Constructor with all required fields.
	 *
	 * @param authenticationSessionToken
	 * @param user
	 * @param expirationTime
	 */
	public AuthenticationSession(String authenticationSessionToken, User user, Date expirationTime) {
		this.authenticationSessionToken = authenticationSessionToken;
		this.user = user;
		this.expirationTime = expirationTime;
	}

	public String getAuthenticationSessionToken() {
		return authenticationSessionToken;
	}

	public void setAuthenticationSessionToken(String authenticationSessionToken) {
		this.authenticationSessionToken = authenticationSessionToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * Equality is based on the restriction unique user/group/role combination.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AuthenticationSession that = (AuthenticationSession) o;

		if (that.authenticationSessionToken == null ||
				!authenticationSessionToken.equals(that.authenticationSessionToken)) {
			return false;
		}
		if (that.expirationTime == null || expirationTime.equals(that.expirationTime)) {
			return false;
		}
		if (that.user == null || user.getId() != that.user.getId()) {
			return false;
		}

		return true;
	}

	/**
	 * Hashing is based on the restriction unique user/group/role combination.
	 */
	@Override
	public int hashCode() {
		int result = authenticationSessionToken.hashCode();
		result = 31 * result + user.getId().hashCode();
		result = 31 * result + expirationTime.hashCode();
		return result;
	}

}
