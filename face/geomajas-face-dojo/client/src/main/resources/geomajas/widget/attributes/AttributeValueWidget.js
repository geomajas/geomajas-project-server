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