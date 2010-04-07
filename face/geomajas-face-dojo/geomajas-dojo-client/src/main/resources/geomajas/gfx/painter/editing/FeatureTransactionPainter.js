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

dojo.provide("geomajas.gfx.painter.editing.FeatureTransactionPainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("FeatureTransactionPainter", Painter, {

	/**
	 * @class Painter responsible for drawing the FeatureTransaction objects.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapView) {
		this.transform = new WorldViewTransformation(mapView);
		this.pointStyle = new ShapeStyle("#FF6600", "1", "#FFFFFF", "0", "1", null, null);
		this.edgeStyle = new ShapeStyle("#FFFFFF", "0", "#FFAA00", "1", "3", null, null);
		this.lineStringStyle = new ShapeStyle("#FFFFFF", "0", "#FFFFFF", "0", "0", null, null);
		this.linearRingStyle = new ShapeStyle("#FFEE00", "0", "#FFAA00", "1", "3", null, null);
		this.polygonStyle = new ShapeStyle("#FFCC00", "0.5", "#FFAA00", "0", "0", null, null);
	},

	/**
	 * @param featureTransaction A FeatureTransaction object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*Object*/featureTransaction, /*GraphicsContext*/graphics) {
		var features = featureTransaction.getNewFeatures();
		if (features == null) {
			return;
		}
		for (var i=0; i<features.length; i++) {
			// The reason we transform here, and not in the graphics, is because style shouldn't be transformed. (things like stroke-width for example)
			var geometry = this.transform.worldGeometryToView(features[i].getGeometry());
			if (geometry instanceof Point) {
				this._paintPoint (geometry, graphics, featureTransaction.getId()+".feature"+i);
			} else if (geometry instanceof LineString) {
				this._paintLineString (geometry, graphics, featureTransaction.getId()+".feature"+i);
			} else if (geometry instanceof MultiLineString) {
				this._paintMultiLineString (geometry, graphics, featureTransaction.getId()+".feature"+i);
			} else if (geometry instanceof Polygon) {
				this._paintPolygon (geometry, graphics, featureTransaction.getId()+".feature"+i);
			} else if (geometry instanceof MultiPolygon) {
				this._paintMultiPolygon (geometry, graphics, featureTransaction.getId()+".feature"+i);
			}
		}
	},

	/**
	 * @private
	 */
	_paintPoint : function (point, graphics, id) {
		if (!point.isEmpty()) {
			var circle = new Circle (id+".coordinate0");
			circle.setR ("5");
			circle.setPosition (point.getCoordinateN(0));
			circle.setStyle (this.pointStyle);
			graphics.drawCircle (circle, { id:circle.getId(), style:this.pointStyle });
		}
	},

	/**
	 * @private
	 */
	_paintLineString : function (lineString, graphics, id) {
		var coords = lineString.getCoordinates();
		graphics.drawLine (lineString, { id:id+".area", style:this.lineStringStyle });

		// Individual lines:
		var factory = lineString.getGeometryFactory();
		for (var i=1; i<coords.length; i++) {
			var line = factory.createLineString([coords[i-1], coords[i]]);
			graphics.drawLine (line, {id:id+".edge"+i, style:this.edgeStyle });
		}

		// Points:
		for (var i=0; i<coords.length; i++) {
			var circle = new Circle (id+".coordinate"+i);
			circle.setR ("5");
			circle.setPosition (coords[i]);
			circle.setStyle (this.pointStyle);
			graphics.drawCircle (circle, { id:circle.getId(), style:this.pointStyle });
		}
	},

	/**
	 * @private
	 */
	_paintLinearRing : function (linearRing, graphics, id) {
		var coords = linearRing.getCoordinates();
		graphics.drawPolygon (linearRing, { id:id+".area", style:this.linearRingStyle });

		// Individual lines:
		var factory = linearRing.getGeometryFactory();
		for (var i=1; i<coords.length; i++) {
			var line = factory.createLineString([coords[i-1], coords[i]]);
			graphics.drawLine (line, {id:id+".edge"+i, style:this.edgeStyle });
		}

		// Points:
		for (var i=0; i<coords.length-1; i++) {
			var circle = new Circle (id+".coordinate"+i);
			circle.setR ("5");
			circle.setPosition (coords[i]);
			circle.setStyle (this.pointStyle);
			graphics.drawCircle (circle, { id:circle.getId(), style:this.pointStyle });
		}
	},

	/**
	 * @private
	 */
	_paintMultiLineString : function (multiLineString, graphics, id) {
		for (var i=0; i<multiLineString.getNumGeometries(); i++) {
			var lineString = multiLineString.getGeometryN(i);
			this._paintLineString(lineString, graphics, id+".lineString"+i)
		}
	},

	/**
	 * @private
	 */
	_paintPolygon : function (polygon, graphics, id) {
		graphics.drawPolygon (polygon, { id:id+".background", style:this.polygonStyle });
		if (polygon.getExteriorRing() != null) {
			var shell = polygon.getExteriorRing();
			this._paintLinearRing(shell, graphics, id+".shell");
		}
		for (var i=0; i<polygon.getNumInteriorRing(); i++) {
			var hole = polygon.getInteriorRingN(i);
			this._paintLinearRing(hole, graphics, id+".hole"+i);
		}
	},

	/**
	 * @private
	 */
	_paintMultiPolygon : function (multiPolygon, graphics, id) {
		for (var i=0; i<multiPolygon.getNumGeometries(); i++) {
			var polygon = multiPolygon.getGeometryN(i);
			this._paintPolygon(polygon, graphics, id+".polygon"+i)
		}
	}
});