package com.m4jortom.isyyachting.web.rest;

import com.m4jortom.isyyachting.domain.RentalPrice;
import com.m4jortom.isyyachting.repository.RentalPriceRepository;
import com.m4jortom.isyyachting.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.m4jortom.isyyachting.domain.RentalPrice}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RentalPriceResource {

    private final Logger log = LoggerFactory.getLogger(RentalPriceResource.class);

    private static final String ENTITY_NAME = "rentalPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalPriceRepository rentalPriceRepository;

    public RentalPriceResource(RentalPriceRepository rentalPriceRepository) {
        this.rentalPriceRepository = rentalPriceRepository;
    }

    /**
     * {@code POST  /rental-prices} : Create a new rentalPrice.
     *
     * @param rentalPrice the rentalPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentalPrice, or with status {@code 400 (Bad Request)} if the rentalPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rental-prices")
    public ResponseEntity<RentalPrice> createRentalPrice(@Valid @RequestBody RentalPrice rentalPrice) throws URISyntaxException {
        log.debug("REST request to save RentalPrice : {}", rentalPrice);
        if (rentalPrice.getId() != null) {
            throw new BadRequestAlertException("A new rentalPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentalPrice result = rentalPriceRepository.save(rentalPrice);
        return ResponseEntity
            .created(new URI("/api/rental-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rental-prices/:id} : Updates an existing rentalPrice.
     *
     * @param id the id of the rentalPrice to save.
     * @param rentalPrice the rentalPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalPrice,
     * or with status {@code 400 (Bad Request)} if the rentalPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rental-prices/{id}")
    public ResponseEntity<RentalPrice> updateRentalPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RentalPrice rentalPrice
    ) throws URISyntaxException {
        log.debug("REST request to update RentalPrice : {}, {}", id, rentalPrice);
        if (rentalPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RentalPrice result = rentalPriceRepository.save(rentalPrice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalPrice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rental-prices/:id} : Partial updates given fields of an existing rentalPrice, field will ignore if it is null
     *
     * @param id the id of the rentalPrice to save.
     * @param rentalPrice the rentalPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalPrice,
     * or with status {@code 400 (Bad Request)} if the rentalPrice is not valid,
     * or with status {@code 404 (Not Found)} if the rentalPrice is not found,
     * or with status {@code 500 (Internal Server Error)} if the rentalPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rental-prices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RentalPrice> partialUpdateRentalPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RentalPrice rentalPrice
    ) throws URISyntaxException {
        log.debug("REST request to partial update RentalPrice partially : {}, {}", id, rentalPrice);
        if (rentalPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RentalPrice> result = rentalPriceRepository
            .findById(rentalPrice.getId())
            .map(existingRentalPrice -> {
                if (rentalPrice.getValue() != null) {
                    existingRentalPrice.setValue(rentalPrice.getValue());
                }
                if (rentalPrice.getSeason() != null) {
                    existingRentalPrice.setSeason(rentalPrice.getSeason());
                }

                return existingRentalPrice;
            })
            .map(rentalPriceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalPrice.getId().toString())
        );
    }

    /**
     * {@code GET  /rental-prices} : get all the rentalPrices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentalPrices in body.
     */
    @GetMapping("/rental-prices")
    public List<RentalPrice> getAllRentalPrices() {
        log.debug("REST request to get all RentalPrices");
        return rentalPriceRepository.findAll();
    }

    /**
     * {@code GET  /rental-prices/:id} : get the "id" rentalPrice.
     *
     * @param id the id of the rentalPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalPrice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rental-prices/{id}")
    public ResponseEntity<RentalPrice> getRentalPrice(@PathVariable Long id) {
        log.debug("REST request to get RentalPrice : {}", id);
        Optional<RentalPrice> rentalPrice = rentalPriceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rentalPrice);
    }

    /**
     * {@code DELETE  /rental-prices/:id} : delete the "id" rentalPrice.
     *
     * @param id the id of the rentalPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rental-prices/{id}")
    public ResponseEntity<Void> deleteRentalPrice(@PathVariable Long id) {
        log.debug("REST request to delete RentalPrice : {}", id);
        rentalPriceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
