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

	/**
	 * @see #setLabel()
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set an optional label.
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @see #setLabelSymbolizerInfo()
	 * @return
	 */
	public TextSymbolizerInfo getLabelSymbolizerInfo() {
		return labelSymbolizerInfo;
	}

	/**
	 * Set the symbolizer for the label.
	 * @param labelSymbolizerInfo
	 */
	public void setLabelSymbolizerInfo(TextSymbolizerInfo labelSymbolizerInfo) {
		this.labelSymbolizerInfo = labelSymbolizerInfo;
	}

	/**
	 * @see #setGeometrySymbolizerInfo()
	 * @return
	 */
	public SymbolizerTypeInfo getGeometrySymbolizerInfo() {
		return geometrySymbolizerInfo;
	}

	/**
	 * Set the symbolizer for the geometry/ellipse/rectangle.
	 * @param geometrySymbolizerInfo
	 */
	public void setGeometrySymbolizerInfo(SymbolizerTypeInfo geometrySymbolizerInfo) {
		this.geometrySymbolizerInfo = geometrySymbolizerInfo;
	}

	
	/**
	 * @see #setShowInLegend()
	 * @return
	 */
	public boolean isShowInLegend() {
		return showInLegend;
	}

	/**
	 * Set if this world paintable should be shown in the legend.
	 * @param showInLegend
	 */
	public void setShowInLegend(boolean showInLegend) {
		this.showInLegend = showInLegend;
	}

	
	/**
	 * @see #setLegendTitle()
	 * @return
	 */
	public String getLegendTitle() {
		return legendTitle == null ? label : legendTitle;
	}

	/**
	 * Set the legend title.
	 * @param legendTitle
	 */
	public void setLegendTitle(String legendTitle) {
		this.legendTitle = legendTitle;
	}
	
	

}
