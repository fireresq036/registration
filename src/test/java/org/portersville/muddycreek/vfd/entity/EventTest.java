package org.portersville.muddycreek.vfd.entity;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by mark on 3/12/15.
 */
public class EventTest {
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description for me";
  private static final String LOCATION = "Moraine";
  private static final String START_DATE_STRING = "06/21/2015";
  private static final String END_DATE_STRING = "06/22/2015";
  private static final int TEAM_SIZE = 2;
  private static final SimpleDateFormat SDF =
      new SimpleDateFormat("MM/dd/yyyy");
  @Test
  public void testCreate_StringDates() throws ParseException {
    Event event = Event.newBuilder()
        .setName(NAME)
        .setDescription(DESCRIPTION)
        .setLocation(LOCATION)
        .setStartDate(START_DATE_STRING)
        .setEndDate(END_DATE_STRING)
        .setTeamSize(TEAM_SIZE)
        .build();
    assertEquals(NAME, event.getName());
    assertEquals(DESCRIPTION, event.getDescription());
    assertEquals(LOCATION, event.getLocation());
    assertEquals(START_DATE_STRING, SDF.format(event.getStartDate()));
    assertEquals(END_DATE_STRING, SDF.format(event.getEndDate()));
    assertEquals(TEAM_SIZE, event.getTeamSize());
  }
  @Test
  public void testCreate_DateDates() throws ParseException {
    Date start_date = SDF.parse(START_DATE_STRING);
    Date end_date = SDF.parse(END_DATE_STRING);
    Event event = Event.newBuilder()
        .setName(NAME)
        .setDescription(DESCRIPTION)
        .setLocation(LOCATION)
        .setStartDate(start_date)
        .setEndDate(end_date)
        .setTeamSize(TEAM_SIZE)
        .build();
    assertEquals(NAME, event.getName());
    assertEquals(DESCRIPTION, event.getDescription());
    assertEquals(LOCATION, event.getLocation());
    assertEquals(start_date, event.getStartDate());
    assertEquals(end_date, event.getEndDate());
    assertEquals(TEAM_SIZE, event.getTeamSize());
  }
  public void testCopy() throws ParseException {
    Event event = Event.newBuilder()
        .setName(NAME)
        .setDescription(DESCRIPTION)
        .setLocation(LOCATION)
        .setStartDate(START_DATE_STRING)
        .setEndDate(END_DATE_STRING)
        .setTeamSize(TEAM_SIZE)
        .build();
    String new_description = DESCRIPTION + "1";
    Event event2 = Event.newBuilder()
        .setDescription(new_description)
        .build();
    assertEquals(event.getName(), event2.getName());
    assertNotEquals(event.getDescription(), event2.getDescription());
    assertEquals(event.getLocation(), event2.getLocation());
    assertEquals(event.getStartDate(), event2.getStartDate());
    assertEquals(event.getEndDate(), event2.getEndDate());
    assertEquals(event.getTeamSize(), event2.getTeamSize());
    assertEquals(event.getId(), event2.getId());
  }
}
