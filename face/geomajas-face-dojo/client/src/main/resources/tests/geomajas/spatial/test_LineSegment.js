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

dojo.provide("tests.geomajas.spatial.test_LineSegment");

tests.register("tests.geomajas.spatial.test_LineSegment", [

function test_intersects (t) {

	var c1 = new Coordinate (5, 5);
	var c2 = new Coordinate (25, 10);
	var c3 = new Coordinate (10, 15);
	var c4 = new Coordinate (20, 0);

	var ls1 = new LineSegment (c1, c2);
	var ls2 = new LineSegment (c3, c4);

	t.assertTrue(ls1.intersects(ls2));
},

function test_intersectionPoint (t) {

	var c1 = new Coordinate (5, 5);
	var c2 = new Coordinate (25, 10);
	var c3 = new Coordinate (10, 15);
	var c4 = new Coordinate (20, 0);

	var ls1 = new LineSegment (c1, c2);
	var ls2 = new LineSegment (c3, c4);

	t.assertEqual(new Coordinate(15, 7.5), ls1.getIntersection(ls2));
},

function test_distance (t) {

	var c1 = new Coordinate (5, 5);
	var c2 = new Coordinate (25, 10);
	var c3 = new Coordinate (0, 25);

	var ls1 = new LineSegment (c1, c2);

	t.assertEqual(Math.sqrt(425), ls1.distance(c3));
}

]);
