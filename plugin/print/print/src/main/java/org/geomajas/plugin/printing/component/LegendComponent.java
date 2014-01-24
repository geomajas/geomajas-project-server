/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component;

import java.awt.Font;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;

/**
 * Component representing a legend.
 * 
 * @author Jan De Moerloose
 *
 */
public interface LegendComponent extends PrintComponent<LegendComponentInfo> {

	Font getFont();

	String getTitle();

	String getMapId();

	void clearItems();

	void addVectorLayer(ClientVectorLayerInfo info);

	void addRasterLayer(ClientRasterLayerInfo info);

	String getApplicationId();

	void setMapId(String mapId);

	void setApplicationId(String applicationId);

}