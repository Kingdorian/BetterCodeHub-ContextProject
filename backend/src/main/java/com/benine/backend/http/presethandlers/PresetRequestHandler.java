package com.benine.backend.http.presethandlers;

import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.PresetCamera;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.RequestHandler;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;

/**
 * Handles the request concerning the presets.
 */
public abstract class PresetRequestHandler extends RequestHandler {

  /**
   * PresetRequesthandler, for request with presets.
   * @param httpserver to create the handler for.
   */
  public PresetRequestHandler(HTTPServer httpserver) {
    super(httpserver);
  }
  
  /**
   * Returns the presetcamera with id, if it's not an preset camera returns null.
   * @param camID of the camera to get.
   * @return presetcamera.
   */
  protected PresetCamera getPresetCamera(String camID) {
    if (camID == null) {
      return null;
    }
    int id = Integer.parseInt(camID);
    Camera camera = getCameraController().getCameraById(id);
    if (camera instanceof PresetCamera) {
      return (PresetCamera) camera;
    }
    return null;
  }
  
  /**
   * Get the right prest from the preset controller
   * @param presetID of the preset to retrieve
   * @return preset with the right id.
   */
  protected Preset getPreset(int presetID) {
    return getPresetController().getPresetById(presetID);
  }
  
  @Override
  protected Logger getLogger() {
    return super.getLogger();
  }
  
  @Override
  protected PresetController getPresetController() {
    return super.getPresetController();
  }

}