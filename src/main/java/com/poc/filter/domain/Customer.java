package com.poc.filter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poc.filter.domain.enumeration.CustomerType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Customer.
 */
@Table("customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("age")
    private Integer age;

    @Column("distance_travelled")
    private Long distanceTravelled;

    @Column("amount_spent")
    private BigDecimal amountSpent;

    @Column("amount_saved")
    private Float amountSaved;

    @Column("amount_earned")
    private Double amountEarned;

    @Column("happy_person")
    private Boolean happyPerson;

    @Column("dob")
    private Instant dob;

    @Column("created_date")
    private LocalDate createdDate;

    @Column("travel_date")
    private ZonedDateTime travelDate;

    @Column("travel_time")
    private Duration travelTime;

    @Column("customer_type")
    private CustomerType customerType;

    @Transient
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public Customer age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getDistanceTravelled() {
        return this.distanceTravelled;
    }

    public Customer distanceTravelled(Long distanceTravelled) {
        this.setDistanceTravelled(distanceTravelled);
        return this;
    }

    public void setDistanceTravelled(Long distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public BigDecimal getAmountSpent() {
        return this.amountSpent;
    }

    public Customer amountSpent(BigDecimal amountSpent) {
        this.setAmountSpent(amountSpent);
        return this;
    }

    public void setAmountSpent(BigDecimal amountSpent) {
        this.amountSpent = amountSpent != null ? amountSpent.stripTrailingZeros() : null;
    }

    public Float getAmountSaved() {
        return this.amountSaved;
    }

    public Customer amountSaved(Float amountSaved) {
        this.setAmountSaved(amountSaved);
        return this;
    }

    public void setAmountSaved(Float amountSaved) {
        this.amountSaved = amountSaved;
    }

    public Double getAmountEarned() {
        return this.amountEarned;
    }

    public Customer amountEarned(Double amountEarned) {
        this.setAmountEarned(amountEarned);
        return this;
    }

    public void setAmountEarned(Double amountEarned) {
        this.amountEarned = amountEarned;
    }

    public Boolean getHappyPerson() {
        return this.happyPerson;
    }

    public Customer happyPerson(Boolean happyPerson) {
        this.setHappyPerson(happyPerson);
        return this;
    }

    public void setHappyPerson(Boolean happyPerson) {
        this.happyPerson = happyPerson;
    }

    public Instant getDob() {
        return this.dob;
    }

    public Customer dob(Instant dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Customer createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getTravelDate() {
        return this.travelDate;
    }

    public Customer travelDate(ZonedDateTime travelDate) {
        this.setTravelDate(travelDate);
        return this;
    }

    public void setTravelDate(ZonedDateTime travelDate) {
        this.travelDate = travelDate;
    }

    public Duration getTravelTime() {
        return this.travelTime;
    }

    public Customer travelTime(Duration travelTime) {
        this.setTravelTime(travelTime);
        return this;
    }

    public void setTravelTime(Duration travelTime) {
        this.travelTime = travelTime;
    }

    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public Customer customerType(CustomerType customerType) {
        this.setCustomerType(customerType);
        return this;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCustomer(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCustomer(this));
        }
        this.addresses = addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Customer addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", distanceTravelled=" + getDistanceTravelled() +
            ", amountSpent=" + getAmountSpent() +
            ", amountSaved=" + getAmountSaved() +
            ", amountEarned=" + getAmountEarned() +
            ", happyPerson='" + getHappyPerson() + "'" +
            ", dob='" + getDob() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", travelDate='" + getTravelDate() + "'" +
            ", travelTime='" + getTravelTime() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            "}";
    }
}
