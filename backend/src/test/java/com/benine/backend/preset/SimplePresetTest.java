package com.benine.backend.preset;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.ServerController;

public class SimplePresetTest extends PresetTest {
  
  SimplePreset preset;
  
  @Before
  public void setup() {
    Set<String> keywords = new HashSet<>();
    keywords.add("foo");
    preset = new SimplePreset(1, keywords);
    preset.setName("name");
    preset.setId(1);
    preset.setImage("test");
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController.getInstance();
  }
  
  @Test
  public void testGetID() {
    Assert.assertEquals(1, preset.getId());
  }
  
  @Test
  public void testToJSON() {
    JSONObject jsonObject = preset.toJSON();
    Assert.assertEquals(1, jsonObject.get("id"));
    Assert.assertEquals("test", jsonObject.get("image"));
    JSONArray expectedtagsJSON = new JSONArray();
    expectedtagsJSON.add("foo");
    Assert.assertEquals(expectedtagsJSON, jsonObject.get("tags"));
  }

  @Override
  public Preset getPreset() {
    Preset preset = new SimplePreset(3);
    preset.setName("Name");
    return preset;
  }
}
