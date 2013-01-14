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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common.themeconfig;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.themeconfig.ThemeConfigurationPanel.State;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * @author Oliver May
 * 
 */
public class LayerConfigPanel extends Layout {

	private static final int FORMITEM_WIDTH = 300;

	private DynamicForm form;

	private StaticTextItem layerTitle;

	private RadioGroupItem visibility;

	private SliderItem opacity;

	private ThemeConfigurationPanel themeConfigurationPanel;

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	/**
	 * @param themeConfigurationPanel
	 */
	public LayerConfigPanel(ThemeConfigurationPanel themeConfigurationPanel) {
		super();
		this.themeConfigurationPanel = themeConfigurationPanel;
		layout();
	}

	private void layout() {
		// Left layout

		HLayout layout = new HLayout();

		form = new DynamicForm();
		// form.setWidth(ThemeConfigurationPanel.LEFT_WIDTH);
		form.setAutoFocus(true);
		form.setWidth(FORMITEM_WIDTH + 100);
		// form.setWrapItemTitles(false);

		layerTitle = new StaticTextItem();
		layerTitle.setTitle(MESSAGES.themeConfigLayerTitle());
		layerTitle.setRequired(true);
		layerTitle.setWidth(FORMITEM_WIDTH);

		visibility = new RadioGroupItem();
		visibility.setTitle(MESSAGES.themeConfigLayerVisibility());
		visibility.setValueMap(MESSAGES.themeConfigLayerVisibilityOn(), MESSAGES.themeConfigLayerVisibilityOff());
		visibility.setRequired(true);
		visibility.setWidth(FORMITEM_WIDTH);
		visibility.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				themeConfigurationPanel.getState().getLayerConfig()
						.setVisible(MESSAGES.themeConfigLayerVisibilityOn().equals(visibility.getValueAsString()));
			}
		});

		opacity = new SliderItem();
		opacity.setTitle(MESSAGES.themeConfigLayerOpacity());
		opacity.setMinValue(0);
		opacity.setMaxValue(100);
		opacity.setWidth(FORMITEM_WIDTH);
		opacity.setRequired(true);
		opacity.addChangedHandler(new ChangedHandler() {
			
			public void onChanged(ChangedEvent event) {
				themeConfigurationPanel.getState().getLayerConfig().setOpacity(opacity.getValueAsFloat() / 100);
			}
		});

		form.setFields(layerTitle, visibility, opacity);

		layout.addMember(form);

		HLayout group = new HLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.themeConfigThemeConfigGroup());
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	/**
	 * @param state
	 */
	public void update(State state) {
		if (state.getLayerConfig() != null) {
			layerTitle.setValue(state.getLayerConfig().getLayer().getLabel());
			if (state.getLayerConfig().isVisible()) {
				visibility.setValue(MESSAGES.themeConfigLayerVisibilityOn());
			} else {
				visibility.setValue(MESSAGES.themeConfigLayerVisibilityOff());
			}
			opacity.setValue(state.getLayerConfig().getOpacity() * 100);
		}
	}

}
