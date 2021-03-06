package com.benine.backend.preset;

import com.benine.backend.camera.*;
import com.benine.backend.camera.ipcameracontrol.FocusValue;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.camera.ipcameracontrol.IrisValue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 3-5-16.
 */
public class IPCameraPresetTest extends PresetTest {
  
  IPCameraPreset preset;
  CameraController cameraController;

  public IPCameraPreset getPreset() {
    IPCameraPreset preset = new IPCameraPreset(new ZoomPosition(4.2, 42.42, 4), new FocusValue(2, false), new IrisValue(5, true), 34);
    preset.setName("name");
    return preset;
  }
  
  @Before
  public void setup() {
    Set<String> keywords = new HashSet<>();
    keywords.add("foo");
    preset = new IPCameraPreset(new ZoomPosition(10, 12, 13), new FocusValue(40, true), new IrisValue(56, false), 0);
    preset.setName("name");
    preset.addTags(keywords);
    cameraController = mock(CameraController.class);
  }
  
  @Test
  public void testExcecutePresetMoveTo() throws CameraConnectionException, CameraBusyException {
    IPCamera camera = mock(IPCamera.class);
    when(cameraController.getCameraById(0)).thenReturn(camera);
    preset.excecutePreset(cameraController);
    verify(camera).moveTo(new Position(10, 12), 15, 1);
  }
  
  @Test(expected = CameraConnectionException.class)
  public void testExcecutePresetException() throws CameraConnectionException, CameraBusyException {
    SimpleCamera camera = mock(SimpleCamera.class);
    when(cameraController.getCameraById(0)).thenReturn(camera);
    preset.excecutePreset(cameraController);
  }

  @Test
  public void testToJSON() {
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("foo");
    IPCameraPreset preset = getPreset();
    preset.addTags(new HashSet<>(keywords));

    JSONArray tagsArray = new JSONArray();
    keywords.forEach(k -> tagsArray.add(k));

    JSONObject jsonObject = preset.toJSON();

    Assert.assertEquals(preset.getPosition().getPan(), jsonObject.get("pan"));
    Assert.assertEquals(preset.getPosition().getTilt(), jsonObject.get("tilt"));
    Assert.assertEquals(preset.getPosition().getZoom(), jsonObject.get("zoom"));
    Assert.assertEquals(preset.getFocus().getFocus(), jsonObject.get("focus"));
    Assert.assertEquals(preset.getIris().getIris(), jsonObject.get("iris"));
    Assert.assertEquals(preset.getFocus().isAutofocus(), jsonObject.get("autofocus"));
    Assert.assertEquals(preset.getPanspeed(), jsonObject.get("panspeed"));
    Assert.assertEquals(preset.getTiltspeed(), jsonObject.get("tiltspeed"));
    Assert.assertEquals(preset.getIris().isAutoiris(), jsonObject.get("autoiris"));
    Assert.assertEquals(tagsArray, jsonObject.get("tags"));
  }


  @Test
  public void testSetPosition() {
    IPCameraPreset preset = getPreset();
    preset.setPosition(new ZoomPosition(1, 2, 3));
    ZoomPosition expected = new ZoomPosition(1, 2, 3);
    Assert.assertEquals(expected, preset.getPosition());
  }
  
  @Test
  public void testSetPanSpeed() {
    IPCameraPreset preset = getPreset();
    preset.setPanspeed(20);
    Assert.assertEquals(20, preset.getPanspeed());
  }
  
  @Test
  public void testSetTiltSpeed() {
    IPCameraPreset preset = getPreset();
    preset.setTiltspeed(20);
    Assert.assertEquals(20, preset.getTiltspeed());
  }
  
  @Test
  public void testSetIris() {
    IPCameraPreset preset = getPreset();
    IrisValue iris = new IrisValue(60, true);
    preset.setIris(iris);
    Assert.assertEquals(iris, preset.getIris());
  }
  
  @Test
  public void testSetFocus() {
    IPCameraPreset preset = getPreset();
    FocusValue focus = mock(FocusValue.class);
    preset.setFocus(focus);
    Assert.assertEquals(focus, preset.getFocus());
  }
  
  @Test
  public void testHashCode() {
    Preset preset1 = getPreset();
    Preset preset2 = getPreset();
    Assert.assertEquals(preset1.hashCode(), preset2.hashCode());
  }

  
  @Test
  public void testSetId() {
    preset.setId(1);
    Assert.assertEquals(1, preset.getId());
  }

