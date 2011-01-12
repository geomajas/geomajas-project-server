dojo.provide("geomajas.gfx.painter.BaseComponentPainter");
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
dojo.requireLocalization("geomajas.gfx.painter", "BaseComponentPainter");

dojo.declare("BaseComponentPainter", Painter, {

	/**
	 * @class Painter implementation for text. This painter always
	 * paints in viewspace!
	 * @author Pieter De Graef
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
		log.info("painting "+component.declaredClass);
		// redraw the groups
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
			rect = this._getViewRect(component, component.getId()+".idle");
			log.info("drawing rect "+rect);
			this._drawIdle(graphics,rect);
		} else if (component.isHovered()){
			rect = this._getViewRect(component, component.getId()+".idle");
			this._drawHover(graphics,rect);
		} else if (component.isEditing()){
			rect = this._getViewRect(component, component.getId()+".idle");
			this._drawIdle(graphics,rect);
			rect = this._getViewRect(component, component.getId()+".edit");
			this._drawEditing(graphics,rect);
			if(component.action != null) {
				var editRect = this._getEditRect(component, component.getId()+".action");
				this._drawAction(graphics,editRect,component.action.bounds);
			}
		}
		if(component.getTag()){
			var locale = dojo.i18n.getLocalization("geomajas.gfx.painter", "BaseComponentPainter");
			if(dojo.isIE != 0){
				var textId = component.getId()+".idle.bounds.text_0_"+component.getLabel();
			} else {
				var textId = component.getId()+".text.0";
			}
			var text = new Text(textId, locale[component.getLabel()]);
			text.setStyle(new FontStyle("black"));
			var origin = rect.getPosition().clone();
			text.setPosition(origin);
			graphics.drawText(text,{id:text.getId(),style:text.getStyle()});
		}
	},
	
	_getViewRect : function (component, id) {
		var rect = new Rectangle(id);
		var bounds = component.getBounds();
		var viewBounds = component.getViewBounds();
		rect.setPosition(viewBounds.getOrigin());
		rect.setWidth(viewBounds.getWidth());
		rect.setHeight(viewBounds.getHeight());
		return rect;
	},
	
	_getEditRect : function (component, id) {
		var rect = new Rectangle(id);
		var editBounds = component.getEditBounds();
		rect.setPosition(editBounds.getOrigin());
		rect.setWidth(editBounds.getWidth());
		rect.setHeight(editBounds.getHeight());
		return rect;
	},

	_drawIdle : function (graphics, rect) {
		// draw the  bounds in view coordinates
		rect.setStyle(new ShapeStyle("#000000", "0.0", "#000000", "1", "1", null, null));
		graphics.drawRectangle (rect,{ id:rect.getId()+".bounds", style:rect.getStyle()});
	},
	
	_drawHover : function (graphics, rect) {
		// draw the  bounds in view coordinates
		rect.setStyle(new ShapeStyle("#0000FF", "0.2", "#0000FF", "1", "2", null, null));
		graphics.drawRectangle (rect,{ id:rect.getId()+".bounds", style:rect.getStyle()});
	},
	
	_drawEditing : function (graphics, rect) {
		var editRect = rect.clone();
		// draw the bounds in view coordinates
		var s = new ShapeStyle("#0000FF", "0.5", "#000000", "1", "1", null, null);
		editRect.setStyle(s);
		graphics.drawRectangle (editRect,{ id:editRect.getId()+".bounds", style:editRect.getStyle()});
		// draw the handles
		var handles = editRect.getEditHandles();
		for(var i = 0; i < handles.length; i++){
			// draw the handle
			handles[i].setStyle(new ShapeStyle("#FFFFFF", "1", "#000000", "1", "1", null, null));
			graphics.drawRectangle (handles[i],{ id:handles[i].getId(), style:handles[i].getStyle()});
		}
	},

	_drawAction : function (graphics, rect, bounds) {
		// draw the  bounds in view coordinates
		rect.setStyle(new ShapeStyle("#000000", "0.5", "#000000", "1", "1", "5,2", null));
		graphics.drawRectangle (rect,{ id:rect.getId()+".bounds", style:rect.getStyle()});
		// draw the coordinate info
		if(dojo.isIE != 0){
			var textId = rect.getId()+".bounds.text";
		} else {
			var textId = rect.getId()+".text.0";
		}
		var text = new Text(textId, bounds.toString());
		text.setStyle(new FontStyle("white"));
		text.setPosition(new Coordinate(0,0));
		graphics.drawText(text,{id:text.getId(),style:text.getStyle()});
	}

});
