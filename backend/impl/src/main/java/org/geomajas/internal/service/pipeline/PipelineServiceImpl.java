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
		log.debug("executing pipeline {}", pipeline);
		for (PipelineStep<RESPONSE> step : pipeline.getPipeline()) {
			if (context.isFinished()) {
				log.debug("context finished, pipeline {} execution done.", pipeline);
				break;
			}
			log.debug("executing step {} for pipeline {}.", step.getId(), pipeline);
			step.execute(context, response);
		}
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

	@PostConstruct
	public void postConstruct() throws GeomajasException {
		// extend the delegating pipelines
		for (PipelineInfo<RESPONSE> pipeline : pipelineInfos) {
			if (pipeline.getPipeline() == null) {
				List<PipelineStep<RESPONSE>> steps = new ArrayList<PipelineStep<RESPONSE>>();
				extendPipeline(pipeline, steps);
				pipeline.setPipeline(steps);
			}
		}
		// add the interceptors
		for (PipelineInfo<RESPONSE> pipeline : pipelineInfos) {
			List<PipelineStep<RESPONSE>> steps = new ArrayList<PipelineStep<RESPONSE>>(pipeline.getPipeline());
			addInterceptors(pipeline, steps);
			pipeline.setPipeline(steps);
		}
	}

	@SuppressWarnings("unchecked")
	private void extendPipeline(PipelineInfo<RESPONSE> pipeline, List<PipelineStep<RESPONSE>> steps)
			throws GeomajasException {
		if (null == pipeline.getPipeline() && null != pipeline.getDelegatePipeline()) {
			PipelineInfo<RESPONSE> delegate = pipeline.getDelegatePipeline();
			extendPipeline(delegate, steps);

			Map<String, PipelineStep<RESPONSE>> extensions = pipeline.getExtensions();
			if (delegate.getPipeline() != null) {
				steps.addAll(delegate.getPipeline());
				if (extensions != null) {
					int count = 0;
					for (int i = steps.size() - 1; i >= 0; i--) {
						PipelineStep<RESPONSE> step = steps.get(i);
						if (step instanceof PipelineHook) {
							PipelineStep<RESPONSE> ext = extensions.get(step.getId());
							if (null != ext) {
								steps.add(i + 1, ext);
								count++;
							}
						}
					}
					if (count != extensions.size()) {
						throw new GeomajasException(ExceptionCode.PIPELINE_UNSATISFIED_EXTENSION,
								pipeline.getPipelineName(), pipeline.getLayerId());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addInterceptors(PipelineInfo<RESPONSE> pipeline, List<PipelineStep<RESPONSE>> steps)
			throws GeomajasException {
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

		List<PipelineInterceptorStep<RESPONSE>> interceptorSteps = new ArrayList<PipelineInterceptorStep<RESPONSE>>();
		for (PipelineInterceptor<RESPONSE> interceptor : interceptors) {
			interceptorSteps.add(new PipelineInterceptorStep<RESPONSE>(interceptor, steps));
		}
		// sort by width, smallest first
		Collections.sort(interceptorSteps, new WidthComparator());

		for (PipelineInterceptorStep<RESPONSE> interceptorStep : interceptorSteps) {
			int fromIndex = -1;
			int toIndex = -1;
			// find the from and to step indices (takes nesting into account)
			for (PipelineStep<RESPONSE> pipelineStep : steps) {
				if (pipelineStep instanceof PipelineInterceptorStep) {
					PipelineInterceptorStep<RESPONSE> pis = (PipelineInterceptorStep<RESPONSE>) pipelineStep;
					if (pis.getFromStep().equals(interceptorStep.getFromStep())) {
						fromIndex = steps.indexOf(pis);
					}
					if (pis.getToStep().equals(interceptorStep.getToStep())) {
						toIndex = steps.indexOf(pis);
					}
				} else {
					if (pipelineStep.equals(interceptorStep.getFromStep())) {
						fromIndex = steps.indexOf(pipelineStep);
					}
					if (pipelineStep.equals(interceptorStep.getToStep())) {
						toIndex = steps.indexOf(pipelineStep);
					}
				}
			}
			if (fromIndex < 0 || toIndex < 0 || fromIndex > toIndex) {
				throw new GeomajasException(ExceptionCode.PIPELINE_HIDDEN_INTERCEPTOR_STEP,
						interceptorStep.getId());
			}
			// nest the steps
			interceptorStep.setSteps(new ArrayList<PipelineStep<RESPONSE>>(steps.subList(fromIndex, toIndex + 1)));
			for (int i = toIndex; i >= fromIndex; i--) {
				steps.remove(i);
			}
			steps.add(fromIndex, interceptorStep);
		}
	}

	/** @inheritDoc */
	public PipelineContext createContext() {
		return new PipelineContextImpl();
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
			int fromIndex = -1;
			int toIndex = -1;
			for (PipelineStep<T> pipelineStep : steps) {
				if (pipelineStep.getId().equals(interceptor.getFromStepId())) {
					fromIndex = steps.indexOf(pipelineStep);
					fromStep = pipelineStep;
				}
				if (pipelineStep.getId().equals(interceptor.getToStepId())) {
					toIndex = steps.indexOf(pipelineStep);
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
			ExecutionMode mode = interceptor.beforeSteps(context, response);
			if (mode == null) {
				mode = ExecutionMode.EXECUTE_ALL;
			}
			switch (mode) {
				case EXECUTE_ALL:
				case EXECUTE_STEPS:
					if (!context.isFinished()) {
						for (PipelineStep<T> step : getSteps()) {
							if (context.isFinished()) {
								break;
							}
							step.execute(context, response);
						}
					}
			}
			switch (mode) {
				case EXECUTE_ALL:
				case EXECUTE_AFTER:
					interceptor.afterSteps(context, response);
			}
		}
	}

	/**
	 * Comparator for sorting interceptors by width.
	 * @author Jan De Moerloose
	 *
	 */
	protected class WidthComparator implements Comparator<PipelineInterceptorStep<RESPONSE>> {

		public int compare(PipelineInterceptorStep<RESPONSE> o1, PipelineInterceptorStep<RESPONSE> o2) {
			return o1.getWidth() - o2.getWidth();
		}

	}

}
