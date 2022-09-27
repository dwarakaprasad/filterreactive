package com.poc.filter.service;

import com.poc.filter.domain.Address;
import com.poc.filter.domain.criteria.AddressCriteria;
import com.poc.filter.repository.AddressRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Address}.
 */
@Service
@Transactional
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Save a address.
     *
     * @param address the entity to save.
     * @return the persisted entity.
     */
    public Mono<Address> save(Address address) {
        log.debug("Request to save Address : {}", address);
        return addressRepository.save(address);
    }

    /**
     * Update a address.
     *
     * @param address the entity to save.
     * @return the persisted entity.
     */
    public Mono<Address> update(Address address) {
        log.debug("Request to update Address : {}", address);
        return addressRepository.save(address);
    }

    /**
     * Partially update a address.
     *
     * @param address the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Address> partialUpdate(Address address) {
        log.debug("Request to partially update Address : {}", address);

        return addressRepository
            .findById(address.getId())
            .map(existingAddress -> {
                if (address.getStreet() != null) {
                    existingAddress.setStreet(address.getStreet());
                }
                if (address.getCity() != null) {
                    existingAddress.setCity(address.getCity());
                }
                if (address.getState() != null) {
                    existingAddress.setState(address.getState());
                }
                if (address.getZip() != null) {
                    existingAddress.setZip(address.getZip());
                }

                return existingAddress;
            })
            .flatMap(addressRepository::save);
    }

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Address> findAll() {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll();
    }

    /**
     * Returns the number of addresses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return addressRepository.count();
    }

    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Address> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        return addressRepository.deleteById(id);
    }

    public Flux<Address> findByCriteria(AddressCriteria criteria) {
        return addressRepository.findByCriteria(criteria);
    }
}
