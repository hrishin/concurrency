package com.hriships.httpserver.model;


/**
 * @author Hrishikesh
 *
 */
public class HttpRequest extends HttpMessage {

  private static final long serialVersionUID = -6943392345559056311L;

  private String statusCode;

  public HttpRequest() {
    super();
    this.statusCode = Http.HTTP_200;
  }

  public HttpRequest(String statusCode) {
    super();
    this.statusCode = statusCode;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }
}
