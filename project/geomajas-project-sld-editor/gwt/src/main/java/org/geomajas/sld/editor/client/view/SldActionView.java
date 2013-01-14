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
package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.presenter.SldActionPresenter;
import org.geomajas.sld.editor.common.client.presenter.event.SldCloseEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldRefreshEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldRefreshEvent.HasSldRefreshHandlers;
import org.geomajas.sld.editor.common.client.presenter.event.SldRefreshEvent.SldRefreshHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.common.client.view.ViewUtil;
import org.geomajas.sld.editor.common.client.view.ViewUtil.YesNoCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Default implementation of {@link SldActionPresenter.MyView}.
 * 
 * @author Jan De Moerloose
 *
 */
public class SldActionView extends ViewImpl implements SldActionPresenter.MyView, HasSldRefreshHandlers {

	// Panel for SLD Action buttons (Save/Close/Cancel)
	private HLayout sldActionsPanel;

	private IButton saveButton;

	private CancelButton cancelButton;

	private CloseButton closeSldButton;

	private final EventBus eventBus;

	private final ViewUtil viewUtil;

	private final SldEditorMessages sldEditorMessages;

	@Inject
	public SldActionView(final EventBus eventBus, final ViewUtil viewUtil, final SldEditorMessages sldEditorMessages) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		sldActionsPanel = new HLayout(10);
		saveButton = new SaveButton();
		saveButton.disable();
		sldActionsPanel.addMember(saveButton);

		cancelButton = new CancelButton();
		cancelButton.disable();
		sldActionsPanel.addMember(cancelButton);

		closeSldButton = new CloseButton();
		closeSldButton.disable();
		sldActionsPanel.addMember(closeSldButton);

		sldActionsPanel.setAlign(Alignment.CENTER);
		sldActionsPanel.setHeight(40); /* fixed size for buttons strip for saving */
		sldActionsPanel.setPadding(10);
	}

	public Widget asWidget() {
		return sldActionsPanel;
	}

	/** Definition of the Save button. */
	private class SaveButton extends IButton implements ClickHandler {

		public SaveButton() {
			// @todo FIX setIcon(WidgetLayout.iconSave);
			setShowDisabledIcon(false);
			setTitle(sldEditorMessages.saveButtonTitle());
			setTooltip(sldEditorMessages.saveButtonTooltip());

			// TODO: validate form first
			setDisabled(false);

			addClickHandler(this);
		}

		public void onClick(ClickEvent event) {
			GWT.log("SldWidget: User clicked on SLD save button");
			SldSaveEvent.fire(SldActionView.this);
		}

	}

	// -------------------------------------------------------------------------
	// Private class CancelButton:
	// -------------------------------------------------------------------------

	/** Definition of the Cancel button. */
	private class CancelButton extends IButton implements ClickHandler {

		public CancelButton() {
			setIcon("[ISOMORPHIC]/geomajas/icons/silk/cancel.png");
			setShowDisabledIcon(false);
			setTitle(sldEditorMessages.cancelButtonTitle());
			setTooltip("Cancel changes.");
			addClickHandler(this);
		}

		public void onClick(ClickEvent event) {
			viewUtil.showYesNoMessage("Wenst u al uw wijzigingen sinds de laatste bewaaroperatie te annuleren?"
					+ "<br/>Zo ja, druk op <b>'Ja'</b>."
					+ "<br/>Druk op <b>'Nee'</b> om uw wijzigingen (voorlopig) te behouden.", new YesNoCallback() {

				public void onYes() {
					SldRefreshEvent.fire(SldActionView.this);
				}

				public void onNo() {
				}

				public void onCancel() {
				}
			});
		}

	}

	// -------------------------------------------------------------------------
	// Private class CancelButton:
	// -------------------------------------------------------------------------

	/** Definition of the Cancel button. */
	private class CloseButton extends IButton implements ClickHandler {

		public static final String ICON_CLOSE = "[ISOMORPHIC]/geomajas/icons/silk/door_out.png";

		public CloseButton() {
			if (null != ICON_CLOSE) {
				setIcon(ICON_CLOSE);
			}
			setShowDisabledIcon(false);
			setTitle(sldEditorMessages.closeButtonTitle());
			setTooltip(sldEditorMessages.closeButtonTooltip());
			addClickHandler(this);
		}

		public void onClick(ClickEvent event) {
			viewUtil.showYesNoMessage(sldEditorMessages.confirmSavingChangesBeforeUnloadingSld(), new YesNoCallback() {

				public void onYes() {
					SldCloseEvent.fireSave(SldActionView.this);
				}

				public void onNo() {
					SldCloseEvent.fire(SldActionView.this);
				}

				public void onCancel() {
				}
			});
		}
	}

	public void setCloseEnabled(boolean enabled) {
		closeSldButton.setDisabled(!enabled);
	}

	public void setResetEnabled(boolean enabled) {
		cancelButton.setDisabled(!enabled);
	}

	public void setSaveEnabled(boolean enabled) {
		saveButton.setDisabled(!enabled);
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public HandlerRegistration addSldSaveHandler(SldSaveHandler handler) {
		return eventBus.addHandler(SldSaveEvent.getType(), handler);
	}

	public HandlerRegistration addSldRefreshHandler(SldRefreshHandler handler) {
		return eventBus.addHandler(SldRefreshEvent.getType(), handler);
	}

}
