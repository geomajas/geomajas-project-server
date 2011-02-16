/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineHook;
import org.geomajas.service.pipeline.PipelineInfo;
import org.geomajas.service.pipeline.PipelineInterceptor;
import org.geomajas.service.pipeline.PipelineInterceptor.ExecutionMode;
import org.geomajas.service.pipeline.PipelineService;
import org.geomajas.service.pipeline.PipelineStep;
import org.geotools.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service which allows "executing" a pipeline.
 * 
 * @param <RESPONSE>
 *            type of response object for the pipeline
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
@Component
public class PipelineServiceImpl<RESPONSE> implements PipelineService<RESPONSE> {

	private final Logger log = LoggerFactory.getLogger(PipelineServiceImpl.class);
	
	private static final String INDENT = "   ";

	@Autowired
	private List<PipelineInfo<RESPONSE>> pipelineInfos;

	/** @inheritDoc */
	public void execute(String key, String layerId, PipelineContext context, RESPONSE response)
			throws GeomajasException {
		execute(getPipeline(key, layerId), context, response);
	}

	/** @inheritDoc */
	public void execute(PipelineInfo<RESPONSE> pipeline, PipelineContext startContext, RESPONSE response)
			throws GeomajasException {
		PipelineContext context = startContext;
		if (null == context) {
			context = createContext();
		}
		log.debug("executing pipeline {}", pipeline.getPipelineName());
		for (PipelineStep<RESPONSE> step : pipeline.getPipeline()) {
			if (context.isFinished()) {
				log.debug("context finished, pipeline {} execution done.", pipeline.getPipelineName());
				break;
			}
			log.debug("executing step {} for pipeline {}.", step.getId(), pipeline.getPipelineName());
			step.execute(context, response);
		}
		log.debug("");
	}

	/** @inheritDoc */
	@SuppressWarnings("unchecked")
	public PipelineInfo<RESPONSE> getPipeline(String pipelineName, String layerId) throws GeomajasException {
		PipelineInfo<RESPONSE> layerPipeline = null;
		PipelineInfo<RESPONSE> defaultPipeline = null;
		for (PipelineInfo<RESPONSE> pipeline : pipelineInfos) {
			if (pipeline.getPipelineName().equals(pipelineName)) {
				String lid = pipeline.getLayerId();
				if (null == lid) {
					defaultPipeline = pipeline;
				} else if (lid.equals(layerId)) {
					layerPipeline = pipeline;
				}
			}
		}
		if (null == layerPipeline) {
			layerPipeline = defaultPipeline;
		}
		if (null == layerPipeline) {
			throw new GeomajasException(ExceptionCode.PIPELINE_UNKNOWN, pipelineName, layerId);
		}
		return layerPipeline;
	}

