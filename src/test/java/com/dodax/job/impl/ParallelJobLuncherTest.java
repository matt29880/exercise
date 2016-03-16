package com.dodax.job.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.dodax.job.IJob;

/**
 * Tests of the class ParallelJobLuncherTest.
 * @author Mathieu
 *
 */
public class ParallelJobLuncherTest {

	// Pool sizes
	/** Size of a small thread pool. */
	private static final int SMALL_THREAD_POOL = 3;
	/** Size of a huge thread pool. */
	private static final int HUGE_THREAD_POOL = 200;
	// Jobs numbers
	/** Size of a small jobs list. */
	private static final int SMALL_JOBS_LIST = 4;
	/** Size of a huge thread list. */
	private static final int HUGE_JOBS_LIST = 4000;

	/**
	 * Test the lunch method in a correct situation with an empty list.
	 * 
	 * @throws ExecutionException
	 *             Exception from thread execution
	 * @throws InterruptedException
	 *             Exception from thread interruption
	 */
	@Test
	public void lunchWithEmptyList()
			throws InterruptedException, ExecutionException {
		ParallelJobLuncher luncher = new ParallelJobLuncher();
		luncher.lunch(new ArrayList<>(), SMALL_THREAD_POOL);
	}

	/**
	 * Test the lunch method in a correct situation.
	 * 
	 * @throws ExecutionException
	 *             Exception from thread execution
	 * @throws InterruptedException
	 *             Exception from thread interruption
	 */
	@Test
	public void lunchInCorrectSituation()
			throws InterruptedException, ExecutionException {
		ParallelJobLuncher luncher = new ParallelJobLuncher();
		List<IJob> jobs = dataInCorrectSituation(SMALL_JOBS_LIST);
		luncher.lunch(jobs, SMALL_THREAD_POOL);
		// Testing
		for (int i = 0; i < SMALL_JOBS_LIST; i++) {
			testACorrectJob(jobs.get(i));
		}
	}

	/**
	 * Test the lunch method in a correct situation with a lot of jobs.
	 * 
	 * @throws ExecutionException
	 *             Exception from thread execution
	 * @throws InterruptedException
	 *             Exception from thread interruption
	 */
	@Test
	public void lunchInCorrectSituationWithHugeJobList()
			throws InterruptedException, ExecutionException {
		ParallelJobLuncher luncher = new ParallelJobLuncher();
		List<IJob> jobs = dataInCorrectSituation(HUGE_JOBS_LIST);
		luncher.lunch(jobs, SMALL_THREAD_POOL);
		// Testing
		for (int i = 0; i < HUGE_JOBS_LIST; i++) {
			testACorrectJob(jobs.get(i));
		}
	}

	/**
	 * Test the lunch method with an exception.
	 * Receive the exception is the test case.
	 * 
	 * @throws ExecutionException
	 *             Exception from thread execution
	 * @throws InterruptedException
	 *             Exception from thread interruption
	 */
	@Test(expected = ExecutionException.class)
	public void lunchWithException() throws InterruptedException, ExecutionException {
		ParallelJobLuncher luncher = new ParallelJobLuncher();
		List<IJob> jobs = dataInCorrectSituation(SMALL_JOBS_LIST);

		// The 2nd element is the exception
		addJobExceptionIn2ndPlace(jobs);

		luncher.lunch(jobs, SMALL_THREAD_POOL);
	}

	/**
	 * Test the lunch method with an exception which stop other current jobs.
	 * The huge number of elements permits to have an exception and to stop current thread jobs.
	 * 
	 * @throws ExecutionException
	 *             Exception from thread execution
	 * @throws InterruptedException
	 *             Exception from thread interruption
	 */
	@Test
	public void lunchWithCurrentlyJobsStopped() {
		ParallelJobLuncher luncher = new ParallelJobLuncher();
		List<IJob> jobs = dataInCorrectSituation(HUGE_JOBS_LIST);

		// The 2nd element is the exception
		addJobExceptionIn2ndPlace(jobs);

		boolean anExceptionHappened = false;

		// Lunch the jobs, an exception should occurred
		try {
			luncher.lunch(jobs, HUGE_THREAD_POOL);
		} catch (InterruptedException | ExecutionException e) {
			anExceptionHappened = true;
		}
		assertTrue(anExceptionHappened);

		// Check if there is called and not finished jobs
		boolean isStartedWithoutResult = false;
		for (IJob iJob : jobs) {
			JobExample job = (JobExample) iJob;
			if (job.getStarted() && job.getResult() == null) {
				isStartedWithoutResult = true;
			}
		}
		// Asserts
		assertTrue(isStartedWithoutResult);
	}

	/**
	 * Create data in a correct situation (without exception).
	 * @param nb
	 * @return
	 */
	private List<IJob> dataInCorrectSituation(int nb) {
		List<IJob> jobs = new ArrayList<>();
		for (int i = 1; i <= nb; i++) {
			jobs.add(new JobExample(i));
		}
		return jobs;
	}

	/**
	 * Tests a correct job (without exception).
	 * @param iJob
	 */
	private void testACorrectJob(IJob iJob) {
		JobExample job = (JobExample) iJob;
		Integer expected = JobExample.FACTOR * job.getNumber();
		Integer actual = job.getResult();
		assertEquals(expected, actual);
	}
	
	/**
	 * Add a job with exception in the 2nd place.
	 * @param jobs The job list
	 */
	private void addJobExceptionIn2ndPlace(List<IJob> jobs){
		JobExample jobException = new JobExample(2);
		jobException.fail();
		jobs.add(1, jobException);
	}

}
