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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Transform the features in a tile to map coordinates.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class GetTileTransformStep implements PipelineStep<GetTileContainer> {

	private String id;

	@Autowired
	private GeoService geoService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		// Determine transformation to apply
		InternalTile tile = response.getTile();
		CrsTransform transform = context.get(PipelineCode.CRS_TRANSFORM_KEY, CrsTransform.class);

		// convert feature geometries to layer, need to copy to assure cache is not affected
		List<InternalFeature> features = new ArrayList<InternalFeature>();
		for (InternalFeature feature : tile.getFeatures()) {
			if (null != feature.getGeometry()) {
				InternalFeature newFeature = feature.cloneWithoutGeometry();
				newFeature.setGeometry(geoService.transform(feature.getGeometry(), transform));
				features.add(newFeature);
			}
		}
		// replace the contents of the list
		tile.setFeatures(features);
	}
}
