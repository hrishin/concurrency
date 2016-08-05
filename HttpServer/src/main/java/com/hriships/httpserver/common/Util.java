package com.hriships.httpserver.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author Hrishikesh
 *
 */

public class Util {

  private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

  private Util() {

  }

  public static void sleepFor(final int miliSeconds) {
    try {
      Thread.sleep(miliSeconds);
    } catch (InterruptedException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    }
  }

  public static String getServerTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.HEADER_DATE_FORMAT, Locale.US);
    Date currentDate = new Date();
    return dateFormat.format(currentDate);
  }

  public static String prepareHttpHeader(String headerName, Object headerValue) {
    if (headerName == null || headerValue == null) {
      return null;
    }

    return headerName.trim() + ": " + String.valueOf(headerValue).trim() + AppConstants.CRLF;
  }

  public static String prepareHttpHeader(String headerName, Object headerValue, String joinString) {
    if (headerName == null || headerValue == null || joinString == null) {
      return null;
    }

    return headerName.trim() + joinString + String.valueOf(headerValue).trim() + AppConstants.CRLF;
  }

}
