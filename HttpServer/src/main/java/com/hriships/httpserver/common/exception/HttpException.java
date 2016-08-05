package com.hriships.httpserver.common.exception;

import com.hriships.httpserver.model.HttpRequest;

/**
 * @author Hrishikesh
 *
 */

public class HttpException extends Exception {

  private static final long serialVersionUID = 3272302624385454655L;
  private final HttpRequest request;

  public HttpException() {
    super();
    request = null;
  }

  public HttpException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, HttpRequest request) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.request = request;
  }

  public HttpException(String message, Throwable cause, HttpRequest request) {
    super(message, cause);
    this.request = request;
  }

  public HttpException(String message, HttpRequest request) {
    super(message);
    this.request = request;
  }

  public HttpException(Throwable cause, HttpRequest request) {
    super(cause);
    this.request = request;
  }

  public HttpRequest getRequest() {
    return request;
  }
}
