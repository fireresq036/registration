package org.portersville.muddycreek.vfd.servlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventProcessingTest {
  private static final Logger log =
      Logger.getLogger(AdminServlet.class.getName());
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
  private EventProcessing processing;

  @BeforeClass
  public static void initializeObjectify() {
    ObjectifyService.register(Log.class);
    ObjectifyService.register(Event.class);
  }
  @Before
  public void setUp() throws Exception {
    int count = 0;
    for (int i = 0; i < 4; i++) {
      events.add(Event.newBuilder()
          .setName(NAME)
          .setDescription(DESCRIPTION)
          .setLocation(LOCATION)
          .setStartDate(START_DATE_STRING)
          .setEndDate(END_DATE_STRING)
          .setTeamSize(TEAM_SIZE)
          .build());
    }
    helper.setUp();
    UserService userService = UserServiceFactory.getUserService();
    user = userService.getCurrentUser();
    processing = new EventProcessing();
    assertNotNull(processing);
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
//    final EventProcessing processing = new EventProcessing();
//    assertNotNull(processing);
    final HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);

    final Event source = events.get(0);
    given(req.getParameter("eventName")).willReturn(source.getName());
    given(req.getParameter("eventDescription")).willReturn(source.getDescription());
    given(req.getParameter("eventLocation")).willReturn(source.getLocation());
    given(req.getParameter("eventStartDate")).willReturn(START_DATE_STRING);
    given(req.getParameter("eventEndDate")).willReturn(END_DATE_STRING);
    given(req.getParameter("eventTeamSize")).
        willReturn(new Integer(source.getTeamSize()).toString());
    when(resp.getWriter())
        .thenReturn(new PrintWriter(new StringWriter()));
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        try {
          Event result = null;
          result = processing.addEvent(req, user);
          assertNotNull(result);
          assertNotNull(result.getId());
          log.log(Level.INFO, "id = {0}", result.getId());
          assertEquals(source.getName(), result.getName());
          assertEquals(source.getLocation(), result.getLocation());
          assertEquals(source.getDescription(), result.getDescription());
          assertEquals(source.getStartDate(), result.getStartDate());
          assertEquals(source.getEndDate(), result.getEndDate());
          assertEquals(source.getTeamSize(), result.getTeamSize());
        } catch (ParseException e) {
          fail("could not parse the date: " + e);
        }
      }
    });
  }
  @Test
  public void testGet() throws IOException, ParseException {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Event result = null;
        for (Event event : events) {
          processing.saveEvent(event, user);
        }
        List<Event> foundEvents = processing.loadEvents();
        assertEquals(events.size(), foundEvents.size());
        for (int i=0; i < events.size(); i++) {
          Event source = events.get(i);
          Event target = foundEvents.get(i);
          assertEquals(source.getTeamSize(), target.getTeamSize());
          assertEquals(source.getLocation(), target.getLocation());
          assertEquals(source.getEndDate(), target.getEndDate());
        }
      }
    });
  }
}
