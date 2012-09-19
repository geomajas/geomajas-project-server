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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * @author Kristof Heirwegh
 */
public class SaveButtonBar extends HLayout {

	private final IButton edit;

	private final IButton save;

	private final IButton cancel;

	private Canvas control;

	private WoaEventHandler eventHandler;

	/**
	 * This is just a convenienceMethod.
	 * 
	 * @param eventHandler also needs to support Canvas
	 */
	public SaveButtonBar(final WoaEventHandler eventHandler) {
		this(eventHandler, null);
		if (!(eventHandler instanceof Canvas)) {
			throw new IllegalArgumentException("Also needs to be a Canvas!");
		}
	}

	/**
	 * @param eventHandler
	 * @param parent control that is hosting the editsession.
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

		edit = new IButton("Wijzigen");
		edit.setIcon(WidgetLayout.iconEdit);
		edit.setAutoFit(true);
		edit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doEditClick(event);
			}
		});

		save = new IButton("Opslaan");
		save.setIcon(WidgetLayout.iconSave);
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doSaveClick(event);
			}
		});

		cancel = new IButton("Annuleren");
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.setAutoFit(true);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doCancelClick(event);
			}
		});

		addMember(edit);
	}

	/**
	 * mainly for internal use, but you can use this method to fake a button-click.
	 */
	public void doEditClick(ClickEvent event) {
		if (eventHandler.onEditClick(event)) {
			removeMember(edit);
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
			Whiteboard.fireEvent(new EditSessionEvent(false, control));
		}
	}

	// -------------------------------------------------

	/**
	 * 
	 */
	public interface WoaEventHandler {

		/**
		 * @return state of event. (eg. when event is save, return true when successfully saved, false if for instance
		 *         some fields were invalid) a return value of false means the buttons will not change state and no
		 *         editingSessionevents will be fired.
		 */
		boolean onEditClick(ClickEvent event);

		boolean onSaveClick(ClickEvent event);

		boolean onCancelClick(ClickEvent event);
	}
}
