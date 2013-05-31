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

package org.geomajas.sld.editor.expert.example.client;

import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.expert.client.SldEditorWindow;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.SldCancelledHandler;
import org.geomajas.sld.editor.expert.example.client.gin.SldEditorClientGinjector;
import org.geomajas.sld.editor.expert.example.client.i18n.Messages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.smartgwt.client.util.SC;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Kristof Heirwegh
 */
public class SldEditorExample implements EntryPoint {

	public static final Messages MSG = GWT.create(Messages.class);

	private final SldEditorClientGinjector ginjector = GWT.create(SldEditorClientGinjector.class);

	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);

		// -- Add a SaveEventListener, you can also add cancel / close listeners
		ginjector.getSldEditorExpertPresenter().get().addSldSaveHandler(new SldSaveHandler() {
			public void onSldSave(SldSaveEvent event) {
				SC.say(MSG.onSave());
				
				// -- get the data:
				// as raw xml string: ginjector.getSldEditorExpertPresenter().get().getModel().getRawSld().getXml();
				// or as object: ginjector.getSldEditorExpertPresenter().get().getModel().getSldDescriptor();
				// you can also check if data changed in the model
				
				// -- if you wish the editor to close at this point, call:
				// ginjector.getSldEditorExpertPresenter().get().closeEditor();
			}
		});

		ginjector.getSldEditorExpertPresenter().get().addSldCancelledHandler(new SldCancelledHandler() {
			public void onSldCancelled(SldCancelledEvent event) {
				SC.say(MSG.onCancel());
				
				// -- if you wish the editor to close at this point, call:
				ginjector.getSldEditorExpertPresenter().get().closeEditor();
			}
		});

		// -- defaults to modal, which would block te rest of the sample case.
		SldEditorWindow window = (SldEditorWindow) ginjector.getSldEditorExpertPresenter().get().getView().asWidget();
		window.setIsModal(false);
		
		// ---------------------------------------------------------------

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MSG.sampleTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", SldEditorPanel.TITLE, "SldEditor",
				SldEditorPanel.FACTORY));
	}
}
