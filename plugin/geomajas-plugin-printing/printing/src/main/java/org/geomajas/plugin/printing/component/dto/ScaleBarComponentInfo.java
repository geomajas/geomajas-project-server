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
 * DTO object for ScaleBarComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.ScaleBarComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class ScaleBarComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/**
	 * The unit (meter, mile, degree)
	 */
	private String unit = "units";

	/**
	 * The number of tics for the scalebar
	 */
	private int ticNumber;

	/**
	 * The label font
	 */
	private FontStyleInfo font;

	public ScaleBarComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.LEFT);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.BOTTOM);
		getLayoutConstraint().setMarginX(20);
		getLayoutConstraint().setMarginY(20);
		getLayoutConstraint().setWidth(200);
		font = new FontStyleInfo();
		font.setFamily("Dialog");
		font.setStyle("Plain");
		font.setSize(10);
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getTicNumber() {
		return ticNumber;
	}

	public void setTicNumber(int ticNumber) {
		this.ticNumber = ticNumber;
	}

	public FontStyleInfo getFont() {
		return font;
	}

	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

}
