package com.hriships.httpserver.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hriships.httpserver.common.AppConstants;
import com.hriships.httpserver.common.AppConstants.MESSAGES;

/**
 * ServerThreadPoolExecutor extends An {@link ThreadPoolExecutor} to add custom behavior in task
 * execution. If no threads are available in thread pool, the accept thread should block until a
 * worker thread is available.
 * 
 * @author Hrishikesh
 *
 */
public class ServerThreadPoolExecutor extends ThreadPoolExecutor {

  private static final Logger LOGGER = Logger.getLogger(ThreadPoolExecutor.class.getName());

  private final Semaphore semaphore;

  /**
   * ServerThreadPoolExecutor constructor
   * 
   * @param corePoolSize the number of threads to keep in the pool, even if they are idle, unless
   *        {@code allowCoreThreadTimeOut} is set
   * @param maximumPoolSize the maximum number of threads to allow in the pool
   * @param keepAliveTime when the number of threads is greater than the core, this is the maximum
   *        time that excess idle threads will wait for new tasks before terminating.
   * @param unit the time unit for the {@code keepAliveTime} argument
   * @param workQueue the queue to use for holding tasks before they are executed. This queue will
   *        hold only the {@code Runnable} tasks submitted by the {@code execute} method.
   * @param semaphoreCount this is count for controlling number of parallel threads to run
   */
  public ServerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit, BlockingQueue<Runnable> workQueue, int semaphoreCount) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    semaphore = new Semaphore(semaphoreCount);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
   */
  @Override
  public void execute(final Runnable task) {

    acquireSemaphoreLock();

    try {
      super.execute(task);
    } catch (final RejectedExecutionException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL,
          MESSAGES.EXCP_AQUIRING_INTTERRUPTED.toString(), exception);
      semaphore.release();
      throw exception;
    }
  }

  // request the semaphore lock and execute the task
  private void acquireSemaphoreLock() {
    boolean semaphoreAcquired = false;
    do {
      try {
        semaphore.acquire();
        semaphoreAcquired = true;
      } catch (final InterruptedException exception) {
        LOGGER.log(Level.WARNING, MESSAGES.EXCP_AQUIRING_INTTERRUPTED.toString(), exception);
      }
    } while (!semaphoreAcquired);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
   * java.lang.Throwable)
   */
  @Override
  protected void afterExecute(Runnable task, Throwable throwable) {
    super.afterExecute(task, throwable);
    if (throwable != null) {
      LOGGER.log(Level.WARNING, MESSAGES.EXCP_AQUIRING_INTTERRUPTED.toString(), throwable);
    }
    semaphore.release();
  }
}
