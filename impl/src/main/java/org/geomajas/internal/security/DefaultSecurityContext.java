/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.AttributeAuthorization;
import org.geomajas.security.Authentication;
import org.geomajas.security.AuthorizationNeedsWiring;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.FeatureAuthorization;
import org.geomajas.security.SavedAuthentication;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.VectorLayerSelectFilterAuthorization;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * {@link org.geomajas.security.SecurityContext} implementation.
 * <p/>
 * The security context is a thread scoped service which allows you to query the authorization details for the logged in
 * user.
 * 
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultSecurityContext implements SecurityContext {

	private final Logger log = LoggerFactory.getLogger(DefaultSecurityContext.class);

	private final List<Authentication> authentications = new ArrayList<Authentication>();

	private String token;

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
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Default constructor.
	 *
	 * @since 1.10.0
	 */
	@Api
	public DefaultSecurityContext() {
		// method provided to be able to put @Api annotation on it
	}

	/**
	 * Set the token and authentications for this security context.
	 * <p/>
	 * This method can be overwritten to handle custom policies.
	 *
	 * @param token current token
	 * @param authentications authentications for token
	 */
	@Api
	public void setAuthentications(String token, List<Authentication> authentications) {
		this.token = token;
		this.authentications.clear();
		if (null != authentications) {
			for (Authentication auth : authentications) {
				for (BaseAuthorization ba : auth.getAuthorizations()) {
					if (ba instanceof AuthorizationNeedsWiring) {
						((AuthorizationNeedsWiring) ba).wire(applicationContext);
					}
				}
				this.authentications.add(auth);
			}
		}
		userInfoInit();
	}

	/**
	 * @inheritDoc
	 */
	public List<Authentication> getSecurityServiceResults() {
		return authentications;
	}

	public String getToken() {
		return token;
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
					if (null != auth.getUserLocale() &&
							(null == userLocale || !userLocale.equals(auth.getUserLocale()))) {
						userLocale = null;
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
	 * @param org
	 *            base string to append to (avoiding duplication).
	 * @param add
	 *            string to add
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
		if (isLayerUpdateCapable(layerId)) {
			for (Authentication authentication : authentications) {
				for (BaseAuthorization authorization : authentication.getAuthorizations()) {
					if (authorization.isLayerUpdateAuthorized(layerId)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLayerCreateAuthorized(String layerId) {
		if (isLayerCreateCapable(layerId)) {
			for (Authentication authentication : authentications) {
				for (BaseAuthorization authorization : authentication.getAuthorizations()) {
					if (authorization.isLayerCreateAuthorized(layerId)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLayerDeleteAuthorized(String layerId) {
		if (isLayerUpdateCapable(layerId)) {
			for (Authentication authentication : authentications) {
				for (BaseAuthorization authorization : authentication.getAuthorizations()) {
					if (authorization.isLayerDeleteAuthorized(layerId)) {
						return true;
					}
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
		return areaCombine(layerId, new VisibleAreaCombineGetter(layerId));
	}

	private Geometry areaCombine(String layerId, AreaCombineGetter areaGetter) {
		if (null == authentications || authentications.size() == 0) {
			// no authorizations, so nothing should be allowed
			return null;
		}

		Layer<?> layer = configurationService.getLayer(layerId);
		if (null == layer) {
			log.error("areaCombine on unknown layer " + layerId);
			return null;
		}
		LayerInfo layerInfo = layer.getLayerInfo();

		// base is the max bounds of the layer
		Envelope maxBounds = converterService.toInternal(layerInfo.getMaxExtent());
		PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
		int srid = geoService.getSridFromCrs(layer.getLayerInfo().getCrs());
		GeometryFactory geometryFactory = new GeometryFactory(precisionModel, srid);
		Geometry geometry = geometryFactory.toGeometry(maxBounds);

		// limit based on authorizations
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof AreaAuthorization) {
					geometry = geometry.intersection(areaGetter.get((AreaAuthorization) authorization));
				}
			}
		}
		geometry.setSRID(srid); // force srid, even when not set correctly by security service
		return geometry;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyVisibleSufficient(final String layerId) {
		return areaPartlySufficientCombine(new PartlyVisibleAuthorizationGetter(layerId));
	}

	private boolean areaPartlySufficientCombine(AuthorizationGetter<AreaAuthorization> partlySufficientGetter) {
		boolean res = false;
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof AreaAuthorization) {
					if (!partlySufficientGetter.get((AreaAuthorization) authorization)) {
						return false;
					}
					res = true;
				}
			}
		}
		return res;
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getUpdateAuthorizedArea(final String layerId) {
		if (!isLayerUpdateCapable(layerId)) {
			return null;
		}
		return areaCombine(layerId, new UpdateAreaCombineGetter(layerId));
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyUpdateAuthorizedSufficient(final String layerId) {
		return areaPartlySufficientCombine(new PartlyUpdateAuthorizedAuthorizationGetter(layerId));
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getCreateAuthorizedArea(final String layerId) {
		if (!isLayerCreateCapable(layerId)) {
			return null;
		}
		return areaCombine(layerId, new CreateAreaCombineGetter(layerId));
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyCreateAuthorizedSufficient(final String layerId) {
		return areaPartlySufficientCombine(new PartlyCreateAuthorizedAuthorizationGetter(layerId));
	}

	/**
	 * @inheritDoc
	 */
	public Geometry getDeleteAuthorizedArea(final String layerId) {
		if (!isLayerDeleteCapable(layerId)) {
			return null;
		}
		return areaCombine(layerId, new DeleteAreaCombineGetter(layerId));
	}

	/**
	 * @inheritDoc
	 */
	public boolean isPartlyDeleteAuthorizedSufficient(final String layerId) {
		return areaPartlySufficientCombine(new PartlyDeleteAuthorizedAuthorizationGetter(layerId));
	}

	/**
	 * @inheritDoc
	 */
	public boolean isFeatureVisible(final String layerId, final InternalFeature feature) {
		return policyCombine(new AuthorizationGetter<BaseAuthorization>() {

			public boolean get(BaseAuthorization auth) {
				if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureVisible(layerId, feature);
				} else {
					return auth.isLayerVisible(layerId);
				}
			}
		});
	}

	private boolean policyCombine(AuthorizationGetter<BaseAuthorization> auth) {
		for (Authentication authentication : authentications) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (auth.get(authorization)) {
					return true;
				}
			}
		}
		return false;
	}

	/** @inheritDoc */
	public boolean isFeatureUpdateAuthorized(final String layerId, final InternalFeature feature) {
		return isLayerUpdateCapable(layerId) && policyCombine(new AuthorizationGetter<BaseAuthorization>() {
			public boolean get(BaseAuthorization auth) {
				if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureUpdateAuthorized(layerId, feature);
				} else {
					return auth.isLayerUpdateAuthorized(layerId);
				}
			}
		});
	}

	/** @inheritDoc */
	public boolean isFeatureUpdateAuthorized(final String layerId, final InternalFeature orgFeature,
			final InternalFeature newFeature) {
		return isLayerUpdateCapable(layerId) && policyCombine(new AuthorizationGetter<BaseAuthorization>() {
			public boolean get(BaseAuthorization auth) {
				if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureUpdateAuthorized(layerId, orgFeature, newFeature);
				} else {
					return auth.isLayerUpdateAuthorized(layerId);
				}
			}
		});
	}

	/** @inheritDoc */
	public boolean isFeatureDeleteAuthorized(final String layerId, final InternalFeature feature) {
		return isLayerDeleteCapable(layerId) && policyCombine(new AuthorizationGetter<BaseAuthorization>() {
			public boolean get(BaseAuthorization auth) {
				if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureDeleteAuthorized(layerId, feature);
				} else {
					return auth.isLayerDeleteAuthorized(layerId);
				}
			}
		});
	}

	/** @inheritDoc */
	public boolean isFeatureCreateAuthorized(final String layerId, final InternalFeature feature) {
		return isLayerCreateCapable(layerId) && policyCombine(new AuthorizationGetter<BaseAuthorization>() {
			public boolean get(BaseAuthorization auth) {
				if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureCreateAuthorized(layerId, feature);
				} else {
					return auth.isLayerCreateAuthorized(layerId);
				}
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public boolean isAttributeReadable(final String layerId, final InternalFeature feature,
			final String attributeName) {
		return policyCombine(new AuthorizationGetter<BaseAuthorization>() {

			public boolean get(BaseAuthorization auth) {
				if (auth instanceof AttributeAuthorization) {
					return ((AttributeAuthorization) auth).isAttributeReadable(layerId, feature, attributeName);
				} else if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureVisible(layerId, feature);
				} else {
					return auth.isLayerVisible(layerId);
				}
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public boolean isAttributeWritable(final String layerId, final InternalFeature feature,
			final String attributeName) {
		return isLayerUpdateCapable(layerId) && policyCombine(new AuthorizationGetter<BaseAuthorization>() {
			public boolean get(BaseAuthorization auth) {
				if (auth instanceof AttributeAuthorization) {
					return ((AttributeAuthorization) auth).isAttributeWritable(layerId, feature, attributeName);
				} else if (auth instanceof FeatureAuthorization) {
					return ((FeatureAuthorization) auth).isFeatureUpdateAuthorized(layerId, feature);
				} else {
					return auth.isLayerUpdateAuthorized(layerId);
				}
			}
		});
	}

	private boolean isLayerUpdateCapable(String layerId) {
		if (null == configurationService) {
			return true; // for testing, when there is no spring context
		}
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		return null != layer && layer.isUpdateCapable();
	}

	private boolean isLayerCreateCapable(String layerId) {
		if (null == configurationService) {
			return true; // for testing, when there is no spring context
		}
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		return null != layer && layer.isCreateCapable();
	}

	private boolean isLayerDeleteCapable(String layerId) {
		if (null == configurationService) {
			return true; // for testing, when there is no spring context
		}
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		return null != layer && layer.isDeleteCapable();
	}

	/** @inheritDoc */
	public SavedAuthorization getSavedAuthorization() {
		return new SavedAuthorizationImpl(this);
	}

	/**
	 * Restore authentications from persisted state.
	 *
	 * @param savedAuthorization saved authorizations
	 */
	@Api
	public void restoreSecurityContext(SavedAuthorization savedAuthorization) {
		List<Authentication> auths = new ArrayList<Authentication>();
		if (null != savedAuthorization) {
			for (SavedAuthentication sa : savedAuthorization.getAuthentications()) {
				Authentication auth = new Authentication();
				auth.setSecurityServiceId(sa.getSecurityServiceId());
				auth.setAuthorizations(sa.getAuthorizations());
				auths.add(auth);
			}
		}
		setAuthentications(null, auths);
		userInfoInit();
	}

	/**
	 * Interface to unify/generalise the different areas.
	 */
	private interface AreaCombineGetter {

		/**
		 * Get the area which needs to be combined.
		 *
		 * @param auth
		 *            authorization object to get data from
		 * @return area to combine
		 */
		Geometry get(AreaAuthorization auth);
	}

	/**
	 * Interface to unify/generalise the different areas.
	 */
	private interface AuthorizationGetter<AUTH extends BaseAuthorization> {

		/**
		 * Get the "partly sufficient" status.
		 *
		 * @param auth
		 *            authorization object to get data from
		 * @return true when part in area is sufficient
		 */
		boolean get(AUTH auth);
	}

	/**
	 * Visible area combine getter for DefaultSecurityContext.
	 */
	private final class VisibleAreaCombineGetter implements AreaCombineGetter {

		private final String layerId;

		private VisibleAreaCombineGetter(String layerId) {
			this.layerId = layerId;
		}

		public Geometry get(AreaAuthorization auth) {
			return auth.getVisibleArea(layerId);
		}
	}

	/**
	 * Update area combine getter for DefaultSecurityContext.
	 */
	private final class UpdateAreaCombineGetter implements AreaCombineGetter {

		private final String layerId;

		private UpdateAreaCombineGetter(String layerId) {
			this.layerId = layerId;
		}

		public Geometry get(AreaAuthorization auth) {
			return auth.getUpdateAuthorizedArea(layerId);
		}
	}

	/**
	 * Create area combine getter for DefaultSecurityContext.
	 */
	private final class CreateAreaCombineGetter implements AreaCombineGetter {

		private final String layerId;

		private CreateAreaCombineGetter(String layerId) {
			this.layerId = layerId;
		}

		public Geometry get(AreaAuthorization auth) {
			return auth.getCreateAuthorizedArea(layerId);
		}
	}

	/**
	 * Delete area combine getter for DefaultSecurityContext.
	 */
	private final class DeleteAreaCombineGetter implements AreaCombineGetter {

		private final String layerId;

		private DeleteAreaCombineGetter(String layerId) {
			this.layerId = layerId;
		}

		public Geometry get(AreaAuthorization auth) {
			return auth.getDeleteAuthorizedArea(layerId);
		}
	}

	/**
	 * Partly visible authorization getter for DefaultSecurityContext.
	 */
	private final class PartlyVisibleAuthorizationGetter implements AuthorizationGetter<AreaAuthorization> {

		private final String layerId;

		private PartlyVisibleAuthorizationGetter(String layerId) {
			this.layerId = layerId;
		}

		public boolean get(AreaAuthorization auth) {
			return auth.isPartlyVisibleSufficient(layerId);
		}
	}

	/**
	 * Partly update authorization getter for DefaultSecurityContext.
	 */
	private final class PartlyUpdateAuthorizedAuthorizationGetter implements AuthorizationGetter<AreaAuthorization> {

		private final String layerId;

		private PartlyUpdateAuthorizedAuthorizationGetter(String layerId) {
			this.layerId = layerId;
		}

		public boolean get(AreaAuthorization auth) {
			return auth.isPartlyUpdateAuthorizedSufficient(layerId);
		}
	}

	/**
	 * Partly delete authorization getter for DefaultSecurityContext.
	 */
	private final class PartlyDeleteAuthorizedAuthorizationGetter implements AuthorizationGetter<AreaAuthorization> {

		private final String layerId;

		private PartlyDeleteAuthorizedAuthorizationGetter(String layerId) {
			this.layerId = layerId;
		}

		public boolean get(AreaAuthorization auth) {
			return auth.isPartlyDeleteAuthorizedSufficient(layerId);
		}
	}

	/**
	 * Partly create authorization getter for DefaultSecurityContext.
	 */
	private final class PartlyCreateAuthorizedAuthorizationGetter implements AuthorizationGetter<AreaAuthorization> {

		private final String layerId;

		private PartlyCreateAuthorizedAuthorizationGetter(String layerId) {
			this.layerId = layerId;
		}

		public boolean get(AreaAuthorization auth) {
			return auth.isPartlyCreateAuthorizedSufficient(layerId);
		}
	}
}
