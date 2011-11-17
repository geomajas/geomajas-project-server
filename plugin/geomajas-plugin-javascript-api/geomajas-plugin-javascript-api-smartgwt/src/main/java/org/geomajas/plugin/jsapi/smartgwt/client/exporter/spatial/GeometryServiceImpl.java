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

package org.geomajas.plugin.jsapi.smartgwt.client.exporter.spatial;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.plugin.jsapi.client.spatial.GeometryService;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Implementation of the {@link GeometryService} for the GWT face.
 * 
 * @author Pieter De Graef
 */
@Export("GeometryService")
@ExportPackage("org.geomajas.jsapi.spatial")
public class GeometryServiceImpl implements GeometryService, Exportable {

	public GeometryServiceImpl() {
	}

	public Bbox getBounds(Geometry geometry) {
		org.geomajas.gwt.client.spatial.geometry.Geometry geom = GeometryConverter.toGwt(geometry);
		return GeometryConverter.toDto(geom.getBounds());
	}
}