package org.geomajas.sld.editor.client;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

import org.geomajas.sld.client.presenter.MainPagePresenter;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorPresenter;
import org.geomajas.sld.editor.client.view.StyledLayerDescriptorView;
import com.google.gwt.inject.client.AbstractGinModule;
/**
 * @author An Buyle
 */
public class MyModule extends AbstractPresenterModule {

  @Override
  protected void configure() {
    // Default implementation of standard resources
    install(new DefaultModule(MyPlaceManager.class));

    // Presenters
    bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
        MainPageView.class, MainPagePresenter.MyProxy.class);

    bindPresenter(StyledLayerDescriptorPresenter.class, StyledLayerDescriptorPresenter.MyView.class,
        ResponseView.class, StyledLayerDescriptorPresenter.MyProxy.class);
  }
}