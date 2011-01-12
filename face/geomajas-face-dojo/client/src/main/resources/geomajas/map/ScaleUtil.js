dojo.provide("geomajas.map.ScaleUtil");
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
dojo.declare("ScaleUtil", null, {

	/**
	 * @fileoverview General utility class for scale handling functions, such
	 * as the conversion of (1:xx) to a float, expressed in pixels. It is 
	 * initialized with a mapId, after which it will determine how many meters 
	 * a unit in the map's CRS is. Together with a default pixel width of 1/72
	 * inch, it is possible to calculate scale from and to different displaying
	 * methods.
	 *
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
    constructor : function () {
        /** How many meters is 1 unit of a map's CRS? */
        this.unitLength = 1;

        /** Width of a pixel. Default is 1/96 inch. This determines how
         *  accurate the calculations in this class are! */
        this.pixelWidth = 0.000264583333;
    },

	/**
	 * Set a new unit length. The unit length must be expressed in meters per 
	 * unit (of a map's CRS).
	 * @param unitLength The length if a unit in meters. Is usually a float.
	 */
	setUnitLength : function (unitLength) {
		this.unitLength = unitLength;
	},

	/**
	 * Get the unit length must be expressed in meters per 
	 * unit (of a map's CRS).
	 */
	getUnitLength : function () {
		return this.unitLength;
	},
	
    /**
     * Set a new pixel width.
     * @param pixelWidth The width of a pixel in meters
     */
    setPixelWidth : function (pixelWidth) {
        this.pixelWidth = pixelWidth;
    },

	/**
	 * Return the width of a pixel. Default is 1/72 inch. This determines how
	 * accurate the calculations in this class are!
	 */
	getPixelWidth : function () {
		return this.pixelWidth;
	},

	/**
	 * Transform a floating point scale (as they come from the MapView), to a 
	 * string format, such as (1:x).
	 * @param scale The scale you wish to format.
	 * @return Returns a string like (1:x).
	 */
	scaleToString : function (scale) {
		var value = Math.round(this.unitLength / (scale * this.pixelWidth));
		var inverse = false;
		if(value == 0){
			value = Math.round((scale * this.pixelWidth) / this.unitLength);
			inverse = true;
		} 
		var strValue = "" + value;
		var count = strValue.length - 3;
		while (count > 0) {
			var before = strValue.substring(0, count);
			var after = strValue.substring(count);
			strValue = before + "." + after;
			count = count - 3;
		}
		return inverse ? strValue+": 1" : "1 : " + strValue;
	},

	/**
	 * Transforms a string like (1:x) to a floating point scale value. This
	 * floating point scale value can be used be the MapView object.
	 * @param label The string format of a scale (1:x).
	 * @return Returns the floating point equivalent of the string scale.
	 */
	stringToScale : function (label) {
		if (label == null || label == "") {
			return -1;
		}
		var pos = label.indexOf("1 : ");
		var inverse = false;
		var tmp = label;
		if (pos == 0){
			tmp = label.substring(4);
		} else {
			pos = label.indexOf(" : 1");
			inverse = true;
			tmp = label.substring(0,pos);
		}
		while (tmp.indexOf(".") >= 0) {
			tmp = tmp.replace(".","");
		}

		var value = parseInt(tmp);
		if (isNaN(value)) {
			log.error ("Could not parse integer value ("+value+")!");
			return -1;
		} else {
			var scale = 1;
			if(inverse){
				scale = parseFloat(value * this.pixelWidth / this.unitLength);
			}else {
				scale = parseFloat(this.unitLength / (value * this.pixelWidth));
			}
			return scale;
		}
	},

	/**
	 * Return a length string, readable for the user. The user specifies a
	 * length in pixels, and receives it in, for example, miles. For more 
	 * clarity: the map's scalebar uses this.
	 * @param scale The scale you wish to use, as a floating point (as they 
	 *              come from the MapView).
	 * @param pixelCount The amount of pixels the length has to cover.
	 * @param unitType Type of measuring system. Can be either "metric", 
	 *                 "english", or "crs". CRS will simply use the units of
	 *                 the map's CRS as default.
	 */
	readableScaleFormat : function (scale, pixelCount, unitType) {
		if (scale == null || pixelCount == null) {
			return null;
		}
		var text = "";
		
		if (unitType == "ENGLISH") {
			var length = Math.round( (this.unitLength * pixelCount) / scale );
			length = parseFloat(length / 1609.344); // to miles
			if (length < 10) {
				text = length.toFixed(2) + " mi";
			} else if (length < 100) {
				text = length.toFixed(2) + " mi";
			} else {
				text = length.toFixed(1) + " mi";
			}
		} else if (unitType == "CRS") {
			var length = parseFloat( pixelCount / scale );
			if (length < 10) {
				text = length.toFixed(5) + " u";
			} else if (length < 100) {
				text = length.toFixed(3) + " u";
			} else {
				text = length.toFixed(2) + " u";
			}
		} else { // metric
			var length = Math.round( (this.unitLength * pixelCount) / scale );
			if (length < 10000) {
				text = length+" m";
			} else if (length < 100000) {
				text = (length / 1000).toFixed(2) + " km";
			} else {
				text = (length / 1000).toFixed(1) + " km";
			}
		}
		return text;
	}
});
