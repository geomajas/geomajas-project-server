dojo.provide("geomajas._base");
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
(function(){
	var mjs = geomajas;
	
	//	summary
	//	Enumeration for various Layer Types.
	mjs.LayerTypes = { 
		RASTER:"RASTER",
		POINT:"POINT",
		LINESTRING:"LINESTRING",
		POLYGON:"POLYGON",
		MULTIPOINT:"MULTIPOINT",
		MULTILINESTRING:"MULTILINESTRING",
		MULTIPOLYGON:"MULTIPOLYGON"
	};
	
	mjs.GeometryTypes = {   
		POINT : "Point",
		LINESTRING : "LineString",
		LINEARRING : "LinearRing",
		POLYGON : "Polygon",
		MULTILINESTRING : "MultiLineString",
		MULTIPOLYGON : "MultiPolygon"
	};
	
	mjs.ActivityState = {   
		RUNNING : 1,
		WAITING : 2,
		COMPLETED : 3
	};
	
	mjs.ZoomOption = {
		ZOOM_OPTION_EXACT : 0,
		ZOOM_OPTION_LEVEL_CHANGE : 1,
		ZOOM_OPTION_LEVEL_CLOSEST : 2,
		ZOOM_OPTION_LEVEL_FIT : 3
	};
		
})();

var temp = document.URL.substring(0, document.URL.lastIndexOf("/applications")+1);

// geomajasConfig defined here, props now come from the script tag
if (typeof geomajasConfig == 'undefined'){
	geomajasConfig = {
		useLazyLoading: false, // no lazy loading
		lazyFeatureIncludesDefault: 0, // don't include anything by default
		lazyFeatureIncludesSelect: 3, // attributes + geometry on select
		lazyFeatureIncludesAll: 11 // attributes + geometry + label (no style, never used), see VectorLayerService)
	};

} 

geomajasConfig.serverBase = temp;
geomajasConfig.serverAddress = temp + "geomajas.do";


// grab geomajasConfig props from script tag !!!
if(document && document.getElementsByTagName){
	var scripts = document.getElementsByTagName("script");
	for(var i = 0; i < scripts.length; i++){
		var src = scripts[i].getAttribute("src");
		if(src){
			var configStr = scripts[i].getAttribute("geomajasConfig");
			if(configStr) {
				var config = eval("({ "+configStr+" })");
				for(var x in config){
					geomajasConfig[x] = config[x];
				}
			}
		}
	}
}

// assure proper defaults for lazy loading
if (!geomajasConfig.lazyFeatureIncludesAll || 0 == geomajasConfig.lazyFeatureIncludesAll) {
	geomajasConfig.lazyFeatureIncludesDefault = 4; // style only, see VectorLayerService)
	geomajasConfig.lazyFeatureIncludesSelect = 15; // attributes + geometry + style + label, see VectorLayerService)
	geomajasConfig.lazyFeatureIncludesAll = 15; // attributes + geometry + style + label, see VectorLayerService)
}
if (!geomajasConfig.useLazyLoading) {
	geomajasConfig.lazyFeatureIncludesDefault = 15; // attributes + geometry + style + label, see VectorLayerService)
	geomajasConfig.lazyFeatureIncludesSelect = 15; // attributes + geometry + style + label, see VectorLayerService)
	geomajasConfig.lazyFeatureIncludesAll = 15; // attributes + geometry + style + label, see VectorLayerService)
}


createLog = function(show) {
	if (show) {
		// Create the logger. "mylogger" is the unique name of the logger and
		// can be any string.
		var log = log4javascript.getLogger("mylogger"); 

		// Create a PopUpAppender with default options
		var popUpAppender = new log4javascript.PopUpAppender();
	
		// Change the desired configuration options
		popUpAppender.setFocusPopUp(false);
		popUpAppender.setNewestMessageAtTop(false);
	
		// Add the appender to the logger
		log.addAppender(popUpAppender);
	
		// Test the logger
		log.debug("log4javascript started");
		return log;
	} else {
		return new DummyLog();
	}
};

