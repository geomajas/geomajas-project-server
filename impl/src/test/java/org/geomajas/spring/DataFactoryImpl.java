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

package org.geomajas.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Test data factory.
 *
 * @author Joachim Van der Auwera
 */
@Component("DataFactory")
public class DataFactoryImpl implements DataFactory {

	@Autowired
	private BeanFactory beanFactory;

	public Data createData() {
		return beanFactory.getBean("Data", Data.class);
	}
}
