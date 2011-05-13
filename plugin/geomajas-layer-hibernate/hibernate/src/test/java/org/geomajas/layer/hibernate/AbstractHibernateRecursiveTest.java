/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate;

import javax.annotation.Resource;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.hibernate.recursive.pojo.RecursiveTopFeature;
import org.geomajas.service.FilterService;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for test cases that wish to test a Hibernate model with recursive annotations in the Geomajas
 * configuration.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/recursiveContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public abstract class AbstractHibernateRecursiveTest {

	public static final String MTO_TEXT = RecursiveTopFeature.PARAM_MTO_ATTR + HibernateLayerUtil.SEPARATOR
			+ "textAttr";

	public static final String MTO_MTO = RecursiveTopFeature.PARAM_MTO_ATTR + HibernateLayerUtil.SEPARATOR
			+ "manyToOne";

	public static final String MTO_OTM = RecursiveTopFeature.PARAM_MTO_ATTR + HibernateLayerUtil.SEPARATOR
			+ "oneToMany";

	public static final String MTO_GEOMETRY = RecursiveTopFeature.PARAM_MTO_ATTR + HibernateLayerUtil.SEPARATOR
			+ "geometry";

	public static final String MTO_MTO_TEXT = MTO_MTO + HibernateLayerUtil.SEPARATOR + "textAttr";

	@Autowired
	protected SessionFactory factory;

	@Resource(name = "layer1")
	protected VectorLayer layer1;

	@Resource(name = "layer2")
	protected VectorLayer layer2;

	@Resource(name = "layer3")
	protected VectorLayer layer3;

	@Autowired
	protected FilterService filterCreator;
}