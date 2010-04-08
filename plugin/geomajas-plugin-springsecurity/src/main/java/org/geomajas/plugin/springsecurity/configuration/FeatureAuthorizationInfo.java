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

package org.geomajas.plugin.springsecurity.configuration;

import org.geomajas.global.Api;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.FeatureAuthorization;

import java.util.List;
import java.util.Map;

/**
 * Allow configuring authorizations, with feature level authorizations.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class FeatureAuthorizationInfo extends LayerAuthorizationInfo {

	private Map<String, LayerFeatureAuthorizationInfo> layers;

	/**
	 * Set feature authorizations per layer.
	 *
	 * @return feature authorizations per layer
	 */
	public Map<String, LayerFeatureAuthorizationInfo> getLayers() {
		return layers;
	}

	/**
	 * Get the feature authorizations per layer.
	 *
	 * @param layers feature authorizations per layer
	 */
	public void setLayers(Map<String, LayerFeatureAuthorizationInfo> layers) {
		this.layers = layers;
	}

	@Override
	public BaseAuthorization getAuthorization() {
		return new LocalAuthorization(this);
	}

	/**
	 * Authorization implementation class.
	 */
	private final class LocalAuthorization extends LayerAuthorization implements FeatureAuthorization {

		private FeatureAuthorizationInfo info;

		public LocalAuthorization(FeatureAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		public boolean isFeatureVisible(String layerId, InternalFeature feature) {
			LayerFeatureAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null && check(feature, layer.getVisibleIncludes(), layer.getVisibleExcludes());
		}

		public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
			LayerFeatureAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null &&
					check(feature, layer.getUpdateAuthorizedIncludes(), layer.getUpdateAuthorizedExcludes());
		}

		public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature,
												 InternalFeature newFeature) {
			LayerFeatureAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null &&
					check(newFeature, layer.getUpdateAuthorizedIncludes(), layer.getUpdateAuthorizedExcludes());
		}

		public boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature) {
			LayerFeatureAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null &&
					check(feature, layer.getDeleteAuthorizedIncludes(), layer.getDeleteAuthorizedExcludes());
		}

		public boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature) {
			LayerFeatureAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null && layer.isCreateAuthorized();
		}

		private boolean check(InternalFeature feature, List<String> includes, List<String> excludes) {
			return check(feature.getId(), includes) && !check(feature.getId(), excludes);
		}
	}
}
