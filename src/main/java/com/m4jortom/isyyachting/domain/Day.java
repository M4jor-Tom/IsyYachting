package com.m4jortom.isyyachting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m4jortom.isyyachting.domain.enumeration.CancelingWeather;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Day.
 */
@Entity
@Table(name = "day")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Day implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private Instant time;

    @Enumerated(EnumType.STRING)
    @Column(name = "forecasted_weather")
    private CancelingWeather forecastedWeather;

    @OneToMany(mappedBy = "day")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "day", "client", "rental" }, allowSetters = true)
    private Set<Reservation> reservationLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Day id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return this.time;
    }

    public Day time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public CancelingWeather getForecastedWeather() {
        return this.forecastedWeather;
    }

    public Day forecastedWeather(CancelingWeather forecastedWeather) {
        this.setForecastedWeather(forecastedWeather);
        return this;
    }

    public void setForecastedWeather(CancelingWeather forecastedWeather) {
        this.forecastedWeather = forecastedWeather;
    }

    public Set<Reservation> getReservationLists() {
        return this.reservationLists;
    }

    public void setReservationLists(Set<Reservation> reservations) {
        if (this.reservationLists != null) {
            this.reservationLists.forEach(i -> i.setDay(null));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.setDay(this));
        }
        this.reservationLists = reservations;
    }

    public Day reservationLists(Set<Reservation> reservations) {
        this.setReservationLists(reservations);
        return this;
    }

    public Day addReservationList(Reservation reservation) {
        this.reservationLists.add(reservation);
        reservation.setDay(this);
        return this;
    }

    public Day removeReservationList(Reservation reservation) {
        this.reservationLists.remove(reservation);
        reservation.setDay(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Day)) {
            return false;
        }
        return id != null && id.equals(((Day) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Day{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", forecastedWeather='" + getForecastedWeather() + "'" +
            "}";
    }
}
