package com.hriships.httpserver;

import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hriships.httpserver.common.AppConstants;
import com.hriships.httpserver.common.Util;
import com.hriships.httpserver.common.AppConstants.CONFIG;
import com.hriships.httpserver.common.exception.HttpException;
import com.hriships.httpserver.common.exception.ParsingException;
import com.hriships.httpserver.core.WorkerThread;
import com.hriships.httpserver.core.WorkerThreadImpl;
import com.hriships.httpserver.model.Http;
import com.hriships.httpserver.model.HttpRequest;

public class AppTest {

  private static final Logger LOGGER = Logger.getLogger(AppTest.class.getName());

  @Mock
  Socket clientSocket;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testPositiveParsePostRequest() throws ParsingException {

    try {
      String postMessage = "Hello Server";

      StringBuffer requestInput = new StringBuffer();
      requestInput.append(Util.prepareHttpHeader(Http.POST_METHOD, Http.PROTOCOL, " / "));
      requestInput.append(Util.prepareHttpHeader(Http.CONTENT_TYPE_HEADER, Http.TEXT_PLAIN_MIME));
      requestInput.append(Util.prepareHttpHeader(Http.CONTENT_LENGTH_HEADER, postMessage.length()));
      requestInput.append(AppConstants.CRLF);
      requestInput.append(postMessage);
      requestInput.append(AppConstants.CRLF);

      HttpRequest request = parseRequest(requestInput.toString());

      boolean result =
          request.getHeaders().get(Http.CONTENT_TYPE_HEADER).equalsIgnoreCase(Http.TEXT_PLAIN_MIME)
              && request.getMessageBody().equalsIgnoreCase(postMessage);
      Assert.assertEquals(true, result);

    } catch (IOException e) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, e.getMessage(), e);
    }
  }

  @Test(expected = ParsingException.class)
  public void testNegativeParseRequest() throws ParsingException {
    try {
      parseRequest("");
    } catch (IOException e) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, e.getMessage(), e);
    }
  }

  @Test(expected = ParsingException.class)
  public void testNegativeParseRequestLine() throws ParsingException {
    try {
      parseRequest(Http.POST_METHOD + " /" + AppConstants.CRLF);
    } catch (IOException e) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, e.getMessage(), e);
    }
  }

  private HttpRequest parseRequest(String requestInput) throws IOException, ParsingException {
    final ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(requestInput.getBytes(StandardCharsets.UTF_8.name()));
    when(clientSocket.getInputStream()).thenReturn(byteArrayInputStream);

    WorkerThread worker =
        new WorkerThreadImpl(clientSocket, CONFIG.ENABLE_CHARCTER_DECODING.getBoolean());
    BufferedReader requestStreamReader =
        new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),
            StandardCharsets.UTF_8.name()));
    return worker.parseRequest(requestStreamReader);
  }

  @Test
  public void testPositiveFilter() throws HttpException {

    String message = "Hello Server";
    HttpRequest request = new HttpRequest(Http.HTTP_200);
    request.setRequestMethod(Http.POST_METHOD);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put(Http.CONTENT_LENGTH_HEADER, "" + message.length());
    request.setHeaders(headers);
    request.setMessageBody(message);

    WorkerThread worker =
        new WorkerThreadImpl(clientSocket, CONFIG.ENABLE_CHARCTER_DECODING.getBoolean());
    worker.filterRequest(request);
  }

  @Test(expected = HttpException.class)
  public void testNegativeFilter() throws HttpException {

    HttpRequest request = new HttpRequest(Http.HTTP_200);
    request.setRequestMethod(Http.DELETE_METHOD);

    WorkerThread worker =
        new WorkerThreadImpl(clientSocket, CONFIG.ENABLE_CHARCTER_DECODING.getBoolean());
    worker.filterRequest(request);
  }

  @Test
  public void testPositivewriteResponse() throws ParsingException {

    try {
      String postMessage = "Hello Server";

      StringBuffer requestInput = new StringBuffer();
      requestInput.append(Util.prepareHttpHeader(Http.POST_METHOD, Http.PROTOCOL, " / "));
      requestInput.append(Util.prepareHttpHeader(Http.CONTENT_TYPE_HEADER, Http.TEXT_PLAIN_MIME));
      requestInput.append(Util.prepareHttpHeader(Http.CONTENT_LENGTH_HEADER, postMessage.length()));
      requestInput.append(AppConstants.CRLF);
      requestInput.append(postMessage);
      requestInput.append(AppConstants.CRLF);

      HttpRequest request = parseRequest(requestInput.toString());

      writeResponse(request);

    } catch (IOException e) {
      LOGGER.log(AppConstants.GLOBAL_LOG_ERROR_LEVEL, e.getMessage(), e);
    }
  }

  private void writeResponse(HttpRequest request) throws IOException {

    when(clientSocket.getOutputStream()).thenReturn(System.out);

    WorkerThread worker =
        new WorkerThreadImpl(clientSocket, CONFIG.ENABLE_CHARCTER_DECODING.getBoolean());
    BufferedWriter requestStreamWriter =
        new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),
            StandardCharsets.UTF_8.name()));
    worker.writeResponse(requestStreamWriter, request);
  }
}
