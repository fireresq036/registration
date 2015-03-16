package org.portersville.muddycreek.vfd.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by mark on 3/12/15.
 */
@Entity
public class Address {
  @Id
  Long id;
  String street1;
  String street2;
  String city;
  String state;
  String zip;

  protected Address() {
    id = null;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Address address) {
    return new Builder(address);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet1() {
    return street1;
  }

  public void setStreet1(String street1) {
    this.street1 = street1;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }
  public static class Builder {
    Address address;

    protected Builder() { this.address = new Address(); }

    protected Builder(Address address) {
      this.address = address;
    }

    public Builder setStreet1(String street1) {
      address.setStreet1(street1);
      return this;
    }

    public Builder setStreet2(String street2) {
      address.setStreet2(street2);
      return this;
    }

    public Builder setCity(String city) {
      address.setCity(city);
      return this;
    }

    public Builder setState(String state) {
      address.setState(state);
      return this;
    }

    public Builder setZip(String zip) {
      address.setZip(zip);
      return this;
    }

    public Address build() {
      Address address_out = address;
      address = null;
      return address_out;
    }

  }
}
