/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.project.profiling.service;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.MultiThreadedClaimStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.RingBuffer;
import org.geomajas.annotation.Api;
import org.geomajas.project.profiling.jmx.GroupData;
import org.geomajas.project.profiling.jmx.ProfilingBean;
import org.geomajas.project.profiling.jmx.ProfilingData;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bean for building and reading profiling data.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class ProfilingContainer implements ProfilingBean {

	private static final OneContainer CLEAR = new OneContainer(0, 0);
	private static final GroupDataComparator GROUP_DATA_COMPARATOR = new GroupDataComparator();

	private int ringSize = 1024; // must be a power of two
	private ExecutorService executorService;
	private Disruptor<Registration> disruptor;
	private RingBuffer<Registration> ringBuffer;
	
	private final Map<String, OneContainerContainer> groupData = 
			new ConcurrentHashMap<String, OneContainerContainer>();
	private OneContainer total = CLEAR;

	/**
	 * Set ring size. Should be done before starting the ProfilingContainer. Otherwise the default value may be used.
	 *
	 * @param ringSize ring size
	 */
	@Api
	public void setRingSize(int ringSize) {
		this.ringSize = ringSize;
	}

	/**
	 * Set up the disruptor service to have a single consumer which aggregates the data.
	 */
	@PostConstruct
	public void start() {
		executorService = Executors.newCachedThreadPool();
		disruptor = new Disruptor<Registration>(Registration.FACTORY,
				executorService,
				new MultiThreadedClaimStrategy(ringSize),
				new BlockingWaitStrategy());
		disruptor.handleEventsWith(new ContainerEventHandler());
		ringBuffer = disruptor.start();
	}

	/**
	 * Stop processing incoming data.
	 */
	@PreDestroy
	public void shutdown() {
		disruptor.shutdown();
		executorService.shutdownNow();
	}

	/** {@inheritDoc} */
	@Api
	public void clear() {
		register("", -1);
	}

	/**
	 * Add a registration for a group.
	 *
	 * @param group group name
	 * @param duration duration
	 */
	@Api
	public void register(String group, long duration) {
		if (null == group) {
			group = "";
		}
		final long sequence = ringBuffer.next();
		final Registration registration = ringBuffer.get(sequence);

		registration.setGroup(group);
		registration.setDuration(duration);

		ringBuffer.publish(sequence);
	}

	/** {@inheritDoc} */
	@Api
	public List<GroupData> getGroupData() {
		List<GroupData> result = new ArrayList<GroupData>();
		boolean done = false;
		while (!done) {
			try {
				for (Map.Entry<String, OneContainerContainer> entry : groupData.entrySet()) {
					OneContainer gv = entry.getValue().getValue();
					GroupData gd = new GroupContainer(entry.getKey(), gv.getInvocationCount(), gv.getTotalRunTime());
					result.add(gd);
				}
				done = true;
			} catch (ConcurrentModificationException cme) {
				result.clear();
			}
		}
		Collections.sort(result, GROUP_DATA_COMPARATOR);
		return result;
	}

	/** {@inheritDoc} */
	@Api
	public ProfilingData getTotal() {
		return total;
	}

	/**
	 * Handler which reads the Registration messages and merges in the current profiling data.
	 */
	private class ContainerEventHandler implements EventHandler<Registration> {

		public void onEvent(final Registration registration, final long sequence, final boolean endOfBatch) 
				throws Exception {
			String group = registration.getGroup();
			long duration = registration.getDuration();
			if (duration < 0) {
				// clear data
				groupData.clear();
				total = CLEAR;
			} else {
				total = new OneContainer(total.getInvocationCount() + 1, total.getTotalRunTime() + duration);
				OneContainerContainer container = groupData.get(group);
				if (null == container) {
					container = new OneContainerContainer();
					container.setValue(new OneContainer(1, duration));
					groupData.put(group, container);
				} else {
					OneContainer gd = container.getValue();
					container.setValue(new OneContainer(gd.getInvocationCount() + 1, gd.getTotalRunTime() + duration));
				}
			}
		}
	}

	/**
	 * Comparator for {@link GroupData} instances.
	 */
	private static class GroupDataComparator implements Comparator<GroupData> {

		public int compare(GroupData left, GroupData right) {
			return left.getGroup().compareTo(right.getGroup());
		}
	}

	/**
	 * Container which contains a {@link OneContainer}.
	 */
	private static class OneContainerContainer {

		private OneContainer value;

		/**
		 * Get contained OneContainer.
		 *
		 * @return oneContainer
		 */
		public OneContainer getValue() {
			return value;
		}

		/**
		 * Set contained OneContainer.
		 *
		 * @param value oneContainer
		 */
		public void setValue(OneContainer value) {
			this.value = value;
		}
	}
}
