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
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.widget.client.dialog.CloseableDialogBox;
import org.geomajas.puregwt.widget.example.client.ExampleJar;
import org.geomajas.puregwt.widget.example.client.resource.ExampleWidgetResource;

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

	private static final CloseableDialogUiBinder UIBINDER = GWT.create(CloseableDialogUiBinder.class);

	/**
	 * UI binder interface.
	 */
	interface CloseableDialogUiBinder extends
			UiBinder<DockLayoutPanel, CloseableDialogExample> {

	}

	public CloseableDialogExample() {
		rootElement = UIBINDER.createAndBindUi(this);
		closeableDialogBox = new CloseableDialogBox();
		Image dialogContent = new Image(ExampleWidgetResource.INSTANCE.geomajasLogo().getSafeUri());
		closeableDialogBox.add(dialogContent);
		closeableDialogBox.setModal(true);
		closeableDialogBox.setAutoHideEnabled(true);
		closeableDialogBox.setAnimationEnabled(true);

		button.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent mouseDownEvent) {
				closeableDialogBox.setText(ExampleJar.getMessages().closeableDialogTitle());
				closeableDialogBox.show();
			}
		});

	}

	@Override
	public Widget asWidget() {
		return rootElement;
	}
}