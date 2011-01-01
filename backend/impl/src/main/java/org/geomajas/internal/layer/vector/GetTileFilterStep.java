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