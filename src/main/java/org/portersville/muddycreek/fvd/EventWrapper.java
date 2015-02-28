package org.portersville.muddycreek.fvd;

import org.portersville.muddycreek.vfd.registration.Registration.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark on 2/28/15.
 */
public class EventWrapper {
  private static final DateFormat format =
      new SimpleDateFormat("dd/MM/yyyy");
  private Event event;

  public EventWrapper(Event event) {
    this.event = event;
  }

  public String getName() {
    return event.getName();
  }

  public String getDecription() {
    return event.getDescritpion();
  }

  public String getLocation() {
    return event.getLocation();
  }

  public String getStartDate() {
    return format.format(new Date(event.getStartDate()));
  }

  public String getEndDate() {
    return format.format(new Date(event.getEndDate()));
  }

  public String getTeamSize() {
    return String.valueOf(event.getTeamSize());
  }

}
