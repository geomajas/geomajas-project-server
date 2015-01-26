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
package org.geomajas.layer.hibernate;

import javax.annotation.Resource;

import org.geomajas.layer.VectorLayer;
import org.geomajas.service.FilterService;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit test that tests filters for the HibernateLayer.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/simpleContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public abstract class AbstractHibernateSimpleTest {

	public static final String PARAM_TEXT_ATTR = "textAttr";

	public static final String PARAM_INT_ATTR = "intAttr";

	public static final String PARAM_FLOAT_ATTR = "floatAttr";

	public static final String PARAM_DOUBLE_ATTR = "doubleAttr";

	public static final String PARAM_BOOLEAN_ATTR = "booleanAttr";

	public static final String PARAM_DATE_ATTR = "dateAttr";

	public static final String PARAM_MANY_TO_ONE = "manyToOne";

	public static final String PARAM_ONE_TO_MANY = "oneToMany";

	public static final String PARAM_GEOMETRY_ATTR = "geometry";

	@Autowired
	protected SessionFactory factory;

	@Resource(name = "layer")
	protected VectorLayer layer;

	@Resource(name = "scrollableResultSetLayer")
	protected VectorLayer scrollableResultSetLayer;

	@Autowired
	protected FilterService filterCreator;
}