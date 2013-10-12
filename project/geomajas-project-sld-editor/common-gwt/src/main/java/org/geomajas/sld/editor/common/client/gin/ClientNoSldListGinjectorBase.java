/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.common.client.gin;

import org.geomajas.sld.editor.common.client.model.SldManager;
import org.geomajas.sld.editor.common.client.presenter.FilterPresenter;
import org.geomajas.sld.editor.common.client.presenter.LineSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.MainLayoutPresenter;
import org.geomajas.sld.editor.common.client.presenter.PointSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.PolygonSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.RulePresenter;
import org.geomajas.sld.editor.common.client.presenter.RuleSelectorPresenter;
import org.geomajas.sld.editor.common.client.presenter.SldActionPresenter;
import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorLayoutPresenter;
import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 * Base {@link Ginjector} for the SLD editor modules. Gives access to the common part and should be extended in the
 * specific modules for GWT and GWT2.
 * 
 * @author Jan De Moerloose
 * 
 */
@GinModules({ ClientModule.class })
public interface ClientNoSldListGinjectorBase extends Ginjector {

	Provider<MainLayoutPresenter> getMainLayoutPresenter();

	Provider<StyledLayerDescriptorLayoutPresenter> getStyledLayerDescriptorLayoutPresenter();

	Provider<StyledLayerDescriptorPresenter> getStyledLayerDescriptorPresenter();

	Provider<RuleSelectorPresenter> getRuleSelectorPresenter();

	Provider<RulePresenter> getRulePresenter();

	Provider<FilterPresenter> getFilterPresenter();

	Provider<PointSymbolizerPresenter> getPointSymbolizerPresenter();

	Provider<LineSymbolizerPresenter> getLineSymbolizerPresenter();

	Provider<PolygonSymbolizerPresenter> getPolygonSymbolizerPresenter();

	Provider<SldActionPresenter> getSldActionPresenter();

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	SldManager getSldManager();
}