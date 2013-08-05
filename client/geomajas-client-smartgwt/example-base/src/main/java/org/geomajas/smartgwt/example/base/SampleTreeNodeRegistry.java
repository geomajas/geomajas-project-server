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

package org.geomajas.smartgwt.example.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for the samples for the {@link com.smartgwt.client.widgets.tree.TreeGrid} on the left side.
 *
 * @author Joachim Van der Auwera
 */
public final class SampleTreeNodeRegistry {

	private static final List<SampleTreeNode> SAMPLE_NODES = new ArrayList<SampleTreeNode>();

	private SampleTreeNodeRegistry() {
		// do not allow instantiation
	}

	public static void addSampleTreeNode(SampleTreeNode node) {
		SAMPLE_NODES.add(node);
	}

	public static SampleTreeNode[] getSampleTreeNodeArray() {
		return SAMPLE_NODES.toArray(new SampleTreeNode[SAMPLE_NODES.size()]);
	}

}
