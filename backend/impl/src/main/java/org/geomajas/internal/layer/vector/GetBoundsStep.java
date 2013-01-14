/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetBoundsContainer;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Step for getBounds in {@link org.geomajas.layer.VectorLayerService}.
 *
 * @author Joachim Van der Auwera
 */
public class GetBoundsStep implements PipelineStep<GetBoundsContainer> {

	@Autowired
	private GeoService geoService;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetBoundsContainer response)
			throws GeomajasException {
		if (null == response.getEnvelope()) {
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			CrsTransform crsTransform = context.get(PipelineCode.CRS_TRANSFORM_KEY, CrsTransform.class);
			Filter filter = context.get(PipelineCode.FILTER_KEY, Filter.class);
			Envelope bounds = layer.getBounds(filter);
			bounds = geoService.transform(bounds, crsTransform);
			response.setEnvelope(bounds);
		}
	}
}
