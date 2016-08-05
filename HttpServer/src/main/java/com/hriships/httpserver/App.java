package com.hriships.httpserver;

import java.util.logging.Logger;

import com.hriships.httpserver.common.AppConstants;
import com.hriships.httpserver.common.AppConstants.CONFIG;
import com.hriships.httpserver.common.AppConstants.MESSAGES;
import com.hriships.httpserver.core.Server;
import com.hriships.httpserver.core.ServerImpl;

/**
 * Application executor class
 * 
 * @author Hrishikesh
 *
 */

public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());

  private App() {

  }

  public static void main(String[] args) {

    // Get port number from command line
    final int portNumber = readPortNumber(args);

    // Get URL decoding option
    final boolean decodeURLEncodedData =
        args.length == 2 ? Boolean.valueOf(args[1]) : CONFIG.ENABLE_CHARCTER_DECODING.getBoolean();

    // Initiate and start the server
    Server webServerver =
        portNumber != 0 ? new ServerImpl(portNumber, decodeURLEncodedData) : new ServerImpl(
            decodeURLEncodedData);
    webServerver.start();
  }

  private static int readPortNumber(String[] args) {
    int portNumber = 0;

    if (args.length > 0) {
      try {
        portNumber = Integer.parseInt(args[0].trim());
        if (isInvalidPortRange(portNumber) || isReservedPort(portNumber)) {
          throw new IllegalArgumentException(MESSAGES.EXCP_PORT_INVALID.toString());
        }
      } catch (final IllegalArgumentException exception) {
        portNumber = 0;
        LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
      }
    }

    return portNumber;
  }

  private static boolean isInvalidPortRange(int portNumber) {
    return portNumber < Server.MIN_PORT_NUMBER || portNumber > Server.MAX_PORT_NUMBER;
  }

  private static boolean isReservedPort(int portNumber) {
    return portNumber < Server.WELL_KNOWN_PORT_CAP && portNumber != Server.STANDARD_HTTP_PORT
        && portNumber != Server.SSL_HTTP_PORT;
  }
}
