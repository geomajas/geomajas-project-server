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

package org.geomajas.configuration;

import org.geomajas.annotation.Api;

/**
 * Base information which is shared between all read-only attributes.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.10.0 split out of AttributeInfo which is now deprecated
 */
@Api(allMethods = true)
public abstract class AbstractReadOnlyAttributeInfo extends AttributeInfo {

	private static final long serialVersionUID = 1100L;

	/** No-arguments constructor needed for GWT. */
	public AbstractReadOnlyAttributeInfo() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public boolean isEditable() { // NOSONAR override needed for @Api
		return super.isEditable();
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public void setEditable(boolean editable) { // NOSONAR override needed for @Api
		super.setEditable(editable);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public String getLabel() {
		return super.getLabel();
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public void setLabel(String label) {
		super.setLabel(label);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public boolean isIdentifying() {
		return super.isIdentifying();
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public void setIdentifying(boolean identifying) {
		super.setIdentifying(identifying);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public String getFormInputType() {
		return super.getFormInputType();
	}

	/**
	 * {@inheritDoc}
	 * @since 1.13.0 don't expose deprecated methods from AttributeInfo directly.
	 */
	@Override
	public void setFormInputType(String formInputType) {
		super.setFormInputType(formInputType);
	}
}