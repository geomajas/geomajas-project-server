dojo.provide("geomajas.map.print.PrintController");
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
dojo.require("geomajas.event.MouseListener");

dojo.declare("PrintController", MouseListener, {
	

	/**
	 * @fileoverview Mouselistener for controlling the editing of the print template.
	 * @class A MouseListener for controlling the editing of the print template.
	 * Allows editing of bounds (ppt-alike) and view ports.
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param templateManager A reference to the print template manager.
	 */
	constructor : function (map, title) {
		/** The current component */
		this.component = null;
		/** The current action */
		this.action = null;
		/** model */
		this.mapModel = map.getMapModel();
		/** map */
		this.map = map;
		/** floater for widget */
		this.floater = null;
		/** title */
		this.title = title;
		/** delegate pan controller */
		this.panController = new PanController(map,true);

	},

	/**
	 * Get the name of this controller.
	 * @returns A unique name.
	 */
	getName : function () {
		return "PrintController";
	},

	/**
	 * This function is called on a "onClick" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		log.info("clicked "+event.getTargetId());
		var comp = this._getComponentFromEvent(event);
		if(this.component == null) {
			if(comp != null){
				log.info("setting editing "+event.getTargetId());
				comp.setEditing(event.getTargetId());
				this.component = comp;
				dojo.publish(this.mapModel.getRenderTopic(), [comp, "update"]);
			}
		} else {
			log.info("setting idle "+event.getTargetId());
			this.component.setIdle();
			dojo.publish(this.mapModel.getRenderTopic(), [this.component, "delete"]);
			dojo.publish(this.mapModel.getRenderTopic(), [this.component, "update"]);
			this.component = null;
		}
	},

	/**
	 * This function is called on a "onMouseOver" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseEntered : function (/*HtmlMouseEvent*/event) {
		log.info("entered "+event.getTargetId());
		var comp = this._getComponentFromEvent(event);
		if(this.component == null) {
			if(comp != null){
				log.info("setting hovered "+event.getTargetId());
				comp.setHovered();
				dojo.publish(this.mapModel.getRenderTopic(), [comp, "update"]);
			}
		} else {
			var corner = this._getCornerFromEvent(event);
			if(corner != null){
				this._setCornerCursor(corner);
			}
		}
	},

	/**
	 * This function is called on a "onMouseOut" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseExited : function (/*HtmlMouseEvent*/event) {
		log.info("exited "+event.getTargetId());
		var comp = this._getComponentFromEvent(event);
		if(this.component == null) {
			if(comp != null){
				if(!comp.isIdle()){
					log.info("setting idle "+event.getTargetId());
					comp.setIdle();
					dojo.publish(this.mapModel.getRenderTopic(), [comp, "update"]);
				}
			}
		} else {
			if(!this.component.isDragging()){
				this.map.setCursor("default");
			}
		}
	},

	/**
	 * This function is called on a "onMouseDown" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
		log.info("Printcontroller mousePressed");
		if(this.component != null) {
			var corner = this._getCornerFromEvent(event);
			var comp = this._getComponentFromEvent(event);
			var printPosition = geomajasConfig.printManager.getTransform().transformCoordinateViewToPrint(event.getPosition());
			if(corner != null){
				log.info("start dragging "+event.getTargetId());
				this.map.setCursor("crosshair");
				this.component.startDragging(corner, printPosition);
				dojo.publish(this.mapModel.getRenderTopic(), [this.component, "update"]);
			} else if(this.component === comp) {
				this.map.setCursor("move");
				this.component.startDragging("component", printPosition);
				dojo.publish(this.mapModel.getRenderTopic(), [this.component, "update"]);
			}
		} else {
			this.panController.mousePressed(event);
		}
	},

	/**
	 * This function is called on a "onMouseUp" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if(this.component != null) {
			if(this.component.isDragging()){
			log.info("stop dragging "+event.getTargetId());
				var printPosition = geomajasConfig.printManager.getTransform().transformCoordinateViewToPrint(event.getPosition());
			this.component.stopDragging(printPosition);
			log.info("default for  "+event.getTargetId());
			this.map.setCursor("default");
			dojo.publish(this.mapModel.getRenderTopic(), [this.component, "update"]);
			}
		} else {
			this.panController.mouseReleased(event);
		}
	},

	/**
	 * This function is called on a "onMouseMove" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if(this.component != null) {
			if(this.component.isDragging()){
				log.info("continue dragging "+event.getTargetId());
				var printPosition = geomajasConfig.printManager.getTransform().transformCoordinateViewToPrint(event.getPosition());
				this.component.continueDragging(printPosition);
				dojo.publish(this.mapModel.getRenderTopic(), [this.component, "update"]);
			}
		} else {
			this.panController.mouseMoved(event);
		}
	},

	/**
	 * This function is called on a "oncontextmenu" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	contextMenu : function (/*HtmlMouseEvent*/event) {
	},
	
	/**
	 * This function is called on a "ondblclick" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	doubleClick : function (event) {
	},

	/**
	 * This function is called when the MouseListener is activated.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	onActivate : function () {
		if (!dijit.byId("templatePrintFloater")) {
			this.floater = new geomajas.widget.FloatingPane({
				id:"templatePrintFloater",
				title: this.title,
				dockable: true,maxable: false,
				closable: false,
				resizable: true
			}, null);
			// content injection works !
			this.floater.setContent("<div id='templatePrinter' dojoType='geomajas.widget.TemplatePrintWidget' style='width:100%;height:100%'></div>"); 
			var templatePrinter = dijit.byId("templatePrinter");
			templatePrinter.setSourceMap(this.map);
			this.floater.startup();
			var div = dojo.body();
			if (geomajasConfig.connectionPoint) {
				var div = dojo.byId(geomajasConfig.connectionPoint);
			}
			div.appendChild (this.floater.domNode);
		}
		this.floater.resize({ w:400, h:600, l:200, t:40 });
		this.floater.show();
		this.floater.bringToTop();
	},
	
	/**
	 * This function is called when the MouseListener is deactivated.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	onDeactivate : function () {
		this.floater.forcedClose();
		this.floater=null;
	},
	
	_getComponentFromEvent : function (event) {
		var id = event.getTargetId();
		if(id.endsWith(".bounds")){
			var template = geomajasConfig.printManager.getCurrentPrint();
			var comp = template.getBestMatchingComponentById(id);
			if(comp !== template.getPage()){
				return comp;
			} else {
				return null;
			}
			log.info("component = "+comp.getId());			
		}
	},
	
	_getCornerFromEvent : function (event) {
		var id = event.getTargetId();
		var compId = this.component.getId();
		if(id.startsWith(compId) && id.endsWith("_hdl")) {
			return id.substring(id.length-6,id.length-4);
		} else {
			return null;
		}
	},
	
	_setCornerCursor : function (corner) {
		if(corner == "bl") {
			this.map.setCursor("sw-resize");		
		} else if(corner == "br") {
			this.map.setCursor("se-resize");		
		} else if(corner == "tl") {
			this.map.setCursor("nw-resize");		
		} else if(corner == "tr") {
			this.map.setCursor("ne-resize");		
		} else if(corner == "bm") {
			this.map.setCursor("s-resize");		
		} else if(corner == "tm") {
			this.map.setCursor("n-resize");		
		} else if(corner == "lm") {
			this.map.setCursor("w-resize");		
		} else if(corner == "rm") {
			this.map.setCursor("e-resize");		
		}  
	}
	
	

});
