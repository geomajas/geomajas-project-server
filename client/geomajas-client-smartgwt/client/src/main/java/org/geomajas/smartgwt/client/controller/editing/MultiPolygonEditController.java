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

package org.geomajas.smartgwt.client.controller.editing;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseEvent;

/**
 * Editing controller for MultiPolygon geometries. Supports MultiPolygons with only one Polygon at the moment.
 * 
 * @author Pieter De Graef
 */
public class MultiPolygonEditController extends PolygonEditController {

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public MultiPolygonEditController(MapWidget mapWidget, EditController parent) {
		super(mapWidget, parent);
	}

	// -------------------------------------------------------------------------
	// EditController implementation:
	// -------------------------------------------------------------------------

	public TransactionGeomIndex getGeometryIndex() {
		if (index == null) {
			index = new TransactionGeomIndex();
			index.setExteriorRing(true);
			index.setGeometryIndex(0);
			index.setCoordinateIndex(0);
			index.setFeatureIndex(0);
		}
		return index;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void updateTempLines(FeatureTransaction featureTransaction, MouseEvent<?> event) {
		if (featureTransaction.getNewFeatures() != null && featureTransaction.getNewFeatures().length > 0) {
			if (tempLine1 == null) {
				createTempLines(featureTransaction, event);
			}
			MultiPolygon multiPolygon = (MultiPolygon) getGeometryIndex().getGeometry(featureTransaction);
			LinearRing ring = getGeometryIndex().getLinearRing(multiPolygon.getGeometryN(0));
			if (ring != null) {
				Coordinate[] coordinates = ring.getCoordinates();
				if (coordinates != null && coordinates.length > 0) {
					Coordinate lastCoordinate = coordinates[coordinates.length - 2];
					LineString lineString1 = featureTransaction.getNewFeatures()[index.getFeatureIndex()].getGeometry()
							.getGeometryFactory().createLineString(
									new Coordinate[] {getTransformer().worldToPan(lastCoordinate),
											getPanPosition(event)});
					tempLine1.setGeometry(lineString1);
					mapWidget.render(tempLine1, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);

					LineString lineString2 = featureTransaction.getNewFeatures()[index.getFeatureIndex()].getGeometry()
							.getGeometryFactory().createLineString(
									new Coordinate[] {getTransformer().worldToPan(coordinates[0]),
											getPanPosition(event)});
					tempLine2.setGeometry(lineString2);
					mapWidget.render(tempLine2, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				}
			}
		}
	}
}
