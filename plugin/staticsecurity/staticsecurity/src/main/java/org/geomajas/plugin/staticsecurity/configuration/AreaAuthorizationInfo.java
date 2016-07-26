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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geomajas.annotation.Api;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.BaseAuthorization;

import java.util.Map;

/**
 * Allow configuring authorizations, with area limitation authorizations.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class AreaAuthorizationInfo extends LayerAuthorizationInfo {
	
	private Map<String, LayerAreaAuthorizationInfo> layers;

	/**
	 * Set area authorizations per layer.
	 *
	 * @return area authorizations per layer
	 */
	public Map<String, LayerAreaAuthorizationInfo> getLayers() {
		return layers;
	}

	/**
	 * Get the area authorizations per layer.
	 *
	 * @param layers area authorizations per layer
	 */
	public void setLayers(Map<String, LayerAreaAuthorizationInfo> layers) {
		this.layers = layers;
	}

	@Override
	public BaseAuthorization getAuthorization() {
		return new LocalAuthorization(this);
	}

	/**
	 * Authorization implementation class.
	 */
	public static final class LocalAuthorization extends LayerAuthorization implements AreaAuthorization {

		private AreaAuthorizationInfo info; // NOSONAR

		/**
		 * Create a {@link LocalAuthorization} for deserialization.
		 */
		protected LocalAuthorization() {
			super(); // for deserialization
		}

		/**
		 * Create a {@link LocalAuthorization} for a specific {@link AreaAuthorizationInfo} object.
		 *
		 * @param info authorization info
		 */
		public LocalAuthorization(AreaAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		@Override
		public Geometry getVisibleArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			return readWkt(layer.getVisibleArea());
		}

		@Override
		public boolean isPartlyVisibleSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyVisibleSufficient();
		}

		@Override
		public Geometry getUpdateAuthorizedArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			return readWkt(layer.getUpdateAuthorizedArea());
		}

		@Override
		public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyUpdateAuthorizedSufficient();
		}

		@Override
		public Geometry getCreateAuthorizedArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			return readWkt(layer.getCreateAuthorizedArea());
		}

		@Override
		public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyCreateAuthorizedSufficient();
		}

		@Override
		public Geometry getDeleteAuthorizedArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			return readWkt(layer.getDeleteAuthorizedArea());
		}

		@Override
		public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyDeleteAuthorizedSufficient();
		}

		private Geometry readWkt(String area) {
			try {
				WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalArgumentException("Could not parse geometry " + area, pe);
			}
		}
	}

}
