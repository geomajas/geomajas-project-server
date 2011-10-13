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
 * This file contains javascript functions that operate the Geomajas map through the JS API.
 */

// Navigation:

function translate(directionX, directionY) {
	var distance = document.getElementById("translateDistance").value;
	var position = map.getViewPort().getPosition();
	var newX = position.getX() + directionX * distance;
	var newY = position.getY() + directionY * distance;
	map.getViewPort().applyPosition(new org.geomajas.jsapi.spatial.Coordinate(newX, newY));
}

function zoomIn() {
	var factor = document.getElementById("zoomFactor").value;
	var scale = map.getViewPort().getScale();
	map.getViewPort().applyScale(scale * parseFloat(factor));
}	

function zoomOut() {
	var factor = document.getElementById("zoomFactor").value;
	var scale = map.getViewPort().getScale();
	map.getViewPort().applyScale(scale / parseFloat(factor));			
}		

function displayBounds() {
	var bbox = map.getViewPort().getBounds();
	document.getElementById("boundsXmin").value = bbox.getX();
	document.getElementById("boundsYmin").value = bbox.getY();
	document.getElementById("boundsXmax").value = bbox.getMaxX();
	document.getElementById("boundsYmax").value = bbox.getMaxY();
}

function applyBounds() {
	var xmin = parseFloat(document.getElementById("boundsXmin").value);
	var ymin = parseFloat(document.getElementById("boundsYmin").value);
	var xmax = parseFloat(document.getElementById("boundsXmax").value);
	var ymax = parseFloat(document.getElementById("boundsYmax").value);

	var bbox = new org.geomajas.jsapi.spatial.Bbox(xmin, ymin, xmax - xmin, ymax - ymin);
	map.getViewPort().applyBounds(bbox);
}

// Map & layers:

function getLayers() {
	var text = "";
	for (var i=0; i<map.getLayersModel().getLayerCount(); i++) {
		var layer = map.getLayersModel().getLayerAt(i);
		text += layer.getTitle() + "\n";
	}
	document.getElementById("layersList").value = text;
}

function toggleVisibility() {
	// Toggle visibility for the second layer:
	var layer = map.getLayersModel().getLayerAt(1);
	var visible = eval(layer.isMarkedAsVisible().toString()); // Boolean -> boolean conversion
	layer.setMarkedAsVisible(!visible);
}
