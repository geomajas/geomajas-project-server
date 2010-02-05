/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.security;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityContext;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@link org.geomajas.security.SecurityContext} implementation.
 * <p/>
 * The security context is a thread scoped service which allows you to query the authorization details for the
 * logged in user.
 *
 * @author Joachim Van der Auwera
 */
@Component
@Scope("thread")
public class SecurityContextImpl implements SecurityContext {

	private List<Authentication> authentications = new ArrayList<Authentication>();

	private String id; // SecurityContext id

	// user info
	private String userId;
	private String userName;
	private Locale userLocale;
	private String userOrganization;
	private String userDivision;

	void setAuthentications(List<Authentication> authentications) {
		this.authentications.clear();
		if (null != authentications) {
			this.authentications.addAll(authentications);
		}
		userInfoInit();
	}

	/**
	 * @inheritDoc
	 */
	public List<Authentication> getSecurityServiceResults() {
		return authentications;
	}

	/**
	 * @inheritDoc
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @inheritDoc
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @inheritDoc
	 */
	public Locale getUserLocale() {
		return userLocale;
	}

	/**
	 * @inheritDoc
	 */
	public String getUserOrganization() {
		return userOrganization;
	}

	/**
	 * @inheritDoc
	 */
	public String getUserDivision() {
		return userDivision;
	}

	/**
	 * Calculate UserInfo strings.
	 */
	private void userInfoInit() {
		boolean first = true;
		userId = null;
		userLocale = null;
		userName = null;
		userOrganization = null;
		userDivision = null;
		if (null != authentications) {
			for (Authentication auth : authentications) {
				userId = combine(userId, auth.getUserId());
				userName = combine(userName, auth.getUserName());
				if (first) {
					userLocale = auth.getUserLocale();
					first = false;
				} else {
					if (null != auth.getUserLocale()) {
						if (null == userLocale || !userLocale.equals(auth.getUserLocale())) {
							userLocale = null;
						}
					}
				}
				userOrganization = combine(userOrganization, auth.getUserOrganization());
				userDivision = combine(userDivision, auth.getUserDivision());
			}
		}

		// now calculate the "id" for this context, this should be independent of the data order, so sort
		Map<String, List<String>> idParts = new HashMap<String, List<String>>();
		if (null != authentications) {
			for (Authentication auth : authentications) {
				List<String> auths = new ArrayList<String>();
				for (BaseAuthorization ba : auth.getAuthorizations()) {
					auths.add(ba.getId());
				}
				Collections.sort(auths);
				idParts.put(auth.getSecurityServiceId(), auths);
			}
		}
		StringBuilder sb = new StringBuilder();
		List<String> sortedKeys = new ArrayList<String>(idParts.keySet());
		Collections.sort(sortedKeys);
		for (String key : sortedKeys) {
			if (sb.length() > 0) {
				sb.append('|');
			}
			List<String> auths = idParts.get(key);
			first = true;
			for (String ak : auths) {
				if (first) {
					first = false;
				} else {
					sb.append('|');
				}
				sb.append(ak);
			}
			sb.append('@');
			sb.append(key);
		}
		id = sb.toString();
	}

	/**
	 * Combine user information strings.
	 * <p/>
	 * Extra information is appended (separated by a comma) if not yet present in the string.
	 *
	 * @param org base string to append to (avoiding duplication).
	 * @param add string to add
	 * @return org + ", " + add
	 */
	private String combine(String org, String add) {
		if (null == org) {
			return add;
		}
		if (null == add || org.equals(add) || org.startsWith(add + ", ") || org.endsWith(", " + add)) {
			return org;
		}
		return org + ", " + add;
	}

	/**
	 * @inheritDoc
	 */
	public String getId() {
		return id;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isToolAuthorized(String toolId) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isToolAuthorized(toolId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isCommandAuthorized(String commandName) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isCommandAuthorized(commandName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLayerVisible(String layerId) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isLayerVisible(layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLayerUpdateAuthorized(String layerId) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isLayerUpdateAuthorized(layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLayerCreateAuthorized(String layerId) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isLayerCreateAuthorized(layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLayerDeleteAuthorized(String layerId) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isLayerDeleteAuthorized(layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public Filter getFeatureFilter(String layerId) {
		Filter filter = null;
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization.isLayerDeleteAuthorized(layerId)) {
					return null;
				}
			}
		}
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isAttributeReadable(String layerId, InternalFeature feature, String attributeName) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isFeatureVisible(String layerId, InternalFeature feature) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getVisibleArea(String layerId, CoordinateReferenceSystem crs) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyVisibleSufficient(String layerId) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isAttributeWritable(String layerId, InternalFeature feature, String attributeName) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getUpdateAuthorizedArea(String layerId, CoordinateReferenceSystem crs) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature, InternalFeature newFeature) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getCreateAuthorizedArea(String layerId, CoordinateReferenceSystem crs) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getDeleteAuthorizedArea(String layerId, CoordinateReferenceSystem crs) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
