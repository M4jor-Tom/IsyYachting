package com.m4jortom.isyyachting.repository;

import com.m4jortom.isyyachting.domain.Rental;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {}
