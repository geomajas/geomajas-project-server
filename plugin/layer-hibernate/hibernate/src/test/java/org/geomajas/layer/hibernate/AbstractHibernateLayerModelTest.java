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
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.hibernate.pojo.HibernateTestManyToOne;
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
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/testContext.xml" })
@Transactional(rollbackFor = {org.geomajas.global.GeomajasException.class})
public abstract class AbstractHibernateLayerModelTest {

	public static final String PARAM_AT_ID_ATTR = "@id";
	public static final String PARAM_ID_ATTR = "id";
	public static final String PARAM_TEXT_ATTR = "textAttr";
	public static final String PARAM_INT_ATTR = "intAttr";
	public static final String PARAM_FLOAT_ATTR = "floatAttr";
	public static final String PARAM_DOUBLE_ATTR = "doubleAttr";
	public static final String PARAM_BOOLEAN_ATTR = "booleanAttr";
	public static final String PARAM_DATE_ATTR = "dateAttr";
	public static final String PARAM_MANY_TO_ONE = "manyToOne";
	public static final String PARAM_ONE_TO_MANY = "oneToMany";
	public static final String PARAM_GEOMETRY_ATTR = "geometry";

	/** Filter: manyToOne.@id */
	public static final String ATTR__MANY_TO_ONE__DOT__ID = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ PARAM_AT_ID_ATTR;

	/** Filter: manyToOne.textAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__TEXT = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_TEXT_ATTR;

	/** Filter: manyToOne.intAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__INT = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_INT_ATTR;

	/** Filter: manyToOne.floatAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__FLOAT = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_FLOAT_ATTR;

	/** Filter: manyToOne.doubleAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__DOUBLE = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_DOUBLE_ATTR;

	/** Filter: manyToOne.booleanAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__BOOLEAN = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_BOOLEAN_ATTR;

	/** Filter: manyToOne.dateAttr */
	public static final String ATTR__MANY_TO_ONE__DOT__DATE = PARAM_MANY_TO_ONE
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_DATE_ATTR;

	/** Filter: oneToMany.textAttr */
	public static final String ATTR__ONE_TO_MANY__DOT__TEXT = PARAM_ONE_TO_MANY
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_TEXT_ATTR;

	/** Filter: oneToMany.intAttr */
	public static final String ATTR__ONE_TO_MANY__DOT__INT = PARAM_ONE_TO_MANY
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_INT_ATTR;

	/** Filter: oneToMany.floatAttr */
	public static final String ATTR__ONE_TO_MANY__DOT__FLOAT = PARAM_ONE_TO_MANY
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_FLOAT_ATTR;

	/** Filter: oneToMany.doubleAttr */
	public static final String ATTR__ONE_TO_MANY__DOT__DOUBLE = PARAM_ONE_TO_MANY
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_DOUBLE_ATTR;

	/** Filter: oneToMany.booleanAttr */
	public static final String ATTR__ONE_TO_MANY__DOT__BOOLEAN = PARAM_ONE_TO_MANY
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_BOOLEAN_ATTR;

	/** Filter: oneToMany.dateAttr */
	public static final String ATTR__ONE_TO_MANY__DOT__DATE = PARAM_ONE_TO_MANY
			+ HibernateLayerUtil.SEPARATOR
			+ HibernateTestManyToOne.PARAM_DATE_ATTR;

	@Autowired
	protected SessionFactory factory;

	@Resource(name="layer")
	protected VectorLayer layer;

	@Resource(name="scrollableResultSetLayer")
	protected VectorLayer scrollableResultSetLayer;

	@Resource(name="associationLayer")
	protected VectorLayerAssociationSupport associationLayer;

	@Autowired
	protected FilterService filterCreator;

}
