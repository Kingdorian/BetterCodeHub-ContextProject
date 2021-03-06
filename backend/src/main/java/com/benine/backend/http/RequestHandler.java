package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 20-05-16.
 */
public abstract class RequestHandler extends AbstractHandler {
  
  private Logger logger;
  
  /**
   * Constructs a request handler.
   * @param httpserver to interact with the rest of the system.
   */
  public RequestHandler(HTTPServer httpserver) {
    this.logger = httpserver.getLogger();
  }

  /**
   * Responds to a request with status 200.
   * @param request the httpservletrequest to process.
   * @param response the httpservletresponse to respond to.
   * @param body a string with the response.
   */
  public void respond(Request request, HttpServletResponse response, String body) {
    try {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentLength(body.length());

      PrintWriter out = response.getWriter();
      out.write(body);
      out.close();

    } catch (IOException e) {
      getLogger().log("Error occured while writing the response to a request at URI"
              + request.getRequestURI(), LogEvent.Type.WARNING);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Responds a success true JSON.
   * @param request   The request to respond to.
   * @param response  The response
   * @param succes true if request is finished.
   */
  public void respond(Request request, HttpServletResponse response, Boolean succes) {
    if (succes) {
      respond(request, response, "{\"succes\":\"true\"}");
    } else {
      respond(request, response, "{\"succes\":\"false\"}");
    }
  
  }

  /**
   * Returns the ServerController logger.
   * @return  A Logger object.
   */
  protected Logger getLogger() {
    return logger;
  }
}
