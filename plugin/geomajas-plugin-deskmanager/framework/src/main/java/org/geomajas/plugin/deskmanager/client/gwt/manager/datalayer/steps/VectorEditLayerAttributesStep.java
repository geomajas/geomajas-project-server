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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps;

import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.LayerAttributesGrid;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DiscoveryCommService;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;

import java.util.Map;


import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;

/**
 * @author Kristof Heirwegh
 */
public class VectorEditLayerAttributesStep extends WizardStepPanel {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private Map<String, String> connectionProps;

	private String layerTypeName;

	private LayerAttributesGrid grid;

	private boolean loading;

	private String previousStep = NewLayerModelWizardWindow.STEP_VECTOR_CHOOSE_LAYER;

	public VectorEditLayerAttributesStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_ATTRIBUTES, 
				MESSAGES.vectorEditLayerAttributesStepNumbering() + " " +
				MESSAGES.vectorEditLayerAttributesStepTitle(), false, parent);
		setWindowTitle(MESSAGES.vectorEditLayerAttributesStepTitle());

		grid = new LayerAttributesGrid();

		addMember(grid);
	}

	@Override
	public void initialize() {
		reset();
		if (connectionProps != null && layerTypeName != null && !"".equals(layerTypeName)) {
			loading = true;
			fireChangedEvent();
			DiscoveryCommService.getVectorLayerConfiguration(connectionProps, layerTypeName,
					new DataCallback<VectorLayerConfiguration>() {

						public void execute(VectorLayerConfiguration result) {
							grid.setData(result);
							loading = false;
							fireChangedEvent();
						}
					}, new DataCallback<String>() {

						public void execute(String result) {
							reset();
							grid.setWarning("<b><i>" + result + "</i></b>");
							loading = false;
							fireChangedEvent();
						}
					});
		}
	}

	public void setData(Map<String, String> connectionProps, String layerTypeName) {
		this.connectionProps = connectionProps;
		this.layerTypeName = layerTypeName;
	}

	public VectorLayerConfiguration getData() {
		return grid.getData();
	}

	public void setWarning(String warning) {
		grid.setWarning(warning);
		fireChangedEvent(); // relevant in this case as it is used in validation
	}

	@Override
	public boolean isValid() {
		return (grid.isValid() && !loading);
	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE;
	}

	@Override
	public String getPreviousStep() {
		return previousStep;
	}

	public void setPreviousStep(String previousStep) {
		this.previousStep = previousStep;
	}

	@Override
	public void reset() {
		grid.reset();
	}

	@Override
	public void stepFinished() {
		VectorEditLayerStyleStep nextStep = (VectorEditLayerStyleStep) parent.getStep(
				NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE);
		if (nextStep != null) {
			nextStep.setData(getData());
		} else {
			SC.warn(MESSAGES.vectorEditLayerAttributesStepNextStepNotFound());
		}
	}

	// -------------------------------------------------

	// private LinkedHashMap<String, String> getTypes() {
	// LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
	// values.put(PrimitiveType.BOOLEAN.name(), "Boolean");
	// values.put(PrimitiveType.CURRENCY.name(), "Boolean");
	// values.put(PrimitiveType.DATE.name(), "Boolean");
	// values.put(PrimitiveType.DOUBLE.name(), "Boolean");
	// values.put(PrimitiveType.FLOAT.name(), "Boolean");
	// values.put(PrimitiveType.IMGURL.name(), "Boolean");
	// values.put(PrimitiveType.INTEGER.name(), "Boolean");
	// values.put(PrimitiveType.LONG.name(), "Boolean");
	// values.put(PrimitiveType.SHORT.name(), "Boolean");
	// values.put(PrimitiveType.STRING.name(), "Boolean");
	// values.put(PrimitiveType.URL.name(), "Boolean");
	// return values;
	// }

}
