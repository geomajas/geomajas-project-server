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
package org.geomajas.layer.bean;

import java.util.Date;
import java.util.List;

import org.geomajas.layer.feature.FeatureModel;
import org.springframework.util.ObjectUtils;

/**
 * Java bean feature containing all attribute types. For easy Spring configuration, the geometry can be specified in wkt
 * (Well-Known-Text) format. Samples: <code><pre>
POINT(6 10)
LINESTRING(3 4,10 50,20 25)
POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2))
MULTIPOINT((3.5 5.6),(4.8 10.5))
MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))
MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))
GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10))
POINT ZM (1 1 5 60)
POINT M (1 1 80)
POINT EMPTY
MULTIPOLYGON EMPTY
</pre></code>
 * 
 * @author Jan De Moerloose
 */
public class FeatureBean implements FeatureModelAware {

	private static final int PRIME = 31;

	private Long id;

	private Boolean booleanAttr;

	private String currencyAttr;

	private Date dateAttr;

	private Double doubleAttr;

	private Float floatAttr;

	private String imageUrlAttr;

	private Integer integerAttr;

	private Long longAttr;

	private Short shortAttr;

	private String stringAttr;

	private String urlAttr;

	private ManyToOneAttributeBean manyToOneAttr;

	private List<OneToManyAttributeBean> oneToManyAttr;

	private String geometry;
	
	private FeatureModel featureModel;
	
	public FeatureModel getFeatureModel() {
		return featureModel;
	}
	
	public void setFeatureModel(FeatureModel featureModel) {
		this.featureModel = featureModel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getBooleanAttr() {
		return booleanAttr;
	}

	public void setBooleanAttr(Boolean booleanAttr) {
		this.booleanAttr = booleanAttr;
	}

	public String getCurrencyAttr() {
		return currencyAttr;
	}

	public void setCurrencyAttr(String currencyAttr) {
		this.currencyAttr = currencyAttr;
	}

	public Date getDateAttr() {
		return dateAttr;
	}

	public void setDateAttr(Date dateAttr) {
		this.dateAttr = dateAttr;
	}

	public Double getDoubleAttr() {
		return doubleAttr;
	}

	public void setDoubleAttr(Double doubleAttr) {
		this.doubleAttr = doubleAttr;
	}

	public Float getFloatAttr() {
		return floatAttr;
	}

	public void setFloatAttr(Float floatAttr) {
		this.floatAttr = floatAttr;
	}

	public String getImageUrlAttr() {
		return imageUrlAttr;
	}

	public void setImageUrlAttr(String imageUrlAttr) {
		this.imageUrlAttr = imageUrlAttr;
	}

	public Integer getIntegerAttr() {
		return integerAttr;
	}

	public void setIntegerAttr(Integer integerAttr) {
		this.integerAttr = integerAttr;
	}

	public Long getLongAttr() {
		return longAttr;
	}

	public void setLongAttr(Long longAttr) {
		this.longAttr = longAttr;
	}

	public Short getShortAttr() {
		return shortAttr;
	}

	public void setShortAttr(Short shortAttr) {
		this.shortAttr = shortAttr;
	}

	public String getStringAttr() {
		return stringAttr;
	}

	public void setStringAttr(String stringAttr) {
		this.stringAttr = stringAttr;
	}

	public String getUrlAttr() {
		return urlAttr;
	}

	public void setUrlAttr(String urlAttr) {
		this.urlAttr = urlAttr;
	}

	public ManyToOneAttributeBean getManyToOneAttr() {
		return manyToOneAttr;
	}

	public void setManyToOneAttr(ManyToOneAttributeBean manyToOneAttr) {
		this.manyToOneAttr = manyToOneAttr;
	}

	public List<OneToManyAttributeBean> getOneToManyAttr() {
		return oneToManyAttr;
	}

	public void setOneToManyAttr(List<OneToManyAttributeBean> oneToManyAttr) {
		this.oneToManyAttr = oneToManyAttr;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = PRIME * result + ((booleanAttr == null) ? 0 : booleanAttr.hashCode());
		result = PRIME * result + ((currencyAttr == null) ? 0 : currencyAttr.hashCode());
		result = PRIME * result + ((dateAttr == null) ? 0 : dateAttr.hashCode());
		result = PRIME * result + ((doubleAttr == null) ? 0 : doubleAttr.hashCode());
		result = PRIME * result + ((floatAttr == null) ? 0 : floatAttr.hashCode());
		result = PRIME * result + ((geometry == null) ? 0 : geometry.hashCode());
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		result = PRIME * result + ((imageUrlAttr == null) ? 0 : imageUrlAttr.hashCode());
		result = PRIME * result + ((integerAttr == null) ? 0 : integerAttr.hashCode());
		result = PRIME * result + ((longAttr == null) ? 0 : longAttr.hashCode());
		result = PRIME * result + ((manyToOneAttr == null) ? 0 : manyToOneAttr.hashCode());
		result = PRIME * result + ((oneToManyAttr == null) ? 0 : oneToManyAttr.hashCode());
		result = PRIME * result + ((shortAttr == null) ? 0 : shortAttr.hashCode());
		result = PRIME * result + ((stringAttr == null) ? 0 : stringAttr.hashCode());
		result = PRIME * result + ((urlAttr == null) ? 0 : urlAttr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FeatureBean other = (FeatureBean) obj;
		return ObjectUtils.nullSafeEquals(booleanAttr, other.booleanAttr)
				&& ObjectUtils.nullSafeEquals(currencyAttr, other.currencyAttr)
				&& ObjectUtils.nullSafeEquals(dateAttr, other.dateAttr)
				&& ObjectUtils.nullSafeEquals(floatAttr, other.floatAttr)
				&& ObjectUtils.nullSafeEquals(doubleAttr, other.doubleAttr)
				&& ObjectUtils.nullSafeEquals(geometry, other.geometry)
				&& ObjectUtils.nullSafeEquals(id, other.id)
				&& ObjectUtils.nullSafeEquals(imageUrlAttr, other.imageUrlAttr)
				&& ObjectUtils.nullSafeEquals(integerAttr, other.integerAttr)
				&& ObjectUtils.nullSafeEquals(longAttr, other.longAttr)
				&& ObjectUtils.nullSafeEquals(manyToOneAttr, other.manyToOneAttr)
				&& ObjectUtils.nullSafeEquals(oneToManyAttr, other.oneToManyAttr)
				&& ObjectUtils.nullSafeEquals(shortAttr, other.shortAttr)
				&& ObjectUtils.nullSafeEquals(stringAttr, other.stringAttr)
				&& ObjectUtils.nullSafeEquals(urlAttr, other.urlAttr);
	}

	@Override
	public String toString() {
		return "FeatureBean [id=" + id + ", booleanAttr=" + booleanAttr + ", currencyAttr=" + currencyAttr
				+ ", dateAttr=" + dateAttr + ", doubleAttr=" + doubleAttr + ", floatAttr=" + floatAttr
				+ ", imageUrlAttr=" + imageUrlAttr + ", integerAttr=" + integerAttr + ", longAttr=" + longAttr
				+ ", shortAttr=" + shortAttr + ", stringAttr=" + stringAttr + ", urlAttr=" + urlAttr
				+ ", manyToOneAttr=" + manyToOneAttr + ", oneToManyAttr=" + oneToManyAttr + ", geometry=" + geometry
				+ "]";
	}	
	
	
}
