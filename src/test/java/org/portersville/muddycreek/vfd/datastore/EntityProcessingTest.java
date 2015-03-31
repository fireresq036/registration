package org.portersville.muddycreek.vfd.datastore;

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
import org.portersville.muddycreek.vfd.entity.Address;
import org.portersville.muddycreek.vfd.entity.Event;
import org.portersville.muddycreek.vfd.entity.Log;
import org.portersville.muddycreek.vfd.entity.Person;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mark on 3/28/15.
 */
public class EntityProcessingTest {
  private static final String NAME = "Name";
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
  private Person person;
  private User user;
  EntityProcessing<Person> processing;

  @BeforeClass
  public static void initializeObjectify() {
    ObjectifyService.register(Person.class);
    ObjectifyService.register(Log.class);
  }

  @Before
  public void setUp() throws Exception {
    Address address = Address.newBuilder()
        .setStreet1(STREET_1)
        .setStreet2(STREET_2)
        .setCity(CITY)
        .setState(STATE)
        .setZip(ZIP)
        .build();
    person = Person.newBuilder()
        .setName(NAME)
        .setAddress(address)
        .setEmail(EMAIL)
        .setPhone(PHONE)
        .build();
    helper.setUp();
    UserService userService = UserServiceFactory.getUserService();
    user = userService.getCurrentUser();

    processing  = new EntityProcessing<>(Person.class);
    assertNotNull(processing);
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
    person = null;
  }

  @Test
  public void addPerson() {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        processing.saveEntity(person, user);
        Person result = processing.entityFromId(person.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(person.getName(), result.getName());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getEmail(), result.getEmail());
        assertEquals(person.getPhone(), result.getPhone());
        List<Person> foundEntities = processing.loadEntities();
        assertEquals(1, foundEntities.size());
      }
    });
  }

  @Test
  public void updatePerson() {
    ObjectifyService.run(new VoidWork() {
      public void vrun() {
        processing.saveEntity(person, user);
        Person result = processing.entityFromId(person.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(person.getId(), result.getId());
        String new_name = NAME + 1;
        String new_phone = PHONE + 1;
        Person person2 = Person.newBuilder(result)
            .setName(new_name)
            .setPhone(new_phone)
            .build();
        assertEquals(person.getId(), person2.getId());
        processing.updateEntity(person2, user);
        result = processing.entityFromId(person2.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(person2.getId(), result.getId());
        assertEquals(person2.getName(), result.getName());
        assertNotEquals(person2.getAddress(), result.getAddress());
        assertEquals(person2.getEmail(), result.getEmail());
        assertEquals(person2.getPhone(), result.getPhone());
        List<Person> foundEntities = processing.loadEntities();
        assertEquals(1, foundEntities.size());
      }
    });
  }

}
