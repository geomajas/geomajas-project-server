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

import javax.annotation.PostConstruct;

import org.geomajas.annotation.Api;

/**
 * Information about how to access and how to render the label attribute.

 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LabelStyleInfo implements IsInfo {

	private static final int PRIME = 31;

	/**
	 * A special constant to be passed to {@link #setLabelAttributeName(String)}, denoting that the id should be used as
	 * the label text instead of a normal attribute value.
	 * 
	 * @since 1.10.0
	 */
	public static final String ATTRIBUTE_NAME_ID = "@id";

	private static final long serialVersionUID = 151L;
	private String labelAttributeName;
	private String labelValueExpression;
	private FontStyleInfo fontStyle = new FontStyleInfo();
	private FeatureStyleInfo backgroundStyle = new FeatureStyleInfo();
	
	/**
	 * Get label attribute name.
	 *
	 * @return label attribute name
	 */
	public String getLabelAttributeName() {
		return labelAttributeName;
	}

	/**
	 * Set label attribute name.
	 *
	 * @param labelAttributeName label attribute name
	 */
	public void setLabelAttributeName(String labelAttributeName) {
		this.labelAttributeName = labelAttributeName;
	}

	/**
	 * Get label value expression. The expression is evaluated by the
	 * configured {@link org.geomajas.service.FeatureExpressionService}.
	 * 
	 * @return label value expression
	 * @since 1.10.0
	 */
	public String getLabelValueExpression() {
		return labelValueExpression;
	}

	/**
	 * Set label value expression. The expression is evaluated by the
	 * configured {@link org.geomajas.service.FeatureExpressionService}.
	 *
	 * @param labelValueExpression label value expression
	 * @since 1.10.0
	 */
	public void setLabelValueExpression(String labelValueExpression) {
		this.labelValueExpression = labelValueExpression;
	}
	/**
	 * Get font style for label.
	 *
	 * @return font style
	 */
	public FontStyleInfo getFontStyle() {
		return fontStyle;
	}

	/**
	 * Set font style for label.
	 *
	 * @param fontStyle font style
	 */
	public void setFontStyle(FontStyleInfo fontStyle) {
		this.fontStyle = fontStyle;
	}

	/**
	 * Get the background style for the labels.
	 *
	 * @return background style for label
	 */
	public FeatureStyleInfo getBackgroundStyle() {
		return backgroundStyle;
	}

	/**
	 * Set background style for labels.
	 *
	 * @param backgroundStyle background style for label
	 */
	public void setBackgroundStyle(FeatureStyleInfo backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 *
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "LabelStyleInfo{" +
				"labelAttributeName='" + labelAttributeName + '\'' +
				", fontStyle=" + fontStyle +
				", backgroundStyle=" + backgroundStyle +
				'}';
	}

	/**
	 * String representation of object.
	 *
	 * @return string representation of object
	 * @since 1.8.0
	 */
	@Override
	public String toString() {
		return getCacheId();
	}

	/**
	 * Are the two objects equal?
	 *
	 * @param o object to compare
	 * @return true when objects are equal
	 * @since 1.8.0
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LabelStyleInfo)) {
			return false;
		}

		LabelStyleInfo that = (LabelStyleInfo) o;

		if (backgroundStyle != null ? !backgroundStyle.equals(that.backgroundStyle) : that.backgroundStyle != null) {
			return false;
		}
		if (fontStyle != null ? !fontStyle.equals(that.fontStyle) : that.fontStyle != null) {
			return false;
		}
		if (labelAttributeName != null ? !labelAttributeName.equals(that.labelAttributeName) :
				that.labelAttributeName != null) {
			return false;
		}

		return true;
	}

	/**
	 * Calculate object hash code.
	 *
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		int result = labelAttributeName != null ? labelAttributeName.hashCode() : 0;
		result = PRIME * result + (fontStyle != null ? fontStyle.hashCode() : 0);
		result = PRIME * result + (backgroundStyle != null ? backgroundStyle.hashCode() : 0);
		return result;
	}

	/** Finish configuration. */
	@PostConstruct
	protected void postConstruct() {
		if (null == labelValueExpression && null == labelAttributeName) {
			labelAttributeName = ATTRIBUTE_NAME_ID;
		}
	}
}
