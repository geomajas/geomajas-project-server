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
package org.geomajas.plugin.deskmanager.security;

import org.geomajas.plugin.deskmanager.domain.security.Profile;

import java.util.Date;
import java.util.List;

/**
 * Session information.
 *
 * @author Jan Venstermans
 */
public class LoginSession {

	private static long defaultExpirationTime = 1 * 24 * 60 * 60 * 1000; //default one day

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	private List<Profile> profiles;

	private Date expire;

	public LoginSession(List<Profile> profiles) {
		 this(profiles, null);
	}

	public LoginSession(List<Profile> profiles, Date expire) {
		setProfiles(profiles);
		setExpire(expire != null ? expire : new Date(new Date().getTime() + defaultExpirationTime));
	}

	public boolean isExpired() {
		return getExpire().before(new Date());
	}

	public void setSessionExpired() {
		setExpire(new Date(0));
	}

	public static long getDefaultExpirationTime() {
		return defaultExpirationTime;
	}

	public static void setDefaultExpirationTime(long defaultExpirationTime) {
		LoginSession.defaultExpirationTime = defaultExpirationTime;
	}
}
