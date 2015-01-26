/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.FeatureAuthorization;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Simple authorization class, allows everything to features with even id and nothing to features with odd id.
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
