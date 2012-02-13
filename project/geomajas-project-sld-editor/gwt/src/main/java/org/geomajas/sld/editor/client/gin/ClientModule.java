/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client.gin;

import org.geomajas.sld.client.presenter.CreateSldDialogPresenterWidget;
import org.geomajas.sld.client.presenter.MainLayoutPresenter;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorLayoutPresenter;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorListPresenter;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorPresenter;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.presenter.SmartGwtRootPresenter;
import org.geomajas.sld.editor.client.view.CreateSldDialogView;
import org.geomajas.sld.editor.client.view.MainLayoutView;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorLayoutView;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorListView;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorView;
import org.geomajas.sld.editor.client.view.ViewUtilImpl;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

/**
 * @author Jan De Moerloose
 */
public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(SmartGwtRootPresenter.class).asEagerSingleton();
		// utils
		bind(ViewUtil.class).to(ViewUtilImpl.class).in(Singleton.class);
		
		// Presenters
		bindPresenter(MainLayoutPresenter.class, MainLayoutPresenter.MyView.class, MainLayoutView.class,
				MainLayoutPresenter.MyProxy.class);
		bindPresenter(StyledLayerDescriptorListPresenter.class, StyledLayerDescriptorListPresenter.MyView.class,
				StyledLayerDescriptorListView.class, StyledLayerDescriptorListPresenter.MyProxy.class);
		bindPresenter(StyledLayerDescriptorPresenter.class, StyledLayerDescriptorPresenter.MyView.class,
				StyledLayerDescriptorView.class, StyledLayerDescriptorPresenter.MyProxy.class);
	    bindSingletonPresenterWidget(CreateSldDialogPresenterWidget.class,
	            CreateSldDialogPresenterWidget.MyView.class, CreateSldDialogView.class);
		bindPresenter(StyledLayerDescriptorLayoutPresenter.class, StyledLayerDescriptorLayoutPresenter.MyView.class, StyledLayerDescriptorLayoutView.class,
				StyledLayerDescriptorLayoutPresenter.MyProxy.class);
//		bindSingletonPresenterWidget(CreateStyledLayerDescriptorPopupPresenterWidget.class, CreateStyledLayerDescriptorPopupPresenterWidget.MyView.class,
//				CreateStyledLayerDescriptorPopupView.class);

	}
}