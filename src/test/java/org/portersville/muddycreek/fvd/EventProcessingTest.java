package org.portersville.muddycreek.fvd;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.portersville.muddycreek.vfd.registration.Registration.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventProcessingTest {
  private static final Integer ID = 1;
  private static final String NAME = "event";
  private static final String DESCRIPTION = "this is a description";
  private static final String LOCATION = "moraine";
  private static final Long START_DATE = System.currentTimeMillis();
  private static final Long END_DATE = START_DATE + 100;
  private static final String START_DATE_STRING = "6/6/2015";
  private static final String END_DATE_STRING = "6/6/2015";
  private static final Integer TEAM_SIZE = 1;
  private static final String EMAIL = "thisisatest@gmail.com";
  private static final String AUTH_DOAMIN = "Auth_domain";
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig())
          .setEnvIsAdmin(true).setEnvIsLoggedIn(true).setEnvEmail(EMAIL)
          .setEnvAuthDomain(AUTH_DOAMIN);
  private List<Event> events = new ArrayList<>();
  private User user;

  @Before
  public void setUp() throws Exception {
    int count = 0;
    for (int i = 0; i < 4; i++) {
      events.add(Event.newBuilder()
          .setName(NAME)
          .setDescritpion(DESCRIPTION)
          .setLocation(LOCATION)
          .setStartDate(EventProcessing.ConvertDateToSecSinceEpoch(START_DATE_STRING))
          .setEndDate(EventProcessing.ConvertDateToSecSinceEpoch(END_DATE_STRING))
          .setTeamSize(TEAM_SIZE)
          .build());
    }
    helper.setUp();
    UserService userService = UserServiceFactory.getUserService();
    user = userService.getCurrentUser();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void testCreate() {
    EventProcessing processing = new EventProcessing();
    assertNotNull(processing);
  }
  @Test
  public void testAdd() throws IOException, ParseException {
    EventProcessing processing = new EventProcessing();
    assertNotNull(processing);
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);

    Event source = events.get(0);
    given(req.getParameter("eventName")).willReturn(source.getName());
    given(req.getParameter("eventDescription")).willReturn(source.getDescritpion());
    given(req.getParameter("eventLocation")).willReturn(source.getLocation());
    given(req.getParameter("eventStartDate")).willReturn(START_DATE_STRING);
    given(req.getParameter("eventEndDate")).willReturn(END_DATE_STRING);
    given(req.getParameter("eventTeamSize")).
        willReturn(new Integer(source.getTeamSize()).toString());
    when(resp.getWriter())
        .thenReturn(new PrintWriter(new StringWriter()));
    Event result = processing.AddEvent(req, user);
    assertNotNull(result);
    assertEquals(source.getName(), result.getName());
    assertEquals(source.getLocation(), result.getLocation());
    assertEquals(source.getDescritpion(), result.getDescritpion());
    assertEquals(source.getStartDate(), result.getStartDate());
    assertEquals(source.getEndDate(), result.getEndDate());
    assertEquals(source.getTeamSize(), result.getTeamSize());
  }
}