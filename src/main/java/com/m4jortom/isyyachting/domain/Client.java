package com.m4jortom.isyyachting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m4jortom.isyyachting.domain.enumeration.Nationality;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality")
    private Nationality nationality;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "day", "client", "rental" }, allowSetters = true)
    private Set<Reservation> reservationLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Client firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Client lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public Client phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Nationality getNationality() {
        return this.nationality;
    }

    public Client nationality(Nationality nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Set<Reservation> getReservationLists() {
        return this.reservationLists;
    }

    public void setReservationLists(Set<Reservation> reservations) {
        if (this.reservationLists != null) {
            this.reservationLists.forEach(i -> i.setClient(null));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.setClient(this));
        }
        this.reservationLists = reservations;
    }

    public Client reservationLists(Set<Reservation> reservations) {
        this.setReservationLists(reservations);
        return this;
    }

    public Client addReservationList(Reservation reservation) {
        this.reservationLists.add(reservation);
        reservation.setClient(this);
        return this;
    }

    public Client removeReservationList(Reservation reservation) {
        this.reservationLists.remove(reservation);
        reservation.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", nationality='" + getNationality() + "'" +
            "}";
    }
}
