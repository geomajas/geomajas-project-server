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
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;

/**
 * Metadata DTO class that carries extra metadata information about a world paintable.
 * 
 * @author Jan De Moerloose
 * @since 1.2.0
 */
@Api(allMethods = true)
public abstract class WorldPaintableInfo implements IsInfo {

	private static final long serialVersionUID = 120L;

	private TextSymbolizerInfo labelSymbolizerInfo;

	private SymbolizerTypeInfo geometrySymbolizerInfo;

	private String label;

	private boolean showInLegend;
	
	private String legendTitle;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public TextSymbolizerInfo getLabelSymbolizerInfo() {
		return labelSymbolizerInfo;
	}

	public void setLabelSymbolizerInfo(TextSymbolizerInfo labelSymbolizerInfo) {
		this.labelSymbolizerInfo = labelSymbolizerInfo;
	}

	public SymbolizerTypeInfo getGeometrySymbolizerInfo() {
		return geometrySymbolizerInfo;
	}

	public void setGeometrySymbolizerInfo(SymbolizerTypeInfo geometrySymbolizerInfo) {
		this.geometrySymbolizerInfo = geometrySymbolizerInfo;
	}

	
	public boolean isShowInLegend() {
		return showInLegend;
	}

	
	public void setShowInLegend(boolean showInLegend) {
		this.showInLegend = showInLegend;
	}

	
	public String getLegendTitle() {
		return legendTitle == null ? label : legendTitle;
	}

	
	public void setLegendTitle(String legendTitle) {
		this.legendTitle = legendTitle;
	}
	
	

}
