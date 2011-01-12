dojo.provide("geomajas.util.StringFilter");
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
dojo.declare("StringFilter", null, {

	/**
	 * @fileoverview Filter for strings.
	 * @class A general filter for strings. First set a "filterString" to
	 * compare to. Then call "accept" to actually compare a text to the
	 * filterString.<br/> Known filter characters are '*' and '?'.
	 * 
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
		this.filterString = "";
	},

	/**
	 * Set the filter string. This needs to be done before you make calls to 'accept'.
	 * @param filterString The string to compare to.
	 */
	setFilterString : function (filterString) {
		this.filterString = filterString;
	},

	/**
	 * Check whether or not 'aText' corresponds to the filter string to was set.
	 * This function is case sensitive!
	 */
	accept : function (aText) {
		if (this.filterString == aText || this.filterString == "*" || this.filterString == "" || aText == "") {
			return true;
		}

		// replace multiple '*' by a single '*'
		var buffer = this.filterString.charAt(0);
		for (fc = 1; fc < this.filterString.length; fc++) {
			var prevC = this.filterString.charAt(fc - 1);
			var c = this.filterString.charAt(fc);
			if (!(prevC == '*' && c == '*')) {
				buffer += c;
			}
		}

		// replace '*?' by '?*', which has the same meaning but is easier to parse.
		for (fc = 0; fc < buffer.length - 1; fc++) {
			var c = buffer.charAt(fc);
			var nextC = buffer.charAt(fc + 1);
			if (nextC == '?' && c == '*') {
				buffer.setCharAt(fc, '?');
				buffer.setCharAt(fc + 1, '*');
			}
		}

		// Now we begin...
		var ok = true;
		var index = 0;

		for (fc = 0; fc < aText.length; fc++) {
			if (index == buffer.length) {
				ok = false;
				break;
			}
			var fileC = aText.charAt(fc);
			var patC = buffer.charAt(index);
			if (patC == '*') {
				if (index == buffer.length - 1) {
					break;
				} else {
					var nextPatC = buffer.charAt(index + 1);
					if (fileC == nextPatC) {
						index += 2;
					}
				}
			} else if (patC != '?' && fileC != patC) {
				ok = false;
				break;
			} else {
				index++;
			}
		}
		while (index < buffer.length) {
			var patC = buffer.charAt(index);
			if (patC != '*') {
				ok = false;
			}
			index++;
		}

		return ok;
	},

	/**
	 * Check whether or not 'aText' corresponds to the filter string to was set.
	 */
	acceptCaseInsensitive : function (aText) {
		if (this.filterString == aText || this.filterString == "*" || this.filterString == "" || aText == "") {
			return true;
		}

		// replace multiple '*' by a single '*'
		var buffer = this.filterString.charAt(0);
		for (fc = 1; fc < this.filterString.length; fc++) {
			var prevC = this.filterString.charAt(fc - 1);
			var c = this.filterString.charAt(fc);
			if (!(prevC == '*' && c == '*')) {
				buffer += c;
			}
		}

		// replace '*?' by '?*', which has the same meaning but is easier to parse.
		for (fc = 0; fc < buffer.length - 1; fc++) {
			var c = buffer.charAt(fc);
			var nextC = buffer.charAt(fc + 1);
			if (nextC == '?' && c == '*') {
				buffer.setCharAt(fc, '?');
				buffer.setCharAt(fc + 1, '*');
			}
		}

		// Now we begin...
		var ok = true;
		var index = 0;

		for (fc = 0; fc < aText.length; fc++) {
			if (index == buffer.length) {
				ok = false;
				break;
			}
			var fileC = aText.charAt(fc);
			var patC = buffer.charAt(index);
			if (patC == '*') {
				if (index == buffer.length - 1) {
					break;
				} else {
					var nextPatC = buffer.charAt(index + 1);
					if (fileC.toLowerCase() == nextPatC || fileC.toUpperCase() == nextPatC) {
						index += 2;
					}
				}
			} else if (patC != '?' && (fileC.toLowerCase() != patC && fileC.toUpperCase() != patC)) {
				ok = false;
				break;
			} else {
				index++;
			}
		}
		while (index < buffer.length) {
			var patC = buffer.charAt(index);
			if (patC != '*') {
				ok = false;
			}
			index++;
		}

		return ok;
	}

});

//some string extensions (courtesy of http://www.tek-tips.com/faqs.cfm?fid=6620)

String.prototype.trim = function(){return
	(this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""))};
	
String.prototype.startsWith = function(str)
	{return (this.match("^"+str)==str)};
	
String.prototype.endsWith = function(str)
	{return (this.match(str+"$")==str)};
