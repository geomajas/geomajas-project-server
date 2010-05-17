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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.global.Api;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.AttributeAuthorization;
import org.geomajas.security.BaseAuthorization;

import java.util.List;
import java.util.Map;

/**
 * Allow configuring authorizations, with attribute level authorizations.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class AttributeAuthorizationInfo extends LayerAuthorizationInfo {

	private Map<String, LayerAttributeAuthorizationInfo> layers;

	/**
	 * Set attribute authorizations per layer.
	 *
	 * @return attribute authorizations per layer
	 */
	public Map<String, LayerAttributeAuthorizationInfo> getLayers() {
		return layers;
	}

	/**
	 * Get the attribute authorizations per layer.
	 *
	 * @param layers attribute authorizations per layer
	 */
	public void setLayers(Map<String, LayerAttributeAuthorizationInfo> layers) {
		this.layers = layers;
	}

	@Override
	public BaseAuthorization getAuthorization() {
		return new LocalAuthorization(this);
	}

	/**
	 * Authorization implementation class.
	 */
	private final class LocalAuthorization extends LayerAuthorization implements AttributeAuthorization {

		private AttributeAuthorizationInfo info;

		public LocalAuthorization(AttributeAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		public boolean isAttributeReadable(String layerId, InternalFeature feature, String attributeName) {
			LayerAttributeAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null &&
					check(feature, attributeName, layer.getReadableIncludes(), layer.getReadableExcludes());
		}

		public boolean isAttributeWritable(String layerId, InternalFeature feature, String attributeName) {
			LayerAttributeAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null &&
					check(feature, attributeName, layer.getWritableIncludes(), layer.getWritableExcludes());
		}

		private boolean check(InternalFeature feature, String attributeName, List<String> includes,
							  List<String> excludes) {
			String featureId = null;
			if (null != feature) {
				featureId = feature.getId();
			}
			return check(featureId, attributeName, includes) && !check(featureId, attributeName, excludes);
		}

		private boolean check(String featureId, String attributeId, List<String> includes) {
			if (null != includes) {
				for (String check : includes) {
					int atPos = check.indexOf('@');
					if (atPos > 0) {
						if (check(featureId, check.substring(atPos + 1)) &&
								check(attributeId, check.substring(0, atPos))) {
							return true;
						}
					} else {
						if (check(attributeId, check)) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}
