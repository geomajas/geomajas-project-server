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
package org.geomajas.sld.editor.expert.client;

import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.expert.client.gin.SldEditorClientGinjector;
import org.geomajas.sld.editor.expert.client.i18n.Messages;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.SldCancelledHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;

/**
 * Entry point of SmartGWT SLD editor.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorEntryPoint implements EntryPoint {

	private static final Messages MSG = GWT.create(Messages.class);
	
	private final SldEditorClientGinjector ginjector = GWT.create(SldEditorClientGinjector.class);

	public void onModuleLoad() {
		DelayedBindRegistry.bind(ginjector);

		// -- show some background
		HTMLFlow htmlFlow = new HTMLFlow();
		htmlFlow.setWidth100();
		htmlFlow.setHeight100();
		String contents = 
				"<div style='margin-left: 5px; font-size: 100pt; font-weight: bold; color:#DDFFDD'>GEOMAJAS</div>"
				+ "<div style='margin-left: 10px; margin-top:-70px; font-size: 50pt; color:#CCCCCC'>SLD-Editor</div>"
				+ "<div style='margin-left: 10px; margin-top:-15px; font-size: 28pt; color:#DDDDDD'>EXPERT-mode</div>";
		htmlFlow.setContents(contents);
		htmlFlow.draw();

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
				// ginjector.getSldEditorExpertPresenter().get().closeEditor();
			}
		});

		// -- load an sld
//		String yourXmlDataHere = "<test></test>";
//		ginjector.getSldEditorExpertPresenter().get().loadSld(yourXmlDataHere, "name", "title");
		// -- or use StyledSldLayerDescriptorInfo;
//		ginjector.getSldEditorExpertPresenter().get().loadSld(sldObject);
		
		// -- now show the SLD editor in a window
		ginjector.getPlaceManager().revealCurrentPlace();
		
	}
}
