package com.poc.filter.service;

import com.poc.filter.domain.Customer;
import com.poc.filter.repository.CustomerRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Save a customer.
     *
     * @param customer the entity to save.
     * @return the persisted entity.
     */
    public Mono<Customer> save(Customer customer) {
        log.debug("Request to save Customer : {}", customer);
        return customerRepository.save(customer);
    }

    /**
     * Update a customer.
     *
     * @param customer the entity to save.
     * @return the persisted entity.
     */
    public Mono<Customer> update(Customer customer) {
        log.debug("Request to update Customer : {}", customer);
        return customerRepository.save(customer);
    }

    /**
     * Partially update a customer.
     *
     * @param customer the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Customer> partialUpdate(Customer customer) {
        log.debug("Request to partially update Customer : {}", customer);

        return customerRepository
            .findById(customer.getId())
            .map(existingCustomer -> {
                if (customer.getName() != null) {
                    existingCustomer.setName(customer.getName());
                }
                if (customer.getAge() != null) {
                    existingCustomer.setAge(customer.getAge());
                }
                if (customer.getDistanceTravelled() != null) {
                    existingCustomer.setDistanceTravelled(customer.getDistanceTravelled());
                }
                if (customer.getAmountSpent() != null) {
                    existingCustomer.setAmountSpent(customer.getAmountSpent());
                }
                if (customer.getAmountSaved() != null) {
                    existingCustomer.setAmountSaved(customer.getAmountSaved());
                }
                if (customer.getAmountEarned() != null) {
                    existingCustomer.setAmountEarned(customer.getAmountEarned());
                }
                if (customer.getHappyPerson() != null) {
                    existingCustomer.setHappyPerson(customer.getHappyPerson());
                }
                if (customer.getDob() != null) {
                    existingCustomer.setDob(customer.getDob());
                }
                if (customer.getCreatedDate() != null) {
                    existingCustomer.setCreatedDate(customer.getCreatedDate());
                }
                if (customer.getTravelDate() != null) {
                    existingCustomer.setTravelDate(customer.getTravelDate());
                }
                if (customer.getTravelTime() != null) {
                    existingCustomer.setTravelTime(customer.getTravelTime());
                }
                if (customer.getCustomerType() != null) {
                    existingCustomer.setCustomerType(customer.getCustomerType());
                }

                return existingCustomer;
            })
            .flatMap(customerRepository::save);
    }

    /**
     * Get all the customers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Customer> findAll() {
        log.debug("Request to get all Customers");
        return customerRepository.findAll();
    }

    /**
     * Returns the number of customers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return customerRepository.count();
    }

    /**
     * Get one customer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Customer> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id);
    }

    /**
     * Delete the customer by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        return customerRepository.deleteById(id);
    }
}
