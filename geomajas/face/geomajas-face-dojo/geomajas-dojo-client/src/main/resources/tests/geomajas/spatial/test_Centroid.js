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
dojo.require("geomajas.spatial.common");

var GeometryTypes = 
{   
	POINT : "Point",
	LINESTRING : "LineString",
	LINEARRING : "LinearRing",
	POLYGON : "Polygon",
	MULTILINESTRING : "MultiLineString",
	MULTIPOLYGON : "MultiPolygon"
};

dojo.provide("tests.geomajas.spatial.test_Centroid");

tests.register("tests.geomajas.spatial.test_Centroid", [

function test_centroid (t) {
	var fac = new GeometryFactory ();
	var ring = fac.createLinearRing([new Coordinate(5,5), new Coordinate(25,10), new Coordinate(15,30), new Coordinate(0,25)]); // shell
	
	var mathLib = new MathLib();
	var c = ring.getCentroid();
	t.assertTrue(mathLib.isWithin(ring, c));
}

]);
