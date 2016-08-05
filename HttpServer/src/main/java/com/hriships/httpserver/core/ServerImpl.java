package com.hriships.httpserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.hriships.httpserver.common.AppConstants;
import com.hriships.httpserver.common.AppConstants.CONFIG;
import com.hriships.httpserver.common.AppConstants.MESSAGES;

/**
 * @author Hrishikesh
 *
 */
public class ServerImpl implements Server {

  private static final Logger LOGGER = Logger.getLogger(ServerImpl.class.getName());

  private ServerSocket serverSocket;
  private ExecutorService threadExecutor;
  private int requestCount;
  private final int port;
  private final boolean enableCharacterDecoding;

  /**
   * ServerImpl constructor
   */
  public ServerImpl() {
    port = CONFIG.DEFAULT_PORT.getValue();
    enableCharacterDecoding = CONFIG.ENABLE_CHARCTER_DECODING.getBoolean();
  }

  /**
   * ServerImpl constructor
   * 
   * @param port port number for server
   */
  public ServerImpl(final int port) {
    this.port = port;
    enableCharacterDecoding = CONFIG.ENABLE_CHARCTER_DECODING.getBoolean();
  }

  /**
   * ServerImpl constructor
   * 
   * @param enableCharacterDecoding if true the content of request get decoded using URL
   *        decoder
   */
  public ServerImpl(final boolean enableCharacterDecoding) {
    port = CONFIG.DEFAULT_PORT.getValue();
    this.enableCharacterDecoding = enableCharacterDecoding;
  }

  /**
   * ServerImpl constructor port and enableCharacterDecoding input
   * 
   * @param port a port number for server
   * @param enableCharacterDecoding if true the content of request get decoded using URL
   *        decoder
   */
  public ServerImpl(final int port, final boolean enableCharacterDecoding) {
    this.port = port;
    this.enableCharacterDecoding = enableCharacterDecoding;
  }

  /**
   * This is the implementation to start the sever. In this case server read the configurations,
   * bind the socket on given port and apply some thread pool policies. On bind success server start
   * long event loop where it listen and accepts new client connection.
   */
  @Override
  public void start() {

    try {
      LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, MESSAGES.CONFIGURING_SERVER.toString());
      attachShutdownHandler();
      LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, MESSAGES.STARTING_SERVER.toString());
      serverSocket = new ServerSocket(port);
      LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, MESSAGES.SERVER_STARTED.toString() + port);

      BlockingQueue<Runnable> blockingQueue =
          new ArrayBlockingQueue<Runnable>(CONFIG.BLOCKING_THREAD_QUE_SIZE.getValue());
      
      //Custom thread pool executor to enable request throttling mechanism
      threadExecutor = new ServerThreadPoolExecutor(CONFIG.THREAD_POOL_SIZE.getValue(), CONFIG.MAX_THREAD_POOL_SIZE.getValue(), 
    		  										CONFIG.KEEP_ALIVE_TIME.getValue(), TimeUnit.MILLISECONDS, blockingQueue, CONFIG.SEMAPHORE_COUNT.getValue());

      while (true) {
        acceptAndProcessRequest();
      }

    } catch (final IOException | SecurityException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    } finally {
      stop();
    }
  }

  // Accept connection and spawn it in new thread
  private void acceptAndProcessRequest() {
    WorkerThread requestWorker;

    try {
      requestWorker = new WorkerThreadImpl(serverSocket.accept(), enableCharacterDecoding);
      threadExecutor.submit(requestWorker);
      requestCount++;
      LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, MESSAGES.REQUEST_COUNT.toString()
          + requestCount);
    } catch (final RejectedExecutionException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    } catch (final IOException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    }
  }

  // Terminate server before program termination
  private void attachShutdownHandler() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        ServerImpl.this.stop();
      }
    });
  }

  /**
   * This is the implementation to stop the sever. Checking null references and closing socket,
   * thread pool.
   */
  @Override
  public void stop() {
    LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, MESSAGES.STOPPING_SERVER.toString());

    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
      if (threadExecutor != null) {
        threadExecutor.shutdown();
      }
    } catch (IOException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    }
  }

}
