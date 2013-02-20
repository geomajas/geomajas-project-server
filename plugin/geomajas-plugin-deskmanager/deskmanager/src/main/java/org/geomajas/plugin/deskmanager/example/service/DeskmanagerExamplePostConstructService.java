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
package org.geomajas.plugin.deskmanager.example.service;

import javax.annotation.PostConstruct;

import org.geomajas.geometry.service.WktException;
import org.geomajas.global.GeomajasException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Oliver May
 * 
 *         Helper class to call some methods after constructing the context. This and
 *         {@link DeskmanagerExampleDatabaseProvisioningService} can't be combined into one class because
 *         {@link PostConstruct} doesn't wrap transactions.
 */
public class DeskmanagerExamplePostConstructService {

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void postConstruct() throws WktException, GeomajasException {
		DeskmanagerExampleDatabaseProvisioningService provisioningService = 
			(DeskmanagerExampleDatabaseProvisioningService) applicationContext
			.getBean("ExampleDatabaseProvisioningService");
		provisioningService.createData();
	}
}
