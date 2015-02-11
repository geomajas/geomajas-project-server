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

package org.geomajas.security.allowall;

import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.BaseAuthorization;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Simple authorization class, allows everything.
 *
 * @author Joachim Van der Auwera
 */
public class AllowAllAuthorization implements BaseAuthorization, AreaAuthorization {

	private transient Geometry biggestGeometry;

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
			Envelope maxBounds = new Envelope(-Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE);
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
}
