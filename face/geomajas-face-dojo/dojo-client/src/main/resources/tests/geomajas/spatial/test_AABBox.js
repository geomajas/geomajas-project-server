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
dojo.require("geomajas.spatial.Bbox");

dojo.provide("tests.geomajas.spatial.test_Bbox");

tests.register("tests.geomajas.spatial.test_Bbox", [

function test_Bbox_intersects(t){
	var bb1 = new Bbox(0,1,0,1);
	//	test self-intersection
	t.assertTrue(bb1.intersects(bb1));
	var bb1 = new Bbox(0,0,2,2);
	var bb2 = new Bbox(1,1,2,2);
	var b12 = bb1.intersection(bb2);
	t.assertEqual(1, b12.getX());
	t.assertEqual(1, b12.getY());
	t.assertEqual(2, b12.getEndPoint().getX());
	t.assertEqual(2, b12.getEndPoint().getY());
	
},

function test_split(t){
	var id = "1.2.3";
	var ids = id.split(".");
	t.assertEqual("1",ids[0]);
	t.assertEqual("2",ids[1]);
	t.assertEqual("3",ids[2]);
}

]);
