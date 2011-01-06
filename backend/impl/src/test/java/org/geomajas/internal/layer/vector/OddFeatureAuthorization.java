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

package org.geomajas.internal.layer.vector;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.FeatureAuthorization;

/**
 * Simple authorization class, allows everything.
 *
 * @author Kristof Heirwegh
 * @author Joachim Van der Auwera
 */
public class OddFeatureAuthorization implements BaseAuthorization, AreaAuthorization, FeatureAuthorization {

	private Geometry biggestGeometry;

	public String getId() {
		return "AllowAll";
	}

	public boolean isToolAuthorized(String toolId) {
		return true;
	}

	public boolean isCommandAuthorized(String commandName) {
		return true;
	}

	public boolean isLayerVisible(String layerId) {
		return true;
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return true;
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return true;
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return true;
	}

	protected Geometry getAuthorizedArea(String layerId) {
		if (null == biggestGeometry) {
			// build Geometry which covers biggest possible area
			Envelope maxBounds = new Envelope(-Double.MAX_VALUE, Double.MAX_VALUE,
					-Double.MAX_VALUE, Double.MAX_VALUE);
			PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
			GeometryFactory geometryFactory = new GeometryFactory(precisionModel, 0);
			biggestGeometry = geometryFactory.toGeometry(maxBounds);
		}
		return biggestGeometry;
	}

	protected boolean isPartlyAuthorizedSufficient(String layerId) {
		return true;
	}

	public Geometry getVisibleArea(String layerId) {
		return getAuthorizedArea(layerId);
	}

	public boolean isPartlyVisibleSufficient(String layerId) {
		return isPartlyAuthorizedSufficient(layerId);
	}

	public Geometry getUpdateAuthorizedArea(String layerId) {
		return getAuthorizedArea(layerId);
	}

	public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
		return isPartlyAuthorizedSufficient(layerId);
	}

	public Geometry getCreateAuthorizedArea(String layerId) {
		return getAuthorizedArea(layerId);
	}

	public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
		return isPartlyAuthorizedSufficient(layerId);
	}

	public Geometry getDeleteAuthorizedArea(String layerId) {
		return getAuthorizedArea(layerId);
	}

	public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
		return isPartlyAuthorizedSufficient(layerId);
	}

	private boolean isEvenId(InternalFeature feature) {
		return Integer.parseInt(feature.getId()) % 2 == 0;
	}

	public boolean isFeatureVisible(String layerId, InternalFeature feature) {
		return isEvenId(feature);
	}

	public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
		return isEvenId(feature);
	}

	public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature, InternalFeature newFeature) {
		return isEvenId(orgFeature) && isEvenId(newFeature);
	}

	public boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature) {
		return isEvenId(feature);
	}

	public boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature) {
		return isEvenId(feature);
	}
}
