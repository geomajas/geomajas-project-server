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

package org.geomajas.puregwt.example.client.page.overview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.puregwt.example.client.Showcase;
import org.geomajas.puregwt.example.client.sample.ShowcaseSampleDefinition;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * Sample overview widget that displays all samples in a sort of thumbnail view.
 * 
 * @author Pieter De Graef
 */
public class SampleBlockView extends ScrollPanel implements HasSamples {

	private final Map<ShowcaseSampleDefinition, SampleBlock> samples;

	public SampleBlockView(List<ShowcaseSampleDefinition> samples, final SampleOverviewPage overviewPage) {
		setSize("100%", "100%");

		FlowPanel contentPanel = new FlowPanel();
		contentPanel.setSize("100%", "100%");
		setWidget(contentPanel);
		this.samples = new HashMap<ShowcaseSampleDefinition, SampleBlock>();

		for (final ShowcaseSampleDefinition sample : samples) {
			SampleBlock sampleBlock = new SampleBlock(sample);
			this.samples.put(sample, sampleBlock);
			contentPanel.add(sampleBlock);
			sampleBlock.addDomHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					event.stopPropagation();
					Showcase.showSample(sample);
				}
			}, ClickEvent.getType());
		}
	}

	public void setData(List<ShowcaseSampleDefinition> data) {
		// We filter by switching the visibility of the individual sample block widgets:
		for (SampleBlock sampleBlock : samples.values()) {
			sampleBlock.setVisible(false);
		}
		for (final ShowcaseSampleDefinition sample : data) {
			SampleBlock sampleBlock = samples.get(sample);
			if (sampleBlock != null) {
				sampleBlock.setVisible(true);
			}
		}
	}
}