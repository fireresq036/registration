package org.portersville.muddycreek.vfd.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mark on 3/12/15.
 */
public class AddressTest {
  private static final String STREET_1 = "100 Main St";
  private static final String STREET_2 = "nothing";
  private static final String CITY = "Pittsburgh";
  private static final String STATE = "PA";
  private static final String ZIP = "16052";

  @Test
  public void testCreate() {
    Address address = Address.newBuilder()
        .setStreet1(STREET_1)
        .setStreet2(STREET_2)
        .setCity(CITY)
        .setState(STATE)
        .setZip(ZIP)
        .build();
    assertNotNull(address);
    assertEquals(STREET_1, address.getStreet1());
    assertEquals(STREET_2, address.getStreet2());
    assertEquals(CITY, address.getCity());
    assertEquals(STATE, address.getState());
    assertEquals(ZIP, address.getZip());
  }

  @Test
  public void testCopy() {
    Address address = Address.newBuilder()
        .setStreet1(STREET_1)
        .setStreet2(STREET_2)
        .setCity(CITY)
        .setState(STATE)
        .setZip(ZIP)
        .build();
    assertNotNull(address);
    String new_city = CITY + "_test";
    Address address2 = Address.newBuilder(address)
        .setCity(new_city)
        .build();
    assertEquals(STREET_1, address2.getStreet1());
    assertEquals(STREET_2, address2.getStreet2());
    assertEquals(new_city, address2.getCity());
    assertEquals(STATE, address2.getState());
    assertEquals(ZIP, address2.getZip());
  }
}
