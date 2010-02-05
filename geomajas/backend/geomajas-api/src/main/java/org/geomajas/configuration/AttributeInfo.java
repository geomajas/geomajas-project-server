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
package org.geomajas.configuration;

/**
 * Attribute information class.
 *
 * @author Jan De Moerloose
 */
public class AttributeInfo extends AttributeBaseInfo {

	private static final long serialVersionUID = 152L;
	private String label;
	private boolean identifying;
	private boolean hidden;
	private ValidatorInfo validator = new ValidatorInfo();

	/**
	 * Default constructor for GWT.
	 */
	public AttributeInfo() {
		this(null, null);
	}

	/**
	 * Creates a non-editable, non-identifying, non-hidden attribute.
	 *
	 * @param name attribute name
	 * @param label attribute label
	 */
	public AttributeInfo(String name, String label) {
		this(false, false, false, name, label);
	}

	/**
	 * Full-option constructor.
	 *
	 * @param editable
	 * @param hidden
	 * @param identifying
	 * @param label attribute label
	 * @param name attribute name
	 */
	public AttributeInfo(boolean editable, boolean hidden, boolean identifying, String label, String name) {
		setEditable(editable);
		setHidden(hidden);
		setIdentifying(identifying);
		setLabel(label);
		setName(name);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isIdentifying() {
		return identifying;
	}

	public void setIdentifying(boolean identifying) {
		this.identifying = identifying;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public ValidatorInfo getValidator() {
		return validator;
	}

	public void setValidator(ValidatorInfo validator) {
		this.validator = validator;
	}
}
