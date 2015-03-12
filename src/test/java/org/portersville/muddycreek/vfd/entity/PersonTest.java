package org.portersville.muddycreek.vfd.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by mark on 3/12/15.
 */
public class PersonTest {
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
    Address address = Address.newBuilder()
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
        .addAddress(address)
        .addEmail(EMAIL)
        .addPhone(PHONE)
        .build();
    assertEquals(NAME, person.getName());
    assertEquals(1, person.getEmail().size());
    assertEquals(EMAIL, person.getEmail().get(0));
    assertEquals(1, person.getPhone().size());
    assertEquals(PHONE, person.getPhone().get(0));
  }

  @Test
  public void testCopy () {
    Person person = Person.newBuilder()
        .setName(NAME)
        .addAddress(address)
        .addEmail(EMAIL)
        .addPhone(PHONE)
        .build();
    String new_name = NAME + "1";
    String new_email = EMAIL + "1";
    Person person2 = Person.newBuilder(person)
        .setName(new_name)
        .addEmail(new_email)
        .build();
    assertEquals(new_name, person.getName());
    assertEquals(2, person.getEmail().size());
    assertEquals(EMAIL, person.getEmail().get(0));
    assertEquals(new_email, person.getEmail().get(1));
    assertEquals(1, person.getPhone().size());
    assertEquals(PHONE, person.getPhone().get(0));

  }
}
