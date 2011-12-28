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

import javax.validation.constraints.NotNull;

/**
 * Base information which is shared between all read-only attributes.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.10.0 split out of AttributeInfo which is now deprecated
 */
@Api(allMethods = true)
public abstract class AbstractReadOnlyAttributeInfo extends AttributeBaseInfo {

	private static final long serialVersionUID = 1100L;

	@NotNull
	private String label;

	private boolean identifying;

	private boolean hidden;

	private String formInputType;

	/**
	 * Full-option constructor.
	 *
	 * @param hidden hidden status
	 * @param identifying is attribute identifying?
	 * @param name attribute name
	 * @param label attribute label
	 */
	public AbstractReadOnlyAttributeInfo(boolean hidden, boolean identifying, String name, String label) {
		setHidden(hidden);
		setIdentifying(identifying);
		setName(name);
		setLabel(label);
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
	 */
	public void setFormInputType(String formInputType) {
		this.formInputType = formInputType;
	}

}