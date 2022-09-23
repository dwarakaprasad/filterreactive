package com.poc.filter.repository.rowmapper;

import com.poc.filter.domain.Customer;
import com.poc.filter.domain.enumeration.CustomerType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Customer}, with proper type conversions.
 */
@Service
public class CustomerRowMapper implements BiFunction<Row, String, Customer> {

    private final ColumnConverter converter;

    public CustomerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Customer} stored in the database.
     */
    @Override
    public Customer apply(Row row, String prefix) {
        Customer entity = new Customer();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAge(converter.fromRow(row, prefix + "_age", Integer.class));
        entity.setDistanceTravelled(converter.fromRow(row, prefix + "_distance_travelled", Long.class));
        entity.setAmountSpent(converter.fromRow(row, prefix + "_amount_spent", BigDecimal.class));
        entity.setAmountSaved(converter.fromRow(row, prefix + "_amount_saved", Float.class));
        entity.setAmountEarned(converter.fromRow(row, prefix + "_amount_earned", Double.class));
        entity.setHappyPerson(converter.fromRow(row, prefix + "_happy_person", Boolean.class));
        entity.setDob(converter.fromRow(row, prefix + "_dob", Instant.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", LocalDate.class));
        entity.setTravelDate(converter.fromRow(row, prefix + "_travel_date", ZonedDateTime.class));
        entity.setTravelTime(converter.fromRow(row, prefix + "_travel_time", Duration.class));
        entity.setCustomerType(converter.fromRow(row, prefix + "_customer_type", CustomerType.class));
        return entity;
    }
}
