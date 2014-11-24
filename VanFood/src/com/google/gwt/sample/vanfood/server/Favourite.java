package com.google.gwt.sample.vanfood.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import com.google.gwt.sample.vanfood.shared.Vendor;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Favourite {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private User user;
  @Persistent
  private Long vendorID;
  @Persistent
  private Date createDate;

  public Favourite() {
    this.createDate = new Date();
  }

  public Favourite(User user, Long vendorID) {
    this();
    this.user = user;
    this.vendorID = vendorID;
  }

  public Long getId() {
    return this.id;
  }

  public User getUser() {
    return this.user;
  }

  public Long getVendorID() {
    return this.vendorID;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

//  public void setVendor(Vendor vendor) {
//    this.vendor = vendor;
//  }
}