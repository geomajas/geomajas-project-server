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
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

import java.util.ArrayList;
import java.util.List;

/**
 * Base mojo for this plugin.
 * 
 * @author Jan De Moerloose
 *
 */
public abstract class DojoMojo extends AbstractMojo implements Contextualizable {

	/** @component */
	protected ArtifactResolver resolver;

	/** @component */
	protected ArtifactFactory artifactFactory;

	/** @component */
	protected ArchiverManager archiverManager;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	protected ArtifactRepository localRepository;

	/**
	 * @parameter expression="${project.remoteArtifactRepositories}"
	 * @required
	 * @readonly
	 */
	protected List<ArtifactRepository> remoteRepositories;

	/**
	 * The maven project descriptor
	 *
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * Comma-separated list of locales to bake in the build
	 *
	 * @parameter default-value="en"
	 */
	private String localeList;

	/**
	 * The layer optimization procedure (none or shrinksafe)
	 *
	 * @parameter default-value="shrinksafe"
	 */
	private String layerOptimize;

	/**
	 * Dojo version
	 *
	 * @parameter default-value="1.1.0"
	 */
	private String dojoVersion;

	/**
	 * List of dojo modules
	 *
	 * @parameter
	 */
	private List<Module> modules = new ArrayList<Module>();

	/** @component */
	protected MavenProjectHelper projectHelper;

	/**
	 * Name of the layer for the single layer build
	 *
	 * @parameter default-value="modules"
	 */
	private String layerName;


	// ------------------------------
	// Plexus Lifecycle
	// ------------------------------

	public final void contextualize(Context context) throws ContextException {
		PlexusContainer plexusContainer = (PlexusContainer) context.get(PlexusConstants.PLEXUS_KEY);
		try {
			archiverManager = (ArchiverManager) plexusContainer.lookup(ArchiverManager.ROLE);
		} catch (ComponentLookupException e) {
			throw new ContextException(e.getMessage(), e);
		}
	}

	protected Artifact resolve(String groupId, String id, String version, String type, String classifier)
			throws MojoExecutionException {
		Artifact artifact = artifactFactory.createArtifactWithClassifier(groupId, id, version, type, classifier);
		try {
			resolver.resolve(artifact, remoteRepositories, localRepository);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("artifact not found - " + e.getMessage(), e);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("artifact resolver problem - " + e.getMessage(), e);
		}
		return artifact;
	}

	/** @return the project */
	public MavenProject getProject() {
		return project;
	}

	public ArtifactRepository getLocalRepository() {
		return this.localRepository;
	}

	public List<ArtifactRepository> getRemoteRepositories() {
		return this.remoteRepositories;
	}

	protected ArchiverManager getArchiverManager() {
		return archiverManager;
	}

	public List<Module> getModules() {
		return modules;
	}

	public String getLocaleList() {
		return localeList;
	}

	public String getLayerOptimize() {
		return layerOptimize;
	}

	public String getDojoVersion() {
		return dojoVersion;
	}

	public String getLayerName() {
		return layerName;
	}

	@SuppressWarnings("unchecked")
	protected void resolveModules() throws MojoExecutionException {
		// first check project dependencies
		for (Object artifact : project.getArtifacts()) {
			for (Module module : modules) {
				if (module.refersTo((Artifact) artifact)) {
					module.setArtifact((Artifact) artifact);
				}
			}
		}
		// resolve normally
		for (Module module : modules) {
			if (module.getArtifact() == null) {
				Artifact artifact = resolve(module.getGroupId(), module.getArtifactId(), module.getVersion(), module
						.getType(), module.getClassifier());
				module.setArtifact(artifact);
			}
		}
	}

}
