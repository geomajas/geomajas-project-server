/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.validation.ValidatorInfo;

/**
 * Attribute information class.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 * @deprecated use the one of {@link AbstractAttributeInfo} or {@link AbstractReadOnlyAttributeInfo} or
 *             {@link AbstractEditableAttributeInfo}
 */
@Api(allMethods = true)
@Deprecated
public class AttributeInfo extends AttributeBaseInfo {

	private static final long serialVersionUID = 152L;

	@NotNull
	private String label;

	private boolean identifying;

	private boolean hidden;

	private String formInputType;

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
		this(false, false, false, name, label);
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
		super();
		setEditable(editable);
		setHidden(hidden);
		setIdentifying(identifying);
		setName(name);
		setLabel(label);
	}

	/**
	 * Get validator. Returns null as this is a read-only attribute
	 *
	 * @return validator
	 * @deprecated use {@link AbstractEditableAttributeInfo#getValidator()}
	 */
	@Deprecated
	public ValidatorInfo getValidator() {
		return null;
	}

	/**
	 * Set validator. Noop as this is a read-only attribute
	 *
	 * @param validator validator
	 * @deprecated use {@link AbstractEditableAttributeInfo#setValidator(ValidatorInfo)}
	 */
	@Deprecated
	public void setValidator(ValidatorInfo validator) {
		// NOSONAR nothing to do, needed for @Api
	}

	/**
	 * {@inheritDoc}
	 * @since 1.10.0
	 */
	@Override
	public boolean isEditable() { // NOSONAR override needed for @Api
		return super.isEditable();
	}

	/**
	 * {@inheritDoc}
	 * @since 1.10.0
	 */
	@Override
	public void setEditable(boolean editable) { // NOSONAR override needed for @Api
		super.setEditable(editable);
	}

	/**
	 * Get label for attribute.
	 *
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set label for attribute.
	 *
	 * @param label label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Is this an identifying attribute? Is it part of the feature id?
	 *
	 * @return true when attribute is part of the feature id
	 */
	public boolean isIdentifying() {
		return identifying;
	}

	/**
	 * Set whether the attribute is part of the feature id.
	 *
	 * @param identifying true when attribute is part of the feature id
	 */
	public void setIdentifying(boolean identifying) {
		this.identifying = identifying;
	}

	/**
	 * This value determines whether or not this attribute definition should be hidden in editing forms and other
	 * widgets on the client.
	 * <p/>
	 * The default value for this setting is 'false'.
	 *
	 * @return true when hidden.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * This value determines whether or not this attribute definition should be hidden in editing forms and other
	 * widgets on the client.
	 * <p/>
	 * The default value for this setting is 'false'.
	 *
	 * @param hidden new hidden status.
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Get the type of input field that should be used in client side forms when editing this attribute. This value
	 * should only be used when diverting from the default values (which are based upon the attribute type itself).
	 * Using this field though enables users to specify their own client-side form field types and use them through
	 * configuration.
	 *
	 * @return Returns the form input type to be used, or null when the default behavior is desired.
	 * @since 1.9.0
	 */
	public String getFormInputType() {
		return formInputType;
	}

	/**
	 * Set the type of input field that should be used in client side forms when editing this attribute. This value
	 * should only be used when diverting from the default values (which are based upon the attribute type itself).
	 * Using this field though enables users to specify their own client-side form field types and use them through
	 * configuration.
	 *
	 * @param formInputType form input type to be used. Leave null when the default behavior is desired.
	 * @since 1.9.0
	 */
	public void setFormInputType(String formInputType) {
		this.formInputType = formInputType;
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