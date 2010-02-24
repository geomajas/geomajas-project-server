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

package org.geomajas.internal.service.vector;

import org.geomajas.layer.feature.InternalFeature;

/**
 * Request and response object for the "vectorLayer.saveOrUpdateOne" pipeline.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateOneContainer {

	private SaveOrUpdateContainer saveOrUpdateContainer;
	private int index;
	private InternalFeature oldFeature;
	private InternalFeature newFeature;

	public SaveOrUpdateOneContainer(SaveOrUpdateContainer saveOrUpdateContainer) {
		this.saveOrUpdateContainer = saveOrUpdateContainer;
	}

	public SaveOrUpdateContainer getSaveOrUpdateContainer() {
		return saveOrUpdateContainer;
	}

	public void setSaveOrUpdateContainer(SaveOrUpdateContainer saveOrUpdateContainer) {
		this.saveOrUpdateContainer = saveOrUpdateContainer;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public InternalFeature getOldFeature() {
		return oldFeature;
	}

	public void setOldFeature(InternalFeature oldFeature) {
		this.oldFeature = oldFeature;
	}

	public InternalFeature getNewFeature() {
		return newFeature;
	}

	public void setNewFeature(InternalFeature newFeature) {
		this.newFeature = newFeature;
	}
}
