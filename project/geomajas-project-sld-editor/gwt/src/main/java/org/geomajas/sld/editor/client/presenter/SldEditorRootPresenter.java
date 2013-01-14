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

package org.geomajas.sld.editor.client.presenter;

import org.geomajas.sld.editor.client.SmartGwtMainLayoutOfEditor;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * Root presenter of SLD editor. 
 * Sub-class of {@link RootPresenter} to accommodate for SmartGwt way of adding panels to a layout panel.
 * 
 * @author An Buyle
 * 
 */
public class SldEditorRootPresenter extends RootPresenter {

	@Inject
	public SldEditorRootPresenter(EventBus eventBus, StyledLayerDescriptorEditorView view) {
		super(eventBus, view);

	}

	/**
	 * {@link RootPresenter}'s view.
	 */
	public static class StyledLayerDescriptorEditorView extends RootView {

		/**
		 * The glass element.
		 */
		private Element glass;

		private static Layout viewLayout = SmartGwtMainLayoutOfEditor.getLayout();

		public Widget asWidget() {
			return viewLayout;
		}

		@Override
		public void setInSlot(Object slot, Widget content) {
			//Whatever slot
			if (content == null) {
				//Assumes the user wants to clear the slot content.
				if (viewLayout.getMembers().length > 0) {
					//TODO
				}
			} else {
				((Canvas) content).setWidth100();
				((Canvas) content).setHeight100();

				if (viewLayout.hasMember((Canvas) content)) {
					content.setVisible(true);
				} else {
					viewLayout.addMember((Canvas) content);
				}
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
	} // End View

	
	public Widget asWidget() {
		return null;
	}

	@Override
	public void onRevealRootPopupContent(final RevealRootPopupContentEvent revealContentEvent) {
		assert false : "onRevealRootPopupContent() may NOT be called";
	}

	@Override
	public void onRevealRootContent(RevealRootContentEvent event) {
		super.onRevealRootContent(event);
	}

}
