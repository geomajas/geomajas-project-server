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
package org.geomajas.puregwt.widget.example.client.sample.messagebox;

import org.geomajas.puregwt.example.base.client.sample.SamplePanel;
import org.geomajas.puregwt.widget.client.dialog.MessageBox;
import org.geomajas.puregwt.widget.client.dialog.MessageBox.MessageStyleType;
import org.geomajas.puregwt.widget.example.client.i18n.SampleMessages;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Sample showing off MessageBox capabilities.
 *
 * @author Kristof Heirwegh
 */
public class MessageBoxExample implements SamplePanel, Callback<Boolean, Void> {

	private DockLayoutPanel rootElement;

	private static final CloseableDialogUiBinder UIBINDER = GWT.create(CloseableDialogUiBinder.class);

	private static final SampleMessages MSG = GWT.create(SampleMessages.class);
	
	/**
	 * UI binder interface.
	 */
	interface CloseableDialogUiBinder extends UiBinder<DockLayoutPanel, MessageBoxExample> {
	}

	public MessageBoxExample() {
		rootElement = UIBINDER.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return rootElement;
	}
	
	// --------------------------------------------------------
	
	@UiHandler("infoMessageBtn")
	protected void onInfoMessageBtnClicked(ClickEvent event) {
		MessageBox.showMessageBox(MSG.messageBoxInfoMessageBtn(), MSG.messageBoxMessage());
	}

	@UiHandler("warnMessageBtn")
	protected void onWarnMessageBtnClicked(ClickEvent event) {
		MessageBox.showMessageBox(MSG.messageBoxWarnMessageBtn(), MSG.messageBoxMessage(), MessageStyleType.WARN);
	}

	@UiHandler("errorMessageBtn")
	protected void onErrorMessageBtnClicked(ClickEvent event) {
		MessageBox.showMessageBox(MSG.messageBoxErrorMessageBtn(), MSG.messageBoxMessage(), MessageStyleType.ERROR);
	}

	@UiHandler("helpMessageBtn")
	protected void onHelpMessageBtnClicked(ClickEvent event) {
		MessageBox.showMessageBox(MSG.messageBoxHelpMessageBtn(), MSG.messageBoxMessageLong(), MessageStyleType.HELP);
	}

	@UiHandler("yesNoBtn")
	protected void onYesNoBtnClicked(ClickEvent event) {
		MessageBox.showYesNoMessageBox(MSG.messageBoxYesNoBtn(), MSG.messageBoxMessage(), this);
	}

	@UiHandler("yesNoCancelBtn")
	protected void onYesNoCancelBtnClicked(ClickEvent event) {
		MessageBox.showYesNoCancelMessageBox(MSG.messageBoxYesNoCancelBtn(), MSG.messageBoxMessage(), this);
	}

	// --------------------------------------------------------
	// -- Callback 											 --
	// --------------------------------------------------------

	@Override
	public void onFailure(Void reason) {
		MessageBox.showMessageBox("", MSG.messageBoxResponseCancel());
	}

	@Override
	public void onSuccess(Boolean result) {
		MessageBox.showMessageBox("", result ? MSG.messageBoxResponseYes() : MSG.messageBoxResponseNo());
	}

}