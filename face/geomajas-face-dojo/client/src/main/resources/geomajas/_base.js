dojo.provide("geomajas._base");
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
		lazyFeatureIncludesDefault: 15, // include all
		lazyFeatureIncludesSelect: 15, // include all
		lazyFeatureIncludesAll: 15 // include all
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
	geomajasConfig.lazyFeatureIncludesDefault = 12; // style + label only, see GeomajasConstant)
	geomajasConfig.lazyFeatureIncludesSelect = 15; // attributes + geometry + style + label, see GeomajasConstant)
	geomajasConfig.lazyFeatureIncludesAll = 15; // attributes + geometry + style + label, see GeomajasConstant)
}
if (!geomajasConfig.useLazyLoading) {
	geomajasConfig.lazyFeatureIncludesDefault = 15; // attributes + geometry + style + label, see GeomajasConstant)
	geomajasConfig.lazyFeatureIncludesSelect = 15; // attributes + geometry + style + label, see GeomajasConstant)
	geomajasConfig.lazyFeatureIncludesAll = 15; // attributes + geometry + style + label, see GeomajasConstant)
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
