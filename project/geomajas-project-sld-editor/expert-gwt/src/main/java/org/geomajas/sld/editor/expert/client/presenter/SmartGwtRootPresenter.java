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
package org.geomajas.sld.editor.expert.client.presenter;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;
import com.smartgwt.client.widgets.BaseWidget;

/**
 * Sub-class of {@link RootPresenter} to accommodate for SmartGwt way of adding panels to root.
 * 
 * @author Jan De Moerloose
 */
public class SmartGwtRootPresenter extends RootPresenter {

	/**
	 * {@link RootPresenter}'s view.
	 */
	public static class SmartGwtRootView extends RootView {

		/**
		 * The glass element.
		 */
		private Element glass;

		public Widget asWidget() {
			return null;
		}

		@Override
		public void setInSlot(Object slot, Widget content) {
			if (content != null) {
				((BaseWidget) content).draw();
			}
		}
		//Called as a consequence of ginjector.getPlaceManager().revealCurrentPlace();
		public void lockScreen() {
			ensureGlass();
			Document.get().getBody().appendChild(glass);
		}

		public void unlockScreen() {
			ensureGlass();
			Document.get().getBody().removeChild(glass);
		}

		public void ensureGlass() {
			if (glass == null) {
				glass = Document.get().createDivElement();
				Style style = glass.getStyle();
				style.setPosition(Position.ABSOLUTE);
				style.setLeft(0, Unit.PX);
				style.setTop(0, Unit.PX);
				style.setRight(0, Unit.PX);
				style.setBottom(0, Unit.PX);
				style.setZIndex(2147483647); // Maximum z-index
			}
		}
	}

	@Inject
	public SmartGwtRootPresenter(EventBus eventBus, SmartGwtRootView view) {
		super(eventBus, view);
	}

	@Override
	public void onRevealRootPopupContent(final RevealRootPopupContentEvent revealContentEvent) {
		addToPopupSlot(revealContentEvent.getContent());
		// always show pop-up !
		// revealContentEvent.getContent().getView().show();
	}

}
