package org.geomajas.sld.client.model;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.NamedLayerInfo.ChoiceInfo;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.SldUtils;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SldModel {

	private StyledLayerDescriptorInfo sld;

	private SldGeneralInfo generalInfo;

	private RuleGroup ruleGroup;

	private SldEditorMessages messages;

	@Inject
	public SldModel(@Assisted StyledLayerDescriptorInfo sld, SldEditorMessages messages) {
		this.messages = messages;
		setSld(sld);
	}

	public void setSld(StyledLayerDescriptorInfo sld) {
		this.sld = sld;
		SldGeneralInfo info = new SldGeneralInfo(GeometryType.UNSPECIFIED);
		setGeneralInfo(info);
		if (sld.getChoiceList().size() == 1) {
			NamedLayerInfo namedLayerInfo = sld.getChoiceList().get(0).getNamedLayer();
			if (namedLayerInfo.getChoiceList().size() == 1) {
				UserStyleInfo userStyle = namedLayerInfo.getChoiceList().get(0).getUserStyle();
				if (userStyle.getFeatureTypeStyleList().size() == 1) {
					FeatureTypeStyleInfo featureTypeStyle = userStyle.getFeatureTypeStyleList().get(0);
					ruleGroup = new RuleGroup();
					String styleTitle = featureTypeStyle.getTitle();
					if (null == styleTitle) {
						ruleGroup.setTitle("groep 1");
					} else {
						ruleGroup.setTitle(styleTitle);
					}
					ruleGroup.setName(featureTypeStyle.getName());
					ruleGroup.setRuleModelList(new ArrayList<RuleModel>());
					ruleGroup.setGeomType(GeometryType.UNSPECIFIED);
					for (RuleInfo rule : featureTypeStyle.getRuleList()) {
						RuleModel ruleModel = new RuleModel();

						// Determine the rule title to be used in the model, make sure it is NOT null or ""
						String title;

						if (null != rule.getTitle() && rule.getTitle().length() > 0) {
							title = rule.getTitle();
						} else if (null != rule.getName() && rule.getName().length() > 0) {
							title = rule.getName();
						} else {
							title = messages.ruleTitleUnspecified();
						}
						ruleModel.setTitle(title);

						ruleModel.setName(rule.getName()); // The rule name can be null

						RuleData ruleData = new RuleData(SldUtils.GetGeometryType(rule));
						ruleGroup.setGeomType(ruleData.getGeometryType()); // copy the last one (should be equal for all)
						ruleData.setTypeOfRule(RuleData.TypeOfRule.COMPLETE_RULE);
						ruleData.setCompleteRuleBody(rule); // TODO: OK???

						ruleModel.setRuleData(ruleData);
						ruleGroup.getRuleModelList().add(ruleModel);
					}
				}

			}
		}
	}

	public void updateFromGeneralInfo(SldGeneralInfo sldGeneralInfo) {
		// retrieve the first choice
		StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().iterator().next();
		if (!info.ifNamedLayer()) {
			// warning that invalid SLD
			// SC.warn("Only SLD's with a &lt;NamedLayer&gt; element are supported.");
			return; // ABORT
		}

		// Update the name of the layer
		info.getNamedLayer().setName(sldGeneralInfo.getNameOfLayer());

		List<ChoiceInfo> choiceList = info.getNamedLayer().getChoiceList();
		// retrieve the first constraint
		ChoiceInfo choiceInfo = choiceList.iterator().next();

		if (choiceInfo.ifNamedStyle()) {
			// Only the name is specialized
			if (null == choiceInfo.getNamedStyle()) {
				choiceInfo.setNamedStyle(new NamedStyleInfo());
			}
			choiceInfo.getNamedStyle().setName(sldGeneralInfo.getStyleTitle());
		} else if (choiceInfo.ifUserStyle()) {
			choiceInfo.getUserStyle().setTitle(sldGeneralInfo.getStyleTitle());
		}

	}

	public String getName() {
		return sld.getName();
	}

	public StyledLayerDescriptorInfo getSld() {
		return sld;
	}

	public SldGeneralInfo getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(SldGeneralInfo generalInfo) {
		this.generalInfo = generalInfo;
	}

	public RuleGroup getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(RuleGroup ruleGroup) {
		this.ruleGroup = ruleGroup;
	}

}
