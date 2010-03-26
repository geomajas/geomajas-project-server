/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.samples.base;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.samples.SampleArray;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;

import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * <p>
 * The tree used in the left of the sample overview.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SampleTree extends TreeGrid {

	public SampleTree() {
		super();
		setWidth100();
		setHeight100();
		setCustomIconProperty("icon");
		setAnimateFolderTime(100);
		setAnimateFolders(true);
		setAnimateFolderSpeed(500);
		setNodeIcon("[ISOMORPHIC]/geomajas/example/images/application_view_list.png");
		setShowSortArrow(SortArrow.CORNER);
		setShowAllRecords(true);
		setLoadDataOnDemand(false);
		setCanSort(false);

		TreeGridField field = new TreeGridField();
		field.setCanFilter(true);
		field.setName("name");
		field.setTitle("<b>" + I18nProvider.getSampleMessages().sampleTitle(Geomajas.getVersion()) + "</b>");
		setFields(field);

		Tree tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setIdField("nodeId");
		tree.setRootValue("topLevel");
		tree.setData(SampleArray.getSampleArray());

		setData(tree);
		//tree.openAll(tree.getRoot());
		tree.openFolder(tree.getChildren(tree.getRoot())[2]);
	}
}
