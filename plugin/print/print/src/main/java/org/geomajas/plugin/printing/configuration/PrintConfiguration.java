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
package org.geomajas.plugin.printing.configuration;

import org.springframework.stereotype.Component;

/**
 * Access point for PrintTemplateDAO singleton.
 *
 * @author Jan De Moerloose
 */
@Component
public final class PrintConfiguration {

	private static InMemoryPrintTemplateDao inMemDao = new InMemoryPrintTemplateDao();

//	@Autowired
//	private HibernateUtil hibernateUtil;

	private PrintConfiguration() {
	}

	public static PrintTemplateDao getDao() {
		PrintTemplateDao dao;
//		try {
//			dao = new HibernatePrintTemplateDao(hibernateUtil.getCurrentSession());
//		} catch (HibernateLayerException e) {
		dao = inMemDao;
//		}
		return dao;
	}
}
