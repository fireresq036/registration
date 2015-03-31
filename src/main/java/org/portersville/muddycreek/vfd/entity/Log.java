package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark on 3/10/15.
 */
@Entity
public class Log implements Serializable, EntityI {
  public enum EntityType {
    EVENT, LOG, PERSON, TEAM
  }
  @Id
  Long logKey;
  String userId;
  String userEmail;
  Date timestamp;
  String note;
  @Index EntityType entityType;
  @Index Long entityKey;

  private Log() {
    timestamp = new Date();
    logKey = null;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Log log) {
    return new Builder(log);
  }

  @Override
  public Long getId() {
    return getLogKey();
  }

  @Override
  public <T> void update(T entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    return "Log";
  }

  public Long getLogKey() { return logKey;  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public EntityType getEntityType() { return entityType; }

  public void setEntityType(EntityType type) { this.entityType = type; }

  public Long getEntityKey() {
    return entityKey;
  }

  public void setEntityKey(Long entityKey) {
    this.entityKey = entityKey;
  }

  public static class Builder {
    private static final SimpleDateFormat SDF =
        new SimpleDateFormat("MM/dd/yyyy");
    Log log;

    protected Builder() {
      log = new Log();
    }

    protected Builder(Log log) {
      this.log = log;
    }

    public Builder setUserId(String userId) {
      log.setUserId(userId);
      return this;
    }

    public Builder setUserEmail(String userEmail) {
      log.setUserEmail(userEmail);
      return this;
    }

    public Builder setTimestamp(Date timestamp) {
      log.setTimestamp(timestamp);
      return this;
    }

    public Builder setNote(String note) {
      log.setNote(note);
      return this;
    }

    public Builder setEntityType(EntityType type) {
      log.setEntityType(type);
      return this;
    }

    public Builder setEntityKey(Long key) {
      log.setEntityKey(key);
      return this;
    }

    public Log build() {
      Log log_out = log;
      log = null;
      return log_out;
    }

  }
}
