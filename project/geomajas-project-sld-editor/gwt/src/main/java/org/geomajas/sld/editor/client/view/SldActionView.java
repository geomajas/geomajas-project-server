package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.client.presenter.SldActionPresenter;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.client.view.ViewUtil.YesNoCallback;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class SldActionView extends ViewImpl implements SldActionPresenter.MyView {

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
	
	public void setActionsEnabled(boolean enabled) {
		saveButton.setDisabled(!enabled);
		cancelButton.setDisabled(!enabled);
		closeSldButton.setDisabled(!enabled);
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
		}
	}


}
