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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;
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
	public static final class LocalAuthorization extends LayerAuthorization implements AttributeAuthorization {

		private AttributeAuthorizationInfo info; // NOSONAR

		/**
		 * Create a {@link LocalAuthorization} for deserialization.
		 */
		protected LocalAuthorization() {
			super(); // for deserialization
		}

		/**
		 * Create a {@link LocalAuthorization} object for a specific configuration.
		 *
		 * @param info authorization info
		 */
		public LocalAuthorization(AttributeAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		@Override
		public boolean isAttributeReadable(String layerId, InternalFeature feature, String attributeName) {
			LayerAttributeAuthorizationInfo layer = info.getLayers().get(layerId);
			return layer != null &&
					check(feature, attributeName, layer.getReadableIncludes(), layer.getReadableExcludes());
		}

		@Override
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
