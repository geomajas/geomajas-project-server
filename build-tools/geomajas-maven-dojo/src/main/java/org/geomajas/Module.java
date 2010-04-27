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
package org.geomajas;

import org.apache.maven.artifact.Artifact;

/**
 * Dojo module metadata.
 * 
 * @author Jan De Moerloose
 */
public class Module {

	private String groupId;
	private String artifactId;
	private String version;
	private String type;
	private String classifier;
	private String requires;
	private Artifact artifact;

	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public String getRequires() {
		return requires;
	}

	public void setRequires(String require) {
		this.requires = require;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public boolean refersTo(Artifact artifact) {
		boolean ok = artifact.getGroupId().equals(getGroupId());
		ok &= artifact.getArtifactId().equals(getArtifactId());
		if (getVersion() != null) {
			ok &= artifact.getVersion().equals(getVersion());
		}
		if (getType() != null) {
			ok &= artifact.getType().equals(getType());
		}
		if (getClassifier() != null) {
			ok &= artifact.getClassifier().equals(getClassifier());
		}
		return ok;
	}

	public String toString() {
		return groupId + ":" + artifactId + ":" + version;
	}

}
