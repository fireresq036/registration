package org.portersville.muddycreek.vfd.entity;

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
import org.portersville.muddycreek.vfd.datastore.EntityProcessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mark on 3/12/15.
 */
public class TeamTest {
  private static final String TEAM_NAME = "Team Name";
  private static final String NAME = "Name";
  private static final String M_NAME = "member Name";
  private static final String EMAIL = "you@me";
  private static final String PHONE = "8005551212";
  private static final String STREET_1 = "100 Main St";
  private static final String STREET_2 = "nothing";
  private static final String CITY = "Pittsburgh";
  private static final String STATE = "PA";
  private static final String ZIP = "16052";
  private static final String AUTH_DOAMIN = "Auth_domain";
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig())
      .setEnvIsAdmin(true).setEnvIsLoggedIn(true).setEnvEmail(EMAIL)
      .setEnvAuthDomain(AUTH_DOAMIN);
  private Address address;
  private Person captain;
  private Person member;
  private User user;
  EntityProcessing<Person> processing;

  @BeforeClass
  public static void initializeObjectify() {
    ObjectifyService.register(Person.class);
    ObjectifyService.register(Team.class);
    ObjectifyService.register(Log.class);
  }

  @Before
  public void setUp() {
    Address address = Address.newBuilder()
        .setStreet1(STREET_1)
        .setStreet2(STREET_2)
        .setCity(CITY)
        .setState(STATE)
        .setZip(ZIP)
        .build();
    captain = Person.newBuilder()
        .setName(NAME)
        .setAddress(address)
        .setEmail(EMAIL)
        .setPhone(PHONE)
        .build();
    member = Person.newBuilder()
        .setName(M_NAME)
        .setAddress(address)
        .setEmail(EMAIL)
        .setPhone(PHONE)
        .build();
    helper.setUp();
    UserService userService = UserServiceFactory.getUserService();
    user = userService.getCurrentUser();
    processing = new EntityProcessing<>(Person.class);
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        processing.saveEntity(member, user);
        processing.saveEntity(captain, user);
      }
    });

    }

  @After
  public void tearDown(){
    address = null;
  }

  @Test
  public void testCreate() {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Team team = Team.newBuilder()
            .setName(TEAM_NAME)
            .setCaptain(captain)
            .build();
        assertEquals(TEAM_NAME, team.getName());
        assertNotNull(team.getCaptain());
        assertEquals(NAME, team.getCaptain().getName());
        assertEquals(0, team.getMembers().size());
      }
    });
  }

  @Test
  public void testCopy() {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Team team = Team.newBuilder()
            .setName(TEAM_NAME)
            .setCaptain(captain)
            .build();
        String new_name = TEAM_NAME + 1;
        Team team2 = Team.newBuilder(team)
            .setName(new_name)
            .build();
        assertEquals(new_name, team.getName());
        assertNotNull(team.getCaptain());
        assertEquals(NAME, team.getCaptain().getName());
        assertEquals(0, team.getMembers().size());
      }
    });
  }

  @Test
  public void testAddMember() {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        Team team = Team.newBuilder()
            .setName(TEAM_NAME)
            .setCaptain(captain)
            .addMember(member)
            .build();
        assertEquals(TEAM_NAME, team.getName());
        assertNotNull(team.getCaptain());
        assertEquals(NAME, team.getCaptain().getName());
        assertEquals(1, team.getMembers().size());
        assertEquals(M_NAME, team.getMembers().get(0).getName());
      }
    });
  }
}
