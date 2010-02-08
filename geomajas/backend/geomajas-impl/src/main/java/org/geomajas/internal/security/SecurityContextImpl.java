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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.VectorLayerSelectFilterAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	private Logger log = LoggerFactory.getLogger(SecurityManagerImpl.class);

	private List<Authentication> authentications = new ArrayList<Authentication>();

	private String id; // SecurityContext id

	// user info
	private String userId;
	private String userName;
	private Locale userLocale;
	private String userOrganization;
	private String userDivision;

	@Autowired
	private FilterService filterService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

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
				if (authorization instanceof VectorLayerSelectFilterAuthorization) {
					Filter part = ((VectorLayerSelectFilterAuthorization) authorization).getFeatureFilter(layerId);
					if (null != part) {
						filter = combineFilter(filter, part);
					}
				}
			}
		}
		return filter;
	}

	private Filter combineFilter(Filter base, Filter add) {
		if (null == base) {
			return add;
		}
		if (null == add) {
			return base;
		}
		return filterService.createAndFilter(base, add);
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getVisibleArea(final String layerId) {
		return areaCombine(layerId, new AreaCombineGetter() {
			public Geometry get(AreaAuthorization auth) {
				return auth.getVisibleArea(layerId);
			}
		});
	}

	private Geometry areaCombine(String layerId, AreaCombineGetter areaGetter) {
System.out.println("appService"+applicationService);
		VectorLayer layer = applicationService.getVectorLayer(layerId);
System.out.println("layer "+layer);
		if (null == layer) {
			log.error("areaCombine on unknown layer " + layerId);
			return null;
		}
		VectorLayerInfo layerInfo = layer.getLayerInfo();

		// base is the max bounds of the layer
		Envelope maxBounds = converterService.toInternal(layerInfo.getMaxExtent());
		PrecisionModel precisionModel  = new PrecisionModel(PrecisionModel.FLOATING);
		GeometryFactory geometryFactory = new GeometryFactory(precisionModel);
		Geometry geometry = geometryFactory.toGeometry(maxBounds);

		// limit based on authorizations
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof AreaAuthorization) {
					geometry = geometry.intersection(areaGetter.get((AreaAuthorization)authorization));
				}
			}
		}
		return geometry;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyVisibleSufficient(final String layerId) {
		return areaPartlySufficientCombine(new AreaPartlySufficientGetter() {
			public boolean get(AreaAuthorization auth) {
				return auth.isPartlyVisibleSufficient(layerId);
			}
		});
	}

	private boolean areaPartlySufficientCombine(AreaPartlySufficientGetter partlySufficientGetter) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof AreaAuthorization) {
					if (!partlySufficientGetter.get((AreaAuthorization)authorization)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getUpdateAuthorizedArea(final String layerId) {
		return areaCombine(layerId, new AreaCombineGetter() {
			public Geometry get(AreaAuthorization auth) {
				return auth.getUpdateAuthorizedArea(layerId);
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyUpdateAuthorizedSufficient(final String layerId) {
		return areaPartlySufficientCombine(new AreaPartlySufficientGetter() {
			public boolean get(AreaAuthorization auth) {
				return auth.isPartlyUpdateAuthorizedSufficient(layerId);
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getCreateAuthorizedArea(final String layerId) {
		return areaCombine(layerId, new AreaCombineGetter() {
			public Geometry get(AreaAuthorization auth) {
				return auth.getCreateAuthorizedArea(layerId);
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyCreateAuthorizedSufficient(final String layerId) {
		return areaPartlySufficientCombine(new AreaPartlySufficientGetter() {
			public boolean get(AreaAuthorization auth) {
				return auth.isPartlyCreateAuthorizedSufficient(layerId);
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getDeleteAuthorizedArea(final String layerId) {
		return areaCombine(layerId, new AreaCombineGetter() {
			public Geometry get(AreaAuthorization auth) {
				return auth.getDeleteAuthorizedArea(layerId);
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyDeleteAuthorizedSufficient(final String layerId) {
		return areaPartlySufficientCombine(new AreaPartlySufficientGetter() {
			public boolean get(AreaAuthorization auth) {
				return auth.isPartlyDeleteAuthorizedSufficient(layerId);
			}
		});
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
	public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
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
	public boolean isAttributeReadable(String layerId, InternalFeature feature, String attributeName) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * @inheritDoc
	 */
	public boolean isAttributeWritable(String layerId, InternalFeature feature, String attributeName) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Interface to unify/generalise the different areas.
	 */
	private interface AreaCombineGetter {

		/**
		 * Get the area which needs to be combined.
		 *
		 * @param auth authorization object to get data from
		 * @return area to combine
		 */
		Geometry get(AreaAuthorization auth);
	}

	/**
	 * Interface to unify/generalise the different areas.
	 */
	private interface AreaPartlySufficientGetter {

		/**
		 * Get the "partly sufficient" status.
		 *
		 * @param auth authorization object to get data from
		 * @return true when part in area is sufficient
		 */
		boolean get(AreaAuthorization auth);
	}
}
