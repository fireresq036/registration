package org.portersville.muddycreek.vfd.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark on 3/1/15.
 * Entity class for events at Portersville Muddycreek VFD
 */

@PersistenceCapable
public class Event {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private long id;

  @Persistent
  private String name;

  @Persistent
  private String description;

  @Persistent
  private String location;

  @Persistent
  private Date start_date;

  @Persistent
  private Date end_date;

  @Persistent
  private int team_size;

  @Persistent
  private String user_id;

  @Persistent
  private String user_email;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getStartDate() {
    return start_date;
  }

  public void setStartDate(Date start_date) {
    this.start_date = start_date;
  }

  public Date getEndDate() {
    return end_date;
  }

  public void setEndDate(Date end_date) {
    this.end_date = end_date;
  }

  public int getTeamSize() {
    return team_size;
  }

  public void setTeamSize(int team_size) {
    this.team_size = team_size;
  }

  public String getUserId() {
    return user_id;
  }

  public void setUserId(String user_id) { this.user_id = user_id; }

  public String getUserEmail() {
    return user_email;
  }

  public void setUserEmail(String user_email) {
    this.user_email = user_email;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Event event) {
    return new Builder(event);
  }

  public static class Builder {
    private static final SimpleDateFormat SDF =
        new SimpleDateFormat("MM/dd/yyyy");
    Event event;

    protected Builder() {
      event = new Event();
    }

    protected Builder(Event event) {
      this.event = event;
    }

    public Builder id(long id) {
      event.setId(id);
      return this;
    }

    public Builder name(String name) {
      event.setName(name);
      return this;
    }

    public Builder description(String description) {
      event.setDescription(description);
      return this;
    }

    public Builder location(String location) {
      event.setLocation(location);
      return this;
    }

    public Builder teamSize(int team_size) {
      event.setTeamSize(team_size);
      return this;
    }

    public Builder teamSize(String team_size) {
      event.setTeamSize(new Integer(team_size).intValue());
      return this;
    }

    public Builder startDate(String start_date) throws ParseException {
      event.setStartDate(createDate(start_date));
      return this;
    }

    public Builder endDate(String end_date) throws ParseException {
      event.setEndDate(createDate(end_date));
      return this;
    }

    public Builder startDate(Date start_date) {
      event.setStartDate(start_date);
      return this;
    }

    public Builder endDate(Date end_date) {
      event.setEndDate(end_date);
      return this;
    }

    public Builder userId(String userId) {
      event.setUserId(userId);
      return this;
    }

    public Builder userEmail(String email) {
      event.setUserEmail(email);
      return this;
    }

    public Event build() {
      Event event_out = event;
      event = null;
      return event_out;
    }

    private Date createDate(String date_string) throws ParseException {
      return SDF.parse(date_string);
    }
  }
}
