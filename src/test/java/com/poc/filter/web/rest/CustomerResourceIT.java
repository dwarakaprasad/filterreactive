package com.poc.filter.web.rest;

import static com.poc.filter.web.rest.TestUtil.sameInstant;
import static com.poc.filter.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.poc.filter.IntegrationTest;
import com.poc.filter.domain.Address;
import com.poc.filter.domain.Customer;
import com.poc.filter.domain.criteria.CustomerCriteria;
import com.poc.filter.domain.enumeration.CustomerType;
import com.poc.filter.repository.CustomerRepository;
import com.poc.filter.repository.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final Long DEFAULT_DISTANCE_TRAVELLED = 1L;
    private static final Long UPDATED_DISTANCE_TRAVELLED = 2L;
    private static final Long SMALLER_DISTANCE_TRAVELLED = 1L - 1L;

    private static final BigDecimal DEFAULT_AMOUNT_SPENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_SPENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT_SPENT = new BigDecimal(1 - 1);

    private static final Float DEFAULT_AMOUNT_SAVED = 1F;
    private static final Float UPDATED_AMOUNT_SAVED = 2F;
    private static final Float SMALLER_AMOUNT_SAVED = 1F - 1F;

    private static final Double DEFAULT_AMOUNT_EARNED = 1D;
    private static final Double UPDATED_AMOUNT_EARNED = 2D;
    private static final Double SMALLER_AMOUNT_EARNED = 1D - 1D;

    private static final Boolean DEFAULT_HAPPY_PERSON = false;
    private static final Boolean UPDATED_HAPPY_PERSON = true;

    private static final Instant DEFAULT_DOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOB = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_TRAVEL_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRAVEL_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TRAVEL_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Duration DEFAULT_TRAVEL_TIME = Duration.ofHours(6);
    private static final Duration UPDATED_TRAVEL_TIME = Duration.ofHours(12);
    private static final Duration SMALLER_TRAVEL_TIME = Duration.ofHours(5);

    private static final CustomerType DEFAULT_CUSTOMER_TYPE = CustomerType.MVP;
    private static final CustomerType UPDATED_CUSTOMER_TYPE = CustomerType.VP;

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .distanceTravelled(DEFAULT_DISTANCE_TRAVELLED)
            .amountSpent(DEFAULT_AMOUNT_SPENT)
            .amountSaved(DEFAULT_AMOUNT_SAVED)
            .amountEarned(DEFAULT_AMOUNT_EARNED)
            .happyPerson(DEFAULT_HAPPY_PERSON)
            .dob(DEFAULT_DOB)
            .createdDate(DEFAULT_CREATED_DATE)
            .travelDate(DEFAULT_TRAVEL_DATE)
            .travelTime(DEFAULT_TRAVEL_TIME)
            .customerType(DEFAULT_CUSTOMER_TYPE);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .distanceTravelled(UPDATED_DISTANCE_TRAVELLED)
            .amountSpent(UPDATED_AMOUNT_SPENT)
            .amountSaved(UPDATED_AMOUNT_SAVED)
            .amountEarned(UPDATED_AMOUNT_EARNED)
            .happyPerson(UPDATED_HAPPY_PERSON)
            .dob(UPDATED_DOB)
            .createdDate(UPDATED_CREATED_DATE)
            .travelDate(UPDATED_TRAVEL_DATE)
            .travelTime(UPDATED_TRAVEL_TIME)
            .customerType(UPDATED_CUSTOMER_TYPE);
        return customer;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Customer.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        customer = createEntity(em);
    }

    @Test
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().collectList().block().size();
        // Create the Customer
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCustomer.getDistanceTravelled()).isEqualTo(DEFAULT_DISTANCE_TRAVELLED);
        assertThat(testCustomer.getAmountSpent()).isEqualByComparingTo(DEFAULT_AMOUNT_SPENT);
        assertThat(testCustomer.getAmountSaved()).isEqualTo(DEFAULT_AMOUNT_SAVED);
        assertThat(testCustomer.getAmountEarned()).isEqualTo(DEFAULT_AMOUNT_EARNED);
        assertThat(testCustomer.getHappyPerson()).isEqualTo(DEFAULT_HAPPY_PERSON);
        assertThat(testCustomer.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCustomer.getTravelDate()).isEqualTo(DEFAULT_TRAVEL_DATE);
        assertThat(testCustomer.getTravelTime()).isEqualTo(DEFAULT_TRAVEL_TIME);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
    }

    @Test
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCustomersAsStream() {
        // Initialize the database
        customerRepository.save(customer).block();

        List<Customer> customerList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Customer.class)
            .getResponseBody()
            .filter(customer::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(customerList).isNotNull();
        assertThat(customerList).hasSize(1);
        Customer testCustomer = customerList.get(0);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCustomer.getDistanceTravelled()).isEqualTo(DEFAULT_DISTANCE_TRAVELLED);
        assertThat(testCustomer.getAmountSpent()).isEqualByComparingTo(DEFAULT_AMOUNT_SPENT);
        assertThat(testCustomer.getAmountSaved()).isEqualTo(DEFAULT_AMOUNT_SAVED);
        assertThat(testCustomer.getAmountEarned()).isEqualTo(DEFAULT_AMOUNT_EARNED);
        assertThat(testCustomer.getHappyPerson()).isEqualTo(DEFAULT_HAPPY_PERSON);
        assertThat(testCustomer.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCustomer.getTravelDate()).isEqualTo(DEFAULT_TRAVEL_DATE);
        assertThat(testCustomer.getTravelTime()).isEqualTo(DEFAULT_TRAVEL_TIME);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
    }

    @Test
    void getAllCustomers() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(customer.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].age")
            .value(hasItem(DEFAULT_AGE))
            .jsonPath("$.[*].distanceTravelled")
            .value(hasItem(DEFAULT_DISTANCE_TRAVELLED.intValue()))
            .jsonPath("$.[*].amountSpent")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_SPENT)))
            .jsonPath("$.[*].amountSaved")
            .value(hasItem(DEFAULT_AMOUNT_SAVED.doubleValue()))
            .jsonPath("$.[*].amountEarned")
            .value(hasItem(DEFAULT_AMOUNT_EARNED.doubleValue()))
            .jsonPath("$.[*].happyPerson")
            .value(hasItem(DEFAULT_HAPPY_PERSON.booleanValue()))
            .jsonPath("$.[*].dob")
            .value(hasItem(DEFAULT_DOB.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].travelDate")
            .value(hasItem(sameInstant(DEFAULT_TRAVEL_DATE)))
            .jsonPath("$.[*].travelTime")
            .value(hasItem(DEFAULT_TRAVEL_TIME.toString()))
            .jsonPath("$.[*].customerType")
            .value(hasItem(DEFAULT_CUSTOMER_TYPE.toString()));
    }

    @Test
    void getCustomer() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get the customer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(customer.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.age")
            .value(is(DEFAULT_AGE))
            .jsonPath("$.distanceTravelled")
            .value(is(DEFAULT_DISTANCE_TRAVELLED.intValue()))
            .jsonPath("$.amountSpent")
            .value(is(sameNumber(DEFAULT_AMOUNT_SPENT)))
            .jsonPath("$.amountSaved")
            .value(is(DEFAULT_AMOUNT_SAVED.doubleValue()))
            .jsonPath("$.amountEarned")
            .value(is(DEFAULT_AMOUNT_EARNED.doubleValue()))
            .jsonPath("$.happyPerson")
            .value(is(DEFAULT_HAPPY_PERSON.booleanValue()))
            .jsonPath("$.dob")
            .value(is(DEFAULT_DOB.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.travelDate")
            .value(is(sameInstant(DEFAULT_TRAVEL_DATE)))
            .jsonPath("$.travelTime")
            .value(is(DEFAULT_TRAVEL_TIME.toString()))
            .jsonPath("$.customerType")
            .value(is(DEFAULT_CUSTOMER_TYPE.toString()));
    }

    @Test
    void getCustomersByIdFiltering() {
        // Initialize the database
        customerRepository.save(customer).block();

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllCustomersByNameIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where name equals to DEFAULT_NAME
        defaultCustomerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllCustomersByNameIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllCustomersByNameIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where name is not null
        defaultCustomerShouldBeFound("name.specified=true");

        // Get all the customerList where name is null
        defaultCustomerShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllCustomersByNameContainsSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where name contains DEFAULT_NAME
        defaultCustomerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the customerList where name contains UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllCustomersByNameNotContainsSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where name does not contain DEFAULT_NAME
        defaultCustomerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the customerList where name does not contain UPDATED_NAME
        defaultCustomerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllCustomersByAgeIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age equals to DEFAULT_AGE
        defaultCustomerShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the customerList where age equals to UPDATED_AGE
        defaultCustomerShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    void getAllCustomersByAgeIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age in DEFAULT_AGE or UPDATED_AGE
        defaultCustomerShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the customerList where age equals to UPDATED_AGE
        defaultCustomerShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    void getAllCustomersByAgeIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age is not null
        defaultCustomerShouldBeFound("age.specified=true");

        // Get all the customerList where age is null
        defaultCustomerShouldNotBeFound("age.specified=false");
    }

    @Test
    void getAllCustomersByAgeIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age is greater than or equal to DEFAULT_AGE
        defaultCustomerShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the customerList where age is greater than or equal to UPDATED_AGE
        defaultCustomerShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    void getAllCustomersByAgeIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age is less than or equal to DEFAULT_AGE
        defaultCustomerShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the customerList where age is less than or equal to SMALLER_AGE
        defaultCustomerShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    void getAllCustomersByAgeIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age is less than DEFAULT_AGE
        defaultCustomerShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the customerList where age is less than UPDATED_AGE
        defaultCustomerShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    void getAllCustomersByAgeIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where age is greater than DEFAULT_AGE
        defaultCustomerShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the customerList where age is greater than SMALLER_AGE
        defaultCustomerShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }

    @Test
    void getAllCustomersByDistanceTravelledIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled equals to DEFAULT_DISTANCE_TRAVELLED
        defaultCustomerShouldBeFound("distanceTravelled.equals=" + DEFAULT_DISTANCE_TRAVELLED);

        // Get all the customerList where distanceTravelled equals to UPDATED_DISTANCE_TRAVELLED
        defaultCustomerShouldNotBeFound("distanceTravelled.equals=" + UPDATED_DISTANCE_TRAVELLED);
    }

    @Test
    void getAllCustomersByDistanceTravelledIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled in DEFAULT_DISTANCE_TRAVELLED or UPDATED_DISTANCE_TRAVELLED
        defaultCustomerShouldBeFound("distanceTravelled.in=" + DEFAULT_DISTANCE_TRAVELLED + "," + UPDATED_DISTANCE_TRAVELLED);

        // Get all the customerList where distanceTravelled equals to UPDATED_DISTANCE_TRAVELLED
        defaultCustomerShouldNotBeFound("distanceTravelled.in=" + UPDATED_DISTANCE_TRAVELLED);
    }

    @Test
    void getAllCustomersByDistanceTravelledIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled is not null
        defaultCustomerShouldBeFound("distanceTravelled.specified=true");

        // Get all the customerList where distanceTravelled is null
        defaultCustomerShouldNotBeFound("distanceTravelled.specified=false");
    }

    @Test
    void getAllCustomersByDistanceTravelledIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled is greater than or equal to DEFAULT_DISTANCE_TRAVELLED
        defaultCustomerShouldBeFound("distanceTravelled.greaterThanOrEqual=" + DEFAULT_DISTANCE_TRAVELLED);

        // Get all the customerList where distanceTravelled is greater than or equal to UPDATED_DISTANCE_TRAVELLED
        defaultCustomerShouldNotBeFound("distanceTravelled.greaterThanOrEqual=" + UPDATED_DISTANCE_TRAVELLED);
    }

    @Test
    void getAllCustomersByDistanceTravelledIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled is less than or equal to DEFAULT_DISTANCE_TRAVELLED
        defaultCustomerShouldBeFound("distanceTravelled.lessThanOrEqual=" + DEFAULT_DISTANCE_TRAVELLED);

        // Get all the customerList where distanceTravelled is less than or equal to SMALLER_DISTANCE_TRAVELLED
        defaultCustomerShouldNotBeFound("distanceTravelled.lessThanOrEqual=" + SMALLER_DISTANCE_TRAVELLED);
    }

    @Test
    void getAllCustomersByDistanceTravelledIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled is less than DEFAULT_DISTANCE_TRAVELLED
        defaultCustomerShouldNotBeFound("distanceTravelled.lessThan=" + DEFAULT_DISTANCE_TRAVELLED);

        // Get all the customerList where distanceTravelled is less than UPDATED_DISTANCE_TRAVELLED
        defaultCustomerShouldBeFound("distanceTravelled.lessThan=" + UPDATED_DISTANCE_TRAVELLED);
    }

    @Test
    void getAllCustomersByDistanceTravelledIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where distanceTravelled is greater than DEFAULT_DISTANCE_TRAVELLED
        defaultCustomerShouldNotBeFound("distanceTravelled.greaterThan=" + DEFAULT_DISTANCE_TRAVELLED);

        // Get all the customerList where distanceTravelled is greater than SMALLER_DISTANCE_TRAVELLED
        defaultCustomerShouldBeFound("distanceTravelled.greaterThan=" + SMALLER_DISTANCE_TRAVELLED);
    }

    @Test
    void getAllCustomersByAmountSpentIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent equals to DEFAULT_AMOUNT_SPENT
        defaultCustomerShouldBeFound("amountSpent.equals=" + DEFAULT_AMOUNT_SPENT);

        // Get all the customerList where amountSpent equals to UPDATED_AMOUNT_SPENT
        defaultCustomerShouldNotBeFound("amountSpent.equals=" + UPDATED_AMOUNT_SPENT);
    }

    @Test
    void getAllCustomersByAmountSpentIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent in DEFAULT_AMOUNT_SPENT or UPDATED_AMOUNT_SPENT
        defaultCustomerShouldBeFound("amountSpent.in=" + DEFAULT_AMOUNT_SPENT + "," + UPDATED_AMOUNT_SPENT);

        // Get all the customerList where amountSpent equals to UPDATED_AMOUNT_SPENT
        defaultCustomerShouldNotBeFound("amountSpent.in=" + UPDATED_AMOUNT_SPENT);
    }

    @Test
    void getAllCustomersByAmountSpentIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent is not null
        defaultCustomerShouldBeFound("amountSpent.specified=true");

        // Get all the customerList where amountSpent is null
        defaultCustomerShouldNotBeFound("amountSpent.specified=false");
    }

    @Test
    void getAllCustomersByAmountSpentIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent is greater than or equal to DEFAULT_AMOUNT_SPENT
        defaultCustomerShouldBeFound("amountSpent.greaterThanOrEqual=" + DEFAULT_AMOUNT_SPENT);

        // Get all the customerList where amountSpent is greater than or equal to UPDATED_AMOUNT_SPENT
        defaultCustomerShouldNotBeFound("amountSpent.greaterThanOrEqual=" + UPDATED_AMOUNT_SPENT);
    }

    @Test
    void getAllCustomersByAmountSpentIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent is less than or equal to DEFAULT_AMOUNT_SPENT
        defaultCustomerShouldBeFound("amountSpent.lessThanOrEqual=" + DEFAULT_AMOUNT_SPENT);

        // Get all the customerList where amountSpent is less than or equal to SMALLER_AMOUNT_SPENT
        defaultCustomerShouldNotBeFound("amountSpent.lessThanOrEqual=" + SMALLER_AMOUNT_SPENT);
    }

    @Test
    void getAllCustomersByAmountSpentIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent is less than DEFAULT_AMOUNT_SPENT
        defaultCustomerShouldNotBeFound("amountSpent.lessThan=" + DEFAULT_AMOUNT_SPENT);

        // Get all the customerList where amountSpent is less than UPDATED_AMOUNT_SPENT
        defaultCustomerShouldBeFound("amountSpent.lessThan=" + UPDATED_AMOUNT_SPENT);
    }

    @Test
    void getAllCustomersByAmountSpentIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSpent is greater than DEFAULT_AMOUNT_SPENT
        defaultCustomerShouldNotBeFound("amountSpent.greaterThan=" + DEFAULT_AMOUNT_SPENT);

        // Get all the customerList where amountSpent is greater than SMALLER_AMOUNT_SPENT
        defaultCustomerShouldBeFound("amountSpent.greaterThan=" + SMALLER_AMOUNT_SPENT);
    }

    @Test
    void getAllCustomersByAmountSavedIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved equals to DEFAULT_AMOUNT_SAVED
        defaultCustomerShouldBeFound("amountSaved.equals=" + DEFAULT_AMOUNT_SAVED);

        // Get all the customerList where amountSaved equals to UPDATED_AMOUNT_SAVED
        defaultCustomerShouldNotBeFound("amountSaved.equals=" + UPDATED_AMOUNT_SAVED);
    }

    @Test
    void getAllCustomersByAmountSavedIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved in DEFAULT_AMOUNT_SAVED or UPDATED_AMOUNT_SAVED
        defaultCustomerShouldBeFound("amountSaved.in=" + DEFAULT_AMOUNT_SAVED + "," + UPDATED_AMOUNT_SAVED);

        // Get all the customerList where amountSaved equals to UPDATED_AMOUNT_SAVED
        defaultCustomerShouldNotBeFound("amountSaved.in=" + UPDATED_AMOUNT_SAVED);
    }

    @Test
    void getAllCustomersByAmountSavedIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved is not null
        defaultCustomerShouldBeFound("amountSaved.specified=true");

        // Get all the customerList where amountSaved is null
        defaultCustomerShouldNotBeFound("amountSaved.specified=false");
    }

    @Test
    void getAllCustomersByAmountSavedIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved is greater than or equal to DEFAULT_AMOUNT_SAVED
        defaultCustomerShouldBeFound("amountSaved.greaterThanOrEqual=" + DEFAULT_AMOUNT_SAVED);

        // Get all the customerList where amountSaved is greater than or equal to UPDATED_AMOUNT_SAVED
        defaultCustomerShouldNotBeFound("amountSaved.greaterThanOrEqual=" + UPDATED_AMOUNT_SAVED);
    }

    @Test
    void getAllCustomersByAmountSavedIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved is less than or equal to DEFAULT_AMOUNT_SAVED
        defaultCustomerShouldBeFound("amountSaved.lessThanOrEqual=" + DEFAULT_AMOUNT_SAVED);

        // Get all the customerList where amountSaved is less than or equal to SMALLER_AMOUNT_SAVED
        defaultCustomerShouldNotBeFound("amountSaved.lessThanOrEqual=" + SMALLER_AMOUNT_SAVED);
    }

    @Test
    void getAllCustomersByAmountSavedIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved is less than DEFAULT_AMOUNT_SAVED
        defaultCustomerShouldNotBeFound("amountSaved.lessThan=" + DEFAULT_AMOUNT_SAVED);

        // Get all the customerList where amountSaved is less than UPDATED_AMOUNT_SAVED
        defaultCustomerShouldBeFound("amountSaved.lessThan=" + UPDATED_AMOUNT_SAVED);
    }

    @Test
    void getAllCustomersByAmountSavedIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountSaved is greater than DEFAULT_AMOUNT_SAVED
        defaultCustomerShouldNotBeFound("amountSaved.greaterThan=" + DEFAULT_AMOUNT_SAVED);

        // Get all the customerList where amountSaved is greater than SMALLER_AMOUNT_SAVED
        defaultCustomerShouldBeFound("amountSaved.greaterThan=" + SMALLER_AMOUNT_SAVED);
    }

    @Test
    void getAllCustomersByAmountEarnedIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned equals to DEFAULT_AMOUNT_EARNED
        defaultCustomerShouldBeFound("amountEarned.equals=" + DEFAULT_AMOUNT_EARNED);

        // Get all the customerList where amountEarned equals to UPDATED_AMOUNT_EARNED
        defaultCustomerShouldNotBeFound("amountEarned.equals=" + UPDATED_AMOUNT_EARNED);
    }

    @Test
    void getAllCustomersByAmountEarnedIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned in DEFAULT_AMOUNT_EARNED or UPDATED_AMOUNT_EARNED
        defaultCustomerShouldBeFound("amountEarned.in=" + DEFAULT_AMOUNT_EARNED + "," + UPDATED_AMOUNT_EARNED);

        // Get all the customerList where amountEarned equals to UPDATED_AMOUNT_EARNED
        defaultCustomerShouldNotBeFound("amountEarned.in=" + UPDATED_AMOUNT_EARNED);
    }

    @Test
    void getAllCustomersByAmountEarnedIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned is not null
        defaultCustomerShouldBeFound("amountEarned.specified=true");

        // Get all the customerList where amountEarned is null
        defaultCustomerShouldNotBeFound("amountEarned.specified=false");
    }

    @Test
    void getAllCustomersByAmountEarnedIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned is greater than or equal to DEFAULT_AMOUNT_EARNED
        defaultCustomerShouldBeFound("amountEarned.greaterThanOrEqual=" + DEFAULT_AMOUNT_EARNED);

        // Get all the customerList where amountEarned is greater than or equal to UPDATED_AMOUNT_EARNED
        defaultCustomerShouldNotBeFound("amountEarned.greaterThanOrEqual=" + UPDATED_AMOUNT_EARNED);
    }

    @Test
    void getAllCustomersByAmountEarnedIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned is less than or equal to DEFAULT_AMOUNT_EARNED
        defaultCustomerShouldBeFound("amountEarned.lessThanOrEqual=" + DEFAULT_AMOUNT_EARNED);

        // Get all the customerList where amountEarned is less than or equal to SMALLER_AMOUNT_EARNED
        defaultCustomerShouldNotBeFound("amountEarned.lessThanOrEqual=" + SMALLER_AMOUNT_EARNED);
    }

    @Test
    void getAllCustomersByAmountEarnedIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned is less than DEFAULT_AMOUNT_EARNED
        defaultCustomerShouldNotBeFound("amountEarned.lessThan=" + DEFAULT_AMOUNT_EARNED);

        // Get all the customerList where amountEarned is less than UPDATED_AMOUNT_EARNED
        defaultCustomerShouldBeFound("amountEarned.lessThan=" + UPDATED_AMOUNT_EARNED);
    }

    @Test
    void getAllCustomersByAmountEarnedIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where amountEarned is greater than DEFAULT_AMOUNT_EARNED
        defaultCustomerShouldNotBeFound("amountEarned.greaterThan=" + DEFAULT_AMOUNT_EARNED);

        // Get all the customerList where amountEarned is greater than SMALLER_AMOUNT_EARNED
        defaultCustomerShouldBeFound("amountEarned.greaterThan=" + SMALLER_AMOUNT_EARNED);
    }

    @Test
    void getAllCustomersByHappyPersonIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where happyPerson equals to DEFAULT_HAPPY_PERSON
        defaultCustomerShouldBeFound("happyPerson.equals=" + DEFAULT_HAPPY_PERSON);

        // Get all the customerList where happyPerson equals to UPDATED_HAPPY_PERSON
        defaultCustomerShouldNotBeFound("happyPerson.equals=" + UPDATED_HAPPY_PERSON);
    }

    @Test
    void getAllCustomersByHappyPersonIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where happyPerson in DEFAULT_HAPPY_PERSON or UPDATED_HAPPY_PERSON
        defaultCustomerShouldBeFound("happyPerson.in=" + DEFAULT_HAPPY_PERSON + "," + UPDATED_HAPPY_PERSON);

        // Get all the customerList where happyPerson equals to UPDATED_HAPPY_PERSON
        defaultCustomerShouldNotBeFound("happyPerson.in=" + UPDATED_HAPPY_PERSON);
    }

    @Test
    void getAllCustomersByHappyPersonIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where happyPerson is not null
        defaultCustomerShouldBeFound("happyPerson.specified=true");

        // Get all the customerList where happyPerson is null
        defaultCustomerShouldNotBeFound("happyPerson.specified=false");
    }

    @Test
    void getAllCustomersByDobIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where dob equals to DEFAULT_DOB
        defaultCustomerShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the customerList where dob equals to UPDATED_DOB
        defaultCustomerShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    void getAllCustomersByDobIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultCustomerShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the customerList where dob equals to UPDATED_DOB
        defaultCustomerShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    void getAllCustomersByDobIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where dob is not null
        defaultCustomerShouldBeFound("dob.specified=true");

        // Get all the customerList where dob is null
        defaultCustomerShouldNotBeFound("dob.specified=false");
    }

    @Test
    void getAllCustomersByCreatedDateIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the customerList where createdDate equals to UPDATED_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllCustomersByCreatedDateIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the customerList where createdDate equals to UPDATED_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllCustomersByCreatedDateIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate is not null
        defaultCustomerShouldBeFound("createdDate.specified=true");

        // Get all the customerList where createdDate is null
        defaultCustomerShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    void getAllCustomersByCreatedDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the customerList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllCustomersByCreatedDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the customerList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    void getAllCustomersByCreatedDateIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate is less than DEFAULT_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the customerList where createdDate is less than UPDATED_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllCustomersByCreatedDateIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the customerList where createdDate is greater than SMALLER_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    void getAllCustomersByTravelDateIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate equals to DEFAULT_TRAVEL_DATE
        defaultCustomerShouldBeFound("travelDate.equals=" + DEFAULT_TRAVEL_DATE);

        // Get all the customerList where travelDate equals to UPDATED_TRAVEL_DATE
        defaultCustomerShouldNotBeFound("travelDate.equals=" + UPDATED_TRAVEL_DATE);
    }

    @Test
    void getAllCustomersByTravelDateIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate in DEFAULT_TRAVEL_DATE or UPDATED_TRAVEL_DATE
        defaultCustomerShouldBeFound("travelDate.in=" + DEFAULT_TRAVEL_DATE + "," + UPDATED_TRAVEL_DATE);

        // Get all the customerList where travelDate equals to UPDATED_TRAVEL_DATE
        defaultCustomerShouldNotBeFound("travelDate.in=" + UPDATED_TRAVEL_DATE);
    }

    @Test
    void getAllCustomersByTravelDateIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate is not null
        defaultCustomerShouldBeFound("travelDate.specified=true");

        // Get all the customerList where travelDate is null
        defaultCustomerShouldNotBeFound("travelDate.specified=false");
    }

    @Test
    void getAllCustomersByTravelDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate is greater than or equal to DEFAULT_TRAVEL_DATE
        defaultCustomerShouldBeFound("travelDate.greaterThanOrEqual=" + DEFAULT_TRAVEL_DATE);

        // Get all the customerList where travelDate is greater than or equal to UPDATED_TRAVEL_DATE
        defaultCustomerShouldNotBeFound("travelDate.greaterThanOrEqual=" + UPDATED_TRAVEL_DATE);
    }

    @Test
    void getAllCustomersByTravelDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate is less than or equal to DEFAULT_TRAVEL_DATE
        defaultCustomerShouldBeFound("travelDate.lessThanOrEqual=" + DEFAULT_TRAVEL_DATE);

        // Get all the customerList where travelDate is less than or equal to SMALLER_TRAVEL_DATE
        defaultCustomerShouldNotBeFound("travelDate.lessThanOrEqual=" + SMALLER_TRAVEL_DATE);
    }

    @Test
    void getAllCustomersByTravelDateIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate is less than DEFAULT_TRAVEL_DATE
        defaultCustomerShouldNotBeFound("travelDate.lessThan=" + DEFAULT_TRAVEL_DATE);

        // Get all the customerList where travelDate is less than UPDATED_TRAVEL_DATE
        defaultCustomerShouldBeFound("travelDate.lessThan=" + UPDATED_TRAVEL_DATE);
    }

    @Test
    void getAllCustomersByTravelDateIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelDate is greater than DEFAULT_TRAVEL_DATE
        defaultCustomerShouldNotBeFound("travelDate.greaterThan=" + DEFAULT_TRAVEL_DATE);

        // Get all the customerList where travelDate is greater than SMALLER_TRAVEL_DATE
        defaultCustomerShouldBeFound("travelDate.greaterThan=" + SMALLER_TRAVEL_DATE);
    }

    @Test
    void getAllCustomersByTravelTimeIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime equals to DEFAULT_TRAVEL_TIME
        defaultCustomerShouldBeFound("travelTime.equals=" + DEFAULT_TRAVEL_TIME);

        // Get all the customerList where travelTime equals to UPDATED_TRAVEL_TIME
        defaultCustomerShouldNotBeFound("travelTime.equals=" + UPDATED_TRAVEL_TIME);
    }

    @Test
    void getAllCustomersByTravelTimeIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime in DEFAULT_TRAVEL_TIME or UPDATED_TRAVEL_TIME
        defaultCustomerShouldBeFound("travelTime.in=" + DEFAULT_TRAVEL_TIME + "," + UPDATED_TRAVEL_TIME);

        // Get all the customerList where travelTime equals to UPDATED_TRAVEL_TIME
        defaultCustomerShouldNotBeFound("travelTime.in=" + UPDATED_TRAVEL_TIME);
    }

    @Test
    void getAllCustomersByTravelTimeIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime is not null
        defaultCustomerShouldBeFound("travelTime.specified=true");

        // Get all the customerList where travelTime is null
        defaultCustomerShouldNotBeFound("travelTime.specified=false");
    }

    @Test
    void getAllCustomersByTravelTimeIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime is greater than or equal to DEFAULT_TRAVEL_TIME
        defaultCustomerShouldBeFound("travelTime.greaterThanOrEqual=" + DEFAULT_TRAVEL_TIME);

        // Get all the customerList where travelTime is greater than or equal to UPDATED_TRAVEL_TIME
        defaultCustomerShouldNotBeFound("travelTime.greaterThanOrEqual=" + UPDATED_TRAVEL_TIME);
    }

    @Test
    void getAllCustomersByTravelTimeIsLessThanOrEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime is less than or equal to DEFAULT_TRAVEL_TIME
        defaultCustomerShouldBeFound("travelTime.lessThanOrEqual=" + DEFAULT_TRAVEL_TIME);

        // Get all the customerList where travelTime is less than or equal to SMALLER_TRAVEL_TIME
        defaultCustomerShouldNotBeFound("travelTime.lessThanOrEqual=" + SMALLER_TRAVEL_TIME);
    }

    @Test
    void getAllCustomersByTravelTimeIsLessThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime is less than DEFAULT_TRAVEL_TIME
        defaultCustomerShouldNotBeFound("travelTime.lessThan=" + DEFAULT_TRAVEL_TIME);

        // Get all the customerList where travelTime is less than UPDATED_TRAVEL_TIME
        defaultCustomerShouldBeFound("travelTime.lessThan=" + UPDATED_TRAVEL_TIME);
    }

    @Test
    void getAllCustomersByTravelTimeIsGreaterThanSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where travelTime is greater than DEFAULT_TRAVEL_TIME
        defaultCustomerShouldNotBeFound("travelTime.greaterThan=" + DEFAULT_TRAVEL_TIME);

        // Get all the customerList where travelTime is greater than SMALLER_TRAVEL_TIME
        defaultCustomerShouldBeFound("travelTime.greaterThan=" + SMALLER_TRAVEL_TIME);
    }

    @Test
    void getAllCustomersByCustomerTypeIsEqualToSomething() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where customerType equals to DEFAULT_CUSTOMER_TYPE
        defaultCustomerShouldBeFound("customerType.equals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldNotBeFound("customerType.equals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    void getAllCustomersByCustomerTypeIsInShouldWork() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where customerType in DEFAULT_CUSTOMER_TYPE or UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldBeFound("customerType.in=" + DEFAULT_CUSTOMER_TYPE + "," + UPDATED_CUSTOMER_TYPE);

        // Get all the customerList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldNotBeFound("customerType.in=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    void getAllCustomersByCustomerTypeIsNullOrNotNull() {
        // Initialize the database
        customerRepository.save(customer).block();

        // Get all the customerList where customerType is not null
        defaultCustomerShouldBeFound("customerType.specified=true");

        // Get all the customerList where customerType is null
        defaultCustomerShouldNotBeFound("customerType.specified=false");
    }

    /*  No support for querying the non-owning side
    @Test
    void getAllCustomersByAddressIsEqualToSomething() {
        Address address = AddressResourceIT.createEntity(em);
        em.persist(address);
        em.flush();
        customer.addAddress(address);
        customerRepository.saveAndFlush(customer);
        Long addressId = address.getId();

        // Get all the customerList where address equals to addressId
        defaultCustomerShouldBeFound("addressId.equals=" + addressId);

        // Get all the customerList where address equals to (addressId + 1)
        defaultCustomerShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }
*/
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(customer.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].age")
            .value(hasItem(DEFAULT_AGE))
            .jsonPath("$.[*].distanceTravelled")
            .value(hasItem(DEFAULT_DISTANCE_TRAVELLED.intValue()))
            .jsonPath("$.[*].amountSpent")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT_SPENT)))
            .jsonPath("$.[*].amountSaved")
            .value(hasItem(DEFAULT_AMOUNT_SAVED.doubleValue()))
            .jsonPath("$.[*].amountEarned")
            .value(hasItem(DEFAULT_AMOUNT_EARNED.doubleValue()))
            .jsonPath("$.[*].happyPerson")
            .value(hasItem(DEFAULT_HAPPY_PERSON.booleanValue()))
            .jsonPath("$.[*].dob")
            .value(hasItem(DEFAULT_DOB.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].travelDate")
            .value(hasItem(sameInstant(DEFAULT_TRAVEL_DATE)))
            .jsonPath("$.[*].travelTime")
            .value(hasItem(DEFAULT_TRAVEL_TIME.toString()))
            .jsonPath("$.[*].customerType")
            .value(hasItem(DEFAULT_CUSTOMER_TYPE.toString()));
        // Implement this as a next step
        // Check, that the count call also returns 1
        /*webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json("1");*/
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();
        // Implement this as a next step
        // Check, that the count call also returns 0
        /*webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json("0");*/
    }

    @Test
    void getNonExistingCustomer() {
        // Get the customer
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).block();
        updatedCustomer
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .distanceTravelled(UPDATED_DISTANCE_TRAVELLED)
            .amountSpent(UPDATED_AMOUNT_SPENT)
            .amountSaved(UPDATED_AMOUNT_SAVED)
            .amountEarned(UPDATED_AMOUNT_EARNED)
            .happyPerson(UPDATED_HAPPY_PERSON)
            .dob(UPDATED_DOB)
            .createdDate(UPDATED_CREATED_DATE)
            .travelDate(UPDATED_TRAVEL_DATE)
            .travelTime(UPDATED_TRAVEL_TIME)
            .customerType(UPDATED_CUSTOMER_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCustomer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCustomer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCustomer.getDistanceTravelled()).isEqualTo(UPDATED_DISTANCE_TRAVELLED);
        assertThat(testCustomer.getAmountSpent()).isEqualByComparingTo(UPDATED_AMOUNT_SPENT);
        assertThat(testCustomer.getAmountSaved()).isEqualTo(UPDATED_AMOUNT_SAVED);
        assertThat(testCustomer.getAmountEarned()).isEqualTo(UPDATED_AMOUNT_EARNED);
        assertThat(testCustomer.getHappyPerson()).isEqualTo(UPDATED_HAPPY_PERSON);
        assertThat(testCustomer.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustomer.getTravelDate()).isEqualTo(UPDATED_TRAVEL_DATE);
        assertThat(testCustomer.getTravelTime()).isEqualTo(UPDATED_TRAVEL_TIME);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
    }

    @Test
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.name(UPDATED_NAME).amountSaved(UPDATED_AMOUNT_SAVED).dob(UPDATED_DOB).createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCustomer.getDistanceTravelled()).isEqualTo(DEFAULT_DISTANCE_TRAVELLED);
        assertThat(testCustomer.getAmountSpent()).isEqualByComparingTo(DEFAULT_AMOUNT_SPENT);
        assertThat(testCustomer.getAmountSaved()).isEqualTo(UPDATED_AMOUNT_SAVED);
        assertThat(testCustomer.getAmountEarned()).isEqualTo(DEFAULT_AMOUNT_EARNED);
        assertThat(testCustomer.getHappyPerson()).isEqualTo(DEFAULT_HAPPY_PERSON);
        assertThat(testCustomer.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustomer.getTravelDate()).isEqualTo(DEFAULT_TRAVEL_DATE);
        assertThat(testCustomer.getTravelTime()).isEqualTo(DEFAULT_TRAVEL_TIME);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
    }

    @Test
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .distanceTravelled(UPDATED_DISTANCE_TRAVELLED)
            .amountSpent(UPDATED_AMOUNT_SPENT)
            .amountSaved(UPDATED_AMOUNT_SAVED)
            .amountEarned(UPDATED_AMOUNT_EARNED)
            .happyPerson(UPDATED_HAPPY_PERSON)
            .dob(UPDATED_DOB)
            .createdDate(UPDATED_CREATED_DATE)
            .travelDate(UPDATED_TRAVEL_DATE)
            .travelTime(UPDATED_TRAVEL_TIME)
            .customerType(UPDATED_CUSTOMER_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCustomer.getDistanceTravelled()).isEqualTo(UPDATED_DISTANCE_TRAVELLED);
        assertThat(testCustomer.getAmountSpent()).isEqualByComparingTo(UPDATED_AMOUNT_SPENT);
        assertThat(testCustomer.getAmountSaved()).isEqualTo(UPDATED_AMOUNT_SAVED);
        assertThat(testCustomer.getAmountEarned()).isEqualTo(UPDATED_AMOUNT_EARNED);
        assertThat(testCustomer.getHappyPerson()).isEqualTo(UPDATED_HAPPY_PERSON);
        assertThat(testCustomer.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustomer.getTravelDate()).isEqualTo(UPDATED_TRAVEL_DATE);
        assertThat(testCustomer.getTravelTime()).isEqualTo(UPDATED_TRAVEL_TIME);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
    }

    @Test
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().collectList().block().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customer))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCustomer() {
        // Initialize the database
        customerRepository.save(customer).block();

        int databaseSizeBeforeDelete = customerRepository.findAll().collectList().block().size();

        // Delete the customer
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, customer.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll().collectList().block();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
