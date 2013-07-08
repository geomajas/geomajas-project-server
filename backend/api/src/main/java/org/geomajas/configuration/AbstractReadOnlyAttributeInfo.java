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

import javax.validation.constraints.NotNull;

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

	@NotNull
	private String label;

	private boolean identifying;

	private boolean hidden;
	
	/** No-arguments constructor needed for GWT. */
	public AbstractReadOnlyAttributeInfo() {
		super();
	}

	/**
	 * Get label for attribute.
	 *
	 * @return label
	 * @since 1.13.0 split out of deprecated AttributeInfo.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set label for attribute.
	 *
	 * @param label label
	 * @since 1.13.0 split out of deprecated AttributeInfo.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Is this an identifying attribute? Is it part of the feature id?
	 *
	 * @return true when attribute is part of the feature id
	 * @since 1.13.0 split out of deprecated AttributeInfo.
	 */
	public boolean isIdentifying() {
		return identifying;
	}

	/**
	 * Set whether the attribute is part of the feature id.
	 *
	 * @param identifying true when attribute is part of the feature id
	 * @since 1.13.0 split out of deprecated AttributeInfo.
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
	 * @since 1.13.0 split out of deprecated AttributeInfo.
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
	 * @since 1.13.0 split out of deprecated AttributeInfo.
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}