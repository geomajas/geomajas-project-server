dojo.provide("geomajas.map.print.BaseLayerComponentInfo");
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
dojo.declare("BaseLayerComponentInfo", PrintComponentInfo, {

	/**
	 * @class 
	 * A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private boolean visible = true;
//		private boolean selected;
//		private String layerId;

		this.getLayoutConstraint().setAlignmentX(geomajas.LayoutConstraints.JUSTIFIED);
		this.getLayoutConstraint().setAlignmentY(geomajas.LayoutConstraints.JUSTIFIED);
		
		this.visible = true;
	},

	isVisible : function () {
		return this.visible;
	},

	setVisible : function (visible) {
		this.visible = visible;
	},

	isSelected : function () {
		return this.selected;
	},

	setSelected : function (selected) {
		this.selected = selected;
	},

	getLayerId : function () {
		return this.layerId;
	},

	setLayerId : function (layerId) {
		this.layerId = layerId;
	}

});
