package com.m4jortom.isyyachting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m4jortom.isyyachting.domain.enumeration.Season;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RentalPrice.
 */
@Entity
@Table(name = "rental_price")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RentalPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private Double value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false)
    private Season season;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "reservationLists", "rentalPriceLists" }, allowSetters = true)
    private Rental rental;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RentalPrice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public RentalPrice value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Season getSeason() {
        return this.season;
    }

    public RentalPrice season(Season season) {
        this.setSeason(season);
        return this;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Rental getRental() {
        return this.rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public RentalPrice rental(Rental rental) {
        this.setRental(rental);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalPrice)) {
            return false;
        }
        return id != null && id.equals(((RentalPrice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalPrice{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", season='" + getSeason() + "'" +
            "}";
    }
}
