dojo.provide("geomajas.gfx.painter.PageComponentPainter");
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
