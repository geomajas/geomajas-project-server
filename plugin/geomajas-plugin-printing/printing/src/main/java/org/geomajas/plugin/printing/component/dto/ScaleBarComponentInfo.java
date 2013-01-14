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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.annotation.Api;

/**
 * DTO object for ScaleBarComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.ScaleBarComponent
 * @since 2.0.0
 */
@Api(allMethods = true)
public class ScaleBarComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/**
	 * The unit (meter, mile, degree).
	 */
	private String unit = "units";

	/**
	 * The number of tics for the scale bar.
	 */
	private int ticNumber;

	/**
	 * The label font
	 */
	private FontStyleInfo font;

	/** Constructor. */
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

	/**
	 * Get unit (meter, mile, degree).
	 *
	 * @return unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Set unit (meter, mile, degree).
	 *
	 * @param unit unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Get the number of tics for the scale bar.
	 *
	 * @return number of tics
	 */
	public int getTicNumber() {
		return ticNumber;
	}

	/**
	 * Set number of tics for the scale bar.
	 *
	 * @param ticNumber number of tics
	 */
	public void setTicNumber(int ticNumber) {
		this.ticNumber = ticNumber;
	}

	/**
	 * Get font.
	 *
	 * @return font
	 */
	public FontStyleInfo getFont() {
		return font;
	}

	/**
	 * Set font.
	 *
	 * @param font font
	 */
	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

}
