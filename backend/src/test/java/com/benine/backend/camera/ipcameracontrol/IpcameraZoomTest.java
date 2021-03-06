package com.benine.backend.camera.ipcameracontrol;


import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.Config;
import com.benine.backend.Logger;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class to test the IP Camera zoom functions class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraZoomTest {
  
  private IPCamera camera;
  private CameraController cameraController = mock(CameraController.class);
  private Config config = mock(Config.class);
  private Logger logger = mock(Logger.class);
  
  @Before
  public final void setUp(){
    when(config.getValue("IPCameraTimeOut")).thenReturn("2");
    when(cameraController.getConfig()).thenReturn(config);
    when(cameraController.getLogger()).thenReturn(logger);
    camera = Mockito.spy(new IPCamera("test", cameraController));
  }
  
  public void setCameraBehaviour(String cmd, String response) throws IpcameraConnectionException {
    Mockito.doReturn(response).when(camera).sendCommand("aw_ptz?cmd=%23" + cmd + "&res=1");
  }
  
  @Test
  public final void testGetZoomPosition() throws CameraConnectionException {
    setCameraBehaviour("GZ", "gz655");

    int res = camera.getZoom();

    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23GZ&res=1");
    assertEquals(res, 256, 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetZoomPositionException() throws CameraConnectionException {
    setCameraBehaviour("GZ", "gs655");
    camera.getZoom();
  }
  
  @Test
  public final void testZoomTo() throws CameraConnectionException, CameraBusyException {
    setCameraBehaviour("AXZ58F", "axz58F");
    camera.zoomTo(58);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23AXZ58F&res=1");
  }
  
  @Test
  public final void testZoom() throws CameraConnectionException, CameraBusyException {
    setCameraBehaviour("Z80", "zS80");
    camera.zoom(80);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23Z80&res=1");
  }
  
  @Test
  public final void testZoom2() throws CameraConnectionException, CameraBusyException {
    setCameraBehaviour("Z02", "zS02");
    camera.zoom(2);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23Z02&res=1");
  }
  
}
