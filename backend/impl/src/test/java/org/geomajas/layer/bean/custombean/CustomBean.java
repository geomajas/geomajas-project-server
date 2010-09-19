/**
 * 
 */
package org.geomajas.layer.bean.custombean;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Balder Van Camp
 *
 */
public class CustomBean {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Geometry geometry;

	private String name;

}
