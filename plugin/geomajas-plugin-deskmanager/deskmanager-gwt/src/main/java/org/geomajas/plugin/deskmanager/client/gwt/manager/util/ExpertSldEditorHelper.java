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
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.impl.ManagerEntryPoint;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.SldCancelledHandler;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;

/**
 * This class helps you to use the expert SLD editor by (un)wrapping a Geomajas layer to raw sld.
 * <p>
 * This class has state, so do not reuse.
 * 
 * @author Kristof Heirwegh
 */
public class ExpertSldEditorHelper {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private final ClientVectorLayerInfo layer;

	private boolean initialized;
	private boolean styleDataChanged;
	private UserStyleInfo styleData;

	public ExpertSldEditorHelper(ClientVectorLayerInfo layer) {
		this.layer = layer;
		extractUserStyleInfo();
	}

	// ---------------------------------------------------------------

	/**
	 * Applies the changes to the given layerinfo (if dirty).
	 */
	public void apply(final ClientVectorLayerInfo cli) {
		if (styleDataChanged) {
			NamedStyleInfo nsi = cli.getNamedStyleInfo();
			if (nsi == null) {
				nsi = new NamedStyleInfo();
				nsi.setName(layer.getLabel());
				cli.setNamedStyleInfo(nsi);
			}
			nsi.setUserStyle(styleData);
		}
	}

	public void showExpertStyleEditor() {
		initializeExpertEditor();
		ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter().get().forceReveal();
	}
	
	// ---------------------------------------------------------------

	private void initializeExpertEditor() {
		if (!initialized) {
			ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter().get()
					.addSldSaveHandler(new SldSaveHandler() {
						public void onSldSave(SldSaveEvent event) {
							if (extractData()) {
								ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter().get()
										.closeEditor();
							}
						}
					});

			ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter().get()
					.addSldCancelledHandler(new SldCancelledHandler() {
						public void onSldCancelled(SldCancelledEvent event) {
							ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter().get()
									.closeEditor();
						}
					});
			initialized = true;
		}

		// -- load the sld data, if any
		if (styleData != null) {
			ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter().get()
					.loadSld(getStyleDataAsSldI());
		}
	}

	private boolean extractData() {
		styleDataChanged = true;
		styleData = null;

		// Find the userstyle.
		StyledLayerDescriptorInfo sldi = ManagerEntryPoint.getInstance().getGinjector().getSldEditorExpertPresenter()
				.get().getModel().getSldDescriptor();
		if (sldi != null) {
			if (sldi.getChoiceList() != null && sldi.getChoiceList().size() > 0) {
				NamedLayerInfo nli = sldi.getChoiceList().get(0).getNamedLayer();
				if (nli != null && nli.getChoiceList() != null && nli.getChoiceList().size() > 0) {
					styleData = nli.getChoiceList().get(0).getUserStyle();
				}
			}
		}
		if (styleData == null) {
			SC.warn(MESSAGES.layerConfigExpertEditorMalformedSldException());
			styleDataChanged = false;
			return false;
		}
		return true;
	}

	private void extractUserStyleInfo() {
		NamedStyleInfo nsi = layer.getNamedStyleInfo();
		if (nsi != null && nsi.getUserStyle() != null) {
			styleData = nsi.getUserStyle();
		}
	}

	private StyledLayerDescriptorInfo getStyleDataAsSldI() {
		if (styleData == null) {
			return null;
		}
		StyledLayerDescriptorInfo sldi = new StyledLayerDescriptorInfo();
		sldi.setVersion("1.0.0");
		sldi.setName(layer.getId());
		sldi.setTitle(layer.getLabel());
		ChoiceInfo ci = new ChoiceInfo();
		org.geomajas.sld.NamedLayerInfo.ChoiceInfo ci2 = new org.geomajas.sld.NamedLayerInfo.ChoiceInfo();
		sldi.getChoiceList().add(ci);
		ci.setNamedLayer(new NamedLayerInfo());
		ci.getNamedLayer().getChoiceList().add(ci2);
		ci2.setUserStyle(styleData);
		return sldi;
	}

}
