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

import org.geomajas.project.profiling.jmx.GroupData;
import org.geomajas.project.profiling.jmx.ProfilingData;
import org.junit.Test;

import java.util.List;

/**
 * Test to assure ProfillingContainer has good throughput and does not slow down the app too much.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingContainerTest {

	private static final int ITERATIONS = 10000000;
	private static final int PREFIX_DIVISOR = 3700000;
	private static final int THREADS = 4; // system slows down a lot when higher than the number of cores
	
	private static final String[] GROUP = { "zero", "g1", "g2", "group 3", "four" };
	private static final String[] PREFIX = { "", "more.", "another-", "xxx-", "[({?" };
	
	@Test
	public void profilingContainerTest() throws Exception {
		ProfilingContainer container = new ProfilingContainer();
		container.setRingSize(1024);
		container.start();
		
		Thread[] threads = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++ ) {
			threads[i] = new Filler(container);
		}
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < THREADS; i++ ) {
			threads[i].start();
		}
		
		boolean done = false;
		int waitThread = 0;
		while (!done) {
			output(container);

			threads[waitThread].join(1000);
			if (!threads[waitThread].isAlive()) {
				waitThread++;
			}
			done = true;
			for (int i = 0; i < THREADS; i++ ) {
				done &= !threads[i].isAlive();
			}			
		}
		System.out.print("DONE");
		output(container);
		System.out.println("Total run time (in ms): " + (System.currentTimeMillis() - start));
	}

	private void output(ProfilingContainer container) {
		System.out.println("-----------------");
		ProfilingData total = container.getTotal();
		List<GroupData> gd = container.getGroupData();

		System.out.println("TOTAL: " + total.getInvocationCount() + "  " + total.getTotalRunTime() + "  avg " +
				total.getAverageRunTime());
		for (GroupData g : gd) {
			System.out.printf("%20s:  %8d %8d %.3f\n", g.getGroup(),
					g.getInvocationCount(), g.getTotalRunTime(), g.getAverageRunTime());
		}
	}

	/**
	 * Add many registrations which need to be totalled.
	 */
	private static class Filler extends Thread {

		ProfilingContainer container;

		public Filler(ProfilingContainer container) {
			this.container = container;
		}

		@Override
		public void run() {
		 	for (int i = 0; i < ITERATIONS; i++) {
				 long duration = ( i % 5 ) + 1;
				 String group = PREFIX[i/PREFIX_DIVISOR] + GROUP[i%GROUP.length];
				 container.register(group, duration);
			}
		}
	}
}
