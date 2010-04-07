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

dojo.provide("geomajas.gfx.menu.Menu");
dojo.require("dijit.Menu");
dojo.require("geomajas.action.Action");


dojo.declare("Menu", dijit.Menu, {

	templateString:
			'<table class="dijit dijitMenu dijitReset dijitMenuTable" waiRole="menu" dojoAttachEvent="onkeypress:_onKeyPress,oncontextmenu:onContextMenu">' +
				'<tbody class="dijitReset" dojoAttachPoint="containerNode"></tbody>'+
			'</table>',

	postCreate: function() {
		this.inherited("postCreate", arguments);
		// dojo unloading seems buggy
		if(dojo.isIE) {
			window.attachEvent ("onunload",  dojo.hitch(this, "destroyRecursive"));
		}		
	},

	_openMyself: function(/*Event*/ e){
		log.info("_openMyself(e)");
		// summary:
		//		Internal function for opening myself when the user
		//		does a right-click or something similar
		dojo.stopEvent(e);

		// Get coordinates.
		// if we are opening the menu with the mouse or on safari open
		// the menu at the mouse cursor
		// (Safari does not have a keyboard command to open the context menu
		// and we don't currently have a reliable way to determine
		// _contextMenuWithMouse on Safari)
		var x,y;
		// always use page !!!!
		if (e.pageX > 0)
		{
			//DVB: this returns the coords of vml underneath the pointer which are no good as those are not in screen coordinates
			//try using page coords instead; it might be necessary to obtain those from the mapdiv though.
			x=e.pageX;
			y=e.pageY;
		}
		else
		{
			// otherwise open near e.target
			var coords = dojo.coords(e.target, true);
			x = coords.x + 10;
			y = coords.y + 10;
		}

		var self=this;
		var savedFocus = dijit.getFocus(this);
		function closeAndRestoreFocus(){
			// user has clicked on a menu or popup
			//dijit.focus(savedFocus); // can sometimes cause the map to turn completely blank in IE 6.
			dijit.popup.close(self);
		}
		dijit.popup.open({
			popup: this,
			x: x,
			y: y,
			onExecute: closeAndRestoreFocus,
			onCancel: closeAndRestoreFocus,
			orient: this.isLeftToRight() ? 'L' : 'R'
		});
		this.focus();

		// Hack that fixes the IE memory leak.
		// _onBlur is already defined in superclass (dijit.Menu) but no idea why this causes the leak.
		/*
		this._onBlur = function(){
			// Usually the parent closes the child widget but if this is a context
			// menu then there is no parent
			dijit.popup.close(this);
			// don't try to restore focus; user has clicked another part of the screen
			// and set focus there
		}
		*/
	},

	onContextMenu: function (e) {
		dojo.stopEvent(e);
	}
});



dojo.declare("geomajas.gfx.menu.MenuItem", dijit.MenuItem, {

	templateString:
		 '<tr class="dijitReset dijitMenuItem"'
		+'dojoAttachEvent="onmouseenter:_onHover,onmouseleave:_onUnhover,ondijitclick:_onClick,oncontextmenu:onContextMenu">'
		+'<td class="dijitReset"><div class="dijitMenuItemIcon ${iconClass}" dojoAttachPoint="iconNode" ></div></td>'
		+'<td tabIndex="-1" class="dijitReset dijitMenuItemLabel" dojoAttachPoint="containerNode" waiRole="menuitem"></td>'
		+'<td class="dijitReset" dojoAttachPoint="arrowCell">'
			+'<div class="dijitMenuExpand" dojoAttachPoint="expand" style="display:none">'
			+'<span class="dijitInline dijitArrowNode dijitMenuExpandInner">+</span>'
			+'</div>'
		+'</td>'
		+'</tr>',
	action: null,
	event: null,
	simpleEvent : null,

	setOriginalEvent : function (e) {
		this.event = e;
		this.simpleEvent = new SimpleEvent(e);
	},

	constructor: function() {
	},

	onClick : function() {
		log.info("MenuItem.onClick : "+this.event.getTargetId());
		if (this.event.getTargetId() == null) {
			this.action.actionPerformed(this.simpleEvent);
		} else {
			this.action.actionPerformed(this.event);
		}
	},

	onContextMenu: function (e) {
		dojo.stopEvent(e);
	}
});