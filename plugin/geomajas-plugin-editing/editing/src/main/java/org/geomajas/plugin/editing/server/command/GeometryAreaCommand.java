/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.server.command;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.plugin.editing.dto.GeometryAreaRequest;
import org.geomajas.plugin.editing.dto.GeometryAreaResponse;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygonal;

/**
 * <p>
 * This command calculates the (approximate) geographical area of a geometry.
 * </p>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Component
@Api
public class GeometryAreaCommand implements Command<GeometryAreaRequest, GeometryAreaResponse> {

	private static final String EPSG_4326 = "EPSG:4326";

	private static final double RADIUS = 6378137;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private GeoService geoService;

	/** {@inheritDoc} */
	public GeometryAreaResponse getEmptyCommandResponse() {
		return new GeometryAreaResponse();
	}

	/** {@inheritDoc} */
	public void execute(GeometryAreaRequest request, GeometryAreaResponse response) throws Exception {
		List<Double> areas = new ArrayList<Double>(request.getGeometries().size());
		for (org.geomajas.geometry.Geometry g : request.getGeometries()) {
			Geometry geometry = converter.toInternal(g);
			double area = 0;
			if (geometry instanceof Polygonal) {
				if (request.getCrs() != null) {
					if (request.getCrs() != EPSG_4326) {
						geometry = geoService.transform(geometry, request.getCrs(), EPSG_4326);
					}
					// applying global sinusoidal projection (equal-area)
					geometry.apply(new CoordinateFilter() {

						public void filter(Coordinate coord) {
							double newX = coord.x * Math.PI / 180.0 * Math.cos(coord.y * Math.PI / 180.0);
							double newY = coord.y * Math.PI / 180.0;
							coord.x = newX;
							coord.y = newY;

						}
					});
					area = geometry.getArea() * RADIUS * RADIUS;
				} else {
					area = geometry.getArea();
				}
			}
			areas.add(area);
		}
		response.setAreas(areas);
	}

}
