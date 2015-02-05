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

package org.geomajas.internal.layer.feature;

import org.springframework.util.ObjectUtils;

/**
 * Simple bean for bean layer for testing.
 *
 * @author Joachim Van der Auwera
 */
public class NameBean {

	private static final int PRIME = 31;

	private Long id;

	private String name;
	private String surname;
	private String geometry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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
		result = PRIME * result + ((geometry == null) ? 0 : geometry.hashCode());
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		result = PRIME * result + ((id == null) ? 0 : name.hashCode());
		result = PRIME * result + ((id == null) ? 0 : surname.hashCode());
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
		NameBean other = (NameBean) obj;
		return ObjectUtils.nullSafeEquals(id, other.id)
				&& ObjectUtils.nullSafeEquals(name, other.name)
				&& ObjectUtils.nullSafeEquals(surname, other.surname)
				&& ObjectUtils.nullSafeEquals(geometry, other.geometry);
	}

	@Override
	public String toString() {
		return "NameBean [id=" + id + ", name=" + name + ", surname=" + surname + ", geometry=" + geometry + "]";
	}

}
