package com.poc.filter.repository;

import com.poc.filter.domain.Customer;
import com.poc.filter.domain.criteria.CustomerCriteria;
import com.poc.filter.repository.rowmapper.ColumnConverter;
import com.poc.filter.repository.rowmapper.CustomerRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Customer entity.
 */
@SuppressWarnings("unused")
class CustomerRepositoryInternalImpl extends SimpleR2dbcRepository<Customer, Long> implements CustomerRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CustomerRowMapper customerMapper;
    // Needed for converting values in the where clause
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("customer", EntityManager.ENTITY_ALIAS);

    public CustomerRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CustomerRowMapper customerMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnCOnverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Customer.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.customerMapper = customerMapper;
        this.columnConverter = columnCOnverter;
    }

    @Override
    public Flux<Customer> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Customer> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CustomerSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Customer.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Customer> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Customer> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Customer process(Row row, RowMetadata metadata) {
        Customer entity = customerMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Customer> Mono<S> save(S entity) {
        return super.save(entity);
    }

    // New Method implementation to be added to template
    @Override
    public Flux<Customer> findByCriteria(CustomerCriteria criteria) {
        return createQuery(null, buildConditions(criteria)).all();
    }

    // No better way to get column names (nothing similar to JPA metamodel is available)
    private Condition buildConditions(CustomerCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getAge() != null) {
                builder.buildFilterConditionForField(criteria.getAge(), entityTable.column("age"));
            }
            if (criteria.getDistanceTravelled() != null) {
                builder.buildFilterConditionForField(criteria.getDistanceTravelled(), entityTable.column("distance_travelled"));
            }
            if (criteria.getDob() != null) {
                builder.buildFilterConditionForField(criteria.getDob(), entityTable.column("dob"));
            }
            if (criteria.getAmountSpent() != null) {
                builder.buildFilterConditionForField(criteria.getAmountSpent(), entityTable.column("amount_spent"));
            }
            if (criteria.getAmountSaved() != null) {
                builder.buildFilterConditionForField(criteria.getAmountSaved(), entityTable.column("amount_saved"));
            }
            if (criteria.getAmountEarned() != null) {
                builder.buildFilterConditionForField(criteria.getAmountEarned(), entityTable.column("amount_earned"));
            }
            if (criteria.getHappyPerson() != null) {
                builder.buildFilterConditionForField(criteria.getHappyPerson(), entityTable.column("happy_person"));
            }
            if (criteria.getCreatedDate() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedDate(), entityTable.column("created_date"));
            }
            if (criteria.getTravelDate() != null) {
                builder.buildFilterConditionForField(criteria.getTravelDate(), entityTable.column("travel_date"));
            }
            if (criteria.getTravelTime() != null) {
                builder.buildFilterConditionForField(criteria.getTravelTime(), entityTable.column("travel_time"));
            }
            if (criteria.getCustomerType() != null) {
                builder.buildFilterConditionForField(criteria.getCustomerType(), entityTable.column("customer_type"));
            }
            // No support for querying the non-owning side???
            // criteria.getAddressId() - This part will not be generated
        }
        return builder.buildConditions();
    }
}
