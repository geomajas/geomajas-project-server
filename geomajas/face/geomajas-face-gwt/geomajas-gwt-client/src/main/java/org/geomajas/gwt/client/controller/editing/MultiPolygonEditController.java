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

package org.geomajas.gwt.client.controller.editing;

import com.google.gwt.event.dom.client.MouseEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.widget.MapWidget;

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
			index.setExteriorRingIndex(0);
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
									new Coordinate[] {getTransformer().worldToView(lastCoordinate),
											getScreenPosition(event)});
					tempLine1.setGeometry(lineString1);
					mapWidget.render(tempLine1, "all");

					LineString lineString2 = featureTransaction.getNewFeatures()[index.getFeatureIndex()].getGeometry()
							.getGeometryFactory().createLineString(
									new Coordinate[] {getTransformer().worldToView(coordinates[0]),
											getScreenPosition(event)});
					tempLine2.setGeometry(lineString2);
					mapWidget.render(tempLine2, "all");
				}
			}
		}
	}
}
