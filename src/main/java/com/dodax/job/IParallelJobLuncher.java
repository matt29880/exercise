package com.dodax.job;

import java.util.List;

/**
 * Interface to lunch jobs in parallel.
 * 
 * @author Mathieu
 *
 */
public interface IParallelJobLuncher {

	/**
	 * Lunch in parallel some IJob process.
	 * @param jobs The list of IJob to execute.
	 * @param maxAmountProcessInParallel The maximum amount of process in parallel at the same time
	 * @return
	 * @throws Exception
	 */
	void lunch(List<IJob> jobs, int maxAmountProcessInParallel) 
			throws Exception;

}
