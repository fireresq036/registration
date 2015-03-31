package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark on 3/1/15.
 * Entity class for events at Portersville Muddycreek VFD
 */
@Entity
public class Event implements Serializable, EntityI {
  @Id
  private Long id;
  private String name;
  private String description;
  private String location;
  private Date startDate;
  private Date endDate;
  private int teamSize;

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Event event) {
    return new Builder(event);
  }

  private Event() {}

  private Event(Event event) {
    update(event);
  }

//  public void update(Event event) {
//    this.id = (long) event.getId();
//    this.name = event.getName();
//    this.description = event.getDescription();
//    this.location = event.getLocation();
//    this.startDate = event.getStartDate();
//    this.endDate = event.getEndDate();
//    this.teamSize = event.getTeamSize();
//  }

  public Long getId() {
    return id;
  }

  @Override
  public <T> void update(T entity) {
    Event event = (Event) entity;
    this.id = (long) event.getId();
    this.name = event.getName();
    this.description = event.getDescription();
    this.location = event.getLocation();
    this.startDate = event.getStartDate();
    this.endDate = event.getEndDate();
    this.teamSize = event.getTeamSize();

  }

  private void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  private void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  private void setLocation(String location) {
    this.location = location;
  }

  public Date getStartDate() {
    return startDate;
  }

  private void setStartDate(Date start_date) {
    this.startDate = start_date;
  }

  public Date getEndDate() {
    return endDate;
  }

  private void setEndDate(Date end_date) {
    this.endDate = end_date;
  }

  public int getTeamSize() {
    return teamSize;
  }

  private void setTeamSize(int team_size) {
    this.teamSize = team_size;
  }

  public static class Builder {
    private static final SimpleDateFormat SDF =
        new SimpleDateFormat("MM/dd/yyyy");
    Event event;

    protected Builder() {
      event = new Event();
    }

    protected Builder(Event event) {
      this.event = new Event(event);
    }

    public Builder setId(Long id) {
      event.setId(id);
      return this;
    }

    public Builder setName(String name) {
      event.setName(name);
      return this;
    }

    public Builder setDescription(String description) {
      event.setDescription(description);
      return this;
    }

    public Builder setLocation(String location) {
      event.setLocation(location);
      return this;
    }

    public Builder setTeamSize(int team_size) {
      event.setTeamSize(team_size);
      return this;
    }

    public Builder setTeamSize(String team_size) {
      event.setTeamSize(new Integer(team_size).intValue());
      return this;
    }

    public Builder setStartDate(String start_date) throws ParseException {
      event.setStartDate(createDate(start_date));
      return this;
    }

    public Builder setEndDate(String end_date) throws ParseException {
      event.setEndDate(createDate(end_date));
      return this;
    }

    public Builder setStartDate(Date start_date) {
      event.setStartDate(start_date);
      return this;
    }

    public Builder setEndDate(Date end_date) {
      event.setEndDate(end_date);
      return this;
    }

    public Event build() {
      Event event_out = event;
      event = null;
      return event_out;
    }

    public void clearId() {
      event.id = null;
    }

    public void clearName() {
      event.name = null;
    }

    public void clearDescription() { event.description = null; }

    public void clearLocation() {
      event.location = null;
    }

    public void clearStartDate() {
      event.startDate = null;
    }

    public void clearEndDate() {
      event.endDate = null;
    }

    public void clearTeamSize() {
      event.teamSize = 0;
    }

    private Date createDate(String date_string) throws ParseException {
      return SDF.parse(date_string);
    }
  }
}
