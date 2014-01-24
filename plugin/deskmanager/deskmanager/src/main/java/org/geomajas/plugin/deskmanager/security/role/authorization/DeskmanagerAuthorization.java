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

package org.geomajas.plugin.deskmanager.security.role.authorization;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.role.authorization.configuration.DeskmanagerAuthorizationInfo;
import org.geomajas.plugin.deskmanager.service.common.GeodeskConfigurationService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.AuthorizationNeedsWiring;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.service.GeoService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Authorization object for the deskmanager plugin. This object acts as a facade to the different instances of
 * {@link org.geomajas.plugin.deskmanager.security.role.authorization.configuration.DeskmanagerAuthorizationInfo}
 * configured in the applicationContext.
 * 
 * Thise objects get serialized by the cache. Only profile, geodeskId, and deskmanagerAuthorizationInfo get serialized.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class DeskmanagerAuthorization implements BaseAuthorization, AreaAuthorization, AuthorizationNeedsWiring,
		Serializable, DeskmanagerManagementAuthorization, DeskmanagerGeodeskAuthorization {

	private static final long serialVersionUID = 110;

	private static final Logger LOG = LoggerFactory.getLogger(DeskmanagerAuthorizationInfo.class);

	private static Geometry all;
	static {
		try {
			WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));
			all = wktReader.read("POLYGON((-1E20 -1E20,-1E20 1E20,1E20 1E20,1E20 -1E20,-1E20 -1E20))");
		} catch (ParseException pe) {
		}
	}

	// Following fields are serialized thus determine the security key (for caching)
	private Profile profile;

	private String geodeskId;

	private DeskmanagerAuthorizationInfo deskmanagerAuthorizationInfo;

	// End of serialized fields

	private transient Geodesk geodesk;

	private transient ApplicationContext applicationContext;

	private transient GeodeskService geodeskService;

	private transient LayerModelService layerModelService;

	private transient Map<String, DeskmanagerAuthorizationInfo> magdageoAuthorizationInfos;

	private transient GeoService geoService;

	/**
	 * Default constructor for deserialization.
	 */
	public DeskmanagerAuthorization() {
	}

	/**
	 * Construct a Authorization object with the given parameters.
	 * 
	 * @param profile
	 *            the user profile.
	 * @param geodeskId
	 *            the geodesk id this authorization is valid for.
	 * @param applicationContext
	 *            the applicationcontext (for wiring).
	 */
	public DeskmanagerAuthorization(Profile profile, String geodeskId, ApplicationContext applicationContext) {
		this.profile = profile;
		this.geodeskId = geodeskId;
		wire(applicationContext);
	}

	private synchronized DeskmanagerAuthorizationInfo getMagdageoAuthorizationInfo() {
		if (deskmanagerAuthorizationInfo == null) {
			buildMagdageoAuthorizationInfo();
		}
		return deskmanagerAuthorizationInfo;
	}

	private void buildMagdageoAuthorizationInfo() {
		// Set basic authorization info (from configuration)
		deskmanagerAuthorizationInfo = (DeskmanagerAuthorizationInfo) magdageoAuthorizationInfos.get(
				profile.getRole().toString()).clone();

		// Add geodesk specific authorization
		if (geodeskId != null && !RetrieveRolesRequest.MANAGER_ID.equals(geodeskId) && isGeodeskUseAllowed(geodeskId)) {
			try {
				LOG.debug("building magdageoauthorizationinfo");
				Geodesk geodesk = geodeskService.getGeodeskByPublicId(geodeskId);
				ClientApplicationInfo geodeskInfo = applicationContext.getBean(GeodeskConfigurationService.class)
						.createGeodeskConfiguration(geodesk, true);
				// Add all layers visible in this geodesk
				if (geodeskInfo != null) {
					for (ClientMapInfo map : geodeskInfo.getMaps()) {
						for (ClientLayerInfo layer : map.getLayers()) {
							// Add layers if they are public, or the user is not guest.
							if (!profile.getRole().equals(Role.GUEST)
									|| layerModelService.
									getLayerModelByClientLayerIdInternal(layer.getId()).isPublic()) {
								deskmanagerAuthorizationInfo.getVisibleLayersInclude().add(layer.getServerLayerId());
							}
						}
					}
				}
			} catch (GeomajasSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void wire(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.geodeskService = applicationContext.getBean(GeodeskService.class);
		this.magdageoAuthorizationInfos = applicationContext.getBean("security.roles", Map.class);
		this.layerModelService = applicationContext.getBean(LayerModelService.class);
		this.geoService = applicationContext.getBean(GeoService.class);
	}

	@Override
	public String getId() {
		return "DeskmanagerAuthorizationInfo." /* + Integer.toString(role.hashCode()) */;
	}

	@Override
	public boolean isToolAuthorized(String toolId) {
		return check(toolId, getMagdageoAuthorizationInfo().getToolsInclude(), getMagdageoAuthorizationInfo()
				.getToolsExclude());
	}

	@Override
	public boolean isCommandAuthorized(String commandName) {
		return check(commandName, getMagdageoAuthorizationInfo().getCommandsInclude(), getMagdageoAuthorizationInfo()
				.getCommandsExclude());
	}

	@Override
	public boolean isLayerVisible(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getVisibleLayersInclude(), getMagdageoAuthorizationInfo()
				.getVisibleLayersExclude());
	}

	@Override
	public boolean isLayerUpdateAuthorized(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getUpdateAuthorizedLayersInclude(),
				getMagdageoAuthorizationInfo().getUpdateAuthorizedLayersExclude());
	}

	@Override
	public boolean isLayerCreateAuthorized(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getCreateAuthorizedLayersInclude(),
				getMagdageoAuthorizationInfo().getCreateAuthorizedLayersExclude());
	}

	@Override
	public boolean isLayerDeleteAuthorized(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getDeleteAuthorizedLayersInclude(),
				getMagdageoAuthorizationInfo().getDeleteAuthorizedLayersExclude());
	}

	// -- Blueprint --------------------------------------------------------

	@Override
	public boolean readAllowed(BaseGeodesk bp) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		} else if (getRole() == Role.DESK_MANAGER) { // must be able to read blueprints in beheer to get at
														// the
			// layers
			return true;
		}
		return false;
	}

	@Override
	public boolean saveAllowed(BaseGeodesk bp) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteAllowed(BaseGeodesk bp) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		}
		return false;
	}

	@Override
	public Criterion getFilterBlueprints() {
		if (getRole() == Role.ADMINISTRATOR) {
			return null;
		} else if (getRole() == Role.DESK_MANAGER) { // You must add an alias for the groups collection or
														// this
			// won't
			// work!
			return Restrictions.and(Restrictions.eq("active", true),
					Restrictions.eq("groups.id", getTerritory().getId()));
		}
		return Restrictions.sqlRestriction("1 = ?", 2, new IntegerType());
	}

	// -- LayerModel --------------------------------------------------------

	@Override
	public boolean saveAllowed(LayerModel lm) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		} else if (getRole() == Role.DESK_MANAGER) {
			return (getTerritory().equals(lm.getOwner()));
		}
		return false;
	}

	@Override
	public boolean deleteAllowed(LayerModel lm) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		} else if (getRole() == Role.DESK_MANAGER) {
			return (getTerritory().equals(lm.getOwner()));
		}
		return false;
	}

	@Override
	public boolean readAllowed(LayerModel lm) {
		if (getRole() == Role.ADMINISTRATOR || getRole() == Role.DESK_MANAGER) {
			return true;
		}
		return false;
	}

	@Override
	public Criterion getFilterLayerModels() {
		if (getRole() == Role.ADMINISTRATOR) {
			return null;
		} else if (getRole() == Role.DESK_MANAGER) {
			return Restrictions.and(Restrictions.eq("active", true), Restrictions.eq("owner", getTerritory()));
		}
		return Restrictions.sqlRestriction("1 = ?", 2, new IntegerType());
	}

	// -- Geodesk --------------------------------------------------------

	@Override
	public boolean isGeodeskUseAllowed(String geodeskId) {
		return geodeskService.isGeodeskUseAllowed(geodeskId, getRole(), getTerritory());
	}

	@Override
	public boolean readAllowed(Geodesk geodesk) {
		return geodeskService.isGeodeskReadAllowed(geodesk, getRole(), getTerritory());
	}

	@Override
	public boolean saveAllowed(Geodesk geodesk) {
		return geodeskService.isGeodeskSaveAllowed(geodesk, getRole(), getTerritory());
	}

	@Override
	public boolean deleteAllowed(Geodesk geodesk) {
		return geodeskService.isGeodeskDeleteAllowed(geodesk, getRole(), getTerritory());
	}

	@Override
	public Criterion getFilterGeodesks() {
		if (getRole() == Role.ADMINISTRATOR) {
			return null;
		} else if (getRole() == Role.DESK_MANAGER) {
			return Restrictions.eq("owner", getTerritory());
		} else {
			return Restrictions.sqlRestriction("1 = ?", 2, new IntegerType());
		}
	}

	// -- ShapefileUpload -----------------------------------------------

	@Override
	public boolean isShapeFileUploadAllowed(String clientLayerId) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		} else if (getRole() == Role.DESK_MANAGER) {
			try {
				if (clientLayerId != null && !"".equals(clientLayerId)) {
					LayerModel lm = layerModelService.getLayerModelByClientLayerId(clientLayerId);
					if (lm != null) {
						return saveAllowed(lm);
					}
				}
				return true;
			} catch (Exception e) { // not allowed
			}
		}
		return false;
	}

	// -- Convenience methods for check ---------------------------------
	protected boolean check(String id, List<String> includes, List<String> excludes) {
		return check(id, includes) && !check(id, excludes);
	}

	protected boolean check(String id, List<String> includes) {
		if (null != includes) {
			for (String check : includes) {
				if (check(id, check)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean check(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();
	}

	// -- Area---------------------------------

	@Override
	public Geometry getVisibleArea(String layerId) {
		if (!isLayerVisible(layerId)) {
			return null;
		}
		Geometry geometry = null;
		String crs = null;
		if (getGeodesk().mustFilterByUserTerritory()) {
			geometry = getProfile().getTerritory().getGeometry();
			crs = getProfile().getTerritory().getCrs();
		} else if (getGeodesk().mustFilterByCreatorTerritory()) {
			geometry = getGeodesk().getOwner().getGeometry();
			crs = getGeodesk().getOwner().getCrs();
		} else {
			geometry = all;
		}
		Layer<?> layer = (Layer<?>) applicationContext.getBean(layerId);
		if (crs != null && !layer.getLayerInfo().getCrs().equals(crs)) {
			try {
				geometry = geoService.transform(geometry, crs, layer.getLayerInfo().getCrs());
			} catch (GeomajasException e) {
				// cannot happen !
				return null;
			}
		}
		return geometry;
	}

	@Override
	public boolean isPartlyVisibleSufficient(String layerId) {
		return true;
	}

	@Override
	public Geometry getUpdateAuthorizedArea(String layerId) {
		if (!isLayerUpdateAuthorized(layerId)) {
			return null;
		}
		return getVisibleArea(layerId);
	}

	@Override
	public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
		return true;
	}

	@Override
	public Geometry getCreateAuthorizedArea(String layerId) {
		if (!isLayerCreateAuthorized(layerId)) {
			return null;
		}
		return getVisibleArea(layerId);
	}

	@Override
	public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
		return true;
	}

	@Override
	public Geometry getDeleteAuthorizedArea(String layerId) {
		if (!isLayerDeleteAuthorized(layerId)) {
			return null;
		}
		return getVisibleArea(layerId);
	}

	@Override
	public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
		return true;
	}

	// ------ Accessors ------------------

	private Geodesk getGeodesk() {
		if (geodesk == null) {
			try {
				geodesk = geodeskService.getGeodeskByPublicId(geodeskId);
			} catch (GeomajasSecurityException e) {
				e.printStackTrace();
			}
		}
		return geodesk;
	}

	/**
	 * Get the role defined by this authorization.
	 * 
	 * @return the role.
	 */
	public Role getRole() {
		return (profile == null ? null : profile.getRole());
	}

	/**
	 * Get the territory of this authorization.
	 * 
	 * @return
	 */
	public Territory getTerritory() {
		return (profile == null ? null : profile.getTerritory());
	}

	/**
	 * Get the profile of this authorization.
	 * 
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * Get the geodesk id of this authorization.
	 * 
	 * @return
	 */
	public String getGeodeskId() {
		return geodeskId;
	}

}
