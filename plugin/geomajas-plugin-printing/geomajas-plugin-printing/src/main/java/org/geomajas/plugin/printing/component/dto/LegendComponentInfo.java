/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
