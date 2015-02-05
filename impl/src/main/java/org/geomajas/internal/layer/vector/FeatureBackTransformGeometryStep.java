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

import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Convert the geometry (if any) in the feature (in the context) using the backwards transformation (also in the
 * context).
 * 
 * @author Jan De Moerloose
 */
public class FeatureBackTransformGeometryStep implements PipelineStep {

	@Autowired
	private GeoService geoService;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		InternalFeature feature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
		if (null != feature.getGeometry()) {
			CrsTransform layerToMap = context.get(PipelineCode.CRS_BACK_TRANSFORM_KEY, CrsTransform.class);
			feature.setGeometry(geoService.transform(feature.getGeometry(), layerToMap));
		}
	}
}

