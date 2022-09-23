package com.poc.filter.repository;

import com.poc.filter.domain.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends ReactiveCrudRepository<Address, Long>, AddressRepositoryInternal {
    @Query("SELECT * FROM address entity WHERE entity.customer_id = :id")
    Flux<Address> findByCustomer(Long id);

    @Query("SELECT * FROM address entity WHERE entity.customer_id IS NULL")
    Flux<Address> findAllWhereCustomerIsNull();

    @Override
    <S extends Address> Mono<S> save(S entity);

    @Override
    Flux<Address> findAll();

    @Override
    Mono<Address> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AddressRepositoryInternal {
    <S extends Address> Mono<S> save(S entity);

    Flux<Address> findAllBy(Pageable pageable);

    Flux<Address> findAll();

    Mono<Address> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Address> findAllBy(Pageable pageable, Criteria criteria);

}
