package com.m4jortom.isyyachting.repository;

import com.m4jortom.isyyachting.domain.Day;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Day entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DayRepository extends JpaRepository<Day, Long> {}
