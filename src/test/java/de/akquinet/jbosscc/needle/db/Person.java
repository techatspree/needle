package de.akquinet.jbosscc.needle.db;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "personEntity")
@Table(name = Person.TABLE_NAME)
public class Person {

    public static final String TABLE_NAME = "NEEDLE_TEST_PERSON";

    @Column(nullable = false)
    private String myName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
