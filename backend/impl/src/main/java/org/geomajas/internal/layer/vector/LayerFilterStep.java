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

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.filter.Filter;

/**
 * Extend the existing layer filter (in the context, if any) to include layer security.
 * <p/>
 * This combines the visible area, the security filter for the layer, the default filter for the layer,
 *
 * @author Joachim Van der Auwera
 */
public class LayerFilterStep extends AbstractSaveOrUpdateStep {

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		Filter filter = context.getOptional(PipelineCode.FILTER_KEY, Filter.class);
		String layerId = layer.getId();

		// apply visible area filter
		filter = getSecurityFilter(filter, layer, securityContext.getVisibleArea(layerId));

		context.put(PipelineCode.FILTER_KEY, filter);
	}

}
