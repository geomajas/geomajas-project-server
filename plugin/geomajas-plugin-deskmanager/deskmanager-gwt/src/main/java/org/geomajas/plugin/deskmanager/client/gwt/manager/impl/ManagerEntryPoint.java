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
package org.geomajas.plugin.deskmanager.client.gwt.manager.impl;

import org.geomajas.plugin.deskmanager.client.gwt.manager.gin.DeskmanagerClientGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

/**
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class ManagerEntryPoint implements EntryPoint {

	private final DeskmanagerClientGinjector ginjector = GWT.create(DeskmanagerClientGinjector.class);

	public DeskmanagerClientGinjector getGinjector() {
		return ginjector;
	}

	private static ManagerEntryPoint instance;
	
	public ManagerEntryPoint() {
		instance = this;
	}
	
	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);
	}
	
	public static ManagerEntryPoint getInstance() {
		return instance;
	}
}
