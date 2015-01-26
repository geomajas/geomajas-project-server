/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SavedAuthentication;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This SavedAuthentication class is a placeholder for the {@link org.geomajas.security.Authorization}s and the
 * providing {@link org.geomajas.security.SecurityService}. It does not contains actual authentication or user
 * information.
 *
 * @author Joachim Van der Auwera
 */
public class SavedAuthenticationImpl implements SavedAuthentication {

	private static final long serialVersionUID = 190L;

	private static final int PRIME = 31;

	private String securityServiceId;

	private byte[][] authorizations; // serialized authorizations

	/**
	 * Get the id of the {@link org.geomajas.security.SecurityService} which created this authentication.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself.
	 *
	 * @return id of the security service which created this authentication
	 */
	public String getSecurityServiceId() {
		return securityServiceId;
	}

	/**
	 * Set the id of the {@link org.geomajas.security.SecurityService} which created this authentication.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself.
	 *
	 * @param securityServiceId id of the security service which created this authentication
	 */
	public void setSecurityServiceId(String securityServiceId) {
		this.securityServiceId = securityServiceId;
	}

	/**
	 * Get array of authorizations which apply for the user. In many cases there is one object for each role.
	 * A union is used to combine these authorizations (and any other which may apply for the authentication token).
	 *
	 * @return array of {@link org.geomajas.security.BaseAuthorization} objects
	 */
	public BaseAuthorization[] getAuthorizations() {
		BaseAuthorization[] res = new BaseAuthorization[authorizations.length];
		try {
			for (int i = 0; i < authorizations.length; i++) {
				ByteArrayInputStream bais = new ByteArrayInputStream(authorizations[i]);
				JBossObjectInputStream deserialize = new JBossObjectInputStream(bais);
				Object obj = deserialize.readObject();
				res[i] = (BaseAuthorization) obj;
			}
		} catch (ClassNotFoundException cnfe) {
			Logger log = LoggerFactory.getLogger(SavedAuthenticationImpl.class);
			log.error("Can not deserialize object, may cause rights to be lost.", cnfe);
			res = new BaseAuthorization[0]; // assure empty list, otherwise risk of NPE
		} catch (IOException ioe) {
			Logger log = LoggerFactory.getLogger(SavedAuthenticationImpl.class);
			log.error("Can not deserialize object, may cause rights to be lost.", ioe);
			res = new BaseAuthorization[0]; // assure empty list, otherwise risk of NPE
		}
		return res;
	}

	/**
	 * Set the {@link org.geomajas.security.Authorization}s which apply for this authentication.
	 * They are included here in serialized form.
	 *
	 * @param authorizations array of authentications
	 */
	public void setAuthorizations(BaseAuthorization[] authorizations) {
		BaseAuthorization ba = null;
		try {
			this.authorizations = new byte[authorizations.length][];
			for (int i = 0; i < authorizations.length; i++) {
				ba = authorizations[i];
				ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
				JBossObjectOutputStream serialize = new JBossObjectOutputStream(baos);
				serialize.writeObject(ba);
				serialize.flush();
				serialize.close();
				this.authorizations[i] = baos.toByteArray();
			}
		} catch (IOException ioe) {
			Logger log = LoggerFactory.getLogger(SavedAuthenticationImpl.class);
			log.error("Could not serialize " + ba + ", may cause rights to be lost.", ioe);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SavedAuthenticationImpl)) {
			return false;
		}

		SavedAuthenticationImpl that = (SavedAuthenticationImpl) o;

		return equalNestedByteArray(authorizations, that.authorizations) &&
				!(securityServiceId != null ? !securityServiceId.equals(that.securityServiceId) :
						that.securityServiceId != null);

	}

	private boolean equalNestedByteArray(byte[][] a1, byte[][] a2) {
		if (a1 == a2) {
			return true;
		}
		if (a1 == null || a2 == null) {
			return false;
		}

		int length = a1.length;
		if (a2.length != length) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			byte[] o1 = a1[i];
			byte[] o2 = a2[i];
			if (!(o1 == null ? o2 == null : equalByteArray(o1, o2))) {
				return false;
			}
		}

		return true;
	}

	private boolean equalByteArray(byte[] a1, byte[] a2) {
		if (a1 == a2) {
			return true;
		}
		if (a1 == null || a2 == null) {
			return false;
		}

		int length = a1.length;
		if (a2.length != length) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			if (a1[i] != a2[i]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = securityServiceId != null ? securityServiceId.hashCode() : 0;
		result = PRIME * result + (authorizations != null ? hashCodeNestedByteArray(authorizations) : 0);
		return result;
	}

	private int hashCodeNestedByteArray(byte[][] array) {
		if (array == null) {
			return 0;
		}

		int result = 1;

		for (byte[] element : array) {
			result = PRIME * result + (element == null ? 0 : hashCodeByteArray(element));
		}

		return result;

	}

	public int hashCodeByteArray(byte[] array) {
		if (array == null) {
			return 0;
		}

		int result = 1;

		for (byte element : array) {
			result = PRIME * result + element;
		}

		return result;
	}

}
