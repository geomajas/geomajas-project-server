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

dojo.provide("geomajas.spatial.transform.WorldViewTransformation");
dojo.require("geomajas._base");
dojo.require("geomajas.spatial.Bbox");
dojo.require("geomajas.spatial.Coordinate");
dojo.require("geomajas.spatial.Matrix2D");
dojo.require("geomajas.spatial.Vector2D");
dojo.require("geomajas.spatial.geometry.Geometry");
dojo.require("geomajas.map.Camera");
dojo.require("geomajas.map.MapView");

dojo.declare("WorldViewTransformation", null, {

	/**
	 * @fileoverview Transforms coordinates between world- and viewspace.
	 * @class This class is able to transform points and bounds from world- to
	 * viewspace, and the other way around. It simply needs the "MapView"
	 * object from a MapWidget to do this.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param mapView the MapWidget's viewing object. Contains camera location
	            information.
	 */
	constructor : function (/*MapView*/mapView) {
		/** Reference to the MapView object. */
		this.mapView = mapView;

		/** Reference to the MapView's camera. */
		this.cam = mapView.getCamera();
		
		/** @private */
		this.factory = new GeometryFactory(mapView.getSRID(), mapView.getPrecision() + 4);
	},

	/**
	 */
	worldPointToView : function (point) {
        log.debug("WorldViewTransformation.worldPointToView");
		if (point == null) {
			return null;
		}
		
		var pos = new Vector2D ();
		pos.fromCoordinate (point);
		var scale = this.mapView.getScale();
		pos.scale (scale, -scale);
		var transX = -(this.cam.getX()*scale) + (this.mapView.getMapWidth()/2);
		var transY = this.cam.getY()*scale + (this.mapView.getMapHeight()/2);
		pos.translate(transX, transY);

		if (this.cam.getAlpha() != 0) {
			var rotMat = Matrix2D.identity();
			rotMat = rotMat.rotate(this.cam.getAlpha());
			pos.translate (this.cam.getX(), this.cam.getY());
			pos = rotMat.multiplyVector(pos);
			pos.translate (-this.cam.getX(), -this.cam.getY());
		}		

		return new Coordinate (pos.get(0), pos.get(1));
	},

	/**
	 */
	worldPointToPan : function (point) {
		if (point == null) {
			return null;
		}
		
		var pos = new Vector2D ();
		pos.fromCoordinate (point);
		var scale = this.mapView.getScale();
		pos.scale (scale, -scale);
		var panOrigin = this.mapView.getPanOrigin();
		var transX = -(panOrigin.getX()*scale);
		var transY = panOrigin.getY()*scale;
		pos.translate(transX, transY);

		if (this.cam.getAlpha() != 0) {
			var rotMat = Matrix2D.identity();
			rotMat = rotMat.rotate(this.cam.getAlpha());
			pos.translate (this.cam.getX(), this.cam.getY());
			pos = rotMat.multiplyVector(pos);
			pos.translate (-this.cam.getX(), -this.cam.getY());
		}		

		return new Coordinate (pos.get(0), pos.get(1));
	},

	/**
	 * 
	 */
	worldGeometryToView : function (geometry) {
		var type = geometry.getGeometryType();
		if (type == geomajas.GeometryTypes.POINT) {
			var coordinate = this.worldPointToView(geometry.getCoordinateN(0));
			return this.factory.createPoint(coordinate);
		} else if (type == geomajas.GeometryTypes.LINESTRING) {
			var coords = geometry.getCoordinates();
			var newCoords = [];
			for (var i=0; i<coords.length; i++) {
				var c = this.worldPointToView(coords[i]);
				newCoords[i] = c;
			}
			return this.factory.createLineString(newCoords);
		} else if (type == geomajas.GeometryTypes.LINEARRING) {
			var coords = geometry.getCoordinates();
			var newCoords = [];
			for (var i=0; i<coords.length; i++) {
				var c = this.worldPointToView(coords[i]);
				newCoords[i] = c;
			}
			return this.factory.createLinearRing(newCoords);
		} else if (type == geomajas.GeometryTypes.POLYGON) {
			var ring = this.worldGeometryToView(geometry.getExteriorRing());
			var holes = [];
			for (var i=0; i<geometry.getNumInteriorRing(); i++) {
				holes[i] = this.worldGeometryToView(geometry.getInteriorRingN(i));
			}
			return this.factory.createPolygon(ring, holes);
		} else if (type == geomajas.GeometryTypes.MULTIPOLYGON) {
			var polygons = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				polygons[i] = this.worldGeometryToView(geometry.getGeometryN(i));
			}
			return this.factory.createMultiPolygon(polygons);
		} else if (type == geomajas.GeometryTypes.MULTILINESTRING) {
			var lineStrings = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				lineStrings[i] = this.worldGeometryToView(geometry.getGeometryN(i));
			}
			return this.factory.createMultiLineString(lineStrings);
		}
	},

	/**
	 * 
	 */
	worldGeometryToPan : function (geometry) {
		var type = geometry.getGeometryType();
		if (type == geomajas.GeometryTypes.POINT) {
			var coordinate = this.worldPointToPan(geometry.getCoordinateN(0));
			return this.factory.createPoint(coordinate);
		} else if (type == geomajas.GeometryTypes.LINESTRING) {
			var coords = geometry.getCoordinates();
			var newCoords = [];
			for (var i=0; i<coords.length; i++) {
				var c = this.worldPointToPan(coords[i]);
				newCoords[i] = c;
			}
			return this.factory.createLineString(newCoords);
		} else if (type == geomajas.GeometryTypes.LINEARRING) {
			var coords = geometry.getCoordinates();
			var newCoords = [];
			for (var i=0; i<coords.length; i++) {
				var c = this.worldPointToPan(coords[i]);
				newCoords[i] = c;
			}
			return this.factory.createLinearRing(newCoords);
		} else if (type == geomajas.GeometryTypes.POLYGON) {
			var ring = this.worldGeometryToPan(geometry.getExteriorRing());
			var holes = [];
			for (var i=0; i<geometry.getNumInteriorRing(); i++) {
				holes[i] = this.worldGeometryToPan(geometry.getInteriorRingN(i));
			}
			return this.factory.createPolygon(ring, holes);
		} else if (type == geomajas.GeometryTypes.MULTIPOLYGON) {
			var polygons = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				polygons[i] = this.worldGeometryToPan(geometry.getGeometryN(i));
			}
			return this.factory.createMultiPolygon(polygons);
		} else if (type == geomajas.GeometryTypes.MULTILINESTRING) {
			var lineStrings = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				lineStrings[i] = this.worldGeometryToPan(geometry.getGeometryN(i));
			}
			return this.factory.createMultiLineString(lineStrings);
		}
	},
	/**
	 * Transform an Bbox object from world- to viewspace. This means, given
	 * the original world coords defined by the coordinate system of the application,
	 * what would be the pixel coords in a MapWidget's SVG.
	 *
	 * @param bbox An Bbox in world coords. (ie Lambert72 or Lon-Lat)
	 * @returns A Bbox object with the pixel coords.
	 */
	worldBoundsToView : function (bbox) {
		var origin = this.worldPointToView(bbox.getOrigin());
		var end = this.worldPointToView(bbox.getEndPoint());
		var lineString = this.factory.createLineString([origin, end]);
		return lineString.getBounds();
	},

	/**
	 * Transform a point from view- to worldspace. This means, given the pixel
	 * coords in a MapWidget's SVG, what would be the original world coords
	 * defined by the coordinate system of the application.
	 *
	 * @param point The point of SVG pixel coords.
	 * @returns A Coordinate object with the worldspace coords. (ie Lambert72 or Lon-Lat)
	 */
	viewPointToWorld : function (/*Coordinate*/point) {
        log.debug("WorldViewTransformation.viewPointToWorld");
		var box = this.mapView.getCurrentBbox();
		var transX = -this.cam.getX() + (box.getWidth()/2); // -cam: center X axis around cam. +bbox.w/2: to place the origin in the center of the screen
		var transY = -this.cam.getY() - (box.getHeight()/2); // Inverted Y-axis here...

		var pos = new Vector2D ();
		pos.fromCoordinate (point);
		pos.scale (1/this.mapView.getScale(), -1/this.mapView.getScale());
		pos.translate(-transX, -transY);

		if (this.cam.getAlpha() != 0) {
			var rotMat = Matrix2D.identity();
			rotMat = rotMat.rotate(-this.cam.getAlpha());
			pos.translate (-this.cam.getX(), -this.cam.getY());
			pos = rotMat.multiplyVector(pos);
			pos.translate (this.cam.getX(), this.cam.getY());
		}

		return new Coordinate (pos.get(0), pos.get(1));
	},

	viewGeometryToWorld : function (/*Geometry*/geometry) {
		var coords = geometry.getCoordinates();
		var newCoords = [];
		for (var i=0; i<coords.length; i++) {
			var c = this.viewPointToWorld(coords[i]);
			newCoords[i] = c;
		}
		if (geometry instanceof Point) {
			return this.factory.createPoint(newCoords[0]);
		} else if (geometry instanceof LineString) {
			return this.factory.createLineString(newCoords);
		} else if (geometry instanceof Polygon) {
			var ring = this.factory.createLinearRing(newCoords);
			var holes = [];
			for (var i=0; i<geometry.getNumInteriorRing(); i++) {
				holes[i] = this.viewGeometryToWorld(geometry.getInteriorRingN(i));
			}
			return this.factory.createPolygon(ring, holes);
		} else if (geometry instanceof MultiPolygon) {
			var polygons = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				polygons[i] = this.viewGeometryToWorld(geometry.getGeometryN(i));
			}
			return this.factory.createMultiPolygon(polygons);
		} else if (geometry instanceof MultiLineString) {
			var lineStrings = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				lineStrings[i] = this.viewGeometryToWorld(geometry.getGeometryN(i));
			}
			return this.factory.createMultiLineString(lineStrings);
		}
	},

	/**
	 * Transform an Bbox object from view- to worldspace. This means, given
	 * the pixel coords in a MapWidget's SVG, what would be the original world
	 * coords defined by the coordinate system of the application.
	 *
	 * @param bbox An Bbox in pixel coords.
	 * @returns A Bbox object with the worldspace coords. (ie Lambert72 or Lon-Lat)
	 */
	viewBoundsToWorld : function (bbox) {
		var origin = this.viewPointToWorld(bbox.getOrigin());
		var end = this.viewPointToWorld(bbox.getEndPoint());
		var lineString = this.factory.createLineString([origin, end]);
		return lineString.getBounds();
	},
	
	/**
	 * transforms a bbox with a matrix
	 */
	transformBounds : function (bbox, matrix) {
		var origin = this.transformPoint(bbox.getOrigin(),matrix);
		var end = this.transformPoint(bbox.getEndPoint(),matrix);
		var lineString = this.factory.createLineString([origin, end]);
		return lineString.getBounds();
	},
	
	/**
	 * transforms a point with a matrix
	 */
	transformPoint: function(point, matrix){
		return new Coordinate(
		    matrix.xx * point.getX() + matrix.xy * point.getY() + matrix.dx, 
		    matrix.yx * point.getX() + matrix.yy * point.getY() + matrix.dy
		); 
	}
	
});