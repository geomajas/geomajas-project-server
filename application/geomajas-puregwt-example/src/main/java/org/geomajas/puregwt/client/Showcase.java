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

package org.geomajas.puregwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point and main class for pure GWT example application.
 * 
 * @author Pieter De Graef
 */
public class Showcase implements EntryPoint {

	private final GeomajasGinjector geomajasInjector = GWT.create(GeomajasGinjector.class);

	public void onModuleLoad() {
		final SingleSelectionModel<ContentPanel> selectionModel = new SingleSelectionModel<ContentPanel>();
		final ShowcaseLayout layout = new ShowcaseLayout(new ShowcaseTreeViewModel(selectionModel, geomajasInjector));

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			public void onSelectionChange(SelectionChangeEvent event) {
				ContentPanel selected = selectionModel.getSelectedObject();
				if (selected != null) {
					// History.newItem(getContentWidgetToken(selected), true);
					layout.setContent(selected);
				}
			}
		});

		RootPanel.get().add(layout);
	}
}