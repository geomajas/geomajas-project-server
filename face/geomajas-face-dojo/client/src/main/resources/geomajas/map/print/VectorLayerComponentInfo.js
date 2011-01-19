dojo.provide("geomajas.map.print.VectorLayerComponentInfo");
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
dojo.declare("VectorLayerComponentInfo", BaseLayerComponentInfo, {

	/**
	 * @class 
	 * A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private NamedStyleInfo styleInfo;
//		private String filter;
//		private String[] selectedFeatureIds = new String[0];
//		private boolean labelsVisible;

		this.javaClass = "org.geomajas.plugin.printing.component.dto.VectorLayerComponentInfo";
		
		this.selectedFeatureIds = []; // not a list but an array
	},

	setStyleInfo : function (styleInfo) {
		this.styleInfo = styleInfo;
	},

	setFilter : function (filter) {
		this.filter = filter;
	},

	setSelectedFeatureIds : function (selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	},

	setLabelsVisible : function (labelsVisible) {
		this.labelsVisible = labelsVisible;
	}

});