DummyLog = function() {};

DummyLog.prototype = {
	debug: function(msg) {},
	info: function(msg) {},
	warn: function(msg) {},
	error: function(msg) {},
	fatal: function(msg) {}
};

createDummyLog = function () {
	return new DummyLog();
};

if(geomajasConfig){
	log = createLog(geomajasConfig.showLog);
}

log.info(geomajasConfig.connectionPoint);


hasSVGSupport = function () {
	result = false;
	if ((navigator.userAgent.indexOf('MSIE') != -1) && (navigator.userAgent.indexOf('Win') != -1)) {
		document.writeln('<script language="VBscript">'); 
		document.writeln('\n on error resume next \n result = IsObject(CreateObject("Adobe.SVGCtl"))');
		document.writeln('</scr' + 'ipt>');
	} else {
		result = document.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1");
	}
	return result;
};

getResolution = function () {
	return screen.width + "x" + screen.height;
};



getObjectProperties = function (object) {
	if (object == null) {
		return "Object is null";
	}
	var result = '';
	for (var property in object) {
		result += property + ': ' + object[property] + '\r\n';
	}
	return result;
};

getCompleteNodeHeight = function (node) {
	var cs = dojo.getComputedStyle(node);
	var height = parseInt(cs.height);
	if (dojo.isIE) {
		height = node.offsetHeight;
	}
	var temp;
	if (cs.paddingTop != "" && cs.paddingTop != null) {
		temp = parseInt (cs.paddingTop);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.paddingBottom != "" && cs.paddingBottom != null) {
		temp = parseInt (cs.paddingBottom);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.borderTopWidth != "" && cs.borderTopWidth != null) {
		temp = parseInt (cs.borderTopWidth);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.borderBottomWidth != "" && cs.borderBottomWidth != null) {
		temp = parseInt (cs.borderBottomWidth);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.marginTop != "" && cs.marginTop != null) {
		temp = parseInt (cs.marginTop);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.marginBottom != "" && cs.marginBottom != null) {
		temp = parseInt (cs.marginBottom);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	return height;
};

getMarginBorderPaddingHorizontal = function (node) {
	var cs = dojo.getComputedStyle(node);
	var height = 0;
	var temp;
	if (cs.paddingLeft != "" && cs.paddingLeft != null) {
		temp = parseInt (cs.paddingLeft);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.paddingRight != "" && cs.paddingRight != null) {
		temp = parseInt (cs.paddingRight);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.borderLeftWidth != "" && cs.borderLeftWidth != null) {
		temp = parseInt (cs.borderLeftWidth);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.borderRightWidth != "" && cs.borderRightWidth != null) {
		temp = parseInt (cs.borderRightWidth);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.marginLeft != "" && cs.marginLeft != null) {
		temp = parseInt (cs.marginLeft);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.marginRight != "" && cs.marginRight != null) {
		temp = parseInt (cs.marginRight);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	return height;
};

getMarginBorderPaddingVertical = function (node) {
	var cs = dojo.getComputedStyle(node);
	var height = 0;
	var temp;
	if (cs.paddingTop != "" && cs.paddingTop != null) {
		temp = parseInt (cs.paddingTop);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.paddingBottom != "" && cs.paddingBottom != null) {
		temp = parseInt (cs.paddingBottom);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.borderTopWidth != "" && cs.borderTopWidth != null) {
		temp = parseInt (cs.borderTopWidth);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.borderBottomWidth != "" && cs.borderBottomWidth != null) {
		temp = parseInt (cs.borderBottomWidth);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.marginTop != "" && cs.marginTop != null) {
		temp = parseInt (cs.marginTop);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	if (cs.marginBottom != "" && cs.marginBottom != null) {
		temp = parseInt (cs.marginBottom);
		if (!isNaN(temp)) {
			height += temp;
		}
	}
	return height;
};
