package com.benine.backend.http.camerahandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.MJPEGHeader;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.ResizableStreamDistributer;
import com.benine.backend.video.StreamDistributer;
import com.benine.backend.video.StreamReader;
import com.benine.backend.video.StreamType;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.io.PipedInputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class responsible for handling the /camera/ route.
 */
public class CameraStreamHandler extends CameraRequestHandler {
  
  /**
   * Constructs the handler for the streams /camera/id/mjpeg.
   * @param httpserver to construct this handler for.
   */
  public CameraStreamHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    int camID = getCameraId(request);
    String width = request.getParameter("width");
    String height = request.getParameter("height");

    StreamReader streamReader = getStreamReader(camID);

    request.setHandled(true);

    // We need an MJPEG streamreader to stream MJPEG.
    if (streamReader instanceof MJPEGStreamReader) {
      MJPEGStreamReader streamReaderMJPEG = (MJPEGStreamReader) streamReader;
      StreamDistributer distributer = selectDistributer(streamReader, width, height);

      // Set the headers
      setHeaders(streamReaderMJPEG, res);

      // Stream to the client
      res.setStatus(HttpServletResponse.SC_OK);
      stream(request, res, distributer);

    } else {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Select a stream distributer based on if
   *
   * @param reader  The streamreader.
   * @param width   The width of the image.
   * @param height  The height of the image.
   * @return  A ResizableStreamDistributer if valid width and height, else a StreamDistributer.
   */
  private StreamDistributer selectDistributer(StreamReader reader, String width, String height) {
    if (isStreamCompression() && validateResizeArguments(width, height)) {
      int w = Integer.parseInt(width);
      int h = Integer.parseInt(height);

      return new ResizableStreamDistributer(reader, w, h);
    } else {
      return new StreamDistributer(reader);
    }

  }

  /**
   * Validates the resize arguments.
   * @param width   Width argument.
   * @param height  Height argument.
   * @return True if valid, false otherwise.
   */
  private boolean validateResizeArguments(String width, String height) {
    try {
      Integer.parseInt(width);
      Integer.parseInt(height);

      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Sets the HTTP Headers so the browser detects MJPEG.
   *
   * @param reader              The MJPEG stream reader containing the boundary.
   * @param httpServletResponse The response for which the headers should be set.
   */
  private void setHeaders(MJPEGStreamReader reader, HttpServletResponse httpServletResponse) {
    httpServletResponse.setContentType(MJPEGHeader.CONTENT_TYPE.getContents()
            + reader.getBoundary());
    httpServletResponse.setHeader("Cache-Control", MJPEGHeader.CACHE_CONTROL.getContents());
    httpServletResponse.setHeader("Connection", MJPEGHeader.CONNECTION.getContents());
    httpServletResponse.setHeader("Pragma", MJPEGHeader.PRAGMA.getContents());
    httpServletResponse.setHeader("Expires", MJPEGHeader.EXPIRES.getContents());
    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  boolean isAllowed(Camera cam) {
    return cam.getStreamType() == StreamType.MJPEG;
  }

  /**
   * Streams to the response.
   * @param request       The request object.
   * @param res           The response to write to.
   * @param distributer   The streamdistributer delivering the stream.
   */
  private void stream(Request request, HttpServletResponse res, StreamDistributer distributer) {
    int camID = getCameraId(request);

    byte[] bytes = new byte[8192];
    int bytesRead;

    try (PipedInputStream in = new PipedInputStream(distributer.getStream());
         ServletOutputStream os = res.getOutputStream()) {

      while ((bytesRead = in.read(bytes)) != -1) {
        os.write(bytes, 0, bytesRead);
        os.flush();
      }
    } catch (IOException e) {
      getLogger().log("Client "
              + request.getRemoteAddr()
              + " disconnected from MJPEG stream "
              + camID, LogEvent.Type.INFO);
    }
  }
}
