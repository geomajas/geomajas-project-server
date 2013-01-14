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

package org.geomajas.plugin.editing.gwt.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.plugin.editing.gwt.example.client.i18n.EditingMessages;
import org.geomajas.plugin.editing.gwt.example.client.merge.MergePanel;
import org.geomajas.plugin.editing.gwt.example.client.split.SplitPanel;

/**
 * Main entry point registering the editing examples.
 *
 * @author Joachim Van der Auwera
 */
public class EditingExample implements EntryPoint {

	public static final EditingMessages MESSAGES = GWT.create(EditingMessages.class);

	public void onModuleLoad() {

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupEditing(),
				"[ISOMORPHIC]/geomajas/silk/plugin.png", "Editing", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editingPanel(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", EditingPanel.TITLE, "Editing", EditingPanel.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.multiGeometryPanel(),
				"[ISOMORPHIC]/geomajas/osgeo/island.png", MultiGeometryPanel.TITLE, "Editing",
				MultiGeometryPanel.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.mergePanel(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", MergePanel.TITLE, "Editing", MergePanel.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.splitPanel(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", SplitPanel.TITLE, "Editing", SplitPanel.FACTORY));
	}

}
