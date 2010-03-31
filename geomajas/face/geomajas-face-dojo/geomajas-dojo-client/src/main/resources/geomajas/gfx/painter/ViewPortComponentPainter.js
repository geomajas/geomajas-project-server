dojo.provide("geomajas.gfx.painter.ViewPortComponentPainter");
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

dojo.declare("ViewPortComponentPainter", BaseComponentPainter, {

	/**
	 * @class Painter implementation for view port component. This painter always
	 * paints in viewspace!
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapView) {
		this.mapView = mapView;
	},
	
	/**
	 * The actual painting function. Sets the modus to viewspace, applies the
	 * object's style, and then draws the image with the object's id.
	 * @param component A base component.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*BaseComponent*/component, /*GraphicsContext*/graphics) {
		// redraw the groups
		log.info("painting "+component.declaredClass);
		if(!component.isDragging()){
			graphics.deleteShape(component.getId()+".edit",true);
			graphics.deleteShape(component.getId()+".action",true);
		}
		graphics.drawGroup(component.getId()+".idle");
		graphics.drawGroup(component.getId()+".edit");
		graphics.drawGroup(component.getId()+".action");
		graphics.drawGroup(component.getId()+".text");
		var rect = null;
		if (component.isIdle()){
			rect = this._getViewRect(component, component.getId()+".idle.main");
			this._drawIdle(graphics,rect);
			rect = this._getViewRectForPort(component, component.getId()+".idle.port");
			this._drawIdle(graphics,rect);
		} else if (component.isHovered()){
			rect = this._getViewRect(component, component.getId()+".idle.main");
			this._drawHover(graphics,rect);
			rect = this._getViewRectForPort(component, component.getId()+".idle.port");
			this._drawHover(graphics,rect);
		} else if (component.isEditing()){
			rect = this._getViewRect(component, component.getId()+".idle.main");
			this._drawIdle(graphics,rect);
			rect = this._getViewRectForPort(component, component.getId()+".idle.port");
			this._drawIdle(graphics,rect);
			rect = this._getEditingViewRect(component, component.getId()+".edit");
			this._drawEditing(graphics,rect);
			if(component.action != null) {
				var editRect = this._getEditRect(component, component.getId()+".action");
				this._drawAction(graphics,editRect,component.action.bounds);
			}
		}
		// view label
		if(dojo.isIE != 0){
			var textId = component.getId()+".idle.main.bounds.text_0_"+component.getLabel();
		} else {
			var textId = component.getId()+".text.0";
		}
		var text = new Text(textId, component.getLabel());
		text.setStyle(new FontStyle("black"));
		var origin = component.getViewBounds().getOrigin().clone();
		text.setPosition(origin);
		graphics.drawText(text,{id:text.getId(),style:text.getStyle()});
		// port label
		if(dojo.isIE != 0){
			var textId = component.getId()+".idle.port.bounds.text_0_"+component.getLabel();
		} else {
			var textId = component.getId()+".text.port.0";
		}
		var text = new Text(textId, component.getLabel()+"-area");
		text.setStyle(new FontStyle("black"));
		var origin = component.getPortViewBounds().getOrigin().clone();
		text.setPosition(origin);
		graphics.drawText(text,{id:text.getId(),style:text.getStyle()});
	},
	
	_getViewRectForPort : function (component, id) {
		var rect = new Rectangle(id);
		var viewBounds = component.getPortViewBounds();
		rect.setPosition(viewBounds.getOrigin());
		rect.setWidth(viewBounds.getWidth());
		rect.setHeight(viewBounds.getHeight());
		return rect;
	},
		
	_getEditingViewRect : function (component, id) {
		if(component.isEditingPort()){
			var rect = new Rectangle(id);
		} else {
			var rect = new Rectangle(id, true);
		}
		var viewBounds = component.getEditingViewBounds();
		rect.setPosition(viewBounds.getOrigin());
		rect.setWidth(viewBounds.getWidth());
		rect.setHeight(viewBounds.getHeight());
		return rect;
	}

	
});
