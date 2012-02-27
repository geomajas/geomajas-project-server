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
package org.geomajas.sld.editor.client;

import java.util.List;

import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.editor.common.client.model.SldGwtService;
import org.geomajas.sld.editor.common.client.model.SldGwtServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point of SLD editor.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldEditorEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		AbsolutePanel panel = new AbsolutePanel();
		panel.setSize("100%", "100%");
		// list of sld's
		final ListBox sldList = new ListBox();
		sldList.setVisibleItemCount(5);

		// label
		final Label nameLabel = new Label("<name of layer>");

		panel.add(sldList, 100, 100);
		panel.add(nameLabel, 100, 300);

		// Add it to the root panel.
		RootPanel.get().add(panel);

		final SldGwtServiceAsync service = GWT.create(SldGwtService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");
		service.findAll(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {
				GWT.log("got " + result.size() + " SLDs");
				for (String name : result) {
					sldList.addItem(name);
				}
			}

			public void onFailure(Throwable caught) {
				GWT.log("could not access SLDs", caught);
			}
		});
		sldList.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				int index = sldList.getSelectedIndex();
				if (index >= 0) {
					String name = sldList.getItemText(index);
					service.findByName(name, new AsyncCallback<StyledLayerDescriptorInfo>() {

						public void onSuccess(StyledLayerDescriptorInfo sld) {
							if (!sld.getChoiceList().isEmpty()) {
								StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().iterator().next();
								if (info.ifNamedLayer()) {
									NamedLayerInfo namedLayer = info.getNamedLayer();
									nameLabel.setText(namedLayer.getName());
								}
							}
						}

						public void onFailure(Throwable caught) {
							GWT.log("could not access SLDs", caught);
						}
					});
				}
			}
		});
	}

}
