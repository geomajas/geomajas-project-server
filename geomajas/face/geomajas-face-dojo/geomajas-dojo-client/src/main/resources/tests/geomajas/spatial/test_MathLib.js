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
