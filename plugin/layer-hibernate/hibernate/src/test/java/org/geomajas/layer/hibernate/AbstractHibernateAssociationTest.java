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
import org.geomajas.layer.hibernate.association.pojo.ManyToOneProperty;
import org.geomajas.service.FilterService;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstraction for all test cases that wish to test the use of associations (many-to-one and one-to-many) in the
 * Hibernate model.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/associationContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public abstract class AbstractHibernateAssociationTest {

	public static final String MTO = "manyToOne";

	public static final String OTM = "oneToMany";

	public static final String ID = "@id";

	/** Filter: manyToOne.textAttr */
	public static final String MTO_ID = MTO + HibernateLayerUtil.SEPARATOR + ID;

	/** Filter: manyToOne.textAttr */
	public static final String MTO_TEXT = MTO + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_TEXT_ATTR;

	/** Filter: manyToOne.intAttr */
	public static final String MTO_INT = MTO + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_INT_ATTR;

	/** Filter: manyToOne.floatAttr */
	public static final String MTO_FLOAT = MTO + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_FLOAT_ATTR;

	/** Filter: manyToOne.doubleAttr */
	public static final String MTO_DOUBLE = MTO + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_DOUBLE_ATTR;

	/** Filter: manyToOne.booleanAttr */
	public static final String MTO_BOOLEAN = MTO + HibernateLayerUtil.SEPARATOR
			+ ManyToOneProperty.PARAM_BOOLEAN_ATTR;

	/** Filter: manyToOne.dateAttr */
	public static final String MTO_DATE = MTO + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_DATE_ATTR;

	/** Filter: oneToMany.textAttr */
	public static final String OTM_TEXT = OTM + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_TEXT_ATTR;

	/** Filter: oneToMany.intAttr */
	public static final String OTM_INT = OTM + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_INT_ATTR;

	/** Filter: oneToMany.floatAttr */
	public static final String OTM_FLOAT = OTM + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_FLOAT_ATTR;

	/** Filter: oneToMany.doubleAttr */
	public static final String OTM_DOUBLE = OTM + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_DOUBLE_ATTR;

	/** Filter: oneToMany.booleanAttr */
	public static final String OTM_BOOLEAN = OTM + HibernateLayerUtil.SEPARATOR
			+ ManyToOneProperty.PARAM_BOOLEAN_ATTR;

	/** Filter: oneToMany.dateAttr */
	public static final String OTM_DATE = OTM + HibernateLayerUtil.SEPARATOR + ManyToOneProperty.PARAM_DATE_ATTR;

	@Autowired
	protected SessionFactory factory;

	@Resource(name = "layer")
	protected VectorLayer layer;

	@Autowired
	protected FilterService filterCreator;
}