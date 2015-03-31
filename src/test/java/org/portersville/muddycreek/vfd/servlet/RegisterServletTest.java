package org.portersville.muddycreek.vfd.servlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.portersville.muddycreek.vfd.datastore.EventProcessing;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;
import org.portersville.muddycreek.vfd.entity.Person;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by mark on 3/31/15.
 */
public class RegisterServletTest {
  private static final Logger log =
      Logger.getLogger(AdminServletTest.class.getName());

  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String LOCATION = "location";
  private static final String START_DATE_STRING = "6/22/2015";
  private static final String END_DATE_STRING = "6/22/2015";
  private static final String TEAM_SIZE = "2";
  private static final String EMAIL = "you@me.com";
  private static final SimpleDateFormat SDF =
      new SimpleDateFormat("MM/dd/yyyy");
  private static final String AUTH_DOAMIN = "Auth_domain";
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig())
          .setEnvIsAdmin(true).setEnvIsLoggedIn(true).setEnvEmail(EMAIL)
          .setEnvAuthDomain(AUTH_DOAMIN);
  private User user;
  private HttpServletRequest req;

  @BeforeClass
  public static void initializeObjectify() {
    ObjectifyService.register(Person.class);
    ObjectifyService.register(Log.class);
  }

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    UserService userService = UserServiceFactory.getUserService();
    user = userService.getCurrentUser();
    req = mock(HttpServletRequest.class);
    given(req.getParameter("eventId")).willReturn(null);
    given(req.getParameter("eventName")).willReturn(NAME);
    given(req.getParameter("eventDescription")).willReturn(DESCRIPTION);
    given(req.getParameter("eventLocation")).willReturn(LOCATION);
    given(req.getParameter("eventStartDate")).willReturn(START_DATE_STRING);
    given(req.getParameter("eventEndDate")).willReturn(END_DATE_STRING);
    given(req.getParameter("eventTeamSize")).willReturn(TEAM_SIZE);
  }

  @After
  public void shutDown() throws Exception {
    req = null;
  }

  @Test
  public void CreateEvent() throws ParseException {
    AdminServlet servlet = new AdminServlet();
    Event event = servlet.eventFromRequest(req);
    assertNull(event.getId());
    assertEquals(NAME, event.getName());
    assertEquals(LOCATION, event.getLocation());
    assertEquals(DESCRIPTION, event.getDescription());
    assertEquals(SDF.parse(START_DATE_STRING), event.getStartDate());
    assertEquals(SDF.parse(END_DATE_STRING), event.getEndDate());
    assertEquals(new Integer(TEAM_SIZE), new Integer(event.getTeamSize()));
  }
}
