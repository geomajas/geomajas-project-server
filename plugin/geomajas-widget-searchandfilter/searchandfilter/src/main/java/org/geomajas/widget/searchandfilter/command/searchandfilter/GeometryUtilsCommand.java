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
package org.geomajas.widget.searchandfilter.command.searchandfilter;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.service.DtoConverterService;
import org.geomajas.widget.searchandfilter.command.dto.GeometryUtilsRequest;
import org.geomajas.widget.searchandfilter.command.dto.GeometryUtilsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Command to help with common geometry manipulations.
 * <p>
 * Manipulations throw as little exceptions as possible, eg. if you give null
 * you get null returned, if you do not set an action, features are returned
 * as-is.
 * <p>Warning: Merge is not 100% exact, so do not use if area-size is important.
 * @author Kristof Heirwegh
 */
@Component
public class GeometryUtilsCommand implements Command<GeometryUtilsRequest, GeometryUtilsResponse> {

	private final Logger log = LoggerFactory.getLogger(GeometryUtilsCommand.class);

	@Autowired
	private DtoConverterService converter;

	public void execute(final GeometryUtilsRequest request, final GeometryUtilsResponse response) throws Exception {
		if (request.getGeometries() != null && request.getGeometries().length > 0) {
			int geomCount = request.getGeometries().length;
			log.debug("GeometryUtilsCommand for " + geomCount + " geometries.");

			if (request.getActionFlags() == 0) {
				response.setGeometries(request.getGeometries());
			}

			// convert to internal
			Geometry[] lastResult = new Geometry[geomCount];
			org.geomajas.geometry.Geometry[] dtoGeoms = request.getGeometries();
			for (int i = 0; i < geomCount; i++) {
				lastResult[i] = converter.toInternal(dtoGeoms[i]);
			}

			List<Geometry[]> intermediateResults = new ArrayList<Geometry[]>();

			// do merge before buffer
			if ((request.getActionFlags() & GeometryUtilsRequest.ACTION_MERGE) > 0) {
				lastResult = merge(lastResult);
				intermediateResults.add(lastResult);
			}

			if ((request.getActionFlags() & GeometryUtilsRequest.ACTION_BUFFER) > 0) {
				lastResult = buffer(lastResult, request.getBuffer(), request.getBufferQuadrantSegments());
				intermediateResults.add(lastResult);
			}

			// -- other actions here

			// ----------------------------------------------------------

			List<org.geomajas.geometry.Geometry> dtoResult = new ArrayList<org.geomajas.geometry.Geometry>();
			if (request.isIntermediateResults()) {
				for (Geometry[] geometries : intermediateResults) {
					for (Geometry geometry : geometries) {
						dtoResult.add(converter.toDto(geometry));
					}
				}
			} else {
				for (Geometry geometry : lastResult) {
					dtoResult.add(converter.toDto(geometry));
				}
			}
			response.setGeometries(dtoResult.toArray(new org.geomajas.geometry.Geometry[dtoResult.size()]));
		}
	}

	private Geometry[] buffer(Geometry[] geoms, double distance, int quadrants) {
		Geometry[] result = new Geometry[geoms.length];
		for (int i = 0; i < geoms.length; i++) {
			result[i] = geoms[0].buffer(distance, quadrants);
		}
		return result;
	}

	private Geometry[] merge(Geometry[] geoms) {
		Geometry[] result = new Geometry[1];
		// points/lines to polygon, prevents problems later on...
		// TODO this works for meters (1 cm) what with other crs's ? 
		Geometry merged = geoms[0].buffer(0.01);
		for (int i = 1; i < geoms.length; i++) {
			merged = merged.union(geoms[i].buffer(0.01));
		}
		result[0] = merged;
		return result;
	}

	public GeometryUtilsResponse getEmptyCommandResponse() {
		return new GeometryUtilsResponse();
	}
}
