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
dojo.registerModulePath("majas","../../majas");
dojo.registerModulePath("gegis","../../gegis");
dojo.registerModulePath("tests","../../tests");

dojo.provide("tests._base");

try{
	dojo.require("tests.majas.map.snapping.test_ClosestPoint");
	dojo.require("tests.majas.util.test_BinarySearch");
	dojo.require("tests.majas.spatial.test_Bbox");
	dojo.require("tests.majas.spatial.test_Centroid");
	dojo.require("tests.majas.spatial.test_LineSegment");
	dojo.require("tests.majas.spatial.test_MathLib");
	dojo.require("tests.majas.spatial.cache.test_QuadNode");
	dojo.require("tests.majas.spatial.cache.test_QuadTree");
}catch(e){
	doh.debug(e);
}

