dojo.provide("geomajas.map.print.MapComponentInfo");
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
dojo.declare("MapComponentInfo", PrintComponentInfo, {

	/**
	 * @class A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private String applicationId;
//		private String mapId;
//		private Coordinate location;
//		private double rasterResolution = 72;
//		private float ppUnit = 1.0f;

		this.javaClass = "org.geomajas.plugin.printing.component.dto.MapComponentInfo";

		this.rasterResolution = 72;
		this.ppUnit = 1.0;
		
		this.getLayoutConstraint().setAlignmentX(geomajas.LayoutConstraints.JUSTIFIED);
		this.getLayoutConstraint().setAlignmentY(geomajas.LayoutConstraints.JUSTIFIED);
	},
	
	/**
	 * returns the location of the map in world coordinates
	 */
	getLocation : function () {
		return new Coordinate(this.location.x,this.location.y);
	},

	setLocation : function (location) {
		this.location = new Coordinate(location.x,location.y);
	},
	
	/**
	 * returns the scale of the map in pixels per unit (assuming 1 pixel per
	 * user unit (1/72 inch))
	 */
	getPpUnit : function () {
		return this.ppUnit;
	},
	
	setPpUnit : function (ppUnit){
		this.ppUnit = ppUnit;
	},
	
	getRasterResolution : function () {
		return this.rasterResolution;
	},
	
	setRasterResolution : function (rasterResolution){
		this.rasterResolution = rasterResolution;
	},
	
	setMapId : function (mapId){
		this.mapId = mapId;
	},
	
	setApplicationId : function(appId) {
		this.applicationId = appId;
	}

});
