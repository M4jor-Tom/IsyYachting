<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="reservation">
        <h2 class="jh-entity-heading" data-cy="reservationDetailsHeading">
          <span v-text="$t('isyYachtingApp.reservation.detail.title')">Reservation</span> {{ reservation.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('isyYachtingApp.reservation.day')">Day</span>
          </dt>
          <dd>
            <div v-if="reservation.day">
              <router-link :to="{ name: 'DayView', params: { dayId: reservation.day.id } }">{{ reservation.day.time }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.reservation.client')">Client</span>
          </dt>
          <dd>
            <div v-if="reservation.client">
              <router-link :to="{ name: 'ClientView', params: { clientId: reservation.client.id } }">{{
                reservation.client.lastName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.reservation.rental')">Rental</span>
          </dt>
          <dd>
            <div v-if="reservation.rental">
              <router-link :to="{ name: 'RentalView', params: { rentalId: reservation.rental.id } }">{{
                reservation.rental.beginingInstant
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="reservation.id"
          :to="{ name: 'ReservationEdit', params: { reservationId: reservation.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reservation-details.component.ts"></script>
