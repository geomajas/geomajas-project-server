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

package org.geomajas.command.geometry;

import org.geomajas.command.Command;
import org.geomajas.command.dto.MergePolygonRequest;
import org.geomajas.command.dto.MergePolygonResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * This command merges an array of polygons.
 * </p>
 * 
 * @author Pieter De Graef
 * @deprecated use {@link org.geomajas.command.geometry.GeometryMergeCommand}
 */
@Deprecated
@Component()
public class MergePolygonCommand implements Command<MergePolygonRequest, MergePolygonResponse> {

	@Autowired
	private DtoConverterService converter;

	@Override
	public MergePolygonResponse getEmptyCommandResponse() {
		return new MergePolygonResponse();
	}

	@Override
	public void execute(MergePolygonRequest request, MergePolygonResponse response) throws Exception {
		Polygon[] polygons = new Polygon[request.getPolygons().length];
		for (int i = 0; i < request.getPolygons().length; i++) {
			try {
				polygons[i] = (Polygon) converter.toInternal(request.getPolygons()[i]);
			} catch (Exception e) {
				throw new GeomajasException(e, ExceptionCode.MERGE_NO_POLYGON);
			}
		}
		int precision = polygons[0].getPrecisionModel().getMaximumSignificantDigits() - 1;
		PrecisionModel precisionModel = new PrecisionModel(Math.pow(10.0, precision));
		GeometryFactory factory = new GeometryFactory(precisionModel, polygons[0].getSRID());

		Geometry temp = factory.createGeometry(polygons[0]);
		for (int i = 1; i < polygons.length; i++) {
			Geometry polygon = factory.createGeometry(polygons[i]);
			temp = temp.union(polygon.buffer(Math.pow(10.0, -(precision - 1))));
		}
		if (temp instanceof Polygon) {
			MultiPolygon mp = factory.createMultiPolygon(new Polygon[] { (Polygon) temp });
			response.setGeometry(converter.toDto(mp));
		} else if (temp instanceof MultiPolygon && temp.getNumGeometries() != 0
				&& (request.isAllowMultiPolygon() || temp.getNumGeometries() == 1)) {
			response.setGeometry(converter.toDto(temp));
		} else {
			throw new GeomajasException(ExceptionCode.MERGE_NO_POLYGON);
		}
	}
}