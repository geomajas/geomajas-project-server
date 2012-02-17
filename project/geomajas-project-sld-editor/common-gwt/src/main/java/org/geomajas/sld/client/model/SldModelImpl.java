package org.geomajas.sld.client.model;

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
import org.geomajas.sld.client.model.RuleModel.RuleModelState;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.SldUtils;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

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
			setUnsupported("Empty SLD's are not supported.");
		} else if (sld.getChoiceList().size() > 1) {
			setUnsupported("Only SLD's with a single &lt;NamedLayer&gt; element are supported.");
		} else {
			StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().get(0);

			if (!info.ifNamedLayer()) {
				setUnsupported("Only SLD's with a &lt;NamedLayer&gt; element are supported.");
			} else {
				NamedLayerInfo namedLayerInfo = info.getNamedLayer();

				if (null == namedLayerInfo.getChoiceList() || 0 == namedLayerInfo.getChoiceList().size()) {
					setUnsupported("SLD's with an empty &lt;NamedLayer&gt; element are not supported.");
				}
				if (namedLayerInfo.getChoiceList().size() > 1) {
					setUnsupported("SLD's with more than 1 &lt;NamedLayer&gt; element are not supported.");
				}

				this.nameOfLayer = (null != namedLayerInfo.getName()) ? namedLayerInfo.getName() : messages
						.nameUnspecified();

				if (namedLayerInfo.getChoiceList().get(0).ifNamedStyle()) {
					// Only the name is specialized
					this.styleTitle = namedLayerInfo.getChoiceList().get(0).getNamedStyle().getName();
					setUnsupported("SLD with named style are not supported.");
				} else if (namedLayerInfo.getChoiceList().get(0).ifUserStyle()) {
					UserStyleInfo userStyle = namedLayerInfo.getChoiceList().get(0).getUserStyle();
					this.styleTitle = userStyle.getTitle();

					if (null == userStyle.getFeatureTypeStyleList() || userStyle.getFeatureTypeStyleList().size() == 0) {
						setUnsupported("SLD without FeatureTypeStyle are not supported.");
					}
					if (userStyle.getFeatureTypeStyleList().size() > 1) {
						setUnsupported("SLDs with multiple FeatureTypeStyles are not supported.");
					}

					FeatureTypeStyleInfo featureTypeStyle = userStyle.getFeatureTypeStyleList().get(0);

					// Geometry Type can only be determined from the rule data (deeper level)
					geomType = SldUtils.GetGeometryType(userStyle.getFeatureTypeStyleList());

					ruleGroup = new RuleGroupImpl();
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
						RuleModel ruleModel = ruleModelFactory.create(rule, null);
						ruleGroup.setGeomType(ruleModel.getGeometryType()); // copy the last one (should be equal for
																			// all)
						ruleGroup.getRuleModelList().add(ruleModel);
					}

				}
			}

		}

	}
	
	public boolean isComplete() {
		if(!isSupported()) {
			return false;
		} else {
			for (RuleModel rule : getRuleGroup().getRuleModelList()) {
				if(rule.getState() == RuleModelState.INCOMPLETE) {
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
		if(isSupported() && isComplete()) {
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
