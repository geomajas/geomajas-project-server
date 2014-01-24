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

package org.geomajas.internal.security;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.security.Authentication;
import org.geomajas.security.SavedAuthentication;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;

/**
 * Class to cache security context information. Should allow to safely cache and restore the security context. This
 * will only work if all authentications are effectively serializable. Equality condition is currently weak and
 * depends on cache id. No deep copy of authentications, so we assume immutability of the data.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
public class SavedAuthorizationImpl implements SavedAuthorization {

	private static final long serialVersionUID = 190L;

	private final List<SavedAuthentication> authentications = new ArrayList<SavedAuthentication>();

	public SavedAuthorizationImpl(SecurityContext context) {
		if (null != context) {
			for (Authentication org : context.getSecurityServiceResults()) {
				SavedAuthentication sa = new SavedAuthenticationImpl();
				sa.setSecurityServiceId(org.getSecurityServiceId());
				sa.setAuthorizations(org.getAuthorizations());
				authentications.add(sa);
			}
		}
	}

	public List<SavedAuthentication> getAuthentications() {
		return authentications;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SavedAuthorizationImpl)) {
			return false;
		}

		SavedAuthorizationImpl that = (SavedAuthorizationImpl) o;

		return !(authentications != null ? !authentications.equals(that.authentications) :
				that.authentications != null);
	}

	@Override
	public int hashCode() {
		return authentications.hashCode();
	}
}
