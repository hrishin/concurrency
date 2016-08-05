package com.hriships.httpserver.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * App. level constants
 * 
 * METHODS_ALLOWED - Restricts the HTTP request methods by default on POST methods is allowed
 * 
 * @author Hrishikesh
 *
 */

public class AppConstants {

  
  public static enum CONFIG {
    DEFAULT_PORT(80), 
    THREAD_POOL_SIZE(5), 
    SEMAPHORE_COUNT(5), 
    MAX_THREAD_POOL_SIZE(20), 
    KEEP_ALIVE_TIME(10000), 
    BLOCKING_THREAD_QUE_SIZE(30), 
    METHODS_ALLOWED("POST"), 
    ENABLE_CHARCTER_DECODING(false);

    private final transient Object value;

    private CONFIG(final Object value) {
      this.value = value;
    }

    public int getValue() {
      try {
        return Integer.parseInt(value.toString());
      } catch (NumberFormatException exp) {
        return 0;
      }
    }

    public boolean getBoolean() {
      return Boolean.valueOf(value.toString());
    }

    public List<String> getCSVList() {
      String[] arraydata = value.toString().replaceAll("\\s+", "").split(",");
      return new ArrayList<String>(Arrays.asList(arraydata));
    }

    @Override
    public String toString() {
      return value.toString();
    }
  }

  public static final Level GLOBAL_LOG_INFO_LEVEL = Level.INFO;
  public static final Level GLOBAL_LOG_ERROR_LEVEL = Level.OFF;
  
  public static final String HEADER_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
  public static final String SERVER_NAME = "Microsoft";
  public static final String CRLF = "\r\n";

  public static enum MESSAGES {
    CONFIGURING_SERVER("Configuring server"), 
    STARTING_SERVER("Starting server"), 
    SERVER_STARTED("Server started at port number : "), 
    REQUEST_COUNT("**RequestCount : "), 
    STOPPING_SERVER("Stopping server"), 
    EXCP_CONFIG_FILE_NOT_FOUND("Configuration file not found"), 
    EXCP_AQUIRING_INTTERRUPTED("Aquiring semaphore intterrupted"), 
    EXCP_INVALID_REQUEST("Request is not valid"), 
    EXCP_BAD_REQUEST("Bad input or request"), 
    EXCP_METHOD_NOT_ALLOWED("Method not allowed"), 
    EXCP_PORT_INVALID("Invalid port number. Please use the wellknown http port numbers or keep it betwwen valid port number range");

    private final String message;

    private MESSAGES(final String message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return message;
    }
  }

  private AppConstants() {

  }
}
