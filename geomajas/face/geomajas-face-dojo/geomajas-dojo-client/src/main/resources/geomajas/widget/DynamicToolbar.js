dojo.provide("geomajas.widget.DynamicToolbar");
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
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit._Container");
dojo.require("dijit.Toolbar");
dojo.require("dojox.collections.Dictionary");

dojo.require("geomajas.widget.TBButton");
dojo.require("geomajas.widget.TBToggleButton");
dojo.require("geomajas.action.common");

/**
 * This widget is a wrapper around a dojo toolbar widget for adding
 * functionality to a MapWidget. At this moment 2 types of buttons can be added
 * to this toolbar: ToolbarActions and ToolbarTools
 * (see majas.action.*). Actualy these aren't real buttons, but a button will
 * be asociated with them. A ToolbarAction is a single action to be taken when
 * the button is pressed. A ToolbarTool is a modus to be activated
 * and deactivated. Usualy there will be a MouseListener accompanied with a
 * ToolbarTool for interaction with the MapWidget. 
 * @author Pieter De Graef
 */
dojo.declare(
	"geomajas.widget.DynamicToolbar",
	[dijit._Widget, dijit._Templated, dijit._Container],
	{
		widgetsInTemplate : true,
		templateString : '<div class="dijit dijitToolbar" waiRole="toolbar" dojoAttachPoint="containerNode"></div>',

		buttons : null,
		selected : null,

		constructor : function () {
			this.buttons = new dojox.collections.Dictionary();
		},

		/**
		 * This widget's destructor function. First it destroys all the
		 * buttons, only then will it destroy itself.
		 */
		destroy : function (/*Boolean*/ finalize) {
			// Destroy all buttons:
			var btns = this.buttons.getValueList();
			for (var i=0; i<btns.length; i++) {
				var button = btns[i];
				button.destroy();
			}
			
			// Call parent function:
			this.inherited(arguments);
		},
			
		/**
		 * Add a new ToolbarAction to this toolbar. The action determines a
		 * single action to be taken when the asociated button is pressed. That
		 * asociated button, will be created here. The action implementation
		 * will be represented by a dojo ToolbarButton (without toggle).
		 * Futhermore a ToolbarAction determines the image on the button and 
		 * a tooltip.
		 * @param action : ToolbarAction implementation.
		 * @param position : Integer position in the toolbar for this action.
		 */
		addAction : function (action, position) {
			if (!this.buttons.contains(action.getId())) {
				var button = new geomajas.widget.TBButton({name:action.getId(), id:action.getId(), iconClass:action.getImage(), label:action.getTooltip(), showLabel:false}, document.createElement('div'));
				button.init(action);
				this.addChild(button);
				this.buttons.add (action.getId(), button);
				if (!action.isEnabled()) {
					button.setDisabled(true);
				}
				return true;
			}
			return false;
		},

		/**
		 * Add a new ToolbarTool to the toolbar. The activesettable
		 * a modus that can be activated/deactivated an will be represented by
		 * a dojo ToolbarButton with toggle on. Usualy this activated modus'
		 * purpose is some interaction with the MapWidget.<br/>
		 * Also there can be only one ToolbarTool be active at any
		 * one time! This means that if a ToolbarTool is active, and
		 * another is clicked by the user, the active one will be deactivated
		 * first.
		 * @param tool ToolbarTool implementation to be added to the toolbar.
		 * @param position Integer position in the toolbar for this
		 *                 ToolbarTool. 
		 */
		addTool : function (tool, position) {
			if (!this.buttons.contains(tool.getId())) {
				var tbButton = new geomajas.widget.TBToggleButton({name:tool.getId(), id:tool.getId(), iconClass:tool.getImage(), label:tool.getTooltip(), showLabel:false}, document.createElement('div'));
				tbButton.init(tool, this);
				this.addChild(tbButton);
				this.buttons.add (tool.getId(), tbButton);
				if (!tool.isEnabled()) {
					tbButton.setDisabled(true);
				}
				return true;
			}
			return false;
		},

		/**
		 * Adds a toolbar separator to the toolbar. This is a vertical line
		 * without any funcionality. It's only purpose is to divide groups of
		 * buttons.
		 */
		addSeparator : function () {
			var separator = new dijit.ToolbarSeparator();
			this.addChild(separator);
		},

		/**
		 * Returns a TBButton or TBToggleButton if it has been
		 * added.
		 * @param id The identifier of the ToolbarAction / ToolbarTool inside
		 *           the button.
		 * @return TBButton or TBToggleButton
		 */
		getItemById : function (id) {
			if (this.buttons.contains(id)) {
				return this.buttons.item(id);
			}
			return null;
		},

		onSelect : function (id) {
			if (id != null) {
				if (this.selected != null) {
					var button = this.buttons.item(this.selected);
					button.setChecked(false);
				}
				this.selected = id;
			} else {
				if (this.selected != null) {
					var button = this.buttons.item(this.selected);
					button.setChecked(false);
				}
				this.selected = null;
			}
		},

		onDeSelect : function (id) {
			if (this.selected != null && id != null) {
				var button = this.buttons.item(this.selected);
				this.selected = null;
				button.setChecked(false);
			} else {
				this.selected = null;
			}
		},
		
		onConfigDone : function () {
			log.info("post config for toolbar "+this.id);
			dojo.forEach(this.buttons.getValueList(), 
					function onConfigDone(button){ 
				      if(button.action) {button.action.onConfigDone()};
				      if(button.tool) {button.tool.onConfigDone()};
			});
		}		
	}
);
