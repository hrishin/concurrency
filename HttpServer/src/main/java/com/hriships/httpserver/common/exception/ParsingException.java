package com.hriships.httpserver.common.exception;

import com.hriships.httpserver.model.HttpRequest;

/**
 * @author Hrishikesh
 *
 */

public class ParsingException extends HttpException {

  private static final long serialVersionUID = 7237714040302993547L;

  public ParsingException() {
    super();
  }

  public ParsingException(String message, HttpRequest request) {
    super(message, request);
  }

  public ParsingException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, HttpRequest request) {
    super(message, cause, enableSuppression, writableStackTrace, request);
  }

  public ParsingException(String message, Throwable cause, HttpRequest request) {
    super(message, cause, request);
  }

  public ParsingException(Throwable cause, HttpRequest request) {
    super(cause, request);
  }
}
