/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.geomajas.sld.client.presenter;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.model.event.SldAddedEvent;
import org.geomajas.sld.client.model.event.SldAddedEvent.SldAddedHandler;
import org.geomajas.sld.client.presenter.event.CreateSldPopupCreateEvent;
import org.geomajas.sld.client.presenter.event.CreateSldPopupCreateEvent.CreateSldPopupCreateHandler;
import org.geomajas.sld.client.presenter.event.CreateSldPopupCreateEvent.HasCreateSldPopupCreateHandlers;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.GeometryType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

/**
 * The {@link PresenterWidget} of a dialog box that is meant to be displayed no matter which presenter is visible.
 * Compare to {@link LocalDialogPresenterWidget}.
 * 
 * @author Philippe Beaudoin
 */
public class CreateSldDialogPresenterWidget extends PresenterWidget<CreateSldDialogPresenterWidget.MyView> implements
		SldAddedHandler {

	/**
	 * @author Jan De Moerloose
	 */
	public interface MyView extends PopupView, HasCreateSldPopupCreateHandlers {

		GeometryType getGeometryType();
		
		String getName();
	}

	private SldManager manager;
	
	private ViewUtil viewUtil;

	@Inject
	public CreateSldDialogPresenterWidget(final EventBus eventBus, final MyView view,
			final SldManager manager, final ViewUtil viewUtil) {
		super(eventBus, view);
		this.manager = manager;
		this.viewUtil = viewUtil;
		manager.addSldAddedHandler(this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addCreateSldPopupCreateHandler(new CreateSldPopupCreateHandler() {

			public void onCreateSldPopupCreate(CreateSldPopupCreateEvent event) {
				StyledLayerDescriptorInfo newInfo = manager.create(getView().getGeometryType());
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
