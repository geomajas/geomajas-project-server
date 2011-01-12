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

package org.geomajas.internal.layer.vector;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.FilterService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Extend the existing layer filter (in the context, if any) to include layer security.
 * <p/>
 * This combines the visible area, the security filter for the layer, the default filter for the layer,
 *
 * @author Joachim Van der Auwera
 */
public class LayerFilterStep extends AbstractSaveOrUpdateStep {

	private final Logger log = LoggerFactory.getLogger(LayerFilterStep.class);

	@Autowired
	private FilterService filterService;

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		Filter filter = context.getOptional(PipelineCode.FILTER_KEY, Filter.class);
		String layerId = layer.getId();

		// apply generic security filter
		Filter layerFeatureFilter = securityContext.getFeatureFilter(layerId);
		if (null != layerFeatureFilter) {
			filter = and(filter, layerFeatureFilter);
		}

		// apply default filter
		String defaultFilter = layer.getLayerInfo().getFilter();
		if (null != defaultFilter) {
			filter = and(filter, filterService.parseFilter(defaultFilter));
		}

		// apply visible area filter
		Geometry visibleArea = securityContext.getVisibleArea(layerId);
		if (null != visibleArea) {
			String geometryName = layer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			if (securityContext.isPartlyVisibleSufficient(layerId)) {
				filter = and(filter, filterService.createIntersectsFilter(visibleArea, geometryName));
			} else {
				filter = and(filter, filterService.createWithinFilter(visibleArea, geometryName));
			}
		} else {
			log.warn("Visible area is null for layer " + layerId + "removing all content!");
			filter = filterService.createFalseFilter();
		}

		context.put(PipelineCode.FILTER_KEY, filter);
	}

}
