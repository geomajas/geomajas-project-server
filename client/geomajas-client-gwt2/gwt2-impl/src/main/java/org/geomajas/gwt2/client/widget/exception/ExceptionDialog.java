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
package org.geomajas.gwt2.client.widget.exception;

import org.geomajas.gwt2.client.i18n.GlobalMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * Dialog for showing Exceptions. It is not recommended to use this in your applications, it is meant only for internal
 * Geomajas affairs.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class ExceptionDialog {
	
	static {
		ExceptionDialogResource resource = GWT.create(ExceptionDialogResource.class);
		resource.css().ensureInjected();
	};

	/**
	 * {@link UiBinder} for this class.
	 * 
	 * @author Jan De Moerloose / Jan Venstermans
	 */
	interface ExceptionUiBinder extends UiBinder<ExceptionDialogBox, ExceptionDialog> {
	}

	private static final ExceptionUiBinder UIBINDER = GWT.create(ExceptionUiBinder.class);

	private String message;

	private String stack = "This is a stack trace\nWith a lot of lines\nand so on\nand so on\nand so on\nand so on";

	private ExceptionDialogBox exceptionDialog;

	private static GlobalMessages messages = GWT.create(GlobalMessages.class);

	@UiField
	protected Label messageLabel;

	@UiField
	protected Button detailsButton;

	@UiField
	protected HTML stackPanel;

	public ExceptionDialog(String message, String stack) {
		exceptionDialog = UIBINDER.createAndBindUi(this);
		exceptionDialog.setText(messages.exceptionDialogCaptionText());
		stackPanel.setVisible(false);
		this.message = message;
		this.stack = stack;
	}

	public void clear() {
		messageLabel.setText("");
		stackPanel.setHTML("");
	}

	public void show() {
		clear();
		stackPanel.setHTML(new SafeHtmlBuilder().appendEscapedLines(stack).toSafeHtml());
		messageLabel.setText(message);
		messageLabel.setWordWrap(true);
		exceptionDialog.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				exceptionDialog.center();
			}
		});
		exceptionDialog.center();
	}

	@UiHandler("detailsButton")
	void handleDetail(ClickEvent e) {
		toggleStackPanel();
	}

	private void toggleStackPanel() {
		stackPanel.setVisible(!stackPanel.isVisible());
		detailsButton.setText(stackPanel.isVisible() ? messages.exceptionDialogCloseDetails() : messages
				.exceptionDialogShowDetails());
	}
}