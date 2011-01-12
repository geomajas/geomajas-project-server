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
