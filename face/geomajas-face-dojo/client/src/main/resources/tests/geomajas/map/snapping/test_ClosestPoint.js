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
dojo.require("geomajas.spatial.geometry.common");
dojo.require("geomajas.map.snapping.common");

dojo.provide("tests.geomajas.map.snapping.test_ClosestPoint");

tests.register("tests.geomajas.map.snapping.test_ClosestPoint", [

function test_Points(t){
	var factory = new GeometryFactory(31300,2);
	var geometries =  [];
	for(var i = 0; i <= 10000; i++){
		geometries.push(factory.createPoint(new Coordinate(Math.random(),Math.random())));
	}
	var alg = new ClosestPoint(geometries, 0.01);
	var snap = alg.snap(new Coordinate(0.5,0.5),0.01);	
}

]);
