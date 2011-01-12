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

dojo.provide("geomajas.widget.attributes.AttributeSelect");
dojo.require("dijit.form.FilteringSelect");

/**
 * <p>
 * Extension of dojo's 'dijit.form.FilteringSelect' widget that shows the
 * attributes of a layer as a selectbox.
 * </p>
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.attributes.AttributeSelect", dijit.form.FilteringSelect, {

	layer : null,

	/**
	 * Since this widget is actually a view on the attributes of a layer,
	 * it can be adjusted by simply setting a new VectorLayer object.
	 * @param layer Instanceof VectorLayer, for which the attributes are shown.
	 */
	setLayer : function (layer) {
		this.layer = layer;
		if (this.layer && (this.layer instanceof VectorLayer)) {
			var jsonData = {identifier:"identifier", items: [] };
			var attributes = this.layer.getFeatureType().getAttributes().getValueList();
			for (var i=0; i<attributes.length; i++) {
				var atDef = attributes[i];
				jsonData.items.push({name:atDef.getLabel(), identifier:atDef.getName()});
			}
			this.store = new dojo.data.ItemFileWriteStore( { data: jsonData } );
		}
	},

	/**
	 * Return the layer for which attributes are shown.
	 */
	getLayer : function () {
	}
});