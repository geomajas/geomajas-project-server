/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.printing.component.service.PrintConfigurationServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom printconfiguraionservice for the deskmanager. Adds caching of (generated) mapconfigurations.
 * 
 * @author Oliver May
 * 
 */
public class DeskmanagerPrintConfigurationService extends PrintConfigurationServiceImpl {

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private GeodeskConfigurationService configurationService;

	/**
	 * timeout in miliseconds
	 */
	private static final long TIMEOUT = 10000;

	// TODO: this is a hack for the pdf printing performance, for every feature the map configuration is fetched,
	// we must cache this to get any performance!
	private Map<Geodesk, CachedClientApplicationInfo> geodeskConfigurations = 
		new HashMap<Geodesk, CachedClientApplicationInfo>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.service.PrintConfigurationService#getMapInfo(java.lang.String,
	 * java.lang.String)
	 */
	public ClientMapInfo getMapInfo(String mapId, String applicationId) {

		try {
			// FIXME: code duplication from GetConfigurationCommand. move to service!
			String id = applicationId;
			Geodesk geodesk = geodeskService.getGeodeskByPublicId(id); // this checks if geodesk is allowed
			if (geodesk != null) {
				// request from cache
				ClientApplicationInfo geodeskConfig = getClonedGeodeskConfiguration(geodesk);
				for (ClientMapInfo map : geodeskConfig.getMaps()) {
					if (map.getId().equals(mapId)) {
						return map;
					}
				}

			}
		} catch (GeomajasSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private ClientApplicationInfo getClonedGeodeskConfiguration(Geodesk geodesk) {
		if (geodeskConfigurations.containsKey(geodesk)
				&& geodeskConfigurations.get(geodesk).getCacheTime() + TIMEOUT < System.currentTimeMillis()) {
			return geodeskConfigurations.get(geodesk).getApplicationInfo();
		} else {
			ClientApplicationInfo applicationInfo = configurationService
					.createClonedGeodeskConfiguration(geodesk, true);
			geodeskConfigurations.put(geodesk, new CachedClientApplicationInfo(applicationInfo));
			return applicationInfo;
		}
	}

	/**
	 * 
	 * @author Oliver May
	 * 
	 */
	public class CachedClientApplicationInfo {

		private long cacheTime;

		private ClientApplicationInfo applicationInfo;

		public CachedClientApplicationInfo(ClientApplicationInfo applicationInfo) {
			this.cacheTime = System.currentTimeMillis();
			this.applicationInfo = applicationInfo;
		}

		/**
		 * @return the cacheTime
		 */
		public long getCacheTime() {
			return cacheTime;
		}

		/**
		 * @param cacheTime
		 *            the cacheTime to set
		 */
		public void setCacheTime(long cacheTime) {
			this.cacheTime = cacheTime;
		}

		/**
		 * @return the applicationInfo
		 */
		public ClientApplicationInfo getApplicationInfo() {
			return applicationInfo;
		}

		/**
		 * @param applicationInfo
		 *            the applicationInfo to set
		 */
		public void setApplicationInfo(ClientApplicationInfo applicationInfo) {
			this.applicationInfo = applicationInfo;
		}

	}
}
