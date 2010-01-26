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

