/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.writer.svg;

import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * Writer for converting features to svg.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class SvgFeatureWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(SvgFeatureWriter.class);

	private GeometryCoordinateSequenceTransformer transformer;

	public SvgFeatureWriter(GeometryCoordinateSequenceTransformer transformer) {
		this.transformer = transformer;
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		try {
			InternalFeatureImpl feature = (InternalFeatureImpl) object;
			Geometry geom = feature.getGeometry();
			if (feature.isClipped()) {
				geom = feature.getClippedGeometry();
			}
			geom = transformer.transform(geom);

			if (geom instanceof Point || geom instanceof MultiPoint) {
				// write the enclosing group
				document.writeElement("g", asChild);
				document.writeAttribute("id", feature.getId());

				// write the points
				for (int i = 0; i < geom.getNumGeometries(); i++) {
					document.writeObject(geom.getGeometryN(i), true);
					document.writeAttribute("id", feature.getId());
					document.writeAttribute("xlink:href", "#" + feature.getStyleInfo().getStyleId());
					document.closeElement();
				}
			} else {
				document.writeObject(geom, asChild);
				document.writeAttribute("id", feature.getId());
			}
		} catch (TransformException e) {
			log.warn("could not render feature");
		}
	}
}