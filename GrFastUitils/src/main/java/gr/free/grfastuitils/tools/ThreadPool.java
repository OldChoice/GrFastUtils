package gr.free.grfastuitils.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理类(指定同时运行线程数量),单列模式
 * 
 */
public class ThreadPool {
	/**
	 * 用于创建单列模式
	 */
	private static ThreadPool myThreadPool;
	/**
	 * 线程池
	 */
	private static ExecutorService pool;
	/**
	 * 线程池里面线程的数量
	 */
	private static int count=8;
	/**
	 * 构造方法
	 */
	private ThreadPool() {
		pool= Executors.newFixedThreadPool(count);
	}
	/**
	 * 获取线程池
	 * @return
	 */
	public static ExecutorService getInstance() {
		if (myThreadPool == null) {
			myThreadPool = new ThreadPool();
		}
		return pool;
	}

}
