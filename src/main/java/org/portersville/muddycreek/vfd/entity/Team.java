package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import org.apache.commons.lang.NotImplementedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 3/12/15.
 */
@Entity
public class Team implements Serializable, EntityI {
  private static final long serialVersionUID = 101L;
  private static final Logger log =
      Logger.getLogger(Team.class.getName());
  @Id
  protected Long id;
  @Index
  protected String name;
  @Load
  @Parent
  protected Ref<Person> captain;
  @Load
  protected List<Ref<Person>> members;

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Team team) {
    return new Builder(team);
  }

  public Team() {
    id = null;
    members = new ArrayList<Ref<Person>>();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public <T> void update(T entity) {
    throw new NotImplementedException();
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Person> getMembers() {
    log.log(Level.INFO, "getMembers()");
    List<Person> members_list = new ArrayList<>();
    for(Ref<Person> member : members) {
      log.log(Level.INFO, "processing {0}", member.get().getName());
      members_list.add(member.get());
    }
    log.log(Level.INFO, "members_list({0})", members_list.size());
    return members_list;
  }

  public Person getCaptain() {
    return captain.get();
  }

  public void setCaptain(Person captain) {
    this.captain = Ref.create(captain);
  }

  public static class Builder {
    Team team;

    protected Builder() {
      team = new Team();
    }

    protected Builder(Team team) {
      this.team = team;
    }

    public Builder setName(String name) {
      team.setName(name);
      return this;
    }

    public Builder setCaptain(Person captain) {
      team.setCaptain(captain);
      return this;
    }

    public Builder addMember(Person member) {
      team.members.add(Ref.create(member));
      return this;
    }

    public Team build() {
      Team team_out = team;
      team = null;
      return team_out;
    }

  }}
