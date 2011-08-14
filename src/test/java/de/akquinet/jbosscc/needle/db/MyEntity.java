package de.akquinet.jbosscc.needle.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Alphonse Bendt, akquinet tech@spree GmbH
 */
@Entity
public class MyEntity {

    @Column(nullable = false)
    private String myName;

    @Id
    private long id;

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
