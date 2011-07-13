/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client;

import java.util.List;

import org.geomajas.sld.client.SldGwtService;
import org.geomajas.sld.client.SldGwtServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
		// Make a new list box, adding a few items to it.
		final ListBox lb = new ListBox();

		// Make enough room for all five items (setting this value to 1 turns it
		// into a drop-down list).
		lb.setVisibleItemCount(5);

		// Add it to the root panel.
		RootPanel.get().add(lb);

		SldGwtServiceAsync service = GWT.create(SldGwtService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");
		service.findAll(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {
				GWT.log("got " + result.size() + " SLDs");
				for (String name : result) {
					lb.addItem(name);
				}
			}

			public void onFailure(Throwable caught) {
				GWT.log("could not access SLDs", caught);
			}
		});
	}

}
