package com.hriships.httpserver.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Hrishikesh
 *
 */

public abstract class HttpMessage implements Serializable, Http {

  private static final long serialVersionUID = 4781564906803660841L;

  private String requestMethod;
  private String protocol;
  private String requestURI;
  private transient Map<String, String> headers;
  private transient Map<String, String> queryParameters;
  private String messageBody;

  public String getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getRequestURI() {
    return requestURI;
  }

  public void setRequestURI(String requestURI) {
    this.requestURI = requestURI;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getQueryParameters() {
    return queryParameters;
  }

  public void setQueryParameters(Map<String, String> queryParameters) {
    this.queryParameters = queryParameters;
  }

  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }
}
