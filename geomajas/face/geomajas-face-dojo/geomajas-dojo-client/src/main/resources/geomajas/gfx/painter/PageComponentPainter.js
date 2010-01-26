dojo.provide("geomajas.gfx.painter.PageComponentPainter");
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
dojo.require("geomajas.gfx.Painter");

dojo.declare("PageComponentPainter", BaseComponentPainter, {

	/**
	 * @class Painter implementation for page. This painter always
	 * paints in viewspace!
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapView) {
		this.transform = new WorldViewTransformation(mapView);
	},
	
	/**
	 * The actual painting function. Sets the modus to viewspace, applies the
	 * object's style, and then draws the image with the object's id.
	 * @param component A page component.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*PageComponent*/component, /*GraphicsContext*/graphics) {
		if(!this.mapView.isPanning()){
			graphics.deleteShape(component.getId()+".border",true);
		}
		graphics.drawGroup(component.getId()+".border");
		this._drawBorder(component, component.getId()+".border", graphics);
	},
	
	_getViewRect : function (component, id) {
		var rect = new Rectangle(id);
		var bounds = component.getBounds();
		var viewBounds = component.getViewBounds();;
		rect.setPosition(viewBounds.getOrigin());
		rect.setWidth(viewBounds.getWidth());
		rect.setHeight(viewBounds.getHeight());
		return rect;
	},
	
	_drawBorder : function (component, id, graphics) {
		var bounds = component.getBounds();
		var compBounds = component.getViewBounds();
		var mapBounds = this.transform.worldBoundsToView(this.mapView.getCurrentBbox());
		// tedious, draw the border slabs
		var borderStyle = new ShapeStyle("#787878","1","#787878","1","0",null,null)
		if(mapBounds.getEndY() > compBounds.getEndY()){
			var rectBottom = new Rectangle(id+".bottom");
			rectBottom.setPosition(new Coordinate(mapBounds.getOrigX(),compBounds.getEndY()));
			rectBottom.setWidth(mapBounds.getWidth());
			rectBottom.setHeight(mapBounds.getEndY()-compBounds.getEndY());
			rectBottom.setStyle(borderStyle);
			graphics.drawRectangle(rectBottom, {id:rectBottom.getId(),style:rectBottom.getStyle()});
		} else {
			graphics.deleteShape(id+".bottom");
		}
		if(mapBounds.getOrigY() < compBounds.getOrigY()){
			var rectTop = new Rectangle(id+".top");
			rectTop.setPosition(new Coordinate(mapBounds.getOrigX(),mapBounds.getOrigY()));
			rectTop.setWidth(mapBounds.getWidth());
			rectTop.setHeight(compBounds.getOrigY()-mapBounds.getOrigY());
			rectTop.setStyle(borderStyle);
			graphics.drawRectangle(rectTop, {id:rectTop.getId(),style:rectTop.getStyle()});
		} else {
			graphics.deleteShape(id+".top");
		}
		if(mapBounds.getOrigX() < compBounds.getOrigX()){
			var rectLeft = new Rectangle(id+".left");
			rectLeft.setPosition(new Coordinate(mapBounds.getOrigX(),mapBounds.getOrigY()));
			rectLeft.setWidth(compBounds.getOrigX()-mapBounds.getOrigX());
			rectLeft.setHeight(mapBounds.getHeight());
			rectLeft.setStyle(borderStyle);
			graphics.drawRectangle(rectLeft, {id:rectLeft.getId(),style:rectLeft.getStyle()});
		} else {
			graphics.deleteShape(id+".left");
		}
		if(mapBounds.getEndX() > compBounds.getEndX()){
			var rectRight = new Rectangle(id+".right");
			rectRight.setPosition(new Coordinate(compBounds.getEndX(),mapBounds.getOrigY()));
			rectRight.setWidth(mapBounds.getEndX()-compBounds.getEndX());
			rectRight.setHeight(mapBounds.getHeight());
			rectRight.setStyle(borderStyle);
			graphics.drawRectangle(rectRight, {id:rectRight.getId(),style:rectRight.getStyle()});
		} else {
			graphics.deleteShape(id+".right");
		}
	}

});
