/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client.gin;

//import org.geomajas.sld.editor.client.presenter.SmartGwtRootPresenter;
import org.geomajas.sld.editor.client.view.MainLayoutNoLeftSidePanelView;
import org.geomajas.sld.editor.common.client.presenter.MainLayoutPresenter;

/**
 * @author Jan De Moerloose
 * @author An Buyle
 */
public class ClientNoSldListModule extends ClientModuleBase {

	@Override
	protected void configure() {
		super.configure();
		bindPresenter(MainLayoutPresenter.class, MainLayoutPresenter.MyView.class, MainLayoutNoLeftSidePanelView.class,
				MainLayoutPresenter.MyProxy.class);

	}
}