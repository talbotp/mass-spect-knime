package msk.util.preprocess;

public abstract class RunnablePreprocessingTask<T extends Runnable> implements Runnable {

	// Thread Pool is public for easy access.
	public PreprocessingThreadPool<T> threadPool;
	
	public RunnablePreprocessingTask(PreprocessingThreadPool<T> threadPool){ 
		this.threadPool = threadPool;
	}
	
	public RunnablePreprocessingTask() {
		// IMPORTANT : DO NOTHING 
		
		// ADDING CODE HERE WILL EFFECT NON CONCURRENT PREPROCESSING. BAD IDEA.
	}
	
	public abstract void process();
	
	public abstract T getConcurrentInstance(PreprocessingThreadPool<T> threadPool, String type);
	
	@Override
	public void run() {
		process();
	}

}
