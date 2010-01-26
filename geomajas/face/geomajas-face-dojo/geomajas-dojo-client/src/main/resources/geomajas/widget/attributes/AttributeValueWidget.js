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

dojo.provide("geomajas.widget.attributes.AttributeValueWidget");
dojo.require("geomajas.widget.attributes.AttributeEditorFactory");

/**
 * <p>
 * Extension of dojo's 'dijit._Widget, dijit._Templated' and 'dijit._Container'
 * widgets that shows a certain dojo widget depending on what
 * {@link AttributeDefinition} is applied.
 * </p>
 * <p>
 * This widget is meant for editing an attribute's primitive value. Simply set
 * an {@link AttributeDefinition}, and it will automatically display the
 * correct widget for editing.
 * </p>
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.attributes.AttributeValueWidget", [dijit._Widget, dijit._Templated, dijit._Container], {

	widgetsInTemplate : true,
	templateString : "<div><div id='${id}:attach'></div></div>",

	layer : null,
	attributeDefinition : null,
	valueWidgetId : null,
	valueWidget : null,

	/**
	 * Initialization.
	 */
	postCreate : function () {
		this.factory = new AttributeEditorFactory();
		this.valueWidgetId = this.id+":widget";
	},

	/**
	 * Sets a new layer object. This is only needed if you intend to use
	 * the 'setAttributeLabel' or 'setAttributeName' functions to set
	 * AttributeDefinitions.
	 * @param layer VectorLayer object.
	 */
	setLayer : function (layer) {
		this.layer = layer;
	},

	/**
	 * This widget uses another widget to present itself. That other widget
	 * can be a NumberTextBox, DateTextBox, CheckBox, or many other dojo
	 * widgets. It all depends on what type of attribute is set.
	 * So setting a new AttributeDefinition, will change the appearance and
	 * validation of this widget. It uses a AttributeEditorFactory to
	 * accomplish this.
	 * @param attributeDefinition The AttributeDefinition to shape itself to.
	 */
	setAttributeDefinition : function (attributeDefinition) {
		this.attributeDefinition = attributeDefinition;
		if (this.attributeDefinition) {
			var attach = document.getElementById(this.id+":attach");
			if (attach) {
				if (this.valueWidget != null) {
					this.valueWidget.destroy();
				}
				this.valueWidget = this.factory.create (this.valueWidgetId, this.attributeDefinition, null, null);
				attach.appendChild(this.valueWidget.domNode);
			}
		}
	},

	/**
	 * Uses the label of an AttributeDefinition to search for the correct
	 * AttributeDefinition object. In order to do this, the layer has to be
	 * set first! Calls 'setAttributeDefinition'.
	 */
	setAttributeLabel : function (label) {
		if (this.layer) {
			var attribute = this.layer.getFeatureType().getAttributeByLabel(label);
			this.setAttributeDefinition(attribute);
		}
	},

	/**
	 * Uses the name of an AttributeDefinition to search for the correct
	 * AttributeDefinition object. In order to do this, the layer has to be
	 * set first! Calls 'setAttributeDefinition'.
	 */
	setAttributeName : function (name) {
		if (this.layer) {
			var attribute = this.layer.getFeatureType().getAttributeByName(name);
			this.setAttributeDefinition(attribute);
		}
	},

	/**
	 * Get the value.
	 */
	getValue : function () {
		return this.factory.getEditorValue(this.valueWidgetId, this.attributeDefinition);
	}
});