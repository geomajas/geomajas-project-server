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

package org.geomajas.sld.editor.expert.example.client.sample.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.puregwt.widget.client.dialog.MessageBox;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.SldCancelledHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.expert.example.client.ExampleJar;
import org.geomajas.sld.editor.expert.example.client.i18n.SampleMessages;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * This class helps you to use the expert SLD editor by (un)wrapping a Geomajas layer to raw sld.
 * <p>
 * This class has state, so do not reuse.
 * 
 * @author Kristof Heirwegh
 */
public class ExpertSldEditorHelper {

	private static final SampleMessages MSG = GWT.create(SampleMessages.class);

	private ClientVectorLayerInfo layer;

	private boolean initialized;
	private boolean styleDataChanged;
	private UserStyleInfo styleData;
	private String styleName;
	private Callback<ExpertSldEditorHelper, Void> callback;

	private final List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	public ExpertSldEditorHelper(Callback<ExpertSldEditorHelper, Void> onFinished) {
		this.callback = onFinished;
	}

	// ---------------------------------------------------------------

	/**
	 * This will (re)initialize the editor with the style from the given layer.
	 */
	public void setLayer(ClientVectorLayerInfo layer) {
		this.layer = layer;
		extractUserStyleInfo();
	}
	
	/**
	 * Applies the changes to the given layerinfo (if dirty).
	 */
	public void apply(final ClientVectorLayerInfo cli) {
		if (styleDataChanged) {
			NamedStyleInfo nsi = cli.getNamedStyleInfo();
			if (nsi == null) {
				nsi = new NamedStyleInfo();
				cli.setNamedStyleInfo(nsi);
			}
			nsi.setUserStyle(styleData);
			nsi.setName(styleName);
			nsi.setSldStyleName(styleName);
		}
	}

	public void showExpertStyleEditor() {
		initializeExpertEditor();
		ExampleJar.getInjector().getPlaceManager().revealDefaultPlace();
	}

	/**
	 * This will remove/cancel any eventlisteners and other resources.
	 * <p>
	 * You should not (re)use this object after calling destroy();
	 */
	public void destroy() {
		for (HandlerRegistration hr : handlers) {
			hr.removeHandler();
		}
		handlers.clear();
		styleData = null;
		initialized = false;
	}

	// ---------------------------------------------------------------

	private void initializeExpertEditor() {
		if (!initialized) {
			handlers.add(ExampleJar.getInjector().getSldEditorExpertPresenter().get()
					.addSldSaveHandler(new SldSaveHandler() {
						public void onSldSave(SldSaveEvent event) {
							if (extractData()) {
								ExampleJar.getInjector().getSldEditorExpertPresenter().get().closeEditor();
								apply(layer);
								callback.onSuccess(ExpertSldEditorHelper.this);
							}
						}
					}));

			handlers.add(ExampleJar.getInjector().getSldEditorExpertPresenter().get()
					.addSldCancelledHandler(new SldCancelledHandler() {
						public void onSldCancelled(SldCancelledEvent event) {
							ExampleJar.getInjector().getSldEditorExpertPresenter().get().closeEditor();
							initializeExpertEditor(); // reset;
							callback.onFailure(null);
						}
					}));
			initialized = true;
		}

		// -- load the sld data, if any
		if (styleData != null) {
			ExampleJar.getInjector().getSldEditorExpertPresenter().get().loadSld(getStyleDataAsSldI());
		}
	}

	private boolean extractData() {
		styleDataChanged = true;
		styleData = null;
		styleName = "";

		// Find the userstyle.
		StyledLayerDescriptorInfo sldi = ExampleJar.getInjector().getSldEditorExpertPresenter()
				.get().getModel().getSldDescriptor();
		if (sldi != null) {
			if (sldi.getChoiceList() != null && sldi.getChoiceList().size() > 0) {
				NamedLayerInfo nli = sldi.getChoiceList().get(0).getNamedLayer();
				if (nli != null && nli.getChoiceList() != null && nli.getChoiceList().size() > 0) {
					styleData = nli.getChoiceList().get(0).getUserStyle();
					styleName = nli.getName();
				}
			}
		}
		if (styleData == null) {
			MessageBox.showMessageBox(MSG.layerChangeStyleExpertTitle(), MSG.layerChangeStyleExpertMalformedSldExc());
			styleDataChanged = false;
			return false;
		}
		return true;
	}

	private void extractUserStyleInfo() {
		NamedStyleInfo nsi = layer.getNamedStyleInfo();
		if (nsi.getUserStyle() != null) {
			styleData = nsi.getUserStyle();
			styleName = nsi.getName();
		}
	}

	private StyledLayerDescriptorInfo getStyleDataAsSldI() {
		if (styleData == null) {
			return null;
		}
		StyledLayerDescriptorInfo sldi = new StyledLayerDescriptorInfo();
		sldi.setVersion("1.0.0");
		ChoiceInfo ci = new ChoiceInfo();
		org.geomajas.sld.NamedLayerInfo.ChoiceInfo ci2 = new org.geomajas.sld.NamedLayerInfo.ChoiceInfo();
		sldi.getChoiceList().add(ci);
		ci.setNamedLayer(new NamedLayerInfo());
		ci.getNamedLayer().setName(styleName);
		ci.getNamedLayer().getChoiceList().add(ci2);
		ci2.setUserStyle(styleData);
		return sldi;
	}

}
