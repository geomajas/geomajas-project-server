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

dojo.provide("geomajas.gfx.painter.RasterImagePainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("RasterImagePainter", Painter, {

	/**
	 * @class Painter implementation for raster images. 
	 *
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapView) {
		this.mapView = mapView;
	},
	
	/**
	 * The actual painting function. Applies the
	 * object's style, and then draws the image with the object's id.
	 * Draws in view space to avoid image flipping.
	 *
	 * @param object A RasterPicture object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*RasterImage*/object, /*GraphicsContext*/graphics) {
		try {
			// no transform needed !!!
			graphics.drawImage (object, {id : object.getId(), style: object.getStyle(), type: "div"} );
		} catch (e){
			log.error("RasterImagePainter : exception !!! ");
			for (var i in e) log.error(e[i]);
		}
	}
});
