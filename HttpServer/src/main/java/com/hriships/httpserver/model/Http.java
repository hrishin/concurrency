package com.hriships.httpserver.model;

/**
 * @author Hrishikesh
 *
 */

public interface Http {
  String PROTOCOL = "HTTP/1.1";

  String HTTP_200 = "200 OK";
  String HTTP_400 = "400 Bad Request";
  String HTTP_405 = "405 Method Not Allowed";

  String GET_METHOD = "GET";
  String POST_METHOD = "POST";
  String PUT_METHOD = "PUT";
  String DELETE_METHOD = "DELETE";

  String CONTENT_TYPE_HEADER = "Content-Type";
  String SERVER_HEADER = "Server";
  String DATE_HEADER = "Date";
  String TEXT_PLAIN_MIME = "text/plain";
  String CONTENT_LENGTH_HEADER = "Content-Length";

}
