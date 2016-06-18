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
package org.geomajas.layer.hibernate;

import javax.annotation.Resource;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.hibernate.inheritance.pojo.ExtendedAttribute;
import org.geomajas.layer.hibernate.inheritance.pojo.ExtendedHibernateTestFeature;
import org.geomajas.service.FilterService;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstraction for all test cases that wish to test the use of inheritance in the Hibernate model.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/inheritanceContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public abstract class AbstractHibernateInheritanceTest {

	public static final String PARAM_TEXT_ATTR = ExtendedHibernateTestFeature.PARAM_TEXT_ATTR;

	public static final String PARAM_INT_ATTR = ExtendedHibernateTestFeature.PARAM_INT_ATTR;

	public static final String PARAM_MANY_TO_ONE = ExtendedHibernateTestFeature.PARAM_MTO_ATTR;

	public static final String PARAM_ONE_TO_MANY = ExtendedHibernateTestFeature.PARAM_OTM_ATTR;

	public static final String PARAM_GEOMETRY_ATTR = ExtendedHibernateTestFeature.PARAM_GEOMETRY_ATTR;

	/** Filter: manyToOne.textAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__TEXT = PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
			+ ExtendedAttribute.PARAM_TEXT_ATTR;

	/** Filter: manyToOne.intAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__INT = PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
			+ ExtendedAttribute.PARAM_INT_ATTR;

	@Autowired
	protected SessionFactory factory;

	@Resource(name = "layer")
	protected VectorLayer layer;

	@Autowired
	protected FilterService filterCreator;
}