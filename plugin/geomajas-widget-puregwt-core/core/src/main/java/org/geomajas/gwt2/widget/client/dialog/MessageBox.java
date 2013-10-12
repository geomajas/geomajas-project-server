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

package org.geomajas.gwt2.widget.client.dialog;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.widget.client.i18n.WidgetCoreInternationalization;

/**
 * General MessageBox for showing a message.
 * <p>
 * Includes extra buttons so it can also be used to ask a question.
 * 
 * <p>
 * Use the different static methods to conveniently create a MessageBox.
 * 
 * @author Kristof Heirwegh
 */
public class MessageBox implements IsWidget {

	// can't directly inherit from CloseableDialogbox because it has ui:fields in its binder (which we don't need/want).

	private static final WidgetCoreInternationalization MSG = GWT.create(WidgetCoreInternationalization.class);

	/**
	 * Style of the MessageBox.
	 * @author Kristof Heirwegh
	 */
	public enum MessageStyleType {
		INFO, HELP, WARN, ERROR
	}

	/**
	 * Type of the MessageBox.
	 * @author Kristof Heirwegh
	 */
	public enum MessageBoxType {
		MESSAGE, YESNO, YESNOCANCEL
	}

	/**
	 * UI binder interface.
	 */
	interface MyUiBinder extends UiBinder<Widget, MessageBox> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected SimplePanel icon;

	@UiField
	protected HTML message;

	@UiField
	protected Button btnYes;

	@UiField
	protected Button btnNo;

	@UiField
	protected Button btnCancel;

	private CloseableDialogBox dialog;

	/**
	 * Create a MessageBox.
	 * <p>You might want to use one of the static convenience methods instead of creating your own though.
	 * @param title
	 * @param messageText
	 */
	public MessageBox(String title, String messageText) {
		this(title, SafeHtmlUtils.fromString(messageText), null);
	}

	/**
	 * Create a MessageBox.
	 * <p>You might want to use one of the static convenience methods instead of creating your own though.
	 * @param title
	 * @param messageText
	 */
	public MessageBox(String title, SafeHtml messageText) {
		this(title, messageText, null);
	}

	/**
	 * Create a MessageBox.
	 * <p>You might want to use one of the static convenience methods instead of creating your own though.
	 * @param title
	 * @param messageText
	 * @param onFinished
	 */
	public MessageBox(String title, String messageText, Callback<Boolean, Void> onFinished) {
		this(title, SafeHtmlUtils.fromString(messageText), onFinished);
	}