	/** @inheritDoc */
	public PipelineContext createContext() {
		return new PipelineContextImpl();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void postConstruct() throws GeomajasException {
		// sort pipelines so a pipeline is always before it's delegate (to make sure delegates are not handled before
		// delegating)
		List<PipelineInfo<RESPONSE>> delegateLast = new ArrayList<PipelineInfo<RESPONSE>>();
		for (PipelineInfo<RESPONSE> pipeline : pipelineInfos) {
			List<PipelineInfo<RESPONSE>> missing = new ArrayList<PipelineInfo<RESPONSE>>();
			PipelineInfo<RESPONSE> delegate = pipeline;
			while (delegate != null && !delegateLast.contains(delegate)) {
				missing.add(delegate);
				delegate = delegate.getDelegatePipeline();
			}
			// prepend missing delegate last
			delegateLast.addAll(0, missing);
		}

		// extend the delegating pipelines
		for (PipelineInfo<RESPONSE> pipeline : delegateLast) {
			if (pipeline.getPipeline() == null) {
				log.debug("extending pipeline {} with delegate {}", pipeline.getPipelineName(), pipeline
						.getDelegatePipeline() == null ? "none" : pipeline.getDelegatePipeline().getPipelineName());
				extendPipeline(pipeline);
				for (PipelineStep<RESPONSE> pipelineStep : pipeline.getPipeline()) {
					log.debug("added step {} to pipeline {}", pipelineStep.getId(), pipeline.getPipelineName());
				}
				log.debug("pipeline {} finished", pipeline.getPipelineName());
			} else {
				log.debug("pipeline {} with delegate {} has no extensions", pipeline.getPipelineName(), pipeline
						.getDelegatePipeline() == null ? "none" : pipeline.getDelegatePipeline().getPipelineName());
			}
		}
		// add the interceptors
		for (PipelineInfo<RESPONSE> pipeline : delegateLast) {
			interceptPipeline(pipeline);
		}
		// remove double names
		Map<PipelineStamp<RESPONSE>, PipelineInfo<RESPONSE>> map = 
			new HashMap<PipelineStamp<RESPONSE>, PipelineInfo<RESPONSE>>();
		for (PipelineInfo<RESPONSE> pipeline : pipelineInfos) {
			// equal stamps will be overwritten here, last one wins !
			map.put(new PipelineStamp<RESPONSE>(pipeline), pipeline);
		}
		pipelineInfos = new ArrayList<PipelineInfo<RESPONSE>>(map.values());
		if (log.isDebugEnabled()) {
			log.debug("listing pipeline structures");
			for (PipelineInfo<RESPONSE> pipeline : pipelineInfos) {
				log.debug("");
				print(pipeline);
			}
		}
	}

	private void print(PipelineInfo<RESPONSE> pipeline) {
		log.debug("<pipeline name = '{}' layer = '{}'>", pipeline.getPipelineName(), pipeline.getLayerId());
		for (PipelineStep<RESPONSE> step : pipeline.getPipeline()) {
			printStep(INDENT, step);
		}
		log.debug("</pipeline>");
	}

	private void printStep(String indent, PipelineStep<RESPONSE> step) {
		if (step instanceof PipelineInterceptorStep) {
			PipelineInterceptorStep<RESPONSE> pis = (PipelineInterceptorStep<RESPONSE>) step;
			log.debug(indent + "<interceptor id = '{}'>", pis.getId());
			for (PipelineStep<RESPONSE> st : pis.getSteps()) {
				printStep(indent + INDENT, st);
			}
			log.debug(indent + "</interceptor>");
		} else {
			log.debug(indent + "<step id = '{}'/>", step.getId());
		}
	}

	@SuppressWarnings("unchecked")
	private void extendPipeline(PipelineInfo<RESPONSE> pipeline) throws GeomajasException {
		log.debug("extending pipeline {}", pipeline.getPipelineName());
		// collect extensions and steps
		List<PipelineStep<RESPONSE>> steps = new ArrayList<PipelineStep<RESPONSE>>();
		Map<String, List<PipelineStep<RESPONSE>>> extensions = new HashMap<String, List<PipelineStep<RESPONSE>>>();
		PipelineInfo<RESPONSE> delegate = pipeline;
		int total = 0;
		while (delegate != null) {
			if (delegate.getExtensions() != null) {
				for (Map.Entry<String, PipelineStep<RESPONSE>> extensionEntry : delegate.getExtensions().entrySet()) {
					if (!extensions.containsKey(extensionEntry.getKey())) {
						extensions.put(extensionEntry.getKey(), new ArrayList<PipelineStep<RESPONSE>>());
					}
					extensions.get(extensionEntry.getKey()).add(extensionEntry.getValue());
					total++;
				}
			}
			if (delegate.getPipeline() != null) {
				steps.addAll(delegate.getPipeline());
			}
			delegate = delegate.getDelegatePipeline();
		}

		// we have everything, lets extend...
		int count = 0;
		for (int i = steps.size() - 1; i >= 0; i--) {
			PipelineStep<RESPONSE> step = steps.get(i);
			if (step instanceof PipelineHook) {
				List<PipelineStep<RESPONSE>> extList = extensions.get(step.getId());
				if (null != extList) {
					steps.addAll(i + 1, extList);
					for (PipelineStep<RESPONSE> ext : extList) {
						log.debug("extending pipeline {} with step {}", pipeline.getPipelineName(), ext.getId());
					}
					count += extList.size();
				}
			}
		}
		if (count != total) {
			throw new GeomajasException(ExceptionCode.PIPELINE_UNSATISFIED_EXTENSION, pipeline.getPipelineName(),
					pipeline.getLayerId());
		}
		pipeline.setPipeline(steps);
	}

	@SuppressWarnings("unchecked")
	private void interceptPipeline(PipelineInfo<RESPONSE> pipeline) throws GeomajasException {
		// collect interceptors
		List<PipelineInterceptor<RESPONSE>> interceptors = new ArrayList<PipelineInterceptor<RESPONSE>>();
		PipelineInfo<RESPONSE> delegate = pipeline;
		while (delegate != null) {
			if (delegate.getInterceptors() != null) {
				// prepend to preserve order
				interceptors.addAll(0, delegate.getInterceptors());
			}
			delegate = delegate.getDelegatePipeline();
		}

		// create a step for each interceptor
		List<PipelineInterceptorStep<RESPONSE>> interceptorSteps = new ArrayList<PipelineInterceptorStep<RESPONSE>>();
		List<PipelineStep<RESPONSE>> steps = new ArrayList<PipelineStep<RESPONSE>>(pipeline.getPipeline());
		for (PipelineInterceptor<RESPONSE> interceptor : interceptors) {
			interceptorSteps.add(new PipelineInterceptorStep<RESPONSE>(interceptor, steps));
		}
		// sort by width, smallest first
		Collections.sort(interceptorSteps, new WidthComparator());

		// construct the nested hierarchy of normal steps and interceptor steps
		for (PipelineInterceptorStep<RESPONSE> interceptorStep : interceptorSteps) {
			log.debug("adding interceptor {} to pipeline {}", interceptorStep.getId(), pipeline.getPipelineName());
			int fromIndex = 0;
			int toIndex = steps.size() - 1;
			// find the from and to step indices (takes nesting into account)
			for (int i = 0 ; i < steps.size() ; i++) {
				PipelineStep<RESPONSE> pipelineStep = steps.get(i);
				if (pipelineStep instanceof PipelineInterceptorStep) {
					PipelineInterceptorStep<RESPONSE> pis = (PipelineInterceptorStep<RESPONSE>) pipelineStep;
					if (pis.getFromStep().equals(interceptorStep.getFromStep())) {
						fromIndex = i;
					}
					if (pis.getToStep().equals(interceptorStep.getToStep())) {
						toIndex = i;
					}
				} else {
					if (pipelineStep.equals(interceptorStep.getFromStep())) {
						fromIndex = i;
					}
					if (pipelineStep.equals(interceptorStep.getToStep())) {
						toIndex = i;
					}
				}
			}
			if (fromIndex > toIndex) {
				throw new GeomajasException(ExceptionCode.PIPELINE_HIDDEN_INTERCEPTOR_STEP, interceptorStep.getId());
			}
			// nest the steps
			interceptorStep.setSteps(new ArrayList<PipelineStep<RESPONSE>>(steps.subList(fromIndex, toIndex + 1)));
			for (int i = toIndex; i >= fromIndex; i--) {
				steps.remove(i);
			}
			steps.add(fromIndex, interceptorStep);
		}
		pipeline.setPipeline(steps);
	}

	/**
	 * Pipeline step that executes an interceptor.
	 * 
	 * @author Jan De Moerloose
	 * 
	 * @param <T>
	 */
	protected class PipelineInterceptorStep<T> implements PipelineStep<T> {

		private PipelineInterceptor<T> interceptor;

		private List<PipelineStep<T>> steps;

		private PipelineStep<T> fromStep;

		private PipelineStep<T> toStep;

		private int width;

		public PipelineInterceptorStep(PipelineInterceptor<T> interceptor, List<PipelineStep<T>> steps)
				throws GeomajasException {
			this.interceptor = interceptor;
			int fromIndex = 0;
			int toIndex = steps.size() - 1;
			for (int i = 0 ; i < steps.size() ; i++) {
				PipelineStep<T> pipelineStep = steps.get(i);
				if (pipelineStep.getId().equals(interceptor.getFromStepId())) {
					fromIndex = i;
					fromStep = pipelineStep;
				}
				if (pipelineStep.getId().equals(interceptor.getToStepId())) {
					toIndex = i;
					toStep = pipelineStep;
				}
			}
			if (fromIndex < 0 || toIndex < 0 || fromIndex > toIndex) {
				throw new GeomajasException(ExceptionCode.PIPELINE_UNSATISFIED_INTERCEPTOR, interceptor.getId());
			}
			width = toIndex - fromIndex + 1;
		}

		public void setSteps(List<PipelineStep<T>> steps) {
			this.steps = steps;
		}

		public List<PipelineStep<T>> getSteps() {
			return steps;
		}

		public String getId() {
			return interceptor.getId();
		}

		public int getWidth() {
			return width;
		}

		public PipelineStep<T> getFromStep() {
			return fromStep;
		}

		public PipelineStep<T> getToStep() {
			return toStep;
		}

		public void execute(PipelineContext context, T response) throws GeomajasException {
			log.debug("executing beforeSteps for interceptor {}", interceptor.getId());
			ExecutionMode mode = interceptor.beforeSteps(context, response);
			if (mode == null) {
				mode = ExecutionMode.EXECUTE_ALL;
			}
			switch (mode) {
				case EXECUTE_ALL:
				case EXECUTE_STEPS_NOT_AFTER:
					if (!context.isFinished()) {
						for (PipelineStep<T> step : getSteps()) {
							if (context.isFinished()) {
								log.debug("context finished, interceptor {} execution done.", interceptor.getId());
								break;
							}
							log.debug("executing step {} for interceptor {}.", step.getId(), interceptor.getId());
							step.execute(context, response);
						}
					}
					break;
				default:
					log.debug("skipping steps for interceptor {}", interceptor.getId());
			}
			switch (mode) {
				case EXECUTE_ALL:
				case EXECUTE_SKIP_STEPS:
					interceptor.afterSteps(context, response);
					break;
				default:
					log.debug("skipping afterSteps for interceptor {}", interceptor.getId());
			}
		}
	}

	/**
	 * Comparator for sorting interceptors by width.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	protected class WidthComparator implements Comparator<PipelineInterceptorStep<RESPONSE>> {

		public int compare(PipelineInterceptorStep<RESPONSE> o1, PipelineInterceptorStep<RESPONSE> o2) {
			return o1.getWidth() - o2.getWidth();
		}

	}

	/**
	 * Stamp for detection of duplicate pipelines.
	 * 
	 * @author Jan De Moerloose
	 * 
	 * @param <T>
	 */
	protected final class PipelineStamp<T> {

		private final String name;

		private final String layerId;

		public PipelineStamp(PipelineInfo<T> info) {
			this.name = info.getPipelineName();
			this.layerId = info.getLayerId();
		}

		public boolean equals(Object otherObject) {
			if (this == otherObject) {
				return true;
			}
			if (otherObject == null || !(otherObject instanceof PipelineStamp)) {
				return false;
			}
			PipelineStamp<T> otherStamp = (PipelineStamp<T>) otherObject;
			return Utilities.equals(name, otherStamp.name) && Utilities.equals(layerId, otherStamp.layerId);
		}

		public int hashCode() {
			int result = 13;
			result = Utilities.hash(name, result);
			result = Utilities.hash(layerId, result);
			return result;
		}
	}

}
