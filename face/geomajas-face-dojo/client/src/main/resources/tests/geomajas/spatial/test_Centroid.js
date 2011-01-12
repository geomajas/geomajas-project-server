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