	/**
	 * Create a MessageBox with a callback.
	 * <p>You might want to use one of the static convenience methods instead of creating your own though.
	 * @param title
	 * @param messageText
	 * @param onFinished
	 */
	public MessageBox(String title, SafeHtml messageText, final Callback<Boolean, Void> onFinished) {
		MessageBoxResource.INSTANCE.css().ensureInjected();
		Widget inner = UIBINDER.createAndBindUi(this);
		dialog = new CloseableDialogBox();
		dialog.setWidget(inner);
		dialog.setAnimationEnabled(true);
		dialog.setGlassEnabled(true);
		dialog.setModal(true);
		dialog.setHTML(title);
		message.setHTML(messageText);

		btnYes.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (onFinished != null) {
					onFinished.onSuccess(true);
				}
				dialog.hide();
			}
		});

		btnNo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (onFinished != null) {
					onFinished.onSuccess(false);
				}
				dialog.hide();
			}
		});

		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (onFinished != null) {
					onFinished.onFailure(null);
				}
				dialog.hide();
			}
		});

		if (onFinished != null) {
			dialog.setOnCloseHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onFinished.onFailure(null);
				}
			});
		}
	}

	/***
	 * Set width of all buttons in pixels.
	 * 
	 * @param width
	 */
	public void setButtonWidth(int width) {
		btnNo.setWidth(width + "px");
		btnYes.setWidth(width + "px");
		btnCancel.setWidth(width + "px");
	}

	public void center() {
		dialog.center();
	}

	public void hide() {
		dialog.hide();
	}

	public void show() {
		dialog.show();
	}

	@Override
	public Widget asWidget() {
		return dialog;
	}

	// ---------------------------------------------------

	public void setMessageStyleType(MessageStyleType messageStyleType) {
		if (messageStyleType == null) {
			icon.getElement().getStyle().setDisplay(Display.NONE);
		} else {
			icon.getElement().getStyle().setDisplay(Display.BLOCK);
			icon.setStyleName(MessageBoxResource.INSTANCE.css().messageBoxIcon());	
			switch (messageStyleType) {
				case HELP:
					icon.addStyleName(MessageBoxResource.INSTANCE.css().messageBoxIconHelp());
					break;
				case WARN:
					icon.addStyleName(MessageBoxResource.INSTANCE.css().messageBoxIconWarn());
					break;
				case ERROR:
					icon.addStyleName(MessageBoxResource.INSTANCE.css().messageBoxIconError());
					break;
				default:
					icon.addStyleName(MessageBoxResource.INSTANCE.css().messageBoxIconInfo());
			}
		}
	}

	public void setMessageBoxType(MessageBoxType messageBoxType) {
		if (messageBoxType == null) {
			return;
		}
		switch (messageBoxType) {
			case YESNO:
				btnYes.setVisible(true);
				btnNo.setVisible(true);
				btnCancel.setVisible(false);
				break;

			case YESNOCANCEL:
				btnYes.setVisible(true);
				btnNo.setVisible(true);
				btnCancel.setVisible(true);
				btnCancel.setText(MSG.messageBoxBtnCancel());
				break;

			default: // MESSAGE
				btnYes.setVisible(false);
				btnNo.setVisible(false);
				btnCancel.setVisible(true);
				btnCancel.setText(MSG.messageBoxBtnClose());
				break;
		}
	}

	public Button getBtnYes() {
		return btnYes;
	}

	public Button getBtnNo() {
		return btnNo;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	// --------------------------------------------------------

	/**
	 * This will show a MessageBox type Info.
	 */
	public static MessageBox showMessageBox(String title, String message) {
		return showMessageBox(title, SafeHtmlUtils.fromString(message), MessageStyleType.INFO);
	}

	/**
	 * This will show a MessageBox.
	 */
	public static MessageBox showMessageBox(String title, String message, MessageStyleType styleType) {
		return showMessageBox(title, SafeHtmlUtils.fromString(message), styleType);
	}

	/**
	 * This will show a MessageBox.
	 */
	public static MessageBox showMessageBox(String title, SafeHtml message, MessageStyleType styleType) {
		MessageBox box = new MessageBox(title, message);
		box.setMessageStyleType(styleType);
		box.setMessageBoxType(MessageBoxType.MESSAGE);
		box.center();
		return box;
	}

	/**
	 * This will show a MessageBox expecting an answer.
	 * <p>
	 * onFinished.onSuccess will be called with true for yes, and false for no.
	 * <p>
	 * if the dialogbox is closed with the closebutton instead of a button, onFinished.onFailure will be called.
	 * 
	 * @param title
	 * @param question
	 * @param onFinished
	 * @return
	 */
	public static MessageBox showYesNoMessageBox(String title, String question, Callback<Boolean, Void> onFinished) {
		MessageBox box = new MessageBox(title, question, onFinished);
		box.setMessageStyleType(MessageStyleType.HELP);
		box.setMessageBoxType(MessageBoxType.YESNO);
		box.center();
		return box;
	}

	/**
	 * This will show a MessageBox expecting an answer.
	 * <p>
	 * onFinished.onSuccess will be called with true for yes, and false for no. onFinished.onFailure will be called for
	 * cancel and when the dialogbox is closed with the closebutton.
	 * 
	 * @param title
	 * @param question
	 * @param onFinished
	 * @return
	 */
	public static MessageBox showYesNoCancelMessageBox(String title, String question, Callback<Boolean,
			Void> onFinished) {
		MessageBox box = new MessageBox(title, question, onFinished);
		box.setMessageStyleType(MessageStyleType.HELP);
		box.setMessageBoxType(MessageBoxType.YESNOCANCEL);
		box.center();
		return box;
	}

}
