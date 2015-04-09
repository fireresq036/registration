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
import org.portersville.muddycreek.vfd.entity.Log;
import org.portersville.muddycreek.vfd.entity.Person;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
  private static final String ADDRESS1 = "123 Test lnn";
  private static final String ADDRESS2 = "";
  private static final String CITY = "Prospect";
  private static final String STATE = "PA";
  private static final String ZIP = "16000";
  private static final String EMAIL = "you@me.com";
  private static final String PHONE = "8005551212";
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
    given(req.getParameter("captainAddress1")).willReturn(ADDRESS1);
    given(req.getParameter("captainAddress2")).willReturn(ADDRESS2);
    given(req.getParameter("captainCity")).willReturn(CITY);
    given(req.getParameter("captainState")).willReturn(STATE);
    given(req.getParameter("captainZip")).willReturn(ZIP);
    given(req.getParameter("captainName")).willReturn(NAME);
    given(req.getParameter("captainPhone")).willReturn(PHONE);
    given(req.getParameter("captainEmail")).willReturn(EMAIL);
  }

  @After
  public void shutDown() throws Exception {
    req = null;
  }

  @Test
  public void CreateEvent() throws ParseException {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        RegisterServlet servlet = new RegisterServlet();
        Person person = servlet.findOrAddCaptain(req, user);
        assertNotNull(person.getId());
        assertEquals(NAME, person.getName());
        assertEquals(PHONE, person.getPhone());
        assertEquals(EMAIL, person.getEmail());
        assertEquals(ADDRESS1, person.getAddress().getStreet1());
        assertEquals(ADDRESS2, person.getAddress().getStreet2());
        assertEquals(CITY, person.getAddress().getCity());
        assertEquals(STATE, person.getAddress().getState());
        assertEquals(ZIP, person.getAddress().getZip());
      }
    });
  }
}
