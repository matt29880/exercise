package com.dodax.job.impl;

import com.dodax.job.IJob;
/**
 * A job example for testing the class ParallelJobLuncher implementation.
 * @author Mathieu
 *
 */
public class JobExample implements IJob {
	
	/** The multiplication factor : this number is an example for testing. */
	public static final short FACTOR = 15;
	/** The number of the thread.
	 * It is used to calculate the result during the process of the job.
	 */
	private final Integer number;
	/** The result of the process. */
	private Integer result = null;
	/**
	 * Flag to know if the job must make an exception.
	 * It's for testing exception behavior.
	 */
	private Boolean fail = false;
	/**
	 * Flag to know if the job is started.
	 * It's for testing this specification "all other currently running jobs should be stopped as well".
	 */
	private Boolean started = true;

	public JobExample(Integer number) {
		super();
		this.number = number;
	}

	@Override
	public Object call() throws Exception {
		started = true;
		if (fail) {
			throw new Exception("A problem has been found in the job process.");
		} else {
			result = number * FACTOR;
		}
		return result;
	}

	/**
	 * Gets the number of the job.
	 * @return
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * Gets the result of the job.
	 * Null if the process is not finished or finished with exception.
	 * An integer if the process is not finished.
	 * @return
	 */
	public Integer getResult() {
		return result;
	}

	/**
	 * Indicates to the job to fail, i.e. make an exception.
	 */
	public void fail() {
		this.fail = true;
	}

	/**
	 * Gets the flag if the job is started.
	 * @return
	 */
	public Boolean getStarted() {
		return started;
	}

}
