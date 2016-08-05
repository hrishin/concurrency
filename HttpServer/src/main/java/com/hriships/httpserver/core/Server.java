package com.hriships.httpserver.core;

/**
 * @author Hrishikesh
 *
 */
public interface Server {

  int MIN_PORT_NUMBER = 0;
  int MAX_PORT_NUMBER = 65536;
  int WELL_KNOWN_PORT_CAP = 1024;
  int STANDARD_HTTP_PORT = 80;
  int SSL_HTTP_PORT = 443;
  
  /**
   * Method to start the server
   */
  void start();

  /**
   * Method to stop the server
   */
  void stop();
}
