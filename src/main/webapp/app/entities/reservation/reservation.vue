<template>
  <div>
    <h2 id="page-heading" data-cy="ReservationHeading">
      <span v-text="$t('isyYachtingApp.reservation.home.title')" id="reservation-heading">Reservations</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('isyYachtingApp.reservation.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ReservationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-reservation"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('isyYachtingApp.reservation.home.createLabel')"> Create a new Reservation </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && reservations && reservations.length === 0">
      <span v-text="$t('isyYachtingApp.reservation.home.notFound')">No reservations found</span>
    </div>
    <div class="table-responsive" v-if="reservations && reservations.length > 0">
      <table class="table table-striped" aria-describedby="reservations">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.reservation.day')">Day</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.reservation.client')">Client</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.reservation.rental')">Rental</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="reservation in reservations" :key="reservation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReservationView', params: { reservationId: reservation.id } }">{{ reservation.id }}</router-link>
            </td>
            <td>
              <div v-if="reservation.day">
                <router-link :to="{ name: 'DayView', params: { dayId: reservation.day.id } }">{{ reservation.day.time }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="reservation.client">
                <router-link :to="{ name: 'ClientView', params: { clientId: reservation.client.id } }">{{
                  reservation.client.lastName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="reservation.rental">
                <router-link :to="{ name: 'RentalView', params: { rentalId: reservation.rental.id } }">{{
                  reservation.rental.beginingInstant
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ReservationView', params: { reservationId: reservation.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ReservationEdit', params: { reservationId: reservation.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(reservation)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="isyYachtingApp.reservation.delete.question" data-cy="reservationDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-reservation-heading" v-text="$t('isyYachtingApp.reservation.delete.question', { id: removeId })">
          Are you sure you want to delete this Reservation?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-reservation"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeReservation()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./reservation.component.ts"></script>
