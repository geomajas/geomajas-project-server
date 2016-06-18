/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Very simple data bean implementation.
 *
 * @author Joachim Van der Auwera
 */
@Component("Data")
@Scope("prototype")
public class DataBean implements Data {

	private int someValue;

	public int getSomeValue() {
		return someValue;
	}

	public void setSomeValue(int someValue) {
		this.someValue = someValue;
	}
}
