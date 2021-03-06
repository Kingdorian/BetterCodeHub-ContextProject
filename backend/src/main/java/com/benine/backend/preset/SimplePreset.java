package com.benine.backend.preset;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Set;

/**
 * A simple preset which just contains an image and tags.
 */
public class SimplePreset extends Preset {

  /**
   * Create a simple preset
   * @param cameraId of the camera this preset belongs to.
   */
  public SimplePreset(int cameraId) {
    super(cameraId);
  }
  
  /**
   * Create a simple preset
   * @param cameraId of the camera this preset belongs to.
   * @param tags of this preset
   */
  public SimplePreset(int cameraId, Set<String> tags) {
    super(cameraId, tags);
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("image", getImage());
    json.put("id", presetid);
    json.put("cameraid", cameraId);
    json.put("name", name);
    JSONArray tagsJSON = new JSONArray();
    for (String tag : tags) {
      tagsJSON.add(tag);
    }
    json.put("tags", tagsJSON);

    return json;
  }

  @Override
  public void excecutePreset(CameraController cameraController) throws CameraConnectionException {}
}
