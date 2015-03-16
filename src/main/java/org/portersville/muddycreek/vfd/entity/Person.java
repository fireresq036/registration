package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 3/12/15.
 */
@Entity
public class Person {
  @Id
  Long id;
  String name;
  List<Address> address = new ArrayList<Address>();
  List<String> email = new ArrayList<String>();
  List<String> phone = new ArrayList<String>();

  private Person() {
    id = null;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Person person) {
    return new Builder(person);
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

  public List<Address> getAddress() {
    return address;
  }

  public void setAddress(List<Address> address) {
    this.address = address;
  }

  public List<String> getEmail() {
    return email;
  }

  public void setEmail(List<String> email) {
    this.email = email;
  }

  public List<String> getPhone() {
    return phone;
  }

  public void setPhone(List<String> phone) {
    this.phone = phone;
  }

  public static class Builder {
    Person person;

    protected Builder() {
      person = new Person();
    }

    protected Builder(Person person) {
      this.person = person;
    }

    public Builder setName(String name) {
      person.setName(name);
      return this;
    }

    public Builder addAddress(Address address) {
      person.getAddress().add(address);
      return this;
    }

    public Builder addEmail(String email) {
      person.getEmail().add(email);
      return this;
    }

    public Builder addPhone(String phone) {
      person.getPhone().add(phone);
      return this;
    }

    public Person build() {
      Person person_out = person;
      person = null;
      return person_out;
    }

  }
}
