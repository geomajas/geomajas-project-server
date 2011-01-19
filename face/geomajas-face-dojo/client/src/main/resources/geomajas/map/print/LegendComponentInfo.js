dojo.provide("geomajas.map.print.LegendComponentInfo");
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
dojo.declare("LegendComponentInfo", PrintComponentInfo, {

	/**
	 * @class 
	 * A legend component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private String applicationId;
//		private String mapId;
//		private FontStyleInfo font;
//		private String title = "Legend";

		this.javaClass = "org.geomajas.plugin.printing.component.dto.LegendComponentInfo";
		
		this.title = "Legende";
		this.getLayoutConstraint().setAlignmentX(geomajas.LayoutConstraints.RIGHT);
		this.getLayoutConstraint().setAlignmentY(geomajas.LayoutConstraints.BOTTOM);
		this.getLayoutConstraint().setFlowDirection(geomajas.LayoutConstraints.FLOW_Y);
		this.getLayoutConstraint().setMarginX(20);
		this.getLayoutConstraint().setMarginY(20);
		this.font = new FontStyleInfo();
		this.font.setFamily("Dialog");
		this.font.setStyle("Plain");
		this.font.setSize(12);
	},

	getApplicationId : function () {
		return this.applicationId;
	},

	setApplicationId : function (applicationId) {
		this.applicationId = applicationId;
	},

	getMapId : function () {
		return this.mapId;
	},

	setMapId : function (mapId) {
		this.mapId = mapId;
	},

	getFont : function () {
		return this.font;
	},

	setFont : function (font) {
		this.font = font;
	},

	getTitle : function () {
		return this.title;
	},

	setTitle : function (title) {
		this.title = title;
	}
	
});
