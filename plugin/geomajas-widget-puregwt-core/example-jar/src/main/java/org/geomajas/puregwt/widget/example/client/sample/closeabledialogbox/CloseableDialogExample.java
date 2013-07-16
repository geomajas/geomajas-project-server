/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.widget.example.client.sample.closeabledialogbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.widget.client.dialog.CloseableDialogBox;

/**
 * Closeable dialog widget showcase panel.
 *
 * @author Dosi Bingov
 */
public class CloseableDialogExample implements SamplePanel {

	private DockLayoutPanel rootElement;

	private CloseableDialogBox closeableDialogBox;

	@UiField
	protected Button button;

	private static CloseableDialogUiBinder UIBINDER = GWT.create(CloseableDialogUiBinder.class);

	/**
	 * UI binder interface
	 */
	interface CloseableDialogUiBinder extends
			UiBinder<DockLayoutPanel, CloseableDialogExample> {

	}

	public CloseableDialogExample() {
		rootElement = UIBINDER.createAndBindUi(this);
		closeableDialogBox = new CloseableDialogBox();
		button.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent mouseDownEvent) {
				closeableDialogBox.setText("My closeable dialog");
				closeableDialogBox.show();
			}
		});

	}

	@Override
	public Widget asWidget() {
		return rootElement;
	}
}