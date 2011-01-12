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
dojo.require("geomajas.spatial.cache.QuadNode");

dojo.provide("tests.geomajas.spatial.cache.test_QuadNode");

tests.register("tests.geomajas.spatial.cache.test_QuadNode", [
	function test_QuadNode_getOrAddDescendant(t){
		var b1 = new Bbox(0,0,1,1);
		var root = new QuadNode(b1,null);
		var node = root.getOrCreateDescendant("0");
		//	test self-intersection
		t.assertEqual( node, root.getChild(0) );
	}
]);

