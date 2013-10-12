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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt.widget.client.dialog.CloseableDialogBoxResource;

/**
 * Dialogbox with a close-button in the upper-right corner.
 * 
 * @author Pieter De Graef
 */
public class CloseableDialogBox extends DialogBox {

	@UiField
	protected Button closeIcon;

	public CloseableDialogBox() {
		super(new CaptionImpl());
		CloseableDialogBoxResource.INSTANCE.css().ensureInjected();
		setStyleName(CloseableDialogBoxResource.INSTANCE.css().closeableDialogBox());
		((CaptionImpl) getCaption()).setParent(this);
		setAutoHideOnHistoryEventsEnabled(true);
		this.setWidth("100%");
	}

	@Override
	public String getText() {
		return getCaption().getText();
	}

	@Override
	public void setText(String text) {
		getCaption().setText(text);
	}

	public void setCloseButtonTitle(String title) {
		closeIcon.setTitle(title);
	}

	public ClickHandler getOnCloseHandler() {
		return ((CaptionImpl) getCaption()).getOnCloseHandler();
	}

	public void setOnCloseHandler(ClickHandler onCloseHandler) {
		((CaptionImpl) getCaption()).setOnCloseHandler(onCloseHandler);
	}

	// ---------------------------------------------------

	/**
	 * Overriding the default caption to include a close button.
	 * 
	 * @author Pieter De Graef
	 */
	protected static class CaptionImpl extends FocusPanel implements Caption {

		/**
		 * UI binder interface for the ShowcaseDialogBox caption.
		 * 
		 * @author Pieter De Graef
		 */
		interface MyUiBinder extends UiBinder<Widget, CaptionImpl> {
		}

		private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

		@UiField
		protected SimplePanel titleElement;

		private ClickHandler onCloseHandler;

		private CloseableDialogBox parent;

		public CaptionImpl() {
			super();
			setStyleName(CloseableDialogBoxResource.INSTANCE.css().captionBar());
			add(UIBINDER.createAndBindUi(this));
		}

		public void setParent(CloseableDialogBox parent) {
			this.parent = parent;
		}

		public ClickHandler getOnCloseHandler() {
			return onCloseHandler;
		}

		public void setOnCloseHandler(ClickHandler onCloseHandler) {
			this.onCloseHandler = onCloseHandler;
		}

		@Override
		public String getHTML() {
			return titleElement.getElement().getInnerHTML();
		}

		@Override
		public void setHTML(String html) {
			titleElement.getElement().setInnerHTML(html);
		}

		@Override
		public String getText() {
			return titleElement.getElement().getInnerText();
		}

		@Override
		public void setText(String text) {
			titleElement.getElement().setInnerText(text);
		}

		@Override
		public void setHTML(SafeHtml html) {
			titleElement.getElement().setInnerHTML(html.asString());
		}

		@UiHandler("closeIcon")
		protected void onCloseButtonClicked(ClickEvent event) {
			parent.hide();
			if (onCloseHandler != null) {
				onCloseHandler.onClick(event);
			}
		}
	}
}
