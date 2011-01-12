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

dojo.provide("tests.geomajas.spatial.test_MathLib");


var GeometryTypes = 
{   
	POINT : "Point",
	LINESTRING : "LineString",
	LINEARRING : "LinearRing",
	POLYGON : "Polygon",
	MULTILINESTRING : "MultiLineString",
	MULTIPOLYGON : "MultiPolygon"
};

tests.register("tests.geomajas.spatial.test_MathLib", [

function test_isWithin (t) {
	var fac = new GeometryFactory ();
	var ring1 = fac.createLinearRing([new Coordinate(5,5), new Coordinate(25,10), new Coordinate(15,30), new Coordinate(0,25)]); // shell
	var ring2 = fac.createLinearRing([new Coordinate(10,10), new Coordinate(5,25), new Coordinate(20,15)]); // hole

	var polygon = fac.createPolygon(ring1, [ring2]);

	var c1 = new Coordinate (12, 17); // zeker buiten!!
	var c2 = new Coordinate (12, 23); // zeker binnen!!
	
	var mathLib = new MathLib();
	t.assertFalse(mathLib.isWithin(polygon, c1));
	t.assertTrue(mathLib.isWithin(polygon, c2));
}

]);
