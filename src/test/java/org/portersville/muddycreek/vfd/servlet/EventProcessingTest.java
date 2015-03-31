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
import org.portersville.muddycreek.vfd.datastore.EventProcessing;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;
import org.portersville.muddycreek.vfd.util.Entities;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

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
    ObjectifyService.register(Event.class);
    ObjectifyService.register(Log.class);
  }

  @Before
  public void setUp() throws Exception {
    int count = 0;
    for (int i = 0; i < 4; i++) {
      events.add(Event.newBuilder()
          .setName(NAME + i)
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
  public void testSave_CreateId() throws IOException, ParseException {
    final Event source = events.get(0);
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        processing.saveEvent(source, user);
        Event result = processing.eventFromId(source.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(source.getName(), result.getName());
        assertEquals(source.getLocation(), result.getLocation());
        assertEquals(source.getDescription(), result.getDescription());
        assertEquals(source.getStartDate(), result.getStartDate());
        assertEquals(source.getEndDate(), result.getEndDate());
        assertEquals(source.getTeamSize(), result.getTeamSize());
        List<Event> foundEvents = processing.loadEvents();
        assertEquals(1, foundEvents.size());
      }
    });
  }

  @Test
  public void testUpdate() throws IOException, ParseException {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Event source = events.get(0);
        log.log(Level.INFO, "******************");
        log.log(Level.INFO, "source {0}", source);
        processing.saveEvent(source, user);
        Event result = processing.eventFromId(source.getId());
        log.log(Level.INFO, "result {0}", result);
        assertEquals(source.getId(), result.getId());
        String new_descritpion = DESCRIPTION + "-1";
        Event result2 = Event.newBuilder(result)
            .setDescription(new_descritpion)
            .build();
        assertNotNull(result2);
        assertNotNull(result2.getId());
        assertEquals(source.getId(), result.getId());
        assertNotEquals(source.getDescription(), result2.getDescription());
        log.log(Level.INFO, "result2 {0}", result2);
        processing.updateEvent(result2, user);
        Event result3 = processing.eventFromId(source.getId());
        log.log(Level.INFO, "result3 {0}", result3);
        assertEquals(source.getId(), result.getId());
        assertEquals(source.getName(), result3.getName());
        assertEquals(source.getLocation(), result3.getLocation());
        assertEquals(new_descritpion, result3.getDescription());
        assertEquals(source.getStartDate(), result3.getStartDate());
        assertEquals(source.getEndDate(), result3.getEndDate());
        assertEquals(source.getTeamSize(), result3.getTeamSize());
        assertEquals(1, processing.loadEvents().size());
        log.log(Level.INFO, "******************");
      }
    });
  }

  @Test
  public void testLoadEvents() throws IOException, ParseException {
    log.log(Level.INFO, "++++++++++++++++");
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Event result = null;
        for (Event event : events) {
          processing.saveEvent(event, user);
        }
        List<Event> foundEvents = processing.loadEvents();
        assertEquals(events.size(), foundEvents.size());
        for (int i = 0; i < events.size(); i++) {
          Event source = events.get(i);
          Event target = foundEvents.get(i);
          assertEquals(source.getTeamSize(), target.getTeamSize());
          assertEquals(source.getLocation(), target.getLocation());
          assertEquals(source.getEndDate(), target.getEndDate());
        }
      }
    });
    log.log(Level.INFO, "++++++++++++++++");
  }

  @Test
  public void testLogMessages() throws IOException, ParseException {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Event result = null;
        for (Event event : events) {
          processing.saveEvent(event, user);
        }
        List<Event> foundEvents = processing.loadEvents();
        Map<Long, Event> mappedEvents = new TreeMap<Long, Event>();
        for (Event event : foundEvents) {
          mappedEvents.put(event.getId(), event);
        }
        List<Log> logs = ofy().load()
            .type(Log.class).filter("entityType ==", Entities.EVENT).list();
        assertEquals(logs.size(), foundEvents.size());
        for (Log log : logs) {
          assertTrue(mappedEvents.containsKey(log.getEntityKey()));
        }
      }
    });
  }
}
