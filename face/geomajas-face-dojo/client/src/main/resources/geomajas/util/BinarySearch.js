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

dojo.provide("geomajas.util.BinarySearch");
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

/**
 * for a sorted array, this returns the index of the object if it were added to array
 */
Array.prototype.binarySearch = function(sort,o){ 
    var indexMin = -1; 
    var indexMax = this.length; 
    while((indexMax - indexMin) > 1){ 
          var indexMiddle = Math.floor((indexMin + indexMax) / 2); 
          if(sort(this[indexMiddle],o) < 0) {
          	indexMin = indexMiddle; 
          } else {
            indexMax = indexMiddle; 
          }	
    } 
    return indexMax;
}