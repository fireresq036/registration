package org.portersville.muddycreek.vfd.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mark on 3/12/15.
 */
public class PersonTest {
  private static final Logger log =
      Logger.getLogger(PersonTest.class.getName());
  private static final String NAME = "Name";
  private static final String EMAIL = "you@me";
  private static final String PHONE = "8005551212";
  private static final String STREET_1 = "100 Main St";
  private static final String STREET_2 = "nothing";
  private static final String CITY = "Pittsburgh";
  private static final String STATE = "PA";
  private static final String ZIP = "16052";
  private Address address;

  @Before
  public void setUp() {
    address = Address.newBuilder()
        .setStreet1(STREET_1)
        .setStreet2(STREET_2)
        .setCity(CITY)
        .setState(STATE)
        .setZip(ZIP)
        .build();
  }

  @After
  public void tearDown(){
    address = null;
  }

  @Test
  public void testCreate () {
    Person person = Person.newBuilder()
        .setName(NAME)
        .setAddress(address)
        .setEmail(EMAIL)
        .setPhone(PHONE)
        .build();
    assertNotNull(person.getAddress());
    assertEquals(NAME, person.getName());
    assertEquals(EMAIL, person.getEmail());
    assertEquals(PHONE, person.getPhone());
  }

  @Test
  public void testCopy () {
    Person person = Person.newBuilder()
        .setName(NAME)
        .setAddress(address)
        .setEmail(EMAIL)
        .setPhone(PHONE)
        .build();
    String new_name = NAME + "1";
    String new_email = EMAIL + "1";
    Person person2 = Person.newBuilder(person)
        .setName(new_name)
        .setEmail(new_email)
        .build();
    assertEquals(new_name, person2.getName());
    assertEquals(new_email, person2.getEmail());
    assertEquals(person.getPhone(), person2.getPhone());
    assertEquals(PHONE, person2.getPhone());
    assertEquals(NAME, person.getName());
    assertEquals(EMAIL, person.getEmail());
    assertEquals(PHONE, person.getPhone());

  }
}
