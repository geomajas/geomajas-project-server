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

package org.geomajas.widget.utility.gwt.client.ribbon;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.service.ClientConfigurationService;
import org.geomajas.gwt.client.service.WidgetConfigurationCallback;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.ribbon.RibbonBar;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.common.client.ribbon.RibbonGroup;
import org.geomajas.widget.utility.configuration.RibbonBarInfo;
import org.geomajas.widget.utility.configuration.RibbonColumnInfo;
import org.geomajas.widget.utility.configuration.RibbonGroupInfo;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * SmartGWT implementation of the {@link RibbonBar} interface. This widget is based upon a VLayout.
 * 
 * @author Pieter De Graef
 */
public class RibbonBarLayout extends HLayout implements RibbonBar {

	private boolean showGroupTitles = true;

	private List<RibbonGroup> groups = new ArrayList<RibbonGroup>();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Initialize an empty ribbon bar widget. */
	public RibbonBarLayout() {
		setMembersMargin(GuwLayout.ribbonBarInternalMargin);
		setStyleName("ribbon");
		setOverflow(GuwLayout.ribbonBarOverflow);
	}

	/**
	 * Create a ribbon bar widget using the given configuration information and a map.
	 * 
	 * @param barInfo
	 *            The information from which to build the ribbon bar. In order to create the correct sub-widgets, the
	 *            {@link RibbonColumnRegistry} will be used.
	 * @param mapWidget
	 *            The map widget onto which many actions in this ribbon apply.
	 */
	public RibbonBarLayout(RibbonBarInfo barInfo, MapWidget mapWidget) {
		this();
		buildGui(barInfo, mapWidget);
	}

	/**
	 * Create a ribbon bar widget using a back-end spring bean identifier and a map.
	 * 
	 * @param mapWidget
	 *            The map widget onto which many actions in this ribbon apply.
	 * @param application
	 *            The application wherein to search for the ribbon configuration.
	 * @param beanId
	 *            A unique spring bean identifier for a bean of class {@link RibbonBarInfo}. This configuration is then
	 *            fetched and applied.
	 */
	public RibbonBarLayout(final MapWidget mapWidget, final String application, final String beanId) {
		this();

		ClientConfigurationService.getApplicationWidgetInfo(application, beanId,
				new WidgetConfigurationCallback<RibbonBarInfo>() {

					/** {@inheritDoc} */
					public void execute(RibbonBarInfo ribbonBarInfo) {
						if (null == ribbonBarInfo) {
							throw new IllegalStateException("Cannot find ribbon configuration bean " + beanId +
									" for application " + application);
						}
						buildGui(ribbonBarInfo, mapWidget);
					}
				});
	}

	// ------------------------------------------------------------------------
	// Ribbon implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void addGroup(RibbonGroup ribbonGroup) {
		if (null == ribbonGroup) {
			throw new IllegalArgumentException("Cannot add RibbonGroup with null value.");
		}
		groups.add(ribbonGroup);
		ribbonGroup.setShowTitle(showGroupTitles);
		ribbonGroup.asWidget().setStyleName(getStyleName() + "Group");
		addMember(ribbonGroup.asWidget());
	}

	/** {@inheritDoc} */
	public void addGroup(RibbonGroup ribbonGroup, int index) {
		if (null == ribbonGroup) {
			throw new IllegalArgumentException("Cannot add RibbonGroup with null value.");
		}
		groups.add(index, ribbonGroup);
		ribbonGroup.setShowTitle(showGroupTitles);
		ribbonGroup.asWidget().setStyleName(getStyleName() + "Group");
		addMember(ribbonGroup.asWidget(), index);
	}

	/** {@inheritDoc} */
	public void removeGroup(RibbonGroup ribbonGroup) {
		if (null == ribbonGroup) {
			throw new IllegalArgumentException("Cannot remove RibbonGroup with null value.");
		}
		groups.remove(ribbonGroup);
		removeMember((Canvas) ribbonGroup.asWidget());
	}

	/** {@inheritDoc} */
	public void removeGroup(int index) {
		removeGroup(getGroup(index));
	}

	/** {@inheritDoc} */
	public RibbonGroup getGroup(int index) {
		return groups.get(index);
	}

	/** {@inheritDoc} */
	public void setShowGroupTitles(boolean showGroupTitles) {
		this.showGroupTitles = showGroupTitles;
		for (RibbonGroup group : groups) {
			group.setShowTitle(showGroupTitles);
		}
	}

	/** {@inheritDoc} */
	public boolean isShowGroupTitles() {
		return showGroupTitles;
	}

	// ------------------------------------------------------------------------
	// SmartGWT method overrides:
	// ------------------------------------------------------------------------

	@Override
	public void setStyleName(String styleName) {
		super.setStyleName(styleName);
		for (RibbonGroup group : groups) {
			group.asWidget().setStyleName(getStyleName() + "Group");
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui(RibbonBarInfo barInfo, MapWidget mapWidget) {
		if (null == barInfo) {
			throw new IllegalArgumentException("RibbonBarLayout cannot be built without RibbonBarInfo configuration.");
		}
		for (RibbonGroupInfo groupInfo : barInfo.getGroups()) {
			RibbonGroupLayout group = new RibbonGroupLayout(groupInfo.getTitle());
			for (RibbonColumnInfo columnInfo : groupInfo.getColumns()) {
				RibbonColumn ribbonColumn = RibbonColumnRegistry.getRibbonColumn(columnInfo.getType(),
						columnInfo.getTools(), columnInfo.getParameters(), mapWidget);
				if (ribbonColumn != null) {
					group.addColumn(ribbonColumn);
				}
			}
			addGroup(group);
		}
	}
}