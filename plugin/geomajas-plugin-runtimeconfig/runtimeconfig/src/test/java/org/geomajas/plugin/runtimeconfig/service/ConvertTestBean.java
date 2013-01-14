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
package org.geomajas.plugin.runtimeconfig.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConvertTestBean {

	private String primitive;

	private int otherPrimitive;

	private TestEnum testEnum;

	private ConvertTestBean object;

	private List<ConvertTestBean> list;

	private ConvertTestBean[] array;

	private Map<String, ConvertTestBean> map;

	private Map<ConvertTestBean, ConvertTestBean> map2;

	private Set<ConvertTestBean> set;

	public TestEnum getTestEnum() {
		return testEnum;
	}

	public void setTestEnum(TestEnum testEnum) {
		this.testEnum = testEnum;
	}

	public String getPrimitive() {
		return primitive;
	}

	public void setPrimitive(String primitive) {
		this.primitive = primitive;
	}

	public int getOtherPrimitive() {
		return otherPrimitive;
	}

	public void setOtherPrimitive(int otherPrimitive) {
		this.otherPrimitive = otherPrimitive;
	}

	public ConvertTestBean getObject() {
		return object;
	}

	public void setObject(ConvertTestBean object) {
		this.object = object;
	}

	public List<ConvertTestBean> getList() {
		return list;
	}

	public void setList(List<ConvertTestBean> list) {
		this.list = list;
	}

	public ConvertTestBean[] getArray() {
		return array;
	}

	public void setArray(ConvertTestBean[] array) {
		this.array = array;
	}

	public Map<String, ConvertTestBean> getMap() {
		return map;
	}

	public void setMap(Map<String, ConvertTestBean> map) {
		this.map = map;
	}

	public Map<ConvertTestBean, ConvertTestBean> getMap2() {
		return map2;
	}

	public void setMap2(Map<ConvertTestBean, ConvertTestBean> map2) {
		this.map2 = map2;
	}

	public Set<ConvertTestBean> getSet() {
		return set;
	}

	public void setSet(Set<ConvertTestBean> set) {
		this.set = set;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(array);
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((map2 == null) ? 0 : map2.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + otherPrimitive;
		result = prime * result + ((primitive == null) ? 0 : primitive.hashCode());
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		result = prime * result + ((testEnum == null) ? 0 : testEnum.hashCode());
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
		ConvertTestBean other = (ConvertTestBean) obj;
		if (!Arrays.equals(array, other.array)) {
			return false;
		}
		if (list == null) {
			if (other.list != null) {
				return false;
			}
		} else if (!list.equals(other.list)) {
			return false;
		}
		if (map == null) {
			if (other.map != null) {
				return false;
			}
		} else if (!map.equals(other.map)) {
			return false;
		}
		if (map2 == null) {
			if (other.map2 != null) {
				return false;
			}
		} else if (!map2.equals(other.map2)) {
			return false;
		}
		if (object == null) {
			if (other.object != null) {
				return false;
			}
		} else if (!object.equals(other.object)) {
			return false;
		}
		if (otherPrimitive != other.otherPrimitive) {
			return false;
		}
		if (primitive == null) {
			if (other.primitive != null) {
				return false;
			}
		} else if (!primitive.equals(other.primitive)) {
			return false;
		}
		if (set == null) {
			if (other.set != null) {
				return false;
			}
		} else if (!set.equals(other.set)) {
			return false;
		}
		if (testEnum != other.testEnum) {
			return false;
		}
		return true;
	}

}
