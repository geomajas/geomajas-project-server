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
