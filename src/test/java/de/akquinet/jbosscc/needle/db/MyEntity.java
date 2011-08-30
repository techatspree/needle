package de.akquinet.jbosscc.needle.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Alphonse Bendt, akquinet tech@spree GmbH
 */
@Entity
public class MyEntity {

  @Column(nullable = false)
  private String myName;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  public String getMyName() {
    return myName;
  }

  public void setMyName(final String myName) {
    this.myName = myName;
  }

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

}
