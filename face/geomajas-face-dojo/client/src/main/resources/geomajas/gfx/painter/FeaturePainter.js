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
		
		// clipped and normal drawing is the same now ?
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
			options.styleId = feature.styleId;
			graphics.drawSymbol (viewGeometry,options);
		}
	},

	/**
	 * Deletes a features from the map. Also deletes the label.
	 */
	deleteShape : function (/*Feature*/feature, /*GraphicsContext*/graphics) {
		graphics.deleteShape(feature.getSelectionId());
	}
});