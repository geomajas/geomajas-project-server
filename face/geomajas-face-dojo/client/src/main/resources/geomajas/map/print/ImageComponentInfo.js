dojo.provide("geomajas.map.print.ImageComponentInfo");
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
dojo.declare("ImageComponentInfo", PrintComponentInfo, {

	/**
	 * @class 
	 * A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
		this.javaClass = "org.geomajas.plugin.printing.component.dto.ImageComponentInfo";
	},

	getImagePath : function () {
		return this.imagePath;
	},
	
	setImagePath : function (imagePath) {
		this.imagePath = imagePath;
	}

});
