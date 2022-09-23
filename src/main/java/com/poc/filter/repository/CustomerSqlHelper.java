package com.poc.filter.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CustomerSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("age", table, columnPrefix + "_age"));
        columns.add(Column.aliased("distance_travelled", table, columnPrefix + "_distance_travelled"));
        columns.add(Column.aliased("amount_spent", table, columnPrefix + "_amount_spent"));
        columns.add(Column.aliased("amount_saved", table, columnPrefix + "_amount_saved"));
        columns.add(Column.aliased("amount_earned", table, columnPrefix + "_amount_earned"));
        columns.add(Column.aliased("happy_person", table, columnPrefix + "_happy_person"));
        columns.add(Column.aliased("dob", table, columnPrefix + "_dob"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("travel_date", table, columnPrefix + "_travel_date"));
        columns.add(Column.aliased("travel_time", table, columnPrefix + "_travel_time"));
        columns.add(Column.aliased("customer_type", table, columnPrefix + "_customer_type"));

        return columns;
    }
}
