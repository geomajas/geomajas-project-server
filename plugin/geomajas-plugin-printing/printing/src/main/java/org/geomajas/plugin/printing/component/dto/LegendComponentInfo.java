/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.annotation.Api;

/**
 * DTO object for LegendComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.LegendComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class LegendComponentInfo extends PrintComponentInfo {

	private static final long serialVersionUID = 200L;

	private String applicationId;

	private String mapId;

	private FontStyleInfo font;

	private String title = "Legend";

	/**
	 * Default contrustor.
	 */
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

	/**
	 * Get application id.
	 *
	 * @return application id
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Set application id.
	 *
	 * @param applicationId application id
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Get map id.
	 *
	 * @return map id
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * Set map id.
	 *
	 * @param mapId map id
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	/**
	 * Get font style.
	 *
	 * @return font style
	 */
	public FontStyleInfo getFont() {
		return font;
	}

	/**
	 * Set font style.
	 *
	 * @param font font style
	 */
	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

	/**
	 * Get title.
	 *
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set title.
	 *
	 * @param title title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
