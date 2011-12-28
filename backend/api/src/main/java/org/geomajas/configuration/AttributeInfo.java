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
package org.geomajas.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.validation.ValidatorInfo;

/**
 * Attribute information class.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 * @deprecated directly use the AbstractEditableAttributeInfo class
 */
@Api(allMethods = true)
@Deprecated
public class AttributeInfo extends AbstractEditableAttributeInfo {

	private static final long serialVersionUID = 152L;

	private boolean includedInForm;

	/** Default constructor for GWT. */
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
		super(false, false, false, name, label);
	}

	/**
	 * Full-option constructor.
	 *
	 * @param editable editable status
	 * @param hidden hidden status
	 * @param identifying is attribute identifying?
	 * @param name attribute name
	 * @param label attribute label
	 */
	public AttributeInfo(boolean editable, boolean hidden, boolean identifying, String name, String label) {
		super(editable, hidden, identifying, name, label);
	}

	@Override
	public ValidatorInfo getValidator() {
		return super.getValidator(); // NOSONAR	override needed for @Api
	}

	@Override
	public void setValidator(ValidatorInfo validator) {
		super.setValidator(validator);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.10.0
	 */
	@Override
	public boolean isEditable() {
		return super.isEditable(); // NOSONAR override needed for @Api
	}

	/**
	 * {@inheritDoc}
	 * @since 1.10.0
	 */
	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable); // NOSONAR override needed for @Api
	}

	/** {@inheritDoc} */
	@Override
	public String getLabel() {
		return super.getLabel(); // NOSONAR override needed for @Api
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label); // NOSONAR override needed for @Api
	}

	@Override
	public boolean isIdentifying() {
		return super.isIdentifying(); // NOSONAR override needed for @Api
	}

	@Override
	public void setIdentifying(boolean identifying) {
		super.setIdentifying(identifying); // NOSONAR override needed for @Api
	}

	@Override
	public boolean isHidden() {
		return super.isHidden(); // NOSONAR override needed for @Api
	}

	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden); // NOSONAR override needed for @Api
	}

	/**
	 * {@inheritDoc}
	 * @since 1.9.0
	 */
	@Override
	public String getFormInputType() {
		return super.getFormInputType(); // NOSONAR override needed for @Api
	}

	/**
	 * {@inheritDoc}
	 * @since 1.9.0
	 */
	@Override
	public void setFormInputType(String formInputType) {
		super.setFormInputType(formInputType); // NOSONAR override needed for @Api
	}

	/**
	 * Do not use...
	 *
	 * @param includedInForm new value.
	 * @since 1.9.0
	 * @deprecated Wrongfully added, use {@link #setHidden(boolean)} instead
	 */
	@Deprecated
	public void setIncludedInForm(boolean includedInForm) {
		this.includedInForm = includedInForm;
	}

	/**
	 * Do not use...
	 *
	 * @return Include in form value.
	 * @since 1.9.0
	 * @deprecated Wrongfully added, use {@link #isHidden()} instead
	 */
	@Deprecated
	public boolean isIncludedInForm() {
		return includedInForm;
	}
}