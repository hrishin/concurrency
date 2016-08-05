package com.hriships.httpserver.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.hriships.httpserver.common.AppConstants;
import com.hriships.httpserver.common.Util;
import com.hriships.httpserver.common.AppConstants.CONFIG;
import com.hriships.httpserver.common.AppConstants.MESSAGES;
import com.hriships.httpserver.common.exception.HttpException;
import com.hriships.httpserver.common.exception.ParsingException;
import com.hriships.httpserver.model.Http;
import com.hriships.httpserver.model.HttpRequest;

/**
 * This thread process the HTTP request and return the HTTP response
 * 
 * @author Hrishikesh
 *
 */
public class WorkerThreadImpl implements WorkerThread {

  private static final Logger LOGGER = Logger.getLogger(WorkerThreadImpl.class.getName());

  private final Socket clientSocket;

  private final boolean enableCharacterDecoding;

  /**
   * @param clientSocket client connection
   * @param enableCharacterDecoding if true the content of request get decoded using URL decoder
   */
  public WorkerThreadImpl(Socket clientSocket, boolean enableCharacterDecoding) {
    this.clientSocket = clientSocket;
    this.enableCharacterDecoding = enableCharacterDecoding;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    BufferedReader requestStreamReader = null;
    BufferedWriter requestStreamWriter = null;
    
    try {
      requestStreamReader =
          new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
              StandardCharsets.UTF_8.name()));
      requestStreamWriter =
          new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
              StandardCharsets.UTF_8.name()));

      final HttpRequest httpRequest = parseRequest(requestStreamReader);

      filterRequest(httpRequest);

      writeResponse(requestStreamWriter, httpRequest);

      LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, Thread.currentThread().getName());

    } catch (final HttpException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
      handleHttpErrorResponse(requestStreamWriter, exception);
    } catch (final IOException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    } finally {
      cleanup(requestStreamReader, requestStreamWriter);
    }
  }

  @Override
  public HttpRequest parseRequest(BufferedReader requestStreamReader) throws ParsingException {

    final HttpRequest httpRequest = new HttpRequest();

    try {
      parseRequestLine(requestStreamReader, httpRequest);

      parseRequestHeaders(requestStreamReader, httpRequest);

      parseMessageBody(requestStreamReader, httpRequest);

    } catch (IOException | NullPointerException | IllegalArgumentException exception) {
      httpRequest.setStatusCode(Http.HTTP_400);
      throw new ParsingException(MESSAGES.EXCP_INVALID_REQUEST.toString(), exception, httpRequest);
    }

    return httpRequest;
  }

  private void parseRequestLine(BufferedReader requestStreamReader, final HttpRequest httpRequest)
      throws IOException {
    String requestLine = requestStreamReader.readLine();
    if (requestLine == null || requestLine.split(" ", 3).length != 3) {
      throw new IOException(MESSAGES.EXCP_BAD_REQUEST.toString());
    }

    String[] readLineData = requestLine.split(" ", 3);
    httpRequest.setRequestMethod(readLineData[0]);
    httpRequest.setRequestURI(readLineData[1]);
    httpRequest.setProtocol(readLineData[2]);

    parseQueryString(httpRequest);
  }

  private void parseQueryString(final HttpRequest httpRequest) throws UnsupportedEncodingException {
    if (httpRequest.getRequestURI() != null && httpRequest.getRequestURI().contains("?")) {
      String url = URLDecoder.decode(httpRequest.getRequestURI(), StandardCharsets.UTF_8.name());
      String[] urlData = url.split("\\?", 2);
      if (urlData[1].isEmpty()) {
        return;
      }

      Map<String, String> queryParams = new HashMap<String, String>();
      urlData = urlData[1].split("&");

      if (urlData.length == 0) {
        return;
      }

      for (String queryString : urlData) {
        String[] keyValue = queryString.split("=");
        queryParams.put(keyValue[0], keyValue[1]);
      }

      httpRequest.setQueryParameters(queryParams);
    }
  }

  private void parseRequestHeaders(BufferedReader requestStreamReader, final HttpRequest httpRequest)
      throws IOException {
    final Map<String, String> httpRequestMap = new LinkedHashMap<String, String>();
    while (true) {
      String requestHeaderProperty = requestStreamReader.readLine();

      if (requestHeaderProperty == null || requestHeaderProperty.isEmpty()) {
        break;
      }

      String[] headerKeyValue = requestHeaderProperty.split(" ", 2);
      if (headerKeyValue.length == 2) {
        httpRequestMap.put(headerKeyValue[0].replace(":", ""), headerKeyValue[1].trim());
      }
    }
    httpRequest.setHeaders(httpRequestMap);
  }

  private void parseMessageBody(BufferedReader requestStreamReader, final HttpRequest httpRequest)
      throws IOException {
    Map<String, String> httpRequestMap = httpRequest.getHeaders();
    String contentLengthProperty = httpRequestMap.get(Http.CONTENT_LENGTH_HEADER);
    int contentLength = contentLengthProperty != null ? Integer.parseInt(contentLengthProperty) : 0;
    char[] requestBodyBuffer = new char[contentLength];
    int charactersRead = requestStreamReader.read(requestBodyBuffer);
    LOGGER.log(AppConstants.GLOBAL_LOG_INFO_LEVEL, "Data read: " + charactersRead);
    String requestBody =
        enableCharacterDecoding ? URLDecoder.decode(new String(requestBodyBuffer),
            StandardCharsets.UTF_8.name()) : new String(requestBodyBuffer);
    httpRequest.setMessageBody(requestBody);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.hriships.httpserver.core.WorkerThread#filterRequest(com.hriships.httpserver.model.HttpRequest
   * )
   */
  @Override
  public void filterRequest(final HttpRequest httpRequest) throws HttpException {
    String requestMethod = httpRequest.getRequestMethod();

    List<String> methodsAllowed = CONFIG.METHODS_ALLOWED.getCSVList();

    if (!methodsAllowed.contains(requestMethod)) {
      httpRequest.setStatusCode(Http.HTTP_405);
      throw new HttpException(MESSAGES.EXCP_METHOD_NOT_ALLOWED.toString(), httpRequest);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hriships.httpserver.core.WorkerThread#writeResponse(java.io.BufferedWriter,
   * com.hriships.httpserver.model.HttpRequest)
   */
  @Override
  public void writeResponse(BufferedWriter requestStreamWriter, final HttpRequest httpRequest)
      throws IOException {
    
    Map<String, String> httpRequestHeaders = httpRequest.getHeaders();
    requestStreamWriter.write(httpRequest.getProtocol() + " " + httpRequest.getStatusCode()
        + AppConstants.CRLF);
    requestStreamWriter.write("Date: " + getServerTime() + AppConstants.CRLF);
    requestStreamWriter.write("Server: " + AppConstants.SERVER_NAME + AppConstants.CRLF);
    String contentTYpe =
        httpRequestHeaders.get("Content-Type") == null ? "text/plain" : httpRequestHeaders
            .get("Content-Type");
    requestStreamWriter.write("Content-Type: " + contentTYpe + AppConstants.CRLF);
    if (!"GET".equalsIgnoreCase(httpRequest.getRequestMethod())) {
      requestStreamWriter.write("Content-Length: " + httpRequestHeaders.get("Content-Length")
          + AppConstants.CRLF);
    }
    requestStreamWriter.write(AppConstants.CRLF);
    requestStreamWriter.write(httpRequest.getMessageBody());
    requestStreamWriter.write(AppConstants.CRLF);
    requestStreamWriter.flush();
  }

  private void handleHttpErrorResponse(BufferedWriter requestStreamWriter,
      final HttpException exception) {
    try {
      HttpRequest request = exception.getRequest();
      request.setMessageBody(exception.getMessage());
      if (!Http.GET_METHOD.equalsIgnoreCase(request.getRequestMethod())) {
        request.getHeaders().put(Http.CONTENT_LENGTH_HEADER,
            String.valueOf(exception.getMessage().length()));
      }
      writeResponse(requestStreamWriter, exception.getRequest());
    } catch (IOException | NullPointerException e) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), e);
    }
  }

  // clean up all resources
  private void cleanup(BufferedReader requestStreamReader, BufferedWriter requestStreamWriter) {
    try {
      if (clientSocket != null) {
        clientSocket.close();
      }
      if (requestStreamReader != null) {
        requestStreamReader.close();
      }
      if (requestStreamWriter != null) {
        requestStreamWriter.close();
      }
    } catch (IOException exception) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, exception.getMessage(), exception);
    }
  }

  private String prepareHttpHeader(String headerName, Object headerValue, String joinString) {
    if (headerName == null || headerValue == null || joinString == null) {
      return null;
    }

    return headerName.trim() + joinString + String.valueOf(headerValue).trim() + AppConstants.CRLF;
  }

  public static String getServerTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.HEADER_DATE_FORMAT, Locale.US);
    Date currentDate = new Date();
    return dateFormat.format(currentDate);
  }

  public String prepareHttpHeader(String headerName, Object headerValue) {
    if (headerName == null || headerValue == null) {
      return null;
    }

    return headerName.trim() + ": " + String.valueOf(headerValue).trim() + AppConstants.CRLF;
  }
}
