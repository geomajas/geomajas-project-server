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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.BaseAuthorization;

import java.util.Map;

/**
 * Allow configuring authorizations, with area limitation authorizations.
 *
 * @author Joachim Van der Auwera
 */
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
	private final class LocalAuthorization extends LayerAuthorization implements AreaAuthorization {

		private AreaAuthorizationInfo info;
		private WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 0));

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
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could no parse geometry " + area, pe);
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
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could no parse geometry " + area, pe);
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
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could no parse geometry " + area, pe);
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
				return wktReader.read(area);
			} catch (ParseException pe) {
				throw new IllegalStateException("Could no parse geometry " + area, pe);
			}
		}

		public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
			LayerAreaAuthorizationInfo layer = info.getLayers().get(layerId);
			return null != layer && layer.isPartlyDeleteAuthorizedSufficient();
		}
	}

}
