/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.gwt.client.snap;

import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.SnapService;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeSnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestVertexSnapAlgorithm;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public final class SnapRuleUtil {

	private SnapRuleUtil() {
	}

	public static boolean addRule(SnapService service, MapWidget mapWidget, SnappingRuleInfo snappingRuleInfo) {
		Layer<?> layer = mapWidget.getMapModel().getLayer(snappingRuleInfo.getLayerId());
		if (layer instanceof VectorLayer) {
			SnapAlgorithm algorithm;
			switch (snappingRuleInfo.getType()) {
				case CLOSEST_ENDPOINT:
					algorithm = new NearestVertexSnapAlgorithm();
					break;
				case NEAREST_POINT:
					algorithm = new NearestEdgeSnapAlgorithm();
					break;
				default:
					algorithm = new NearestVertexSnapAlgorithm();
			}

			VectorLayerSourceProvider sourceProvider = new VectorLayerSourceProvider((VectorLayer) layer);
			service.addSnappingRule(algorithm, sourceProvider, snappingRuleInfo.getDistance(), true);
			return true;
		}
		return false;
	}
}