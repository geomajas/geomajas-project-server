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
