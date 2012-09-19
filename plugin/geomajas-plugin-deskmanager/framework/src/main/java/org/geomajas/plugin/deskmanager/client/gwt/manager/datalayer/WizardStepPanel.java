/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public abstract class WizardStepPanel extends VLayout {

	protected String name;

	protected String title;

	protected boolean lastStep;

	protected String windowTitle;

	protected Wizard parent;

	public WizardStepPanel(String name, String title, boolean isLastStep, Wizard parent) {
		super();
		if (parent == null) {
			throw new IllegalArgumentException("Need a parent!!");
		}
		this.name = name;
		this.title = title;
		this.parent = parent;
		this.lastStep = isLastStep;
		this.setPadding(10);
		this.setIsGroup(true);
		this.setGroupTitle(title);
		this.setWidth100();
		this.setHeight100();
	}

	public abstract boolean isValid();

	public abstract String getNextStep();

	public abstract String getPreviousStep();

	public abstract void reset();

	/**
	 * Called when user selects [next]. Prepare data for the next step here.
	 */
	public abstract void stepFinished();

	/**
	 * only called on nextstep, not on previousstep, to prevent reloading of data.
	 */
	public void initialize() {
	}

	// -------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isLastStep() {
		return lastStep;
	}

	public void setLastStep(boolean lastStep) {
		this.lastStep = lastStep;
	}

	// -------------------------------------------------

	protected void fireChangedEvent() {
		parent.onChanged(this);
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}
}
