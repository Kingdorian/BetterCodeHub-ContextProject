package com.benine.backend.http;

import com.benine.backend.camera.CameraBusyException;
import org.eclipse.jetty.util.MultiMap;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created by BeNine on 25-5-16.
 */
public class RemoveTagHandlerTest extends RequestHandlerTest {

  @Override
  public RequestHandler supplyHandler() {
    return new RemoveTagHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, JSONException, CameraBusyException {
    super.initialize();
  }

  @Test
  public void testAddTag() throws Exception{
    setPath("/presets/removetag?name=tag1");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("name", "tag1");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(presetController).removeTag("tag1");
    verify(requestMock).setHandled(true);

  }

  @Test
  public void testAddTagNoName() throws Exception{
    setPath("/presets/removetag");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verifyZeroInteractions(presetController);

  }
}