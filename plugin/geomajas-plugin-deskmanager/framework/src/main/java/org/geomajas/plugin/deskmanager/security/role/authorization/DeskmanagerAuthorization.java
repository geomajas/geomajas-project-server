/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
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
 * Authorization object for the deskmanager plugin. This object acts as a facade to the FIXME
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 * 
 */
public class DeskmanagerAuthorization implements BaseAuthorization, AreaAuthorization, AuthorizationNeedsWiring,
		Serializable, DeskmanagerManagementAuthorization, DeskmanagerGeodeskAuthorization {

	private static final long serialVersionUID = 110;

	private final Logger log = LoggerFactory.getLogger(DeskmanagerAuthorizationInfo.class);

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
	public DeskmanagerAuthorization() { }

	public DeskmanagerAuthorization(Profile profile, String geodeskId, ApplicationContext applicationContext) {
		this.profile = profile;
		this.geodeskId = geodeskId;
		wire(applicationContext);
	}

	// Will this be done in multiple threads, I don't think so...
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
		if (geodeskId != null && isGeodeskUseAllowed(geodeskId)) {
			try {
				log.debug("building magdageoauthorizationinfo");
				Geodesk geodesk = geodeskService.getGeodeskByPublicId(geodeskId);
				ClientApplicationInfo geodeskInfo = applicationContext.getBean(GeodeskConfigurationService.class)
						.createGeodeskConfiguration(geodesk, true);
				// Add all layers visible in this geodesk
				if (geodeskInfo != null) {
					for (ClientMapInfo map : geodeskInfo.getMaps()) {
						for (ClientLayerInfo layer : map.getLayers()) {
							// TODO: Zouden we hier niet beter nog een extra check doen op publiek/private? --> if rol =
							// publiek, enkel publieke lagen toevoegen
							deskmanagerAuthorizationInfo.getVisibleLayersInclude().add(layer.getServerLayerId());
						}
					}
				}
			} catch (GeomajasSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void wire(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.geodeskService = applicationContext.getBean(GeodeskService.class);
		this.magdageoAuthorizationInfos = applicationContext.getBean("security.roles", Map.class);
		this.layerModelService = applicationContext.getBean(LayerModelService.class);
		this.geoService = applicationContext.getBean(GeoService.class);
	}

	public String getId() {
		return "DeskmanagerAuthorizationInfo." /* + Integer.toString(role.hashCode()) */;
	}

	public boolean isToolAuthorized(String toolId) {
		return check(toolId, getMagdageoAuthorizationInfo().getToolsInclude(), getMagdageoAuthorizationInfo()
				.getToolsExclude());
	}

	public boolean isCommandAuthorized(String commandName) {
		return check(commandName, getMagdageoAuthorizationInfo().getCommandsInclude(), getMagdageoAuthorizationInfo()
				.getCommandsExclude());
	}

	public boolean isLayerVisible(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getVisibleLayersInclude(), getMagdageoAuthorizationInfo()
				.getVisibleLayersExclude());
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getUpdateAuthorizedLayersInclude(),
				getMagdageoAuthorizationInfo().getUpdateAuthorizedLayersExclude());
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getCreateAuthorizedLayersInclude(),
				getMagdageoAuthorizationInfo().getCreateAuthorizedLayersExclude());
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return check(layerId, getMagdageoAuthorizationInfo().getDeleteAuthorizedLayersInclude(),
				getMagdageoAuthorizationInfo().getDeleteAuthorizedLayersExclude());
	}

	// -- Blueprint --------------------------------------------------------

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

	public boolean saveAllowed(BaseGeodesk bp) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		}
		return false;
	}

	public boolean deleteAllowed(BaseGeodesk bp) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		}
		return false;
	}

	public Criterion getFilterBlueprints() {
		if (getRole() == Role.ADMINISTRATOR) {
			return null;
		} else if (getRole() == Role.DESK_MANAGER) { // You must add an alias for the groups collection or
														// this
			// won't
			// work!
			return Restrictions.and(Restrictions.eq("active", true), Restrictions.eq("groups.id", getGroup().getId()));
		}
		return Restrictions.sqlRestriction("1 = ?", 2, new IntegerType());
	}

	// -- LayerModel --------------------------------------------------------

	// save enkel eigen
	public boolean saveAllowed(LayerModel lm) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		} else if (getRole() == Role.DESK_MANAGER) {
			return (getGroup().equals(lm.getOwner()));
		}
		return false;
	}

	// delete enkel eigen
	public boolean deleteAllowed(LayerModel lm) {
		if (getRole() == Role.ADMINISTRATOR) {
			return true;
		} else if (getRole() == Role.DESK_MANAGER) {
			return (getGroup().equals(lm.getOwner()));
		}
		return false;
	}

	// read beheerders alles (hangt af van blueprint)
	public boolean readAllowed(LayerModel lm) {
		if (getRole() == Role.ADMINISTRATOR || getRole() == Role.DESK_MANAGER) {
			return true;
		}
		return false;
	}

	public Criterion getFilterLayerModels() {
		if (getRole() == Role.ADMINISTRATOR) {
			return null;
		} else if (getRole() == Role.DESK_MANAGER) {
			return Restrictions.and(Restrictions.eq("active", true), Restrictions.eq("owner", getGroup()));
		}
		return Restrictions.sqlRestriction("1 = ?", 2, new IntegerType());
	}

	// -- Geodesk --------------------------------------------------------

	public boolean isGeodeskUseAllowed(String geodeskId) {
		return geodeskService.isGeodeskUseAllowed(geodeskId, getRole(), getGroup());
	}

	public boolean readAllowed(Geodesk geodesk) {
		return geodeskService.isGeodeskReadAllowed(geodesk, getRole(), getGroup());
	}

	public boolean saveAllowed(Geodesk geodesk) {
		return geodeskService.isGeodeskSaveAllowed(geodesk, getRole(), getGroup());
	}

	public boolean deleteAllowed(Geodesk geodesk) {
		return geodeskService.isGeodeskDeleteAllowed(geodesk, getRole(), getGroup());
	}

	public Criterion getFilterLoketten() {
		if (getRole() == Role.ADMINISTRATOR) {
			return null;
		} else if (getRole() == Role.DESK_MANAGER) {
			return Restrictions.eq("owner", getGroup());
		} else {
			return Restrictions.sqlRestriction("1 = ?", 2, new IntegerType());
		}
	}

	// -- ShapefileUpload -----------------------------------------------

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

	public Geometry getVisibleArea(String layerId) {
		if (!isLayerVisible(layerId)) {
			return null;
		}
		Geometry geometry = null;
		String crs = null;
		if (getLoket().mustFilterByUserTerritory()) {
			geometry = getProfile().getTerritory().getGeometry();
			crs = getProfile().getTerritory().getCrs();
		} else if (getLoket().mustFilterByCreatorTerritory()) {
			geometry = getLoket().getOwner().getGeometry();
			crs = getLoket().getOwner().getCrs();
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

	public boolean isPartlyVisibleSufficient(String layerId) {
		return true;
	}

	public Geometry getUpdateAuthorizedArea(String layerId) {
		if (!isLayerUpdateAuthorized(layerId)) {
			return null;
		}
		return getVisibleArea(layerId);
	}

	public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
		return true;
	}

	public Geometry getCreateAuthorizedArea(String layerId) {
		if (!isLayerCreateAuthorized(layerId)) {
			return null;
		}
		return getVisibleArea(layerId);
	}

	public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
		return true;
	}

	public Geometry getDeleteAuthorizedArea(String layerId) {
		if (!isLayerDeleteAuthorized(layerId)) {
			return null;
		}
		return getVisibleArea(layerId);
	}

	public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
		return true;
	}

	// ------ Accessors ------------------

	private Geodesk getLoket() {
		if (geodesk == null) {
			try {
				geodesk = geodeskService.getGeodeskByPublicId(geodeskId);
			} catch (GeomajasSecurityException e) {
				e.printStackTrace();
			}
		}
		return geodesk;
	}

	public Role getRole() {
		return (profile == null ? null : profile.getRole());
	}

	public Territory getGroup() {
		return (profile == null ? null : profile.getTerritory());
	}

	public Profile getProfile() {
		return profile;
	}

}