  @Test
  public void testHashCodefalse() {
    Set<String> keywords = new HashSet<>();
    keywords.add("foo");
    Preset preset2 = new IPCameraPreset(new ZoomPosition(10, 12, 13), new FocusValue(40, true),new IrisValue(56, false), 0);
    preset2.setName("name");
    Assert.assertNotEquals(preset.hashCode(), preset2.hashCode());
  }



  @Test
  public void testHashCodetrue() {
    Preset preset1 = getPreset();
    Preset preset2 = getPreset();
    Assert.assertEquals(preset1.hashCode(), preset2.hashCode());
  }
  
  @Test
  public void testEqualsSameObject() {
    Assert.assertEquals(preset, preset);
  }
  
  @Test
  public void testEqualsNull() {
    Assert.assertNotEquals(preset, null);
  }
  
  @Test
  public void testEqualsOtherObject() {
    Assert.assertNotEquals(preset, 1);
  }
  
  @Test
  public void testEqualsOtherPresetID() {
    IPCameraPreset preset2 = getPreset();
    preset2.setId(5);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPosition() {
    IPCameraPreset preset2 = getPreset();
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherZoom() {
    IPCameraPreset preset2 = getPreset();
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherFocus() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setFocus(new FocusValue(12, true));
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherIris() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    IrisValue iris = new IrisValue(15, false);
    preset2.setIris(iris);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPanspeed() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setPanspeed(preset.getPanspeed()+1);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherTiltspeed() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setTiltspeed(preset.getTiltspeed()+1);
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testEqualsDifferentCameraId() {
    IPCameraPreset preset = new IPCameraPreset(new ZoomPosition(0, 0, 0), new FocusValue(0, false), new IrisValue(0, false), 0);
    IPCameraPreset preset2 = new IPCameraPreset(new ZoomPosition(0, 0, 0), new FocusValue(0, false), new IrisValue(0, false), 1);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsSamePreset() {
    IPCameraPreset preset = new IPCameraPreset(new ZoomPosition(0, 0, 0), new FocusValue(0, false), new IrisValue(0, false), 0);
    IPCameraPreset preset2 = new IPCameraPreset(new ZoomPosition(0, 0, 0), new FocusValue(0, false), new IrisValue(0, false), 0);
    Assert.assertEquals(preset, preset2);
  }

  @Test
  public void testEqualsOtherName() {
    IPCameraPreset preset = new IPCameraPreset(new ZoomPosition(0, 0, 0), new FocusValue(0, false), new IrisValue(0, false), 0);
    IPCameraPreset preset2 = new IPCameraPreset(new ZoomPosition(0, 0, 0), new FocusValue(0, false), new IrisValue(0, false), 0);
    preset2.setName("name");
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testAddTag() {
    IPCameraPreset preset = getPreset();
    preset.addTag("Violin");
    preset.addTag("Piano");
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset.getTags());
  }
  @Test
  public void testEqualsEqualTags() {
    IPCameraPreset preset1 = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset1.addTag("Violin");
    preset2.addTag("Violin");
    Assert.assertEquals(preset1, preset2);
  }
  @Test
  public void testEqualsNotEqualTags() {
    IPCameraPreset preset1 = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset1.addTag("Violin");
    preset1.addTag("Piano");
    Assert.assertNotEquals(preset1, preset2);
  }
  @Test
  public void testAddTagList() {
    IPCameraPreset preset1 = getPreset();
    preset1.addTag("Overview");
    Set<String> keyWords = new HashSet<>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    preset1.addTags(keyWords);
    keyWords.add("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testRemoveTag() {
    IPCameraPreset preset1 = getPreset();
    preset1.addTag("Violin");
    preset1.addTag(("Piano"));
    preset1.removeTag("Violin");
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testDuplicateKeyWords() {
    IPCameraPreset preset1 = getPreset();
    preset1.addTag("Violin");
    preset1.addTag(("Violin"));
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Violin");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testSetCameraId() {
    IPCameraPreset preset = getPreset();
    // Actual method to test
    preset.setCameraId(42);
    Assert.assertEquals(42, preset.getCameraId());
  }

  @Test
  public void testSetName() {
    preset.setName("name2");
    Assert.assertEquals("name2", preset.getName());
  }
}