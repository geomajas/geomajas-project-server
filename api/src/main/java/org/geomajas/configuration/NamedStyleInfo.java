/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.global.CacheableObject;
import org.geomajas.sld.UserStyleInfo;

/**
 * A named layer style for vector layers. The layer style consists of a list of feature styles. Each style has a unique
 * name. Can hold both SLD and simple {@link FeatureStyleInfo} configuration.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class NamedStyleInfo implements IsInfo, CacheableObject {
	
	private static final long serialVersionUID = 154L;
	private static final int HASH_FACTOR = 31;


	/**
	 * Name of the default style. 
	 * @since 1.10.0
	 */
	public static final String DEFAULT_NAME = "Default";

	private List<FeatureStyleInfo> featureStyles = new ArrayList<FeatureStyleInfo>();
	
	private LabelStyleInfo labelStyle;

	@NotNull
	private String name;
	
	private UserStyleInfo userStyle;
	
	private String sldLocation;
	
	private String sldLayerName;
	
	private String sldStyleName;

	/**
	 * Applies default values to all properties that have not been set.
	 * 
	 * @since 1.10.0
	 */
	public void applyDefaults() {
		if (getName() == null) {
			setName(DEFAULT_NAME);
		}
		if (getFeatureStyles().size() == 0) {
			getFeatureStyles().add(new FeatureStyleInfo());
		}
		for (FeatureStyleInfo featureStyle : getFeatureStyles()) {
			featureStyle.applyDefaults();
		}
		if (getLabelStyle().getLabelAttributeName() == null) {
			getLabelStyle().setLabelAttributeName(LabelStyleInfo.ATTRIBUTE_NAME_ID);
		}
		getLabelStyle().getBackgroundStyle().applyDefaults();
		getLabelStyle().getFontStyle().applyDefaults();
	}

	/**
	 * Get the SLD UserStyle info for this layer.
	 *
	 * @return SLD info
	 * @since 1.10.0
	 */
	public UserStyleInfo getUserStyle() {
		return userStyle;
	}
	
	/**
	 * Set SLD UserStyle info, the style configuration for this layer.
	 *
	 * @param userStyle SLD UserStyle info
	 * @since 1.10.0
	 */
	public void setUserStyle(UserStyleInfo userStyle) {
		this.userStyle = userStyle;
	}

	/**
	 * Get the SLD file location (Spring resource format).
	 * 
	 * @return the location of the SLD file
	 * @since 1.10.0
	 */
	public String getSldLocation() {
		return sldLocation;
	}

	/**
	 * Set the SLD file location (Spring resource format).
	 * 
	 * @param sldLocation the SLD location
	 * @since 1.10.0
	 */
	public void setSldLocation(String sldLocation) {
		this.sldLocation = sldLocation;
	}
	
	/**
	 * Get the name of the SLD NamedLayer/UserLayer that contains this style. If null the first NamedLayer should be
	 * chosen.
	 * 
	 * @return the name of the NamedLayer/UserLayer
	 * @since 1.10.0
	 */
	public String getSldLayerName() {
		return sldLayerName;
	}

	/**
	 * Set the name of the SLD NamedLayer/UserLayer that contains this style.
	 * 
	 * @param sldLayerName the name of the NamedLayer/UserLayer
	 * @since 1.10.0
	 */
	public void setSldLayerName(String sldLayerName) {
		this.sldLayerName = sldLayerName;
	}

	/**
	 * Get the name of the SLD UserStyle that corresponds to this style. If null the first UserStyle should be chosen.
	 * 
	 * @return the name of the UserStyle
	 * @since 1.10.0
	 */
	public String getSldStyleName() {
		return sldStyleName;
	}

	/**
	 * Set the name of the SLD UserStyle that contains this style.
	 * 
	 * @param sldStyleName the name of the UserStyle
	 * @since 1.10.0
	 */
	public void setSldStyleName(String sldStyleName) {
		this.sldStyleName = sldStyleName;
	}

	/**
	 * Get possible styles for features. These are traversed from the beginning, the first style for which the
	 * formula evaluates successfully is applied.
	 *
	 * @return list of feature styles
	 */
	public List<FeatureStyleInfo> getFeatureStyles() {
		return featureStyles;
	}

	/**
	 * Set list of possible styles for features. These are traversed from the beginning, the first style for which the
	 * formula evaluates successfully is applied.
	 *
	 * @param featureStyles list of feature styles
	 */
	public void setFeatureStyles(List<FeatureStyleInfo> featureStyles) {
		this.featureStyles = featureStyles;
	}

	/**
	 * Name for style info.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name for style info.
	 *
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get label style.
	 *
	 * @return label style
	 */
	public LabelStyleInfo getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Set label style.
	 *
	 * @param labelStyleInfo label style
	 */
	public void setLabelStyle(LabelStyleInfo labelStyleInfo) {
		this.labelStyle = labelStyleInfo;
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
		return "NamedStyleInfo{" +
				"userStyle=" + userStyle +
				", featureStyles=" + featureStyles +
				", labelStyle=" + labelStyle +
				", name='" + name + '\'' +
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
		if (!(o instanceof NamedStyleInfo)) {
			return false;
		}

		NamedStyleInfo that = (NamedStyleInfo) o;

		if (featureStyles != null ? !featureStyles.equals(that.featureStyles) : that.featureStyles != null) {
			return false;
		}
		if (labelStyle != null ? !labelStyle.equals(that.labelStyle) : that.labelStyle != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		if (sldLayerName != null ? !sldLayerName.equals(that.sldLayerName) : that.sldLayerName != null) {
			return false;
		}
		if (sldLocation != null ? !sldLocation.equals(that.sldLocation) : that.sldLocation != null) {
			return false;
		}
		if (sldStyleName != null ? !sldStyleName.equals(that.sldStyleName) : that.sldStyleName != null) {
			return false;
		}
		if (userStyle != null ? !userStyle.equals(that.userStyle) : that.userStyle != null) {
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
		int result = featureStyles != null ? featureStyles.hashCode() : 0;
		result = HASH_FACTOR * result + (labelStyle != null ? labelStyle.hashCode() : 0);
		result = HASH_FACTOR * result + (name != null ? name.hashCode() : 0);
		result = HASH_FACTOR * result + (userStyle != null ? userStyle.hashCode() : 0);
		result = HASH_FACTOR * result + (sldLocation != null ? sldLocation.hashCode() : 0);
		result = HASH_FACTOR * result + (sldLayerName != null ? sldLayerName.hashCode() : 0);
		result = HASH_FACTOR * result + (sldStyleName != null ? sldStyleName.hashCode() : 0);
		return result;
	}
}
