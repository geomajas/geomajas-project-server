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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout.ChangedHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * @author Kristof Heirwegh
 */
public class SaveButtonBar extends HLayout implements ChangedHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private final IButton edit;

	private final IButton save;

	private final IButton cancel;

	private final IButton reset;

	private Canvas control;

	private WoaEventHandler eventHandler;

	/**
	 * This is just a convenienceMethod.
	 * 
	 * @param eventHandler
	 *            also needs to support Canvas
	 */
	public SaveButtonBar(final WoaEventHandler eventHandler) {
		this(eventHandler, null);
		if (!(eventHandler instanceof Canvas)) {
			throw new IllegalArgumentException("Also needs to be a Canvas!");
		}
	}

	/**
	 * @param eventHandler
	 * @param parent
	 *            control that is hosting the editsession.
	 */
	public SaveButtonBar(final WoaEventHandler eventHandler, Canvas parent) {
		super(10);
		setAutoHeight();
		this.eventHandler = eventHandler;

		if (parent == null && eventHandler instanceof Canvas) {
			control = (Canvas) eventHandler;
		} else {
			control = parent;
		}

		edit = new IButton(MESSAGES.editButtonText());
		edit.setIcon(WidgetLayout.iconEdit);
		edit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doEditClick(event);
			}
		});

		save = new IButton(MESSAGES.saveButtonText());
		save.setIcon(WidgetLayout.iconSave);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doSaveClick(event);
			}
		});

		cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doCancelClick(event);
			}
		});

		reset = new IButton(MESSAGES.resetButtonText());
		reset.setIcon(WidgetLayout.iconUndo);
		reset.setTooltip(MESSAGES.resetButtonTooltip());
		reset.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doResetClick(event);
			}
		});
		eventHandler.registerChangedHandler(this);
	}

	private void resetState() {
		if (hasMember(save)) {
			removeMember(save);
		}
		if (hasMember(cancel)) {
			removeMember(cancel);
		}
		addMember(edit);
		addMember(reset);
		reset.setDisabled(eventHandler.isDefault());
	}

	/**
	 * mainly for internal use, but you can use this method to fake a button-click.
	 */
	public void doEditClick(ClickEvent event) {
		if (eventHandler.onEditClick(event)) {
			removeMember(edit);
			removeMember(reset);
			addMember(save);
			addMember(cancel);
			Whiteboard.fireEvent(new EditSessionEvent(true, control));
		}
	}

	/**
	 * mainly for internal use, but you can use this method to fake a button-click.
	 */
	public void doSaveClick(ClickEvent event) {
		if (eventHandler.onSaveClick(event)) {
			removeMember(save);
			removeMember(cancel);
			addMember(edit);
			reset.setDisabled(eventHandler.isDefault());
			addMember(reset);
			Whiteboard.fireEvent(new EditSessionEvent(false, control));
		}
	}

	/**
	 * mainly for internal use, but you can use this method to fake a button-click.
	 */
	public void doCancelClick(ClickEvent event) {
		if (eventHandler.onCancelClick(event)) {
			removeMember(save);
			removeMember(cancel);
			addMember(edit);
			reset.setDisabled(eventHandler.isDefault());
			addMember(reset);
			Whiteboard.fireEvent(new EditSessionEvent(false, control));
		}
	}

	/**
	 * mainly for internal use, but you can use this method to fake a button-click.
	 */
	public void doResetClick(ClickEvent event) {
		if (eventHandler.onResetClick(event)) {
			resetState();
			Whiteboard.fireEvent(new EditSessionEvent(false, control));
		}
	}

	// -------------------------------------------------
	public void onChange() {
		resetState();
	}

}
