/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Determine the filter which needs to be applied to get the features.
 *
 * @author Joachim Van der Auwera
 */
public class GetTileFilterStep implements PipelineStep<GetTileContainer> {

	private String id;

	@Autowired
	private FilterService filterService;

	@Autowired
	private GeoService geoService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		CrsTransform layerToMap = context.get(PipelineCode.CRS_TRANSFORM_KEY, CrsTransform.class);
		CrsTransform maptoLayer = geoService.getCrsTransform(layerToMap.getTarget(), layerToMap.getSource());

		String geomName = layer.getLayerInfo().getFeatureInfo().getGeometryType().getName();

		String epsg = Integer.toString(geoService.getSridFromCrs(layer.getCrs()));
		// transform tile bounds back to layer coordinates
		// TODO: for non-affine transforms this is not accurate enough !
		Envelope bounds = geoService.transform(response.getTile().getBounds(), maptoLayer);
		Filter filter = filterService.createBboxFilter(epsg, bounds, geomName);
		if (null != metadata.getFilter()) {
			filter = filterService.createAndFilter(filterService.parseFilter(metadata.getFilter()), filter);
		}

		context.put(PipelineCode.FILTER_KEY, filter);
	}
}