package com.m4jortom.isyyachting.repository;

import com.m4jortom.isyyachting.domain.RentalPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RentalPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalPriceRepository extends JpaRepository<RentalPrice, Long> {}
