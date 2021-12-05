<template>
  <div>
    <h2 id="page-heading" data-cy="RentalHeading">
      <span v-text="$t('isyYachtingApp.rental.home.title')" id="rental-heading">Rentals</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('isyYachtingApp.rental.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'RentalCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-rental"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('isyYachtingApp.rental.home.createLabel')"> Create a new Rental </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && rentals && rentals.length === 0">
      <span v-text="$t('isyYachtingApp.rental.home.notFound')">No rentals found</span>
    </div>
    <div class="table-responsive" v-if="rentals && rentals.length > 0">
      <table class="table table-striped" aria-describedby="rentals">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.rental.beginingInstant')">Begining Instant</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.rental.endingInstant')">Ending Instant</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="rental in rentals" :key="rental.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'RentalView', params: { rentalId: rental.id } }">{{ rental.id }}</router-link>
            </td>
            <td>{{ rental.beginingInstant ? $d(Date.parse(rental.beginingInstant), 'short') : '' }}</td>
            <td>{{ rental.endingInstant ? $d(Date.parse(rental.endingInstant), 'short') : '' }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'RentalView', params: { rentalId: rental.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'RentalEdit', params: { rentalId: rental.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(rental)"
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
        ><span id="isyYachtingApp.rental.delete.question" data-cy="rentalDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-rental-heading" v-text="$t('isyYachtingApp.rental.delete.question', { id: removeId })">
          Are you sure you want to delete this Rental?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-rental"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeRental()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./rental.component.ts"></script>
