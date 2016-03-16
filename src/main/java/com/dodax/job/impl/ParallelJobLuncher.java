package com.dodax.job.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.dodax.job.IJob;
import com.dodax.job.IParallelJobLuncher;


/**
 * Implementation of a job luncher which can lunch jobs in parallel.
 * @author Mathieu
 *
 */
public class ParallelJobLuncher implements IParallelJobLuncher {

	@Override
	public void lunch(List<IJob> jobs, int maxAmountProcessInParallel) throws InterruptedException, ExecutionException {
		// Instanciate services
		ExecutorService executor = Executors.newFixedThreadPool(maxAmountProcessInParallel);
		CompletionService<Object> completionService = new ExecutorCompletionService<Object>(executor);
		// Submit the jobs in services
		submitsJobs(jobs, completionService);
		
		Object result = null;
		try {
			// Get the completed jobs
			for (int i = 0; i < jobs.size(); i++) {
				// Takes the next available completed job
				Future<Object> jobCompleted = completionService.take();
				// Get the result if needed
				result = jobCompleted.get();
			}
		} finally {
			executor.shutdown();
		}
	}
	
	/**
	 * Submit the jobs to lunch them.
	 * @param jobs The job list
	 * @param completionService The completion service
	 * @return The future job list
	 */
	private List<Future<Object>> submitsJobs(List<IJob> jobs,CompletionService<Object> completionService){
		List<Future<Object>> futures = new ArrayList<Future<Object>>();
		for (IJob job : jobs) {
			Future<Object> future = completionService.submit(job);
			futures.add(future);
		}
		return futures;
	}

}
