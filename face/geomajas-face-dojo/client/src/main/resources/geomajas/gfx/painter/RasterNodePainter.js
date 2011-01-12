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

dojo.provide("geomajas.gfx.painter.RasterNodePainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("RasterNodePainter", Painter, {

	/**
	 * @class Painter implementation for raster nodes. This painter always paints
	 * in viewspace!
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function () {
		this.maxImagesPerNode = 20;
	},

	/**
	 * The actual painting function.
	 * @param object A Paintable object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (node, graphics) {
		log.debug("painting raster node "+node.getId()+","+node.getImages().count);
		// default for vml is vml:group, needs div option to force div !
		graphics.drawGroup({id : node.getId(), type : "div"});
		if(node.isVisible()){
			log.debug("painting raster node showing "+node.getId());
			graphics.unhide(node.getId());
		} else {
			log.debug("painting raster node hiding "+node.getId());
			graphics.hide(node.getId());
		}
	}
});