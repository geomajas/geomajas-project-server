/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.common.client.presenter;

import org.geomajas.sld.editor.common.client.GeometryType;
import org.geomajas.sld.editor.common.client.model.SldManager;
import org.geomajas.sld.editor.common.client.model.SldModel;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent.SldAddedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.CreateSldDialogCreateEvent;
import org.geomajas.sld.editor.common.client.presenter.event.CreateSldDialogCreateEvent.CreateSldDialogCreateHandler;
import org.geomajas.sld.editor.common.client.presenter.event.CreateSldDialogCreateEvent.HasCreateSldDialogCreateHandlers;
import org.geomajas.sld.editor.common.client.view.ViewUtil;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

/**
 * The {@link PresenterWidget} of a dialog box that is meant to be displayed no matter which presenter is visible.
 * Compare to {@link LocalDialogPresenterWidget}.
 * 
 * @author Jan De Moerloose
 */
public class CreateSldDialogPresenterWidget extends PresenterWidget<CreateSldDialogPresenterWidget.MyView> implements
		SldAddedHandler {

	/**
	 * @author Jan De Moerloose
	 */
	public interface MyView extends PopupView, HasCreateSldDialogCreateHandlers {

		GeometryType getGeometryType();

		String getName();
	}

	private SldManager manager;

	private ViewUtil viewUtil;

	@Inject
	public CreateSldDialogPresenterWidget(final EventBus eventBus, final MyView view, final SldManager manager,
			final ViewUtil viewUtil) {
		super(eventBus, view);
		this.manager = manager;
		this.viewUtil = viewUtil;
		manager.addSldAddedHandler(this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addCreateSldDialogCreateHandler(new CreateSldDialogCreateHandler() {

			public void onCreateSldPopupCreate(CreateSldDialogCreateEvent event) {
				SldModel newInfo = manager.create(getView().getGeometryType());
				manager.add(newInfo);
			}
		}));
	}

	@Override
	public void onReveal() {
		super.onReveal();
	}

	@Override
	public void onHide() {
		super.onHide();
	}

	public void onSldAdded(SldAddedEvent event) {
		viewUtil.showMessage("De SLD met standaard inhoud is succesvol gecre&euml;erd.");
	}

}
