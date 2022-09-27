// Need to move these files under domain package (from service) as there is no elegant
// way convert from Filter<> to ...sql.query
package com.poc.filter.domain.criteria;

import com.poc.filter.domain.enumeration.CustomerType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.poc.filter.domain.Customer} entity. This class is used
 * in {@link com.poc.filter.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CustomerType
     */
    public static class CustomerTypeFilter extends Filter<CustomerType> {

        public CustomerTypeFilter() {}

        public CustomerTypeFilter(CustomerTypeFilter filter) {
            super(filter);
        }

        @Override
        public CustomerTypeFilter copy() {
            return new CustomerTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter age;

    private LongFilter distanceTravelled;

    private BigDecimalFilter amountSpent;

    private FloatFilter amountSaved;

    private DoubleFilter amountEarned;

    private BooleanFilter happyPerson;

    private InstantFilter dob;

    private LocalDateFilter createdDate;

    private ZonedDateTimeFilter travelDate;

    private DurationFilter travelTime;

    private CustomerTypeFilter customerType;

    // Does not support non owning side of the relationship
    // To suuport this, the SelectFromAndJoin will have to be modified (may be as a next step??)
    // private LongFilter addressId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.distanceTravelled = other.distanceTravelled == null ? null : other.distanceTravelled.copy();
        this.amountSpent = other.amountSpent == null ? null : other.amountSpent.copy();
        this.amountSaved = other.amountSaved == null ? null : other.amountSaved.copy();
        this.amountEarned = other.amountEarned == null ? null : other.amountEarned.copy();
        this.happyPerson = other.happyPerson == null ? null : other.happyPerson.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.travelDate = other.travelDate == null ? null : other.travelDate.copy();
        this.travelTime = other.travelTime == null ? null : other.travelTime.copy();
        this.customerType = other.customerType == null ? null : other.customerType.copy();
        // this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public IntegerFilter age() {
        if (age == null) {
            age = new IntegerFilter();
        }
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public LongFilter getDistanceTravelled() {
        return distanceTravelled;
    }

    public LongFilter distanceTravelled() {
        if (distanceTravelled == null) {
            distanceTravelled = new LongFilter();
        }
        return distanceTravelled;
    }

    public void setDistanceTravelled(LongFilter distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public BigDecimalFilter getAmountSpent() {
        return amountSpent;
    }

    public BigDecimalFilter amountSpent() {
        if (amountSpent == null) {
            amountSpent = new BigDecimalFilter();
        }
        return amountSpent;
    }

    public void setAmountSpent(BigDecimalFilter amountSpent) {
        this.amountSpent = amountSpent;
    }

    public FloatFilter getAmountSaved() {
        return amountSaved;
    }

    public FloatFilter amountSaved() {
        if (amountSaved == null) {
            amountSaved = new FloatFilter();
        }
        return amountSaved;
    }

    public void setAmountSaved(FloatFilter amountSaved) {
        this.amountSaved = amountSaved;
    }

    public DoubleFilter getAmountEarned() {
        return amountEarned;
    }

    public DoubleFilter amountEarned() {
        if (amountEarned == null) {
            amountEarned = new DoubleFilter();
        }
        return amountEarned;
    }

    public void setAmountEarned(DoubleFilter amountEarned) {
        this.amountEarned = amountEarned;
    }

    public BooleanFilter getHappyPerson() {
        return happyPerson;
    }

    public BooleanFilter happyPerson() {
        if (happyPerson == null) {
            happyPerson = new BooleanFilter();
        }
        return happyPerson;
    }

    public void setHappyPerson(BooleanFilter happyPerson) {
        this.happyPerson = happyPerson;
    }

    public InstantFilter getDob() {
        return dob;
    }

    public InstantFilter dob() {
        if (dob == null) {
            dob = new InstantFilter();
        }
        return dob;
    }

    public void setDob(InstantFilter dob) {
        this.dob = dob;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            createdDate = new LocalDateFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTimeFilter getTravelDate() {
        return travelDate;
    }

    public ZonedDateTimeFilter travelDate() {
        if (travelDate == null) {
            travelDate = new ZonedDateTimeFilter();
        }
        return travelDate;
    }

    public void setTravelDate(ZonedDateTimeFilter travelDate) {
        this.travelDate = travelDate;
    }

    public DurationFilter getTravelTime() {
        return travelTime;
    }

    public DurationFilter travelTime() {
        if (travelTime == null) {
            travelTime = new DurationFilter();
        }
        return travelTime;
    }

    public void setTravelTime(DurationFilter travelTime) {
        this.travelTime = travelTime;
    }

    public CustomerTypeFilter getCustomerType() {
        return customerType;
    }

    public CustomerTypeFilter customerType() {
        if (customerType == null) {
            customerType = new CustomerTypeFilter();
        }
        return customerType;
    }

    public void setCustomerType(CustomerTypeFilter customerType) {
        this.customerType = customerType;
    }

    /*public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }*/

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(age, that.age) &&
            Objects.equals(distanceTravelled, that.distanceTravelled) &&
            Objects.equals(amountSpent, that.amountSpent) &&
            Objects.equals(amountSaved, that.amountSaved) &&
            Objects.equals(amountEarned, that.amountEarned) &&
            Objects.equals(happyPerson, that.happyPerson) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(travelDate, that.travelDate) &&
            Objects.equals(travelTime, that.travelTime) &&
            Objects.equals(customerType, that.customerType) &&
            // Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            age,
            distanceTravelled,
            amountSpent,
            amountSaved,
            amountEarned,
            happyPerson,
            dob,
            createdDate,
            travelDate,
            travelTime,
            customerType,
            // addressId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (age != null ? "age=" + age + ", " : "") +
            (distanceTravelled != null ? "distanceTravelled=" + distanceTravelled + ", " : "") +
            (amountSpent != null ? "amountSpent=" + amountSpent + ", " : "") +
            (amountSaved != null ? "amountSaved=" + amountSaved + ", " : "") +
            (amountEarned != null ? "amountEarned=" + amountEarned + ", " : "") +
            (happyPerson != null ? "happyPerson=" + happyPerson + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (travelDate != null ? "travelDate=" + travelDate + ", " : "") +
            (travelTime != null ? "travelTime=" + travelTime + ", " : "") +
            (customerType != null ? "customerType=" + customerType + ", " : "") +
            // (addressId != null ? "addressId=" + addressId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
