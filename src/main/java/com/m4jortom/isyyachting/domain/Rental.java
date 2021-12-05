package com.m4jortom.isyyachting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "begining_instant", nullable = false)
    private Instant beginingInstant;

    @NotNull
    @Column(name = "ending_instant", nullable = false)
    private Instant endingInstant;

    @OneToMany(mappedBy = "rental")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "day", "client", "rental" }, allowSetters = true)
    private Set<Reservation> reservationLists = new HashSet<>();

    @OneToMany(mappedBy = "rental")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rental" }, allowSetters = true)
    private Set<RentalPrice> rentalPriceLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rental id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBeginingInstant() {
        return this.beginingInstant;
    }

    public Rental beginingInstant(Instant beginingInstant) {
        this.setBeginingInstant(beginingInstant);
        return this;
    }

    public void setBeginingInstant(Instant beginingInstant) {
        this.beginingInstant = beginingInstant;
    }

    public Instant getEndingInstant() {
        return this.endingInstant;
    }

    public Rental endingInstant(Instant endingInstant) {
        this.setEndingInstant(endingInstant);
        return this;
    }

    public void setEndingInstant(Instant endingInstant) {
        this.endingInstant = endingInstant;
    }

    public Set<Reservation> getReservationLists() {
        return this.reservationLists;
    }

    public void setReservationLists(Set<Reservation> reservations) {
        if (this.reservationLists != null) {
            this.reservationLists.forEach(i -> i.setRental(null));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.setRental(this));
        }
        this.reservationLists = reservations;
    }

    public Rental reservationLists(Set<Reservation> reservations) {
        this.setReservationLists(reservations);
        return this;
    }

    public Rental addReservationList(Reservation reservation) {
        this.reservationLists.add(reservation);
        reservation.setRental(this);
        return this;
    }

    public Rental removeReservationList(Reservation reservation) {
        this.reservationLists.remove(reservation);
        reservation.setRental(null);
        return this;
    }

    public Set<RentalPrice> getRentalPriceLists() {
        return this.rentalPriceLists;
    }

    public void setRentalPriceLists(Set<RentalPrice> rentalPrices) {
        if (this.rentalPriceLists != null) {
            this.rentalPriceLists.forEach(i -> i.setRental(null));
        }
        if (rentalPrices != null) {
            rentalPrices.forEach(i -> i.setRental(this));
        }
        this.rentalPriceLists = rentalPrices;
    }

    public Rental rentalPriceLists(Set<RentalPrice> rentalPrices) {
        this.setRentalPriceLists(rentalPrices);
        return this;
    }

    public Rental addRentalPriceList(RentalPrice rentalPrice) {
        this.rentalPriceLists.add(rentalPrice);
        rentalPrice.setRental(this);
        return this;
    }

    public Rental removeRentalPriceList(RentalPrice rentalPrice) {
        this.rentalPriceLists.remove(rentalPrice);
        rentalPrice.setRental(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        return id != null && id.equals(((Rental) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rental{" +
            "id=" + getId() +
            ", beginingInstant='" + getBeginingInstant() + "'" +
            ", endingInstant='" + getEndingInstant() + "'" +
            "}";
    }
}
