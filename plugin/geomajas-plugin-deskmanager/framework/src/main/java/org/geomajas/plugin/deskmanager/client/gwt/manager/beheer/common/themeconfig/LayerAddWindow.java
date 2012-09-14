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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.themeconfig;

import java.util.LinkedHashMap;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * 
 * @author Oliver May
 * 
 */
public class LayerAddWindow extends DockableWindow {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	private DynamicForm form;
	private ComboBoxItem layerSelect;

	private static final int FORMITEM_WIDTH = 300;

	public LayerAddWindow(final ThemeConfigurationPanel themeConfiguration, final LayerAddCallback callback) {
		super();
		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle(MESSAGES.themeConfigLayerAdd());
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		
		form = new DynamicForm();
		form.setAutoFocus(true);
		form.setWidth(FORMITEM_WIDTH + 100);
		
		layerSelect = new ComboBoxItem();
		layerSelect.setTitle(MESSAGES.themeConfigLayerSelect());
		layerSelect.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				for (ClientLayerInfo layer : themeConfiguration.getMainMap().getLayers()) {
					if (layer.getId().equals(layerSelect.getValueAsString())) {
						callback.execute(layer);
						break;
					}
				}
				hide();
				destroy();
			}
		});
		
		LinkedHashMap<String, String> layers = new LinkedHashMap<String, String>();
		for (ClientLayerInfo layer : themeConfiguration.getMainMap().getLayers()) {
			layers.put(layer.getId(), layer.getLabel());
		}
		layerSelect.setValueMap(layers);
		
		
		form.setFields(layerSelect);
		
		addItem(form);
		
		show();
	}
	
	/**
	 * Callback interface for selecting a layer.
	 * 
	 * @author Oliver May
	 *
	 */
	public interface LayerAddCallback {
		/**
		 * Called when layer is selected.
		 * @param layer
		 */
		void execute(ClientLayerInfo layer);
	}
	
}