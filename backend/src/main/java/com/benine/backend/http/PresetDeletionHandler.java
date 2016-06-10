package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that handles editing presets.
 */
public class PresetDeletionHandler extends RequestHandler {

  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   * @param httpserver this handler is for.
   */
  public PresetDeletionHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    try {
      int presetID = Integer.parseInt(request.getParameter("id"));
      Preset preset = getPresetController().getPresetById(presetID);

      if (preset != null) {
        getPresetController().removePreset(preset);
        respondSuccess(request, res);
      } else {
        respondFailure(request, res);
      }

    } catch (NumberFormatException e) {
      getLogger().log("Invalid parameter input", LogEvent.Type.INFO);
    } catch (SQLException e) {
      getLogger().log("An SQL Exception occured", LogEvent.Type.INFO);
    }

    respondFailure(request, res);
    request.setHandled(true);
  }
}