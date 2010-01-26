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
