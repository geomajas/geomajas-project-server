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

import java.util.UUID;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.CacheService;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the style service.
 * 
 * @author Oliver May
 * 
 */
@Component
public class StyleServiceImpl implements StyleService {
	private static final String CACHE_KEY = StyleServiceImpl.class.toString();
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CacheService cacheService;
	
	@Override
	public String registerStyle(String layerId, NamedStyleInfo style) {
		String uuid = UUID.randomUUID().toString();
		cacheService.put(CACHE_KEY, uuid, style);
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
				NamedStyleInfo nsi = cacheService.get(CACHE_KEY, styleName, NamedStyleInfo.class);
				if (null != nsi) {
					return nsi;
				}
			}
		}
		return null;
	}
}
