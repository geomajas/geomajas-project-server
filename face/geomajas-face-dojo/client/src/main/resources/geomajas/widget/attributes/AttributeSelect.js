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