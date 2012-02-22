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

import org.geomajas.sld.editor.client.presenter.SmartGwtRootPresenter;
import org.geomajas.sld.editor.client.view.CreateSldDialogView;
import org.geomajas.sld.editor.client.view.FilterView;
import org.geomajas.sld.editor.client.view.LineSymbolizerView;
import org.geomajas.sld.editor.client.view.MainLayoutView;
import org.geomajas.sld.editor.client.view.PointSymbolizerView;
import org.geomajas.sld.editor.client.view.PolygonSymbolizerView;
import org.geomajas.sld.editor.client.view.RuleSelectorView;
import org.geomajas.sld.editor.client.view.RuleView;
import org.geomajas.sld.editor.client.view.SldActionView;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorLayoutView;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorListView;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorView;
import org.geomajas.sld.editor.client.view.ViewUtilImpl;
import org.geomajas.sld.editor.common.client.presenter.CreateSldDialogPresenterWidget;
import org.geomajas.sld.editor.common.client.presenter.FilterPresenter;
import org.geomajas.sld.editor.common.client.presenter.LineSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.MainLayoutPresenter;
import org.geomajas.sld.editor.common.client.presenter.PointSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.PolygonSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.RulePresenter;
import org.geomajas.sld.editor.common.client.presenter.RuleSelectorPresenter;
import org.geomajas.sld.editor.common.client.presenter.SldActionPresenter;
import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorLayoutPresenter;
import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorListPresenter;
import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorPresenter;
import org.geomajas.sld.editor.common.client.view.ViewUtil;

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

		bindPresenter(RuleSelectorPresenter.class, RuleSelectorPresenter.MyView.class, RuleSelectorView.class,
				RuleSelectorPresenter.MyProxy.class);

		bindSingletonPresenterWidget(CreateSldDialogPresenterWidget.class, CreateSldDialogPresenterWidget.MyView.class,
				CreateSldDialogView.class);

		bindPresenter(StyledLayerDescriptorLayoutPresenter.class, StyledLayerDescriptorLayoutPresenter.MyView.class,
				StyledLayerDescriptorLayoutView.class, StyledLayerDescriptorLayoutPresenter.MyProxy.class);

		bindPresenter(RulePresenter.class, RulePresenter.MyView.class, RuleView.class, RulePresenter.MyProxy.class);

		bindPresenter(FilterPresenter.class, FilterPresenter.MyView.class, FilterView.class,
				FilterPresenter.MyProxy.class);

		bindPresenter(PointSymbolizerPresenter.class, PointSymbolizerPresenter.MyView.class, PointSymbolizerView.class,
				PointSymbolizerPresenter.MyProxy.class);

		bindPresenter(PolygonSymbolizerPresenter.class, PolygonSymbolizerPresenter.MyView.class,
				PolygonSymbolizerView.class, PolygonSymbolizerPresenter.MyProxy.class);

		bindPresenter(LineSymbolizerPresenter.class, LineSymbolizerPresenter.MyView.class, LineSymbolizerView.class,
				LineSymbolizerPresenter.MyProxy.class);

		bindPresenter(SldActionPresenter.class, SldActionPresenter.MyView.class, SldActionView.class,
				SldActionPresenter.MyProxy.class);
	}
}