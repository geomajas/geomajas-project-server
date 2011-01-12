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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.global.Api;

/**
 * DTO object for LegendComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.LegendComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class LegendComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private String applicationId;

	private String mapId;

	private FontStyleInfo font;

	private String title = "Legend";

	public LegendComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.BOTTOM);
		getLayoutConstraint().setFlowDirection(LayoutConstraintInfo.FLOW_Y);
		getLayoutConstraint().setMarginX(20);
		getLayoutConstraint().setMarginY(20);
		font = new FontStyleInfo();
		font.setFamily("Dialog");
		font.setStyle("Plain");
		font.setSize(12);
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public FontStyleInfo getFont() {
		return font;
	}

	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
