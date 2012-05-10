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
package org.geomajas.gwt.client.action.toolbar.parameter;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.Parameter;

/**
 * {@link Parameter} that is used to configure the layout of a button in ribbon or drop down panel.
 *
 * @author Emiel Ackermann
 * @since 1.11.0
 */
@Api(allMethods = true)
public class ButtonLayoutParameter extends Parameter {

	private static final long serialVersionUID = 1L;
	private ButtonLayoutStyle buttonLayoutStyle;

	/** Parameter name. */
	public static final String NAME = "buttonLayout";

	/** No-arguments constructor. */
	public ButtonLayoutParameter() {
		super.setName(NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		throw new IllegalArgumentException(
				"Public naming is not allowed for LayoutParameter (its name is always 'buttonLayout')");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String value) {
		this.buttonLayoutStyle = ButtonLayoutStyle.valueOf(value);
		super.setValue(buttonLayoutStyle.toString());
	}

	/**
	 * Set button layout style.
	 *
	 * @param buttonLayoutStyle button layout style
	 */
	public void setValue(ButtonLayoutStyle buttonLayoutStyle) {
		this.buttonLayoutStyle = buttonLayoutStyle;
		super.setValue(buttonLayoutStyle.toString());
	}

	/**
	 * Get button layout style.
	 *
	 * @return button layout style
	 */
	public ButtonLayoutStyle getButtonLayoutStyle() {
		return buttonLayoutStyle;
	}

}
