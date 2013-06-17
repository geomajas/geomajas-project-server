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
package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the style service.
 * 
 * @author Oliver May
 * 
 */
@Component
public class StyleServiceImpl implements StyleService {

	private static final long CACHE_TIME = 21600 * 1000;
	private static final long CLEANUP_TIME = 3600 * 1000;

	private Map<String, CachedNamedStyleInfo> styleInfos = new HashMap<String, CachedNamedStyleInfo>();

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public String registerStyle(String layerId, NamedStyleInfo style) {
		String uuid = UUID.randomUUID().toString();
		CachedNamedStyleInfo cachedNamedStyleInfo = new CachedNamedStyleInfo(style, System.currentTimeMillis()
				+ CACHE_TIME);
		styleInfos.put(uuid, cachedNamedStyleInfo);
		return uuid;
	}

	@Override
	public NamedStyleInfo retrieveStyle(String layerId, String styleName) {
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		if (layer != null) {
			NamedStyleInfo namedStyle = layer.getLayerInfo().getNamedStyleInfo(styleName);
			if (namedStyle != null) {
				return namedStyle;
			} else {
				if (styleInfos.containsKey(styleName)) {
					return styleInfos.get(styleName).getNamedStyleInfo();
				}
			}
		}
		return null;
	}

	@Scheduled(fixedRate = CLEANUP_TIME)
	public void cleanUpStyles() {
		List<String> toRemove = new ArrayList<String>();
		for (Entry<String, CachedNamedStyleInfo> styleInfo : styleInfos.entrySet()) {
			if (styleInfo.getValue().getExpireTime() < System.currentTimeMillis()) {
				styleInfos.remove(styleInfo);
			}
		}
		styleInfos.keySet().removeAll(toRemove);
	}

	/**
	 * Helper class to store Named Style Info.
	 * 
	 * @author Oliver May
	 *
	 */
	private class CachedNamedStyleInfo {

		private NamedStyleInfo namedStyleInfo;

		private long expireTime;

		public CachedNamedStyleInfo(NamedStyleInfo namedStyleInfo, long expireTime) {
			this.namedStyleInfo = namedStyleInfo;
			this.expireTime = expireTime;
		}
		
		public NamedStyleInfo getNamedStyleInfo() {
			return namedStyleInfo;
		}
		
		public long getExpireTime() {
			return expireTime;
		}

	}

}
