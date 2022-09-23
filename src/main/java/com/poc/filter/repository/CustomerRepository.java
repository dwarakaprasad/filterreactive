package com.poc.filter.repository;

import com.poc.filter.domain.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long>, CustomerRepositoryInternal {
    @Override
    <S extends Customer> Mono<S> save(S entity);

    @Override
    Flux<Customer> findAll();

    @Override
    Mono<Customer> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CustomerRepositoryInternal {
    <S extends Customer> Mono<S> save(S entity);

    Flux<Customer> findAllBy(Pageable pageable);

    Flux<Customer> findAll();

    Mono<Customer> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Customer> findAllBy(Pageable pageable, Criteria criteria);

}
