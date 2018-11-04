package msk.util.preprocess;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import msk.util.KeyLogic;

/**
 * This class is used for us to take in a Collection of Spectra
 * 
 * @author Andrew P Talbot
 */
public class PreprocessingThreadPool<T extends Runnable> {

	public static final int MIN_THREADS_TO_USE_POOL = 50;

	// Define the number of threads in our ThreadPool
	public static final int THREAD_COUNT_HIGH = 20;
	public static final int THREAD_COUNT_MEDIUM = 10;
	public static final int THREAD_COUNT_LOW = 5;

	private String type;
	private RunnablePreprocessingTask<T> task;
	public ExecutorService es;
	public List<Future<?>> futures;
	public int nThreads;

	// Our spectra to be processed.
	// public variables for quick, easy access.
	public BlockingQueue<MSKSpectrum> toProcess;
	public Map<KeyLogic, double[]> processedMZ;
	public Map<KeyLogic, double[]> processedIntensity;

	/**
	 * Constructor to make the thread pool.
	 * 
	 * @param task
	 *            is the type of preprocessing task that we are multithreading.
	 * @param type
	 *            is the type of the preprocessing task that we are doing.
	 * @param nThreads
	 *            is the number of threads that we are using in our threadpool.
	 */
	public PreprocessingThreadPool(RunnablePreprocessingTask<T> task, String type, int nThreads) {
		es = Executors.newFixedThreadPool(nThreads);

		this.task = task;
		this.nThreads = nThreads;
		this.type = type;
	}

	public void workThreads(List<MSKSpectrum> spectra) {
		// These are the data structures we store unprocessed and processed
		// spectra in.
		toProcess = new ArrayBlockingQueue<MSKSpectrum>(spectra.size(), false, spectra);

		processedMZ = new ConcurrentHashMap<>();
		processedIntensity = new ConcurrentHashMap<>();

		futures = new ArrayList<Future<?>>();
		for (int count = 0; count < nThreads; ++count) {
			futures.add(es.submit(task.getConcurrentInstance(this, type)));
		}

		try {
			for (Future<?> future : futures)
				future.get();
		} catch (Exception e) {
			System.out.println("wadu hek");
		}

		// Now all data has been processed.
	}

	/**
	 * Shutdown the threadPool es.
	 * 
	 * I DID NOT WRITE THIS METHOD : It is sourced form the JavaDoc on
	 * ExecutorService, it is linked here:
	 * 
	 * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
	 */
	public void shutdown() {
		es.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!es.awaitTermination(60, TimeUnit.SECONDS)) {
				es.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!es.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			es.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	// Some basic tests on this and the Normalizer preprocessing class.
	public static void main(String[] args) throws InterruptedException {
		List<MSKSpectrum> set = new ArrayList<>();
		set.add(new MSKSpectrum(new KeyLogic(1, 1), new double[] { 1, 1, 1 }));
		set.add(new MSKSpectrum(new KeyLogic(1, 1), new double[] { 1, 1, 1 }));
		set.add(new MSKSpectrum(new KeyLogic(1, 1), new double[] { 1, 1, 1 }));
		set.add(new MSKSpectrum(new KeyLogic(1, 1), new double[] { 1, 1, 1 }));
		set.add(new MSKSpectrum(new KeyLogic(1, 1), new double[] { 2, 2, 4 }));

		PreprocessingThreadPool<Normalizer> threadPool = new PreprocessingThreadPool<>(new Normalizer(), "TIC", 5);
		threadPool.workThreads(set);
	}

}
