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

dojo.provide("geomajas.widget.LTToggleButton");
dojo.require("dijit.form.Button");

// TODO: Needs decent disable/enable functions.

/**
 * Extension of a dojo button widget, that resembles the functionality of a
 * checkbox. The "LT" stands for LayerTree, and it's purpose is to set
 * and get layer states for the LayerTree widget. A tool is either
 * active or not, so is this button (not the same as enabled/disabled !).<br/>
 * For instance, there is a LayerTreeTool called
 * "LayerVisibleTool", which shows the visibility state for layers.
 * Now if this LTButton widget were to delegate that tool, you could
 * use it in the LayerTree to show which layer are visible and which are not.
 * Also, since this is still a button, you could change that visibility state.
 * Besides a LayerTreeTool, this button can also be initiated with a
 * LayerTreeAction. No active/deactive but can still be enabled/disabled like
 * the tool.
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.LTToggleButton", dijit.form.Button, {

	checked : null,
	baseClass : "dijitToggleButton",
	

	/**
	 * Implementation of the LayerTreeTool class. Determines the
	 * actions of activating/deactivating this button.
	 */
	tool : null,

	focusHandle : null,

	/**
	 * Override the postCreate to subscribe a function to the focus topic.
	 */
	postCreate : function () {
		this.inherited(arguments);
		this.focusHandle = dojo.subscribe("focusNode", this, "onFocusNode");
	},

	destroy : function (finalize) {
		dojo.unsubscribe(this.focusHandle);
		this.inherited(arguments);
	},

	/**
	 * If a new Layer is selected in the LayerTree widget, this function
	 * will automatically be called. It's purpose is to derive from a
	 * Layer object, whether or not, this LTButton widget should be
	 * active or not.
	 * @param layer The selected Layer in the LayerTree widget.
	 * @param event The event object passed when selecting the layer in
	 *              the LayerTree widget.
	 */
	applyLayer : function (layer) {
		if (this.tool) {
			var en = this.tool.getEnabledByLayer(layer);
			if (en) {
				this.setDisabled(false);
				if (layer != null) {
					var sel = this.tool.getSelectionByLayer(layer);
					this.tool.setSelected (sel)
					this.setChecked(sel);
				}
			} else {
				this.setDisabled(true);
				this._setStateClass();
			}
		}
	},

	/**
	 * The final proof that this widget truly is a button ;-). Toggles the
	 * active state of this button, concerning the selected layer in the
	 * LayerTree. Depending on whether this button is initiated with a
	 * LayerTreeTool or a LayerTreeAction, it will do something different.
	 * 
	 * @param event The clicking event from your browser.
	 */
	onClick: function(event) {
		if (this.tool) {
			var toggle = !this.tool.isSelected();
			this.tool.setSelected(toggle);
			this.setChecked(toggle);
		}
	},

	/**
	 * An LTButton should be initiated with EITHER a {@link LayerTreeTool}
	 * or a {@link LayerTreeAction}. This function initiates it with a
	 * tool. This tool should implement the actions to be taken on
	 * activation/deactivation. It should also know whether or not this
	 * button is to be activated/deactivated when a new layer (=node in the
	 * {@link LayerTree}) is selected. Furthermore this LayerTreeTool has
	 * an image and a tooltip for this button.
	 * @param tool Implemantion of the LayerTreeTool class.
	 */
	setTool : function (tool) {
		this.tool = tool;
	},

	setChecked: function(/*Boolean*/ checked){
		this.checked = checked;
		this._setStateClass();
		this.onChange(checked);

		if (this.tool != null) {
			if (checked) {
				this.tool.onSelect(null);
			} else {
				this.tool.onDeSelect(null);
			}
		}
	},

	/**
	 * Function subscribed to the focus topic. It purpose is to make sure that
	 * this LayerTreeButton cannot be focused.
	 * @param node The node being focused. If that node equals this widget's
	 *             focusNode, execute a blur, to defocus.
	 */
	onFocusNode : function(node){
		// Remove the focus:
		if(node == this.focusNode) {
			node.blur();
		}
	}
});