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
dojo.require("geomajas.spatial.cache.QuadTree");
dojo.require("geomajas.map.FeatureNode");

dojo.provide("tests.geomajas.spatial.cache.test_QuadTree");

tests.register("tests.geomajas.spatial.cache.test_QuadTree", [

function test_morton(t) {
	doh.debug("test_morton");
	var b1 = new Bbox(160000, 165000, 5000, 5000);
	var b2 = new Bbox(162274,167000,951,500);
	var tree = new QuadTree(b1,6);
	all = tree._calcAllMorton(b2);
	t.assertEqual("033", all[0]);
},


function test_QuadTree_morton(t){
	var b1 = new Bbox(0,0,1,1);
	var tree = new QuadTree(b1,1);
	t.assertEqual("0", tree._calcMorton(new Coordinate(0.25,0.25)) );
	t.assertEqual("1", tree._calcMorton(new Coordinate(0.75,0.25)) );
	t.assertEqual("2", tree._calcMorton(new Coordinate(0.25,0.75)) );
	t.assertEqual("3", tree._calcMorton(new Coordinate(0.75,0.75)) );
	tree = new QuadTree(b1,2);
	t.assertEqual("03", tree._calcMorton(new Coordinate(0.3,0.3)) );
	t.assertEqual("12", tree._calcMorton(new Coordinate(0.7,0.3)) );
	t.assertEqual("21", tree._calcMorton(new Coordinate(0.3,0.7)) );
	t.assertEqual("30", tree._calcMorton(new Coordinate(0.7,0.7)) );
	var all = tree._calcAllMorton(new Bbox(0.3,0.3,0.4,0.4))
	t.assertEqual(4, all.length );
	tree = new QuadTree(b1,0);
	all = tree._calcAllMorton(new Bbox(0.3,0.3,0.4,0.4))
	t.assertEqual(1, all.length );
	t.assertEqual("", all[0] );
},

function test_Cache(t){
	var b1 = new Bbox(0,0,1,1);
	var tree = new QuadTree(b1,2);
	var nodes = tree.isInCache(new Bbox(0.3,0.3,0.4,0.4));
	t.assertEqual(4, nodes.length);
	var tree = new QuadTree(b1,10);
	t.assertEqual(4, nodes.length);
},

function test_Visit(t){
	var b1 = new Bbox(0,0,1,1);
	var tree = new QuadTree(b1,2);
	var fnode = new FeatureNode();
	fnode.setId("hideho");
	var node = tree.getOrCreateNodeForBounds(new Bbox(0.8,0.6,0.1,0.1));
	node.setData(fnode);	
	t.assertEqual("31", node.getCode());
	var result = [];
	tree.query(new Bbox(0.5,0.5,0.5,0.5),function(node) {
		if(node.getData() != null){
			result.push(node.getData());
		}
	});
	t.assertEqual(1, result.length);
	t.assertEqual("hideho", result[0].getId());
	t.assertEqual(3, tree.size());
		
},

function test_Cache2(t){
	var b1 = new Bbox(0,0,400000,400000);
	var tree = new QuadTree(b1,10);
	var nodes = tree.isInCache(new Bbox(-154761.90476190473,0,709523.8095238095,400000));
	for(var i = 0; i<nodes.length; i++){
		doh.debug("test_Cache2 "+nodes[i].getCode()+","+nodes[i].getBounds());
	}
}

]);

