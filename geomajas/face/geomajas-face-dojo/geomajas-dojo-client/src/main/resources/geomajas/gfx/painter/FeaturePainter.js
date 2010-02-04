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

dojo.provide("geomajas.gfx.painter.FeaturePainter");
dojo.require("geomajas._base");
dojo.require("geomajas.gfx.Painter");

dojo.declare("FeaturePainter", Painter, {

	/**
	 * @class Painter implementation for features. This painter always paints
	 * in worldspace!
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapWidget, mapView) {
		this.mapWidget = mapWidget;
		this.transform = new WorldViewTransformation(mapView);
	},
	
	/**
	 * The actual painting function. Currently paints selected features only.
	 * @param feature A Paintable object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (feature, graphics) {
		var geometry = feature.getGeometry();
		
		// selected features only
		if(!feature.isSelected()){
			return;
		}
		
		var id = feature.getSelectionId();
		// no updates, delete first to update !!! => PDG: why is this??? speed perhaps...
//		if(graphics.getElementById(id)){
//			return;
//		}
		
		log.info("FeaturePainter : painting feature "+feature.getId());
		// Style is shared reference, so we have to clone it before we can change it !
		var style = feature.getStyle().clone();
					
		if(geometry.declaredClass == geomajas.GeometryTypes.POLYGON){
			style.copyFrom(this.mapWidget.getPolygonSelectStyle());
		}
		if(geometry.declaredClass == geomajas.GeometryTypes.LINESTRING){
			style.copyFrom(this.mapWidget.getLineSelectStyle());
		}
		if(geometry.declaredClass == geomajas.GeometryTypes.POINT){
			style.copyFrom(this.mapWidget.getPointSelectStyle());
		}
		if(geometry.declaredClass == geomajas.GeometryTypes.MULTIPOLYGON){
			style.copyFrom(this.mapWidget.getPolygonSelectStyle());
		}
		if(geometry.declaredClass == geomajas.GeometryTypes.MULTILINESTRING){
			style.copyFrom(this.mapWidget.getLineSelectStyle());
		}

		var options = {
			id : id, 
			style: style,
			worldspace : true
		};
		
		
		if(feature.isClipped() && feature.getPath()){
			log.info("FeaturePainter : painting with (clipped) path");
			options.path = feature.getPath();
			if(geometry.declaredClass == geomajas.GeometryTypes.POLYGON){
				graphics.drawPolygon (null,options);
			} else if (geometry.declaredClass == geomajas.GeometryTypes.LINESTRING) {
				graphics.drawLine (null,options);
			} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTIPOLYGON) {
				graphics.drawPolygon (null,options);
			} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTILINESTRING) {
				graphics.drawLine (null,options);		
			} else if (geometry.type == geomajas.GeometryTypes.POINT) {
				graphics.drawSymbol (null,options);
			}
		} else {
			var viewGeometry = this.transform.worldGeometryToPan(geometry);
			if(geometry.declaredClass == geomajas.GeometryTypes.POLYGON){
				graphics.drawPolygon (viewGeometry,options);
			} else if (geometry.declaredClass == geomajas.GeometryTypes.LINESTRING) {
				graphics.drawLine (viewGeometry,options);
			} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTIPOLYGON) {
				graphics.drawPolygon (viewGeometry,options);
			} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTILINESTRING) {
				graphics.drawLine (viewGeometry,options);
			} else if (geometry.type == geomajas.GeometryTypes.POINT) {
				options.styleId = feature.getLayer().getId() + "." + feature.styleId + ".style";
				graphics.drawSymbol (viewGeometry,options);
			}
		}
	},

	/**
	 * Deletes a features from the map. Also deletes the label.
	 */
	deleteShape : function (/*Feature*/feature, /*GraphicsContext*/graphics) {
		graphics.deleteShape(feature.getSelectionId());
	}
});