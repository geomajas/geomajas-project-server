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
package org.geomajas.sld.editor.common.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.NamedLayerInfo.ChoiceInfo;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.editor.common.client.GeometryType;
import org.geomajas.sld.editor.common.client.SldUtils;
import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.model.RuleModel.RuleModelState;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Default implementation of {@link SldModel}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldModelImpl implements SldModel {

	private static Logger logger = Logger.getLogger("SldModelImpl");

	private StyledLayerDescriptorInfo sld;

	private String nameOfLayer;

	private String styleTitle;

	private GeometryType geomType; // Cannot be updated

	private RuleGroup ruleGroup;

	private SldEditorMessages messages;

	private RuleModelFactory ruleModelFactory;

	private boolean supported;

	private String supportedWarning;

	private boolean dirty;

	@Inject
	public SldModelImpl(@Assisted StyledLayerDescriptorInfo sld, SldEditorMessages messages,
			RuleModelFactory ruleModelFactory) {
		this.messages = messages;
		this.ruleModelFactory = ruleModelFactory;
		setSld(sld);
	}

	public void refresh(SldModel other) {
		setSld(other.getSld());
	}

	private void setSld(StyledLayerDescriptorInfo sld) {
		this.sld = sld;
		supported = true;
		dirty = false;
		geomType = GeometryType.UNSPECIFIED;

		if (null == sld.getChoiceList() || sld.getChoiceList().isEmpty()) {
			setUnsupported(messages.unsupportedEmpty());
		} else if (sld.getChoiceList().size() > 1) {
			setUnsupported(messages.unsupportedMultipleNamedLayer());
		} else {
			StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().get(0);

			if (!info.ifNamedLayer()) {
				setUnsupported(messages.unsupportedUserLayer());
			} else {
				NamedLayerInfo namedLayerInfo = info.getNamedLayer();

				if (null == namedLayerInfo.getChoiceList() || 0 == namedLayerInfo.getChoiceList().size()) {
					setUnsupported(messages.unsupportedEmptyNamedLayer());
				}
				if (namedLayerInfo.getChoiceList().size() > 1) {
					setUnsupported(messages.unsupportedMultipleNamedStyle());
				}

				this.nameOfLayer = (null != namedLayerInfo.getName()) ? namedLayerInfo.getName() : messages
						.nameUnspecified();

				if (namedLayerInfo.getChoiceList().get(0).ifNamedStyle()) {
					// Only the name is specialized
					this.styleTitle = namedLayerInfo.getChoiceList().get(0).getNamedStyle().getName();
					setUnsupported(messages.unsupportedNamedStyle());
				} else if (namedLayerInfo.getChoiceList().get(0).ifUserStyle()) {
					UserStyleInfo userStyle = namedLayerInfo.getChoiceList().get(0).getUserStyle();
					this.styleTitle = userStyle.getTitle();

					if (null == userStyle.getFeatureTypeStyleList() || 
							userStyle.getFeatureTypeStyleList().size() == 0) {
						setUnsupported(messages.unsupportedNoFeatureTypeStyle());
					}
					if (userStyle.getFeatureTypeStyleList().size() > 1) {
						setUnsupported(messages.unsupportedMultipleFeatureTypeStyle());
					}

					FeatureTypeStyleInfo featureTypeStyle = userStyle.getFeatureTypeStyleList().get(0);

					// Geometry Type can only be determined from the rule data (deeper level)
					geomType = SldUtils.getGeometryType(userStyle.getFeatureTypeStyleList());

					ruleGroup = new RuleGroupImpl();
					ruleGroup.setTitle(featureTypeStyle.getTitle());
					ruleGroup.setName(featureTypeStyle.getName());
					ruleGroup.setRuleModelList(new ArrayList<RuleModel>());
					ruleGroup.setGeomType(GeometryType.UNSPECIFIED);

					for (RuleInfo rule : featureTypeStyle.getRuleList()) {
						RuleModel ruleModel = ruleModelFactory.create(ruleGroup, rule, null);
						ruleGroup.setGeomType(ruleModel.getGeometryType()); // copy the last one (should be equal for
																			// all)
						ruleGroup.getRuleModelList().add(ruleModel);
					}

				}
			}

		}

	}

	public boolean isComplete() {
		if (!isSupported()) {
			return false;
		} else {
			for (RuleModel rule : getRuleGroup().getRuleModelList()) {
				if (rule.getState() == RuleModelState.INCOMPLETE) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getSupportedWarning() {
		return supportedWarning;
	}

	private void setUnsupported(String warning) {
		supported = false;
		supportedWarning = warning;
	}

	public void synchronize() {
		for (RuleModel model : ruleGroup.getRuleModelList()) {
			model.synchronize();
		}
		if (isSupported() && isComplete()) {
			// retrieve the first choice
			StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().iterator().next();

			// Update the name of the layer
			info.getNamedLayer().setName(getNameOfLayer());

			List<ChoiceInfo> choiceList = info.getNamedLayer().getChoiceList();
			// retrieve the first constraint
			ChoiceInfo choiceInfo = choiceList.iterator().next();

			if (choiceInfo.ifNamedStyle()) {
				// Only the name is specialized
				if (null == choiceInfo.getNamedStyle()) {
					choiceInfo.setNamedStyle(new NamedStyleInfo());
				}
				choiceInfo.getNamedStyle().setName(getStyleTitle());
			} else if (choiceInfo.ifUserStyle()) {
				choiceInfo.getUserStyle().setTitle(getStyleTitle());
				List<RuleInfo> rules = new ArrayList<RuleInfo>();
				for (RuleModel model : getRuleGroup().getRuleModelList()) {
					rules.add(model.getRuleInfo());
				}
				choiceInfo.getUserStyle().getFeatureTypeStyleList().get(0).setRuleList(rules);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.SldModelIntf#getName()
	 */
	public String getName() {
		return sld.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.SldModelIntf#getSld()
	 */
	public StyledLayerDescriptorInfo getSld() {
		return sld;
	}

	public String getNameOfLayer() {
		return nameOfLayer;
	}

	public void setNameOfLayer(String nameOfLayer) {
		this.nameOfLayer = nameOfLayer;
	}

	public String getStyleTitle() {
		return styleTitle;
	}

	public void setStyleTitle(String styleTitle) {
		this.styleTitle = styleTitle;
	}

	public GeometryType getGeomType() {
		return geomType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.SldModelIntf#getRuleGroup()
	 */
	public RuleGroup getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(RuleGroup ruleGroup) {
		this.ruleGroup = ruleGroup;
	}

	public boolean isSupported() {
		return supported;
	}

}
