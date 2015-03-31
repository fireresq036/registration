package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mark on 3/12/15.
 */
@Entity
public class Person implements Serializable, EntityI {
  private static final Logger log =
      Logger.getLogger(Person.class.getName());
  @Id
  Long id;
  String name;
  Address address;
  @Index
  String email;
  String phone;

  private Person() {
    id = null;
  }
  private Person(Person per) {
    id = per.getId();
    name = per.getName();
    assert (per.getAddress() != null);
    address = Address.newBuilder(per.getAddress()).build();
    email = per.getEmail();
    phone = per.getPhone();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Person person) {
    return new Builder(person);
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public <T> void update(T entity) {
    Person person = (Person)entity;
    this.id = person.getId();
    this.name = person.getName();
    this.address = Address.newBuilder(person.getAddress()).build();
    this.email = person.getEmail();
    this.phone = person.getPhone();
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

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public static class Builder {
    Person person;

    protected Builder() {
      person = new Person();
    }

    protected Builder(Person person) {
      this.person = new Person(person);
    }

    public Builder setName(String name) {
      person.setName(name);
      return this;
    }

    public Builder setAddress(Address address) {
      person.setAddress(address);
      return this;
    }

    public Builder setEmail(String email) {
      person.setEmail(email);
      return this;
    }

    public Builder setPhone(String phone) {
      person.setPhone(phone);
      return this;
    }

    public Person build() {
      Person person_out = person;
      person = null;
      return person_out;
    }

  }
}
