package com.m4jortom.isyyachting.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.m4jortom.isyyachting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentalPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentalPrice.class);
        RentalPrice rentalPrice1 = new RentalPrice();
        rentalPrice1.setId(1L);
        RentalPrice rentalPrice2 = new RentalPrice();
        rentalPrice2.setId(rentalPrice1.getId());
        assertThat(rentalPrice1).isEqualTo(rentalPrice2);
        rentalPrice2.setId(2L);
        assertThat(rentalPrice1).isNotEqualTo(rentalPrice2);
        rentalPrice1.setId(null);
        assertThat(rentalPrice1).isNotEqualTo(rentalPrice2);
    }
}
