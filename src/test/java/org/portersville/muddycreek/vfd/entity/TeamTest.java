package org.portersville.muddycreek.vfd.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
  private Address address;
  private Person captain;
  private Person member;
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
        .addAddress(address)
        .addEmail(EMAIL)
        .addPhone(PHONE)
        .build();
    member = Person.newBuilder()
        .setName(M_NAME)
        .addAddress(address)
        .addEmail(EMAIL)
        .addPhone(PHONE)
        .build();
  }

  @After
  public void tearDown(){
    address = null;
  }

  @Test
  public void testCreate() {
    Team team = Team.newBuilder()
        .setName(TEAM_NAME)
        .setCaptain(captain)
        .build();
    assertEquals(TEAM_NAME, team.getName());
    assertNotNull(team.getCaptain());
    assertEquals(NAME, team.getCaptain().getName());
    assertEquals(0, team.getMembers().size());
  }

  public void testCopy() {
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
    assertEquals(0, team.getMembers());
  }

  public void testAddMember() {
    Team team = Team.newBuilder()
        .setName(TEAM_NAME)
        .setCaptain(captain)
        .addMember(member)
        .build();
    assertEquals(TEAM_NAME, team.getName());
    assertNotNull(team.getCaptain());
    assertEquals(NAME, team.getCaptain().getName());
    assertEquals(1, team.getMembers());
    assertEquals(M_NAME, team.getMembers().get(0));
  }
}
