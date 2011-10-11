/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
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

		protected LocalAuthorization() {
			super(); // for deserialization
		}

		public LocalAuthorization(AreaAuthorizationInfo info) {
			super(info);
			this.info = info;
		}

		public Geometry getVisibleArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			String area = layer.getVisibleArea();
			try {
				WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could not parse geometry " + area, pe);
			}
		}

		public boolean isPartlyVisibleSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyVisibleSufficient();
		}

		public Geometry getUpdateAuthorizedArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			String area = layer.getUpdateAuthorizedArea();
			try {
				WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could not parse geometry " + area, pe);
			}
		}

		public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyUpdateAuthorizedSufficient();
		}

		public Geometry getCreateAuthorizedArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			String area = layer.getCreateAuthorizedArea();
			try {
				WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could not parse geometry " + area, pe);
			}
		}

		public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyCreateAuthorizedSufficient();
		}

		public Geometry getDeleteAuthorizedArea(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			if (null == layer) {
				return null;
			}
			String area = layer.getDeleteAuthorizedArea();
			try {
				WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could not parse geometry " + area, pe);
			}
		}

		public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyDeleteAuthorizedSufficient();
		}
	}

}
