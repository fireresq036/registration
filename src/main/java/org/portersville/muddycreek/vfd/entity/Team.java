package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 3/12/15.
 */
@Entity
public class Team {
  @Id
  Long id;
  String name;
  Person captain;
  List<Person> members = new ArrayList<Person>();

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Team team) {
    return new Builder(team);
  }

  public Team() {
    id = null;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Person> getMembers() {
    return members;
  }

  public void setMembers(List<Person> members) {
    this.members = members;
  }

  public Person getCaptain() {
    return captain;
  }

  public void setCaptain(Person captain) {
    this.captain = captain;
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
      team.getMembers().add(member);
      return this;
    }

    public Team build() {
      Team team_out = team;
      team = null;
      return team_out;
    }

  }}
