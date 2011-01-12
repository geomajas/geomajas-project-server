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
dojo.require("geomajas.util.BinarySearch");

dojo.provide("tests.geomajas.util.test_BinarySearch");

tests.register("tests.geomajas.util.test_BinarySearch", [

function test_Simple(t){
	var ar = [3,2,4,1];	
	var increasing = function(a,b){return (a-b);};
	ar.sort(increasing);
	var ar2 = [1,2,3,4];	
	t.assertEqual(ar,ar2);
	var index = ar.binarySearch(increasing, 2.5);
	t.assertEqual(2,index);
	var index = ar.binarySearch(increasing, 0);
	t.assertEqual(0,index);
	var index = ar.binarySearch(increasing, 7);
	t.assertEqual(4,index);
},

function test_Coords(t){
	var ar = [
		new Coordinate(0,0),
		new Coordinate(1,1),
		new Coordinate(7,4),
		new Coordinate(6,2)
	];	
	var increasingX = function(a,b){return (a.getX()-b.getX());};
	ar.sort(increasingX);
	var ar2 = [
		new Coordinate(0,0),
		new Coordinate(1,1),
		new Coordinate(6,2),
		new Coordinate(7,4)
	];	
	t.assertEqual(ar,ar2);
	var index = ar.binarySearch(increasingX,new Coordinate(6.5,-1));
	t.assertEqual(3,index);
}


]);

