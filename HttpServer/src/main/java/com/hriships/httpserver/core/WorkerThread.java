package com.hriships.httpserver.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.hriships.httpserver.common.exception.HttpException;
import com.hriships.httpserver.common.exception.ParsingException;
import com.hriships.httpserver.model.HttpRequest;

/**
 * @author Hrishikesh
 *
 */
public interface WorkerThread extends Runnable {

  /**
   * Parse the content of stream and prepares the {@link HttpRequest} object
   * 
   * @param requestStreamReader
   * @return
   * @throws ParsingException
   */
  HttpRequest parseRequest(BufferedReader requestStreamReader) throws ParsingException;

  /**
   * Apply some filter policies and throws an exception
   * 
   * @param httprequest
   * @throws HttpException
   */
  void filterRequest(HttpRequest httprequest) throws HttpException;

  /**
   * Return the response for give HTTP request
   * 
   * @param requestStreamWriter
   * @param httprequest
   * @throws IOException
   */
  void writeResponse(BufferedWriter requestStreamWriter, HttpRequest httprequest) throws IOException;

}
